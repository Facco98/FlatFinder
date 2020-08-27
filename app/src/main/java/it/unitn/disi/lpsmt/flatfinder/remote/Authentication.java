package it.unitn.disi.lpsmt.flatfinder.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import it.unitn.disi.lpsmt.flatfinder.exception.EmailNotVerifiedException;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.task.Completion;
import timber.log.Timber;

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
                    Boolean male = (Boolean) task.getResult().get("male");
                    String profileImg = (String) task.getResult().get("img");
                    User user = new User(email, name, familyName, phoneNumber, sub, male, profileImg);

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
                    Boolean male = (Boolean) task.getResult().get("male");
                    String profileImg = (String) task.getResult().get("img");
                    User user = new User(email, name, familyName, phoneNumber, sub, male, profileImg);

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
                            Timber.tag(TAG).d("signInWithCredential:success");
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            Map<String, Object> map = new HashMap<>();
                            map.put("phoneN", firebaseUser.getPhoneNumber());
                            map.put("uid", firebaseUser.getUid());
                            map.put("name", task.getResult().getAdditionalUserInfo().getProfile().get("given_name"));
                            map.put("family_name", task.getResult().getAdditionalUserInfo().getProfile().get("family_name"));
                            //System.out.println(task.getResult().getAdditionalUserInfo().getProfile());
                            firebaseFirestore.collection("utenti").document(firebaseUser.getUid()).update(map)
                                    .addOnCompleteListener(voids -> {
                                        firebaseAuth.updateCurrentUser(firebaseUser);
                                        firebaseFirestore.collection("utenti").document(firebaseUser.getUid()).get().addOnCompleteListener((task1) ->
                                        {
                                            String profileImg = (String) task1.getResult().get("img");
                                            Boolean male = (Boolean) task1.getResult().get("male");
                                            if (completion != null) {
                                                User user = new User(firebaseUser.getEmail(), (String) map.get("name"),
                                                        (String) map.get("family_name"), (String) map.get("phoneN"),
                                                        firebaseUser.getUid(), male, profileImg);
                                                completion.onComplete(user, null);
                                            }
                                        });
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            Timber.tag(TAG).w(task.getException(), "signInWithCredential:failure");
                        }
                    } catch ( Exception ex ){

                        if( completion != null ) completion.onComplete(null, ex);

                    }
                });
    }

    public static void forgotPassword(@NonNull String email, @Nullable Completion<Boolean> completion){

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener((voids) -> {

            if( completion != null )
                completion.onComplete(true, null);

        }).addOnFailureListener((err) -> {

            if( completion != null )
                completion.onComplete(null, err);

        });

    }

    public static void updateProfile(@NonNull User user, @Nullable Completion<Boolean> completion){

        Map<String, Object> map = new HashMap<>();
        map.put("phoneN", user.getPhone_number());
        map.put("uid", user.getSub());
        map.put("name", user.getName());
        map.put("family_name", user.getFamily_name());
        map.put("img", user.getProfileImg());
        map.put("male", user.getMale());
        firebaseFirestore.collection("utenti").document(user.getSub()).set(map).addOnCompleteListener((voids2) -> {
            if (completion != null)
                completion.onComplete(true, null);
        }).addOnFailureListener((err) -> {

            if( completion != null )
                completion.onComplete(null, err);

        });

    }


}