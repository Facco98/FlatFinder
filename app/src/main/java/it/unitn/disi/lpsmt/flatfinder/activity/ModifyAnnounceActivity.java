package it.unitn.disi.lpsmt.flatfinder.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.PhotosAdapter;
import it.unitn.disi.lpsmt.flatfinder.exception.EmptyFieldException;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Category;
import it.unitn.disi.lpsmt.flatfinder.model.announce.EnergeticClass;
import it.unitn.disi.lpsmt.flatfinder.model.announce.FornitureStatus;
import it.unitn.disi.lpsmt.flatfinder.model.announce.LocalType;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Photo;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;
import it.unitn.disi.lpsmt.flatfinder.util.Util;

public class ModifyAnnounceActivity extends AppCompatActivity {

    private static final String TAG = "ModifyAnnounceActivity";

    //viwes
    private Spinner spnTipologiaStruttura;
    private Spinner spnCategoria;
    private EditText txtIndirizzo;
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
    private Button btnAnnulla;
    private Button btnScattaFoto;
    private PhotosAdapter gridPhotosAdapter;
    private ViewPager photosPager;
    private ArrayAdapter<LocalType> adapterTipologiaStruttura;
    private ArrayAdapter<FornitureStatus> adapterArredamento;
    private ArrayAdapter<Category> adapterCategoria;
    private ArrayAdapter<EnergeticClass> adapterClasseEnergetica;
    private CheckBox checkBoxAttivo;

    private Point point;

