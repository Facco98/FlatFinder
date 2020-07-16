package it.unitn.disi.lpsmt.flatfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;

public class AnnounceDetailsActivity extends AppCompatActivity {

    private static final String TAG = "AnnounceDetailsAcrivity";

    //private ViewPager viewPagerFotoAnnuncio;
    private TextView lblImgTag;
    private TextView lblAffitto;
    private TextView lblNLocali;
    private TextView lblNPiano;
    private TextView lblDimensione;
    private TextView lblNBagni;
    private TextView lblTipologia;
    private TextView lblIndirizzo;
    private Button btnContatta;
    private Button btnSalva;
    private TextView lblDescrizione;
    private TextView lblDataAnnuncio;
    private TextView lblAltreSpese;
    private TextView lblDisponibilità;
    private TextView lblArredamento;
    private TextView lblClasseEnergetica;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.annuncio_dettagli);
        this.setupUI();
    }

    private void setupUI(){
        //this.viewPagerFotoAnnuncio = findViewById(R.id.dettagli_);
        this.lblImgTag = findViewById(R.id.dettagli_label_imgtag);
        this.lblAffitto = findViewById(R.id.dettagli_lbl_affitto);
        this.lblNLocali = findViewById(R.id.dettagli_lbl_nlocali);
        this.lblNPiano = findViewById(R.id.dettagli_lbl_piano);
        this.lblDimensione = findViewById(R.id.dettagli_lbl_dimensione);
        this.lblNBagni = findViewById(R.id.dettagli_lbl_nbagni);
        this.lblTipologia = findViewById(R.id.dettagli_lbl__tipologia);
        this.lblIndirizzo = findViewById(R.id.dettagli_lbl_indirizzo);
        this.btnContatta = findViewById(R.id.dettagli_btn_contatta);
        this.btnSalva = findViewById(R.id.dettagli_btn_salva);
        this.lblDescrizione = findViewById(R.id.dettagli_lbl_descrizione);
        this.lblDataAnnuncio = findViewById(R.id.dettagli_lbl_dataAnnuncio);
        this.lblAltreSpese = findViewById(R.id.dettagli_lbl_altreSpese);
        this.lblDisponibilità = findViewById(R.id.dettagli_lbl_disponibilita);
        this.lblArredamento = findViewById(R.id.dettagli_lbl_arredamento);
        this.lblClasseEnergetica = findViewById(R.id.dettagli_lbl_classeEnergetica);
        this.mapView = findViewById(R.id.dettagli_view_mappa);

        this.btnContatta.setOnClickListener(this::btnContattaOnClick);
        this.btnSalva.setOnClickListener(this::btnSalvaOnClick);

    }

    private void btnContattaOnClick( View v ){

        Log.d(TAG, "Button Contatta did click");

    }

    private void btnSalvaOnClick( View v ){

        Log.d(TAG, "Button Salva did click");

    }
}