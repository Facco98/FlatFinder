package it.unitn.disi.lpsmt.flatfinder.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.remote.Authentication;
import it.unitn.disi.lpsmt.flatfinder.task.Completion;
import it.unitn.disi.lpsmt.flatfinder.util.Util;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText txtNome;
    private EditText txtCognome;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfermaPassword;
    private CheckBox cbxAccettoTermini;
    private TextView lblAccedi;

    private Button btnRegistrati;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.alertDialog = Util.getDialog(this, "Registrazione in corso", TAG);
        this.initUI();


    }

    private void initUI() {

        this.txtNome = this.findViewById(R.id.signup_txt_name);
        this.txtCognome = this.findViewById(R.id.signup_txt_surname);
        this.txtEmail = this.findViewById(R.id.signup_txt_email);
        this.txtPassword = this.findViewById(R.id.signup_txt_password);
        this.txtConfermaPassword = this.findViewById(R.id.signup_txt_confirmPassword);
        this.cbxAccettoTermini = this.findViewById(R.id.signup_cbx_informativa);
        this.btnRegistrati = this.findViewById(R.id.signup_btn_registrati);
        this.lblAccedi = this.findViewById(R.id.signup_lbl_accedi);

        this.btnRegistrati.setOnClickListener(this::btnRegistratiOnClick);
        this.lblAccedi.setOnClickListener(this::lblAccediOnClick);
        this.resetUI();
    }

    private void lblAccediOnClick(View view) {
        Log.d(TAG, view.getId() + " DID TAP");
        Log.d(TAG, "" + this.lblAccedi.getId() + " DID TAP");
        Intent i = new Intent(this, LoginActivity.class);
        this.startActivity(i);
    }

    private void resetUI() {

        this.cbxAccettoTermini.setChecked(false);
        this.txtNome.setText("");
        this.txtCognome.setText("");
        this.txtPassword.setText("");
        this.txtConfermaPassword.setText("");
        this.txtEmail.setText("");

    }

    private void btnRegistratiOnClick(View v) {

        String nome = this.txtNome.getText().toString();
        String cognome = this.txtCognome.getText().toString();
        String email = this.txtEmail.getText().toString();
        String password = this.txtPassword.getText().toString();
        String confermaPassword = this.txtConfermaPassword.getText().toString();

        boolean terminiAccettati = this.cbxAccettoTermini.isChecked();

        if (!terminiAccettati) {

            Toast.makeText(this.getApplicationContext(), R.string.termini_non_accettati, Toast.LENGTH_LONG).show();

        } else if(nome.trim().isEmpty() || cognome.trim().isEmpty() || email.trim().isEmpty()
                || password.trim().isEmpty() || confermaPassword.trim().isEmpty()){

            Toast.makeText(this.getApplicationContext(), "Si prega di inserire tutti i campi", Toast.LENGTH_LONG).show();

        } else if (!password.equals(confermaPassword)) {

            Toast.makeText(this.getApplicationContext(), R.string.password_non_corrispondenti, Toast.LENGTH_LONG).show();

        } else {

            Completion<Boolean> completion = (result, error) -> {
                Util.dismissDialog(this.alertDialog, TAG);
                if (error != null) {
                    Toast.makeText(this.getApplicationContext(), "Errore durante la registrazione del nuovo utente", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                } else if (result != null) {
                    Log.d(TAG, "Utente registrato");
                    Toast.makeText(this.getApplicationContext(), R.string.email_inviata_verifica, Toast.LENGTH_LONG).show();
                    this.resetUI();
                    this.finish();
                }
            };

            User user = new User(email, nome, cognome, null, null, null, null);
            Util.dismissDialog(this.alertDialog, TAG);
            Authentication.registerUser(user, password, completion);

        }

    }
}


