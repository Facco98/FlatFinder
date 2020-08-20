package it.unitn.disi.lpsmt.flatfinder.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.remote.Authentication;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private ImageView imgProfileImage;
    private EditText txtNome, txtCognome, txtEmail, txtTelefono;
    private RadioGroup rdgSesso;
    private Spinner spnTipoInserzionista;
    private Button btnSalva;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.profilo);
        this.user = User.getCurrentUser();
        if( this.user == null ){

            Log.i(TAG, "USER IS NOT LOGGED IN");
            this.finish();

        }
        this.setupUI();
    }

    private void setupToolbar() {
        Toolbar toolbar = this.findViewById(R.id.profilo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void setupUI() {

        setupToolbar();

        this.imgProfileImage = this.findViewById(R.id.profilo_img_immagineDiProfilo);
        this.txtNome = this.findViewById(R.id.profilo_txt_nome);
        this.txtNome.setText(this.user.getName());
        this.txtCognome = this.findViewById(R.id.profilo_txt_cognome);
        this.txtCognome.setText(this.user.getFamily_name());
        this.txtEmail = this.findViewById(R.id.profilo_txt_email);
        this.txtEmail.setText(this.user.getEmail());
        this.txtTelefono = this.findViewById(R.id.profilo_txt_numeroTelefono);
        this.txtTelefono.setText(this.user.getPhone_number());
        this.rdgSesso = this.findViewById(R.id.profilo_rdgroup_sesso);
        this.spnTipoInserzionista  = this.findViewById(R.id.profilo_spinner_tipoInserzionista);
        this.btnSalva = this.findViewById(R.id.profilo_btn_salva);
        
        this.btnSalva.setOnClickListener(this::btnSalvaOnClick);
    }

    private void btnSalvaOnClick(View view) {

        // ritorna alla pagina home
        this.finish();
    }
}