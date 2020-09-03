package it.unitn.disi.lpsmt.flatfinder.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.mapbox.mapboxsdk.Mapbox;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.exception.EmailNotVerifiedException;
import it.unitn.disi.lpsmt.flatfinder.fragment.RecuperaPasswordDialogFragment;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.remote.Authentication;
import it.unitn.disi.lpsmt.flatfinder.util.Util;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private TextView lblRegistrati;
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnAccedi;
    private Button btnGoogle;
    private Switch swtSalvaCredenziali;
    private TextView lblPasswordDimenticata;

    private AlertDialog alertDialog;


    @Nullable
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_login);
        this.user = null;

        this.alertDialog = Util.getDialog(this, "Autenticazione in corso", TAG);

        AlertDialog alertDialog = Util.getDialog(this, "Attendi mentre carichiamo l'applicazione", TAG);
        Util.showDialog(alertDialog, TAG);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        Authentication.getUser((user, exception) -> {

            Util.dismissDialog(alertDialog, TAG);
            if( exception != null ){

                Log.d(TAG, "User is not signed in");

            } else if( user != null ) {

                Log.d(TAG, "User is signed in " + user.toString());
                this.user = user;
                this.loginAvvenuto();

            }
            this.initUI();

        });

    }

    private void initUI() {

        this.lblRegistrati = this.findViewById(R.id.signin_lbl_registrati);
        this.txtEmail = this.findViewById(R.id.signin_txt_email);
        this.txtPassword = this.findViewById(R.id.signin_txt_password);
        this.btnAccedi = this.findViewById(R.id.signin_btn_accedi);
        this.btnGoogle = this.findViewById(R.id.signin_lbl_google);
        this.swtSalvaCredenziali = this.findViewById(R.id.signin_swc_ricordami);
        this.lblPasswordDimenticata = this.findViewById(R.id.signin_lbl_passwordDimenticata);

        this.lblRegistrati.setOnClickListener(this::lblRegistratiOnClick);
        this.btnAccedi.setOnClickListener(this::btnAccediOnClick);
        this.btnGoogle.setOnClickListener(this::btnGoogleOnClick);
        this.lblPasswordDimenticata.setOnClickListener(this::lblPasswordDimenticata);

        this.resetUI();


    }

    private void lblPasswordDimenticata(View view) {
        Log.d(TAG, "Trying to reset the password");
        DialogFragment dialog = new RecuperaPasswordDialogFragment();
        dialog.show(getSupportFragmentManager(), "recupera password");
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = alertDialogBuilder.setTitle("Caricamento")
                .setMessage("Autenticazione in corso")
                .create();

        alertDialog.show();

        try {
            Authentication.login(email, psw, (user, exception) ->{
                alertDialog.hide();
                alertDialog.dismiss();
                this.loginHandler(user, exception);
            });
        } catch (Exception e) {
            e.printStackTrace();
            alertDialog.hide();
        }

    }

    private void btnGoogleOnClick( View v ){

        Log.d(TAG, "Trying to login with Google");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("128269540845-tpufpa2aon8kapieghrp2l44pav8nmg6.apps.googleusercontent.com")
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 300);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 300) {
            Util.showDialog(this.alertDialog, TAG);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Authentication.loginWithGoole(account.getIdToken(), this::loginHandler);
            } catch (ApiException e) {
                Toast.makeText(this, "Errore durante l'autenticazione con Google", Toast.LENGTH_SHORT).show();
                Util.dismissDialog(this.alertDialog, TAG);
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }

        }
    }

    private void loginAvvenuto(){

        User.setCurrentUser(this.user);
        Intent i = new Intent(this, HomeActivity.class);
        this.startActivity(i);
        this.finish();

    }

    private void loginHandler(User user, Exception ex){

        Util.dismissDialog(this.alertDialog, TAG);
        this.user = user;
        if( user != null ){
            this.loginAvvenuto();
            this.resetUI();
        } else if ( ex != null ) {
            if( ex instanceof EmailNotVerifiedException)
                Toast.makeText(this.getApplicationContext(), R.string.login_utente_non_confermato, Toast.LENGTH_LONG).show();
            else if ( ex instanceof NullPointerException ){
                Toast.makeText(this.getApplicationContext(), R.string.login_email_o_psw_errati, Toast.LENGTH_LONG).show();
            } else {
                ex.printStackTrace();
                Toast.makeText(this, "Errore durante l'autenticazione dell'utente", Toast.LENGTH_SHORT).show();
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
