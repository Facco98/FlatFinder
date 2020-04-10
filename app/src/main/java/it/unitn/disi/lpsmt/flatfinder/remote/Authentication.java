package it.unitn.disi.lpsmt.flatfinder.remote;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.client.results.SignInState;
import com.amazonaws.mobile.client.results.SignUpResult;
import it.unitn.disi.lpsmt.flatfinder.model.User;

import java.util.HashMap;
import java.util.Map;

public abstract class Authentication {

    private static final String TAG = "AWS Authentication";
    private static AWSMobileClient mobileClient = AWSMobileClient.getInstance();

    @NonNull
    public static SignUpResult registerUser( @NonNull User user, @NonNull String password) throws Exception {

        if( mobileClient.isSignedIn() ){
            mobileClient.signOut();
        }

        HashMap<String, String> userAttributes = new HashMap<>();
        userAttributes.put("name", user.getName());
        userAttributes.put("family_name", user.getFamily_name());
        if( user.getPhone_number() != null ){
            userAttributes.put("phone_number", user.getPhone_number());
        }

        SignUpResult signUpResult = mobileClient.signUp(user.getEmail(), password, userAttributes, null);
        Log.d(TAG, "User " + user.getEmail() + " signed up with status " + signUpResult);
        return signUpResult;
    }

    @NonNull
    public static SignUpResult confirmCode(@NonNull User user, @NonNull String code) throws Exception {

        String email = user.getEmail();
        return mobileClient.confirmSignUp(email, code);

    }

    @NonNull
    public static User login( @NonNull String email, @NonNull String password ) throws Exception {

        mobileClient.signIn(email, password, null);
        Map<String, String> attributes = mobileClient.getUserAttributes();
        String name = attributes.get("name");
        String family_name = attributes.get("family_name");
        String phone_number = attributes.get("phone_number");

        return new User(email, name, family_name, phone_number);

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
                    Log.d(TAG, Log.getStackTraceString(e));
                }
            };

        }

        mobileClient.initialize(context, realCallback);

    }



}