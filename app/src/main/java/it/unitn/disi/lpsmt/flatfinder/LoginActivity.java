package it.unitn.disi.lpsmt.flatfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.amazonaws.services.cognitoidentityprovider.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidentityprovider.model.UserNotConfirmedException;
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
    private Switch swtSalvaCredenziali;

    @Nullable
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Authentication.initialize(this.getApplicationContext(), null);
        this.setContentView(R.layout.activity_login);
        this.user = null;
        Authentication.getUser((user, exception) -> {

            if( exception != null ){

                Log.d(TAG, "User is not signed in");

            } else if( user != null ) {

                Log.d(TAG, "User is signed in " + user.toString());
                Authentication.logout();

            }
            this.initUI();

        });

    }

    private void initUI() {

        this.lblRegistrati = this.findViewById(R.id.signin_lbl_registrati);
        this.txtEmail = this.findViewById(R.id.signin_txt_email);
        this.txtPassword = this.findViewById(R.id.signin_txt_password);
        this.btnAccedi = this.findViewById(R.id.signin_btn_accedi);
        this.lblFacebook = this.findViewById(R.id.signin_lbl_facebook);
        this.lblGoogle = this.findViewById(R.id.signin_lbl_google);
        this.swtSalvaCredenziali = this.findViewById(R.id.signin_swc_ricordami);

        this.lblRegistrati.setOnClickListener(this::lblRegistratiOnClick);
        this.btnAccedi.setOnClickListener(this::btnAccediOnClick);
        this.lblGoogle.setOnClickListener(this::lblGoogleOnClick);
        this.lblFacebook.setOnClickListener(this::lblFacebookOnClick);

        this.resetUI();


    }

    private void lblRegistratiOnClick( View v ) {

        Log.d(TAG, v.getId() + " DID TAP");
        Log.d(TAG, "" + this.lblRegistrati.getId() + " DID TAP");
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        this.startActivity(i);
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

        assert this.user != null;
        Toast.makeText(getApplicationContext(), "Signed in correctly, " + this.user.toString(), Toast.LENGTH_LONG).show();
        if( this.swtSalvaCredenziali.isChecked() ){
            SharedPreferences preferences = this.getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", this.txtEmail.getText().toString());
            editor.putString("password", this.txtPassword.getText().toString());
            editor.apply();
        }
        Authentication.logout();

    }

    private void loginHandler(User user, Exception ex){

        this.user = user;
        if( user != null ){
            this.loginAvvenuto();
            this.resetUI();
            Authentication.logout();
        } else if ( ex != null ) {
            if( ex instanceof UserNotConfirmedException )
                Toast.makeText(this.getApplicationContext(), R.string.login_utente_non_confermato, Toast.LENGTH_LONG).show();
            else if ( ex instanceof NotAuthorizedException ){
                Toast.makeText(this.getApplicationContext(), R.string.login_email_o_psw_errati, Toast.LENGTH_LONG).show();
            }
        }

    }

    private void resetUI(){

        SharedPreferences preferences = this.getSharedPreferences("login", MODE_PRIVATE);
        this.txtEmail.setText(preferences.getString("email", ""));
        this.txtPassword.setText(preferences.getString("password", ""));
        this.swtSalvaCredenziali.setChecked(true);

    }
}
