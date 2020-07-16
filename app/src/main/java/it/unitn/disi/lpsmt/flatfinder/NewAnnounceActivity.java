package it.unitn.disi.lpsmt.flatfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class NewAnnounceActivity extends AppCompatActivity {

    private static final String TAG = "NewAnnounceActivity";

    //viwes
    private Spinner spnTipologiaStruttura;
    private Spinner spnCategoria;
    private EditText txtIndirizzo;
    private Button btnVerificaIndirizzo;
    private EditText txtAffitto;
    private EditText txtAltreSpese;
    private EditText txtDescrizione;
    private Spinner spnArredamento;
    private Spinner spnClasseEnergetica;
    private EditText txtDisponibilità;
    private EditText txtNumeroLocali;
    private EditText txtNumeroBagni;
    private EditText txtDimensione;
    private EditText txtNumeroPiano;
    private EditText txtContattiCellulare;
    private Button btnAvanti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.nuovo_annuncio);
        this.setupUI();
    }

    private void setupUI(){
        this.spnTipologiaStruttura = this.findViewById(R.id.nuovo_annuncio_spinner_tipologia_struttura);
        this.spnCategoria = this.findViewById(R.id.nuovo_annuncio_spinner_categoria);
        this.txtIndirizzo = this.findViewById(R.id.nuovo_annuncio_txt_indirizzo);
        this.btnVerificaIndirizzo = this.findViewById(R.id.nuovo_annuncio_btn_verificaIndirizzo);
        this.txtAffitto = this.findViewById(R.id.nuovo_annuncio_txt_affitto);
        this.txtAltreSpese = this.findViewById(R.id.nuovo_annuncio_txt_altreSpese);
        this.txtDescrizione = this.findViewById(R.id.nuovo_annuncio_txt_descrizione);
        this.spnArredamento = this.findViewById(R.id.nuovo_annuncio_spinner_arredamento);
        this.spnClasseEnergetica = this.findViewById(R.id.nuovo_annuncio_spinner_classeenergetica);
        this.txtDisponibilità = this.findViewById(R.id.nuovo_annuncio_txt_disponibilità);
        this.txtNumeroLocali = this.findViewById(R.id.nuovo_annuncio_txt_nLocali);
        this.txtNumeroBagni = this.findViewById(R.id.nuovo_annuncio_txt_nBagni);
        this.txtDimensione = this.findViewById(R.id.nuovo_annuncio_txt_dimensione);
        this.txtNumeroPiano = this.findViewById(R.id.nuovo_annuncio_txt_nPiano);
        this.txtContattiCellulare = this.findViewById(R.id.nuovo_annuncio_txt_contattiCellulare);
        this.btnAvanti = this.findViewById(R.id.nuovo_annuncio_btn_avanti);

        this.btnVerificaIndirizzo.setOnClickListener(this::btnVerificaIndirizzoOnClick);
        this.btnAvanti.setOnClickListener(this::btnAvantiOnClick);

    }

    private void btnVerificaIndirizzoOnClick( View v ){

        Log.d(TAG, "Button Verifica Indirizzo did click");

    }

    private void btnAvantiOnClick( View v ){

        Log.d(TAG, "Button Avanti did clicl");

    }
}