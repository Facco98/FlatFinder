package it.unitn.disi.lpsmt.flatfinder.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.PhotosAdapter;
import it.unitn.disi.lpsmt.flatfinder.fragment.EliminaAnnuncioDialogFragment;
import it.unitn.disi.lpsmt.flatfinder.listener.DeleteFragmentListener;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;
import it.unitn.disi.lpsmt.flatfinder.util.Util;

import java.util.*;

public class AnnounceDetailsActivity extends AppCompatActivity implements DeleteFragmentListener {

    private static final String TAG = "AnnounceDetailsAcrivity";
    private static final String FAVORITES_ARRAY = "favorite_announces";

    private Announce announce;

    private TextView lblAffitto;
    private TextView lblNLocali;
    private TextView lblNPiano;
    private TextView lblDimensione;
    private TextView lblNBagni;
    private TextView lblTipologia;
    private TextView lblIndirizzo;
    private ImageButton btnContatta;
    private ImageButton btnSalva;
    private ImageButton btnMessaggi;
    private Button btnModifica;
    private Button btnElimina;
    private TextView lblDescrizione;
    private TextView lblDataAnnuncio;
    private TextView lblAltreSpese;
    private TextView lblDisponibilita;
    private TextView lblArredamento;
    private TextView lblClasseEnergetica;
    private ViewPager imgViewPager;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private AlertDialog alertDialog;

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

        this.sharedPreferences = this.getSharedPreferences("annunci_preferiti", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.alertDialog = Util.getDialog(this, "Caricamento dell'annuncio in corso", TAG);

        this.setupUI();
        Intent i = this.getIntent();
        if( i != null && i.hasExtra("announceID") ){

            int announceID = i.getIntExtra("announceID", 1);
            Map<String, String> filters = new HashMap<>(1);
            filters.put("id", ""+announceID);
            Util.showDialog(this.alertDialog, TAG);
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
        this.btnMessaggi = findViewById(R.id.dettagli_btn_messaggi);
        this.btnContatta = findViewById(R.id.dettagli_btn_contatta);
        this.btnSalva = findViewById(R.id.dettagli_btn_salva);
        this.btnModifica = findViewById(R.id.dettagli_btn_modifica);
        this.btnElimina = findViewById(R.id.dettagli_button_elimina);
        this.lblDescrizione = findViewById(R.id.dettagli_lbl_descrizione);
        this.lblDataAnnuncio = findViewById(R.id.dettagli_lbl_dataAnnuncio);
        this.lblAltreSpese = findViewById(R.id.dettagli_lbl_altreSpese);
        this.lblDisponibilita = findViewById(R.id.dettagli_lbl_disponibilita);
        this.lblArredamento = findViewById(R.id.dettagli_lbl_arredamento);
        this.lblClasseEnergetica = findViewById(R.id.dettagli_lbl_classeEnergetica);
        this.imgViewPager = this.findViewById(R.id.dettagli_view_pager);
        TabLayout dotsIndicator = this.findViewById(R.id.dettagli_lyt_dotIndicator);
        dotsIndicator.setupWithViewPager(imgViewPager, true);

        this.btnContatta.setOnClickListener(this::btnContattaOnClick);
        this.btnSalva.setOnClickListener(this::btnSalvaOnClick);
        this.btnModifica.setOnClickListener(this::btnModificaOnCick);
        this.btnElimina.setOnClickListener(this::btnEliminaOnClick);
        this.btnMessaggi.setOnClickListener(this::btnMessaggiOnClick);

        setupToolbar();

    }

    private void btnMessaggiOnClick(View view) {
        Log.d(TAG, "Button Messaggi did click");
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("smsto:" + announce.getContact()));  // This ensures only SMS apps respond
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    private void checkBtnSalva(){
        String announceId = announce.getId() + "";
        Log.d(TAG, "announceId: "+announceId);

        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_ARRAY, new HashSet<>());

        if(favorites.contains(announceId)){
            setAnnuncioSalvato();
        } else {
            setSalvaAnnuncio();
        }
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

        String announceId = announce.getId() + "";
        Log.d(TAG, "announceId: "+announceId);

        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_ARRAY, new HashSet<>());
        if(this.btnSalva.getTag().equals(0)){
            Log.d(TAG, "isChecked");
            favorites.add(announceId);
            setAnnuncioSalvato();

        } else {
            Log.d(TAG, "is not Checked");
            favorites.remove(announceId);
            setSalvaAnnuncio();
        }
        editor.clear();
        editor.putStringSet(FAVORITES_ARRAY, favorites);
        editor.commit();
        Log.d(TAG, "preferiti: " + favorites.toString());
        Log.d(TAG, "sharedpref: " + sharedPreferences.getStringSet(FAVORITES_ARRAY, null));


    }

    private void setAnnuncioSalvato(){
        this.btnSalva.setTag(1);
        this.btnSalva.setImageResource(R.drawable.ic_favorite_black_24dp);
    }

    private void setSalvaAnnuncio(){
        this.btnSalva.setTag(0);
        this.btnSalva.setImageResource(R.drawable.ic_favorite_border_black_24dp);
    }

    private void btnModificaOnCick( View v){
        Log.d(TAG, "Button Modifica did click");
        Intent intent = new Intent(AnnounceDetailsActivity.this, ModifyAnnounceActivity.class);
        intent.putExtra("announceID", announce.getId());
        startActivity(intent);
    }

    private void btnEliminaOnClick( View v){
        Log.d(TAG, "Button Elimina did click");
        EliminaAnnuncioDialogFragment eliminaAnnuncioDialogFragment = new EliminaAnnuncioDialogFragment(announce, this, this);
        eliminaAnnuncioDialogFragment.show(getSupportFragmentManager(), TAG);
    }

    private void handleAnnounceLoading(List<Announce> announces, Exception ex) {
        Util.showDialog(alertDialog, TAG);

        if( ex != null ) {
            ex.printStackTrace();
            return;
        }

        if( announces.size() != 1 ) {
            TextView textView = new TextView(this);
            textView.setText("L'annuncio selezionato non esiste");
            this.setContentView(textView);
            return;
        }

        /*Announce*/ announce = announces.get(0);

        Util.dismissDialog(this.alertDialog, TAG);
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
        sb.append(this.getResources().getString(R.string.from)).append(" ").append(Util.dateToString(announce.getStart()));
        this.lblDisponibilita.setText(sb.toString());
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
            this.btnMessaggi.setVisibility(View.GONE);
            this.btnSalva.setVisibility(View.GONE);
            this.btnModifica.setVisibility(View.VISIBLE);
            this.btnElimina.setVisibility(View.VISIBLE);
        } else {
            checkBtnSalva();
        }

        Util.dismissDialog(alertDialog, TAG);

    }

    @Override
    public void announceDeleted(Announce announce) {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if( this.announce != null ) {
            int id = this.announce.getId();
            Map<String, String> filters = new HashMap<>(1);
            filters.put("id", "" + id);
            RemoteAPI.getAnnounceList(filters, this::handleAnnounceLoading);
        }

    }
}