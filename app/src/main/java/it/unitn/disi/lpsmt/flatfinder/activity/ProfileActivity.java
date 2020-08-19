package it.unitn.disi.lpsmt.flatfinder.activity;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import it.unitn.disi.lpsmt.flatfinder.R;

public class ProfileActivity extends AppCompatActivity {
    
    private ImageView imgProfileImage;
    private EditText txtNome, txtCognome, txtEmail, txtTelefono;
    private RadioGroup rdgSesso;
    private CheckBox chkboxNome, chkboxTelefono;
    private Spinner spnTipoInserzionista;
    private Button btnSalva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.profilo);

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
        this.txtCognome = this.findViewById(R.id.profilo_txt_cognome);
        this.txtEmail = this.findViewById(R.id.profilo_txt_email);
        this.txtTelefono = this.findViewById(R.id.profilo_txt_numeroTelefono);
        this.chkboxNome = this.findViewById(R.id.profilo_chkbox_privacyNome);
        this.chkboxTelefono = this.findViewById(R.id.profilo_chkbox_privacyNumeroTelefono);
        this.rdgSesso = this.findViewById(R.id.profilo_rdgroup_sesso);
        this.spnTipoInserzionista  = this.findViewById(R.id.profilo_spinner_tipoInserzionista);
        this.btnSalva = this.findViewById(R.id.profilo_btn_salva);
        
        this.btnSalva.setOnClickListener(this::btnSalvaOnClick);
    }

    private void btnSalvaOnClick(View view) {

        // ritorna alla pagina home
        Intent intent = new Intent(this, HomeActivity.class);
        this.startActivity(intent);
    }
}