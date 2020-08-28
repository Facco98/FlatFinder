package it.unitn.disi.lpsmt.flatfinder.activity;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.maps.MapView;
import com.google.android.material.tabs.TabLayout;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.PhotosAdapter;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;
import it.unitn.disi.lpsmt.flatfinder.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnounceDetailsActivity extends AppCompatActivity {

    private static final String TAG = "AnnounceDetailsAcrivity";

    private Announce announce;

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
    private ViewPager imgViewPager;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.annuncio_dettagli);

        this.user = User.getCurrentUser();
        if ( user == null ){

            //Log.i(TAG, "USER IS NOT LOGGED IN");
            Intent i = new Intent(this, LoginActivity.class);
            this.startActivity(i);
            this.finish();
        }



        this.setupUI();
        Intent i = this.getIntent();
        if( i != null && i.hasExtra("announceID") ){

            int announceID = i.getIntExtra("announceID", 1);
            Map<String, String> filters = new HashMap<>(1);
            filters.put("id", ""+announceID);
            RemoteAPI.getAnnounceList(filters, this::handleAnnounceLoading);

        }


    }

    public boolean onSupportNavigateUp(){
        this.finish();
        return super.onSupportNavigateUp();
    }

    private void setupToolbar() {
        Toolbar toolbar = this.findViewById(R.id.dettagli_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }


    private void setupUI(){

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
        this.imgViewPager = this.findViewById(R.id.dettagli_view_pager);
        TabLayout dotsIndicator = this.findViewById(R.id.annuncio_dettagli_lyt_dotIndicator);
        dotsIndicator.setupWithViewPager(imgViewPager, true);

        this.btnContatta.setOnClickListener(this::btnContattaOnClick);
        this.btnSalva.setOnClickListener(this::btnSalvaOnClick);

        setupToolbar();

    }

    private void btnContattaOnClick( View v ){

        Log.d(TAG, "Button Contatta did click");
        //Apre la tastiera del telefono e mostra il numero, l'utente poi deve premere 'chiama' per effettuare la chiamata
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + announce.getContact()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }


    }

    private void btnSalvaOnClick( View v ){

        Log.d(TAG, "Button Salva did click");

    }

    private void handleAnnounceLoading(List<Announce> announces, Exception ex) {

        if( ex != null ) {
            ex.printStackTrace();
            return;
        }

        if( announces.size() != 1 )
            return;

        /*Announce*/ announce = announces.get(0);

        RemoteAPI.getPhotosForAnnounce(announce.getId(), (photos, error) -> {

            if( error != null ){

                error.printStackTrace();

            } else if( photos != null ) {

                if( photos.size() > 0 ) {
                    this.imgViewPager.setAdapter(new PhotosAdapter(photos, this));
                    this.findViewById(R.id.dettagli_empty_message).setVisibility(View.GONE);
                } else {
                    ((TextView) this.findViewById(R.id.dettagli_empty_message)).setText(R.string.no_photo);
                    this.findViewById(R.id.dettagli_empty_message).setVisibility(View.VISIBLE);
                }
            }

        });

        Log.i(TAG, announce.toString());
        StringBuilder sb = new StringBuilder();
        sb.append(announce.getRentPerMonth()).append(this.getResources().getString(R.string.euro))
                .append(this.getResources().getString(R.string.per_month));
        this.lblAffitto.setText(sb.toString());
        this.lblAltreSpese.setText(announce.getExtras()+"€");
        this.lblArredamento.setText(announce.getFornitureStatus().description);
        this.lblClasseEnergetica.setText(announce.getEnergeticClass().toString());
        sb = new StringBuilder();
        sb.append(this.getResources().getString(R.string.from)).append(" ").append(Util.dateToString(announce.getStart()))
                .append(" ").append(this.getResources().getString(R.string.to))
                .append(" ").append(Util.dateToString(announce.getEnd()));
        this.lblDisponibilità.setText(sb.toString());
        this.lblDataAnnuncio.setText(Util.dateToString(announce.getDate()));
        this.lblDescrizione.setText(announce.getDescription());
        sb = new StringBuilder();
        sb.append(announce.getnLocals()).append(" ").append(this.getResources().getString(R.string.rooms));
        this.lblNLocali.setText(sb.toString());
        sb = new StringBuilder();
        sb.append(announce.getFloor()).append(" ").append(this.getResources().getString(R.string.floor));
        this.lblNPiano.setText(sb.toString());
        sb = new StringBuilder();
        sb.append(announce.getSize()).append(" ").append(this.getResources().getString(R.string.mq));
        this.lblDimensione.setText(sb.toString());
        sb = new StringBuilder();
        sb.append(announce.getnBathrooms()).append(" ").append(this.getResources().getString(R.string.bathrooms));
        this.lblNBagni.setText(sb.toString());

        this.lblTipologia.setText(announce.getType().description);
        this.lblIndirizzo.setText(announce.getAddress());


        //tolgo bottoni slava e contatta se l'annuncio è mio
        if(announce.getCreatorUsername().compareTo(user.getSub()) == 0){ //annuncio è mio
            this.btnContatta.setVisibility(View.GONE);
            this.btnSalva.setVisibility(View.GONE);
        }

    }
}