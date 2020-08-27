package it.unitn.disi.lpsmt.flatfinder.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.PhotosAdapter;
import it.unitn.disi.lpsmt.flatfinder.exception.EmptyFieldException;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.announce.*;
import it.unitn.disi.lpsmt.flatfinder.model.gecoding.GeocodingResponse;
import it.unitn.disi.lpsmt.flatfinder.model.gecoding.GeocodingResult;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;
import it.unitn.disi.lpsmt.flatfinder.util.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    private EditText txtContatti;
    private Button btnAvanti;
    private Button btnCaricaFoto;
    private PhotosAdapter gridPhotosAdapter;

    private Point point;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.nuovo_annuncio);
        this.user = User.getCurrentUser();
        if( user == null ){

            Intent i = new Intent(this, LoginActivity.class);
            this.startActivity(i);
            this.finish();

        }
        this.gridPhotosAdapter = new PhotosAdapter(new ArrayList<>(), this);
        this.setupUI();
    }

    private void setupUI(){

        setupToolbar();

        this.spnTipologiaStruttura = this.findViewById(R.id.nuovo_annuncio_spinner_tipologia_struttura);
        this.spnCategoria = this.findViewById(R.id.nuovo_annuncio_spinner_categoria);
        this.txtIndirizzo = this.findViewById(R.id.nuovo_annuncio_txt_indirizzo);
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
        this.btnAvanti = this.findViewById(R.id.nuovo_annuncio_btn_avanti);
        this.txtContatti = this.findViewById(R.id.nuovo_annuncio_txt_contattiCellulare);
        this.btnCaricaFoto = this.findViewById(R.id.nuovo_annuncio_btn_caricafoto);
        ViewPager photosPager = this.findViewById(R.id.nuovo_annuncio_view_pager);

        photosPager.setAdapter(this.gridPhotosAdapter);

        TabLayout dotsIndicator = this.findViewById(R.id.nuovo_annuncio_lyt_dotIndicator);
        dotsIndicator.setupWithViewPager(photosPager, true);

        this.btnCaricaFoto.setOnClickListener(this::btnCaricaFotoOnClick);
        this.btnAvanti.setOnClickListener(this::btnAvantiOnClick);
        this.txtIndirizzo.setOnClickListener(this::txtIndirizzoOnClick);

        ArrayAdapter<LocalType> adapterTipologiaStruttura = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, LocalType.values());
        this.spnTipologiaStruttura.setAdapter(adapterTipologiaStruttura);
        ArrayAdapter<FornitureStatus> adapterArredamento = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, FornitureStatus.values());
        this.spnArredamento.setAdapter(adapterArredamento);
        ArrayAdapter<Category> adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Category.values());
        this.spnCategoria.setAdapter(adapterCategoria);
        ArrayAdapter<EnergeticClass> adapterClasseEnergetica = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, EnergeticClass.values());
        this.spnClasseEnergetica.setAdapter(adapterClasseEnergetica);

    }

    private void txtIndirizzoOnClick(View view) {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .language("it")
                        .country("it")
                        .build(PlaceOptions.MODE_FULLSCREEN))
                .build(NewAnnounceActivity.this);
        startActivityForResult(intent, 1);
    }

    private void btnAvantiOnClick( View v ){

        Log.d(TAG, "Button Avanti did clicl");

        if( this.txtIndirizzo.getText().toString().trim().isEmpty() ) {
            Toast.makeText(this, R.string.new_announce_must_validate_address, Toast.LENGTH_SHORT).show();
            return;
        }
        LocalType tipoLocale = (LocalType) (this.spnTipologiaStruttura.getSelectedItem());
        FornitureStatus statoArredamento = (FornitureStatus) this.spnArredamento.getSelectedItem();
        Category categoria = (Category) this.spnCategoria.getSelectedItem();
        EnergeticClass classeEnergetica = (EnergeticClass) this.spnClasseEnergetica.getSelectedItem();
        try {
            String indirizzo = this.txtIndirizzo.getText().toString();

            this.checkEmptyValue(indirizzo, "Indirizzo");
            this.checkEmptyValue(this.txtAffitto.getText().toString(), "Affitto");
            this.checkEmptyValue(this.txtAltreSpese.getText().toString(), "Altre Spese");

            Float affittoMensile = Float.parseFloat(this.txtAffitto.getText().toString());
            Float altreSpese = Float.parseFloat(this.txtAltreSpese.getText().toString());

            String contatti = this.txtContatti.getText().toString();
            this.checkEmptyValue(contatti, "Contatti");

            String descrizione = this.txtDescrizione.getText().toString();
            String disponibilita = this.txtDisponibilità.getText().toString();

            this.checkEmptyValue(disponibilita, "Disponibilità");
            this.checkEmptyValue(this.txtNumeroBagni.getText().toString(), "Numero bagni");
            this.checkEmptyValue(this.txtNumeroLocali.getText().toString(), "Numero locali");
            this.checkEmptyValue(this.txtNumeroPiano.getText().toString(), "Piano");
            this.checkEmptyValue(this.txtDimensione.getText().toString(), "Dimensione");

            Integer numeroLocali = Integer.parseInt(this.txtNumeroLocali.getText().toString());
            Integer numeroBagni = Integer.parseInt(this.txtNumeroBagni.getText().toString());
            Integer piano = Integer.parseInt(this.txtNumeroPiano.getText().toString());
            Float dimensione = Float.parseFloat(this.txtDimensione.getText().toString());

            Date inizioDisponibilita = Util.stringToDate(disponibilita.split("-")[0]);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inizioDisponibilita);
            calendar.add(Calendar.YEAR, 1);
            Date fineDisponibilita = calendar.getTime();

            try {

                fineDisponibilita = Util.stringToDate(disponibilita.split("-")[1]);

            } catch ( Exception ex ){

                ex.printStackTrace();

            }

            Announce announce = new Announce(null, this.user.getSub(), tipoLocale, categoria, indirizzo, affittoMensile,
                    descrizione, statoArredamento,classeEnergetica, inizioDisponibilita, fineDisponibilita, numeroLocali,
                    numeroBagni, piano, dimensione, altreSpese, new Date(), contatti, true, this.point.latitude(), this.point.longitude());
            System.out.println(new Gson().toJson(announce));
            RemoteAPI.createNewAnnounce(announce,this::nuovoAnnuncioPubblicato);
        } catch (EmptyFieldException ex){

            StringBuilder sb = new StringBuilder();
            sb.append(R.string.common_empty_field).append(ex.field);
            Toast.makeText(this, R.string.common_empty_field, Toast.LENGTH_SHORT).show();


        } catch (NumberFormatException ex){

            Toast.makeText(this,R.string.invalid_number, Toast.LENGTH_SHORT ).show();

        } catch (ParseException ex){

            Toast.makeText(this,R.string.invalid_date, Toast.LENGTH_SHORT ).show();

        }

    }

    private void checkEmptyValue(String text, String fieldName) throws EmptyFieldException{

        if( text == null || text.trim().isEmpty() || text.trim().equals("") )
            throw new EmptyFieldException(fieldName);

    }

    private void nuovoAnnuncioPubblicato(String response, Exception e){

        if( e != null ){

            e.printStackTrace();
            Toast.makeText(this, "Errore durante il caricamento dell'annuncio", Toast.LENGTH_SHORT).show();

        } else if( response != null ){

            try {
                JSONObject responseJSON = new JSONObject(response);
                int announceID = responseJSON.getInt("createdAnnounceID");
                if( this.gridPhotosAdapter.getItems().size() > 0) {
                    RemoteAPI.uploadPhotosForAnnounce(this.gridPhotosAdapter.getItems(), announceID, (res, ex) -> {

                        if (res != null) {

                            Log.i(TAG, res);
                            this.finish();

                        } else if (ex != null)
                            ex.printStackTrace();

                    });
                } else {

                    this.finish();

                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }


        }

    }

    private void btnCaricaFotoOnClick(View v){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/png");
        this.startActivityForResult(intent, Util.SELECT_IMAGE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Util.SELECT_IMAGE && data != null && data.getData() != null) {

                try {
                    InputStream is = this.getContentResolver().openInputStream(data.getData());
                    Bitmap bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] base64Encoded = Base64.encode(baos.toByteArray(), Base64.DEFAULT);
                    String str = new String(base64Encoded);
                    Photo photo = new Photo(null, str);
                    this.gridPhotosAdapter.addItem(photo);
                    this.gridPhotosAdapter.notifyDataSetChanged();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == Activity.RESULT_OK && requestCode == 1) {
                CarmenFeature feature = PlaceAutocomplete.getPlace(data);

                // set address to toolbar
                this.txtIndirizzo.setText(feature.placeName());
                this.point = (Point) feature.geometry();

            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = this.findViewById(R.id.nuovo_annuncio_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
}