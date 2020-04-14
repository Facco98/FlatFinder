package it.unitn.disi.lpsmt.flatfinder;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.services.cognitoidentityprovider.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidentityprovider.model.UsernameExistsException;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.remote.Authentication;
import it.unitn.disi.lpsmt.flatfinder.task.Completion;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    private EditText txtNome;
    private EditText txtCognome;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfermaPassword;
    private CheckBox cbxAccettoTermini;

    private Button btnRegistrati;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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

        this.btnRegistrati.setOnClickListener(this::btnRegistratiOnClick);
        this.resetUI();
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

        } else if (!password.equals(confermaPassword)) {

            Toast.makeText(this.getApplicationContext(), R.string.password_non_corrispondenti, Toast.LENGTH_LONG).show();

        } else {

            Completion<SignUpResult> completion = (result, error) -> {
                if (error != null) {
                    if (error instanceof UsernameExistsException)
                        Toast.makeText(this.getApplicationContext(), R.string.email_gia_registrata, Toast.LENGTH_LONG).show();
                    else if (error instanceof InvalidPasswordException)
                        Toast.makeText(this.getApplicationContext(), R.string.password_non_valida, Toast.LENGTH_LONG).show();
                    else
                        error.printStackTrace();
                } else if (result != null) {
                    Log.d(TAG, "Utente registrato con status = " + result.getUserSub());
                    Toast.makeText(this.getApplicationContext(), R.string.email_inviata_verifica, Toast.LENGTH_LONG).show();
                    this.resetUI();
                    this.finish();
                }
            };

            User user = new User(email, nome, cognome, null);
            Authentication.registerUser(user, password, completion);

        }

    }
}


