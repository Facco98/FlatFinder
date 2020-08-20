package it.unitn.disi.lpsmt.flatfinder.remote;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import it.unitn.disi.lpsmt.flatfinder.exception.EmailNotVerifiedException;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.task.Completion;
import it.unitn.disi.lpsmt.flatfinder.task.Task;

import java.io.InvalidClassException;
import java.util.HashMap;
import java.util.Map;

public final class Authentication {

    private static final String TAG = "Authentication";
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private Authentication(){

    }

    public static void registerUser( @NonNull User user, @NonNull String password, @Nullable Completion<Boolean> completion) {

        if( firebaseAuth.getCurrentUser() != null )
            logout();

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                try{
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    firebaseAuth.updateCurrentUser(firebaseUser);
                    Map<String, Object> map = new HashMap<>();
                    map.put("phoneN", user.getPhone_number());
                    map.put("uid", firebaseUser.getUid());
                    map.put("name", user.getName());
                    map.put("family_name", user.getFamily_name());
                    firebaseFirestore.collection("utenti").document(firebaseUser.getUid()).set(map).addOnCompleteListener((voids2) -> {
                        firebaseUser.sendEmailVerification().addOnCompleteListener( (vuoti) -> {
                            if (completion != null)
                                    completion.onComplete(true, null);
                        });
                    });

                } catch ( Exception ex ){

                    if( completion != null )
                        completion.onComplete(null, ex);

                }
            }
        });



    }

    public static void login(@NonNull String email, @NonNull String password, @Nullable Completion<User> completion) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((authTask) -> {
            try {
                FirebaseUser firebaseUser = authTask.getResult().getUser();
                if (!firebaseUser.isEmailVerified())
                    throw new EmailNotVerifiedException("Email non verificata");
                firebaseAuth.updateCurrentUser(firebaseUser);

                firebaseFirestore.collection("utenti").document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
                    String phoneNumber = (String) task.getResult().get("phoneN");
                    String name = (String) task.getResult().get("name");
                    String familyName = (String) task.getResult().get("family_name");
                    String sub = firebaseUser.getUid();
                    User user = new User(email, name, familyName, phoneNumber, sub);

                    if( completion != null )
                        completion.onComplete(user, null);
                });


            } catch( Exception ex ){

                if( completion != null )
                    completion.onComplete(null, ex);

            }
        });




    }

    public static void getUser(@Nullable Completion<User> completion) {

        if( firebaseAuth.getCurrentUser() != null ){

            try {
                FirebaseUser attributes = firebaseAuth.getCurrentUser();
                String email = attributes.getEmail();
                firebaseFirestore.collection("utenti").document(attributes.getUid()).get().addOnCompleteListener(task -> {
                    String phoneNumber = (String) task.getResult().get("phoneN");
                    String name = (String) task.getResult().get("name");
                    String familyName = (String) task.getResult().get("family_name");
                    String sub = attributes.getUid();
                    User user = new User(email, name, familyName, phoneNumber, sub);

                    if( completion != null )
                        completion.onComplete(user, null);
                });
            } catch ( Exception ex ){

                if( completion != null )
                    completion.onComplete(null, ex);

            }


        } else {

            if( completion != null )
                completion.onComplete(null, new InvalidClassException("No user"));

        }
    }

    public static void logout(){

        firebaseAuth.signOut();

    }

    public static void loginWithGoole(String idToken, @Nullable Completion<User> completion){

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    try{
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            Map<String, Object> map = new HashMap<>();
                            map.put("phoneN", firebaseUser.getPhoneNumber());
                            map.put("uid", firebaseUser.getUid());
                            map.put("name", task.getResult().getAdditionalUserInfo().getProfile().get("given_name"));
                            map.put("family_name", task.getResult().getAdditionalUserInfo().getProfile().get("family_name"));
                            //System.out.println(task.getResult().getAdditionalUserInfo().getProfile());
                            firebaseFirestore.collection("utenti").document(firebaseUser.getUid()).set(map)
                                    .addOnCompleteListener(voids -> {
                                        firebaseAuth.updateCurrentUser(firebaseUser);
                                        if (completion != null) {
                                            User user = new User(firebaseUser.getEmail(), (String) map.get("name"),
                                                    (String) map.get("family_name"), (String) map.get("phoneN"),
                                                    firebaseUser.getUid());
                                            completion.onComplete(user, null);
                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    } catch ( Exception ex ){

                        if( completion != null ) completion.onComplete(null, ex);

                    }
                });
    }


}