    private User user;
    private Announce announce;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.nuovo_annuncio);
        this.user = User.getCurrentUser();
        if( user == null ){

            Intent i = new Intent(this, LoginActivity.class);
            this.startActivity(i);
            this.finish();

        }

        this.alertDialog = Util.getDialog(this, TAG);

        Intent i = this.getIntent();
        if( i != null && i.hasExtra("announceID") ){

            int announceID = i.getIntExtra("announceID", 1);
            Map<String, String> filters = new HashMap<>(1);
            filters.put("id", ""+announceID);
            Util.showDialog(alertDialog, TAG);
            RemoteAPI.getAnnounceList(filters, this::handleAnnounceLoading);

        }


        this.gridPhotosAdapter = new PhotosAdapter(new ArrayList<>(), this); //??
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
        this.btnAnnulla = this.findViewById(R.id.nuovo_annuncio_annulla);
        this.txtContatti = this.findViewById(R.id.nuovo_annuncio_txt_contattiCellulare);
        this.btnCaricaFoto = this.findViewById(R.id.nuovo_annuncio_btn_caricafoto);
        this.photosPager = this.findViewById(R.id.nuovo_annuncio_view_pager);
        this.btnScattaFoto = this.findViewById(R.id.nuovo_annuncio_btn_scattaFoto);
        this.checkBoxAttivo = this.findViewById(R.id.nuovo_annuncio_checkBox_attivo);

        photosPager.setAdapter(this.gridPhotosAdapter);

        TabLayout dotsIndicator = this.findViewById(R.id.nuovo_annuncio_lyt_dotIndicator);
        dotsIndicator.setupWithViewPager(photosPager, true);

        this.btnCaricaFoto.setOnClickListener(this::btnCaricaFotoOnClick);
        this.btnAvanti.setOnClickListener(this::btnAvantiOnCLick);
        this.btnAnnulla.setOnClickListener(this::btnAnnullaOnClick);
        this.btnScattaFoto.setOnClickListener(this::btnScattaFotoOnClick);

        /*ArrayAdapter<LocalType> */ adapterTipologiaStruttura = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, LocalType.values());
        this.spnTipologiaStruttura.setAdapter(adapterTipologiaStruttura);
        /*ArrayAdapter<FornitureStatus>*/ adapterArredamento = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, FornitureStatus.values());
        this.spnArredamento.setAdapter(adapterArredamento);
        /*ArrayAdapter<Category>*/ adapterCategoria = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Category.values());
        this.spnCategoria.setAdapter(adapterCategoria);
        /*ArrayAdapter<EnergeticClass>*/ adapterClasseEnergetica = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, EnergeticClass.values());
        this.spnClasseEnergetica.setAdapter(adapterClasseEnergetica);

        this.btnAnnulla.setVisibility(View.VISIBLE);
        this.btnAvanti.setText("Salva"); //??

        this.checkBoxAttivo.setVisibility(View.VISIBLE);

    }

    private void setupToolbar() {
        Toolbar toolbar = this.findViewById(R.id.nuovo_annuncio_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Modifica annuncio");
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

                for(Photo p: photos)
                    this.gridPhotosAdapter.addItem(p);
                this.gridPhotosAdapter.notifyDataSetChanged();
                Util.dismissDialog(alertDialog, TAG);
            }

        });

        Log.i(TAG, announce.toString());
        this.txtIndirizzo.setText(String.valueOf(announce.getAddress()));
        this.txtIndirizzo.setEnabled(false);
        this.txtAffitto.setText(String.valueOf((int) announce.getRentPerMonth()));
        this.txtAltreSpese.setText(String.valueOf(announce.getExtras()));
        this.txtDescrizione.setText(String.valueOf(announce.getDescription()));
        this.txtDisponibilità.setText(Util.dateToString(announce.getStart()));
        this.txtNumeroLocali.setText(String.valueOf(announce.getnLocals()));
        this.txtNumeroBagni.setText(String.valueOf(announce.getnBathrooms()));
        this.txtDimensione.setText(String.valueOf((int) announce.getSize()));
        this.txtNumeroPiano.setText(String.valueOf(announce.getFloor()));
        this.txtContatti.setText(String.valueOf(announce.getContact()));

        this.spnTipologiaStruttura.setSelection(adapterTipologiaStruttura.getPosition(announce.getType()));
        this.spnCategoria.setSelection(adapterCategoria.getPosition(announce.getCategory()));
        this.spnArredamento.setSelection(adapterArredamento.getPosition(announce.getFornitureStatus()));
        this.spnClasseEnergetica.setSelection(adapterClasseEnergetica.getPosition(announce.getEnergeticClass()));

        this.checkBoxAttivo.setChecked(announce.isActive());

    }

    public boolean onSupportNavigateUp(){
        this.finish();
        return super.onSupportNavigateUp();
    }

    private void btnAnnullaOnClick(View v){
        this.finish(); //torna indietro
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

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == 1) {
                CarmenFeature feature = PlaceAutocomplete.getPlace(data);

                // set address to toolbar
                this.txtIndirizzo.setText(feature.placeName());
                this.point = (Point) feature.geometry();

            } else if( requestCode == 900 ){

                Bundle extras = data.getExtras();
                Bitmap bmp = (Bitmap) extras.get("data");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); //100-best quality
                String encoded = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
                Photo photo = new Photo(null, encoded );
                this.gridPhotosAdapter.addItem(photo);
                this.gridPhotosAdapter.notifyDataSetChanged();



            }
        }
    }

    private void btnAvantiOnCLick( View v ){

        Log.d(TAG, "Button Avanti did click");

        LocalType tipoLocale = (LocalType) (this.spnTipologiaStruttura.getSelectedItem());
        FornitureStatus statoArredamento = (FornitureStatus) this.spnArredamento.getSelectedItem();
        Category categoria = (Category) this.spnCategoria.getSelectedItem();
        EnergeticClass classeEnergetica = (EnergeticClass) this.spnClasseEnergetica.getSelectedItem();

        try {
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

            //modifico i dati dell'annuncio
            announce.setType(tipoLocale);
            announce.setCategory(categoria);
            announce.setFornitureStatus(statoArredamento);
            announce.setEnergeticClass(classeEnergetica);
            announce.setRentPerMonth(affittoMensile);
            announce.setDescription(descrizione);
            announce.setStart(inizioDisponibilita);
            announce.setEnd(fineDisponibilita);
            announce.setnLocals(numeroLocali);
            announce.setnBathrooms(numeroBagni);
            announce.setFloor(piano);
            announce.setSize(dimensione);
            announce.setExtras(altreSpese);
            announce.setContact(contatti);
            announce.setActive(checkBoxAttivo.isChecked());


            System.out.println(new Gson().toJson(announce));

            Util.showDialog(this.alertDialog, TAG);
            RemoteAPI.updateAnnounce(announce, this::modificaAnnuncio);

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

    private void checkEmptyValue(String text, String fieldName) throws EmptyFieldException {

        if( text == null || text.trim().isEmpty() || text.trim().equals("") )
            throw new EmptyFieldException(fieldName);

    }

    private void modificaAnnuncio(String response, Exception ex) {
        if (ex != null) {

            ex.printStackTrace();
            Toast.makeText(this, "Errore durante la modifica dell'annuncio", Toast.LENGTH_SHORT).show();

        } else if (response != null) {

            Log.i(TAG, response);
            RemoteAPI.uploadPhotosForAnnounce(this.gridPhotosAdapter.getItems(), this.announce.getId(), (res, err) -> {

                if (err != null) {

                    err.printStackTrace();
                    Toast.makeText(this, "Errore durante la modifica dell'annuncio", Toast.LENGTH_SHORT).show();

                } else {

                    Log.i(TAG, res);
                    Util.dismissDialog(this.alertDialog, TAG);
                    this.finish();

                }

            });
        }
    }

    private void btnScattaFotoOnClick(View v){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 900);
        }

    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.img_menu_delete) {
            int currentItemPosition = this.photosPager.getCurrentItem();
            this.gridPhotosAdapter.removeItem(currentItemPosition);
            this.gridPhotosAdapter.notifyDataSetChanged();
            //this.photosPager.setAdapter(this.gridPhotosAdapter);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_context_menu, menu);
    }

}
