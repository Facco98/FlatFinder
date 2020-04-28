package it.unitn.disi.lpsmt.flatfinder.remote;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignUpResult;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.task.Completion;
import it.unitn.disi.lpsmt.flatfinder.task.Task;

import java.util.HashMap;
import java.util.Map;

public abstract class Authentication {

    private static final String TAG = "AWS Authentication";
    private static AWSMobileClient mobileClient = AWSMobileClient.getInstance();

    public static void registerUser( @NonNull User user, @NonNull String password, @Nullable Completion<SignUpResult> completion) {

        if( mobileClient.isSignedIn() )
            logout();

        Map<String, String> userAttributes = new HashMap<>();
        userAttributes.put("name", user.getName());
        userAttributes.put("family_name", user.getFamily_name());

        String phone_number = user.getPhone_number();

        if( phone_number != null ){
            userAttributes.put("phone_number", user.getPhone_number());
        }

        Task<String, SignUpResult> registrationTask = new Task<String, SignUpResult>((params) -> {

            String psw = params[1];
            String email = params[0];

            SignUpResult signUpResult = mobileClient.signUp(email, psw, userAttributes, null);
            return signUpResult;

        }, completion);
        registrationTask.execute(user.getEmail(), password);


    }

    public static void login(@NonNull String email, @NonNull String password, @Nullable Completion<User> completion) throws Exception {

        Task<String, User> loginTask = new Task<String, User>((params) -> {
            User user = null;

            String mail = params[0];
            String psw = params[1];
            mobileClient.signIn(mail, psw, null);
            Map<String, String> attributes = mobileClient.getUserAttributes();
            String name = attributes.get("name");
            String familyName = attributes.get("family_name");
            String phoneNumber = attributes.get("phone_number");
            user = new User(email, name, familyName, phoneNumber);

            return user;
        }, completion);
        loginTask.execute(email, password);


    }

    public static void initialize(@NonNull Context context, @Nullable Callback<UserStateDetails> callback) {

        Callback<UserStateDetails> realCallback = callback;
        if ( realCallback == null ) {
            realCallback = new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails result) {
                    Log.d(TAG, "User details: [ " + result.getUserState() + ", " + result.getDetails() + " ]");
                }

                @Override
                public void onError(Exception e) {
                    //Log.d(TAG, Log.getStackTraceString(e));
                }
            };

        }

        mobileClient.initialize(context, realCallback);

    }

    public static void getUser(@Nullable Completion<User> completion) {

        Task<String, User> retrieveUserTask = new Task<String, User>(params -> {

            User user = null;
            if( mobileClient.isSignedIn() ){

                Map<String, String> attributes = mobileClient.getUserAttributes();
                String email = attributes.get("email");
                String name = attributes.get("name");
                String family_name = attributes.get("family_name");
                String phone_number = attributes.get("phone_number");

                user = new User(email, name, family_name, phone_number);

            }

            return user;

        }, completion);
        retrieveUserTask.execute();

    }

    public static void logout(){

        mobileClient.signOut();

    }


}