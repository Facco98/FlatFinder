package it.unitn.disi.lpsmt.flatfinder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.services.cognitoidentity.model.NotAuthorizedException;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.remote.Authentication;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextView lblRegistrati;
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnAccedi;
    private TextView lblGoogle;
    private TextView lblFacebook;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Authentication.initialize(this.getApplicationContext(), null);
        this.setContentView(R.layout.activity_login);
        this.initUI();


    }

    private void initUI() {

        this.lblRegistrati = this.findViewById(R.id.signin_lbl_registrati);
        this.txtEmail = this.findViewById(R.id.signin_txt_email);
        this.txtPassword = this.findViewById(R.id.signin_txt_password);
        this.btnAccedi = this.findViewById(R.id.signin_btn_accedi);
        this.lblFacebook = this.findViewById(R.id.signin_lbl_facebook);
        this.lblGoogle = this.findViewById(R.id.signin_lbl_google);

        this.lblRegistrati.setOnClickListener(this::lblRegistratiOnClick);
        this.btnAccedi.setOnClickListener(this::btnAccediOnClick);
        this.lblGoogle.setOnClickListener(this::lblGoogleOnClick);
        this.lblFacebook.setOnClickListener(this::lblFacebookOnClick);


    }

    private void lblRegistratiOnClick( View v ) {

        Log.d(TAG, v.getId() + " DID TAP");
        Log.d(TAG, "" + this.lblRegistrati.getId() + " DID TAP");
    }

    private void btnAccediOnClick( View v ){

        String email = this.txtEmail.getText().toString();
        String psw = this.txtPassword.getText().toString();

        Log.d(TAG, "Registrati di tap");
        Log.d(TAG, "VALUES: email="+email+", password="+psw);

        try {
            Authentication.login(email, psw, this::loginHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void lblGoogleOnClick( View v ){

        Log.d(TAG, "Trying to login with Google");

    }

    private void lblFacebookOnClick( View v ){

        Log.d(TAG, "Trying to login with Facebook");

    }

    private void loginAvvenuto(){

        Toast.makeText(getApplicationContext(), "Already signed in", Toast.LENGTH_LONG).show();

        Authentication.logout();

    }

    private void loginHandler(User user, Exception ex){

        this.user = user;
        if( user != null ){
            this.loginAvvenuto();
            Authentication.logout();
        } else {
            Toast.makeText(this.getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
        }

    }
}
