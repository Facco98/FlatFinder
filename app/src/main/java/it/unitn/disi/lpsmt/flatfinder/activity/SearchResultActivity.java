package it.unitn.disi.lpsmt.flatfinder.activity;

import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.AnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.fragment.FilterCompletion;
import it.unitn.disi.lpsmt.flatfinder.fragment.RicercaFiltriDialogFragment;
import it.unitn.disi.lpsmt.flatfinder.fragment.SalvaRicercaDialogFragment;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;
import it.unitn.disi.lpsmt.flatfinder.task.Completion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity implements FilterCompletion {

    private static final String TAG = "SearchResultActivity";

    private Toolbar toolbar;
    private Button btnFiltri, btnOrdina;
    private ToggleButton btnSalvaRicerca;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Announce> announceList;

    private double latitudeCenter, longitudeCenter;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ricerca_esiti_lista);

        this.user = User.getCurrentUser();
        if ( user == null ){

            //Log.i(TAG, "USER IS NOT LOGGED IN");
            Intent i = new Intent(this, LoginActivity.class);
            this.startActivity(i);
            this.finish();
        }

        this.latitudeCenter = getIntent().getDoubleExtra("latitudineCentro", 0);
        this.longitudeCenter = getIntent().getDoubleExtra("longitudineCentro", 0);

        this.setupUI();
        this.setupToolbar();
        this.setRecyclerViewAdapter();



        Map<String, String> filters = new HashMap<>();
        filters.put("distanzaMax", "1");

        updateList(filters);

    }

    private void setupToolbar() {
        toolbar = this.findViewById(R.id.ricerca_esiti_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void setupUI() {
        this.btnFiltri = this.findViewById(R.id.ricerca_esiti_btn_filtri);
        this.btnOrdina = this.findViewById(R.id.ricerca_esiti_btn_ordina);
        this.btnSalvaRicerca = this.findViewById(R.id.ricerca_esiti_btn_salvaRicerca);
        this.recyclerView = this.findViewById(R.id.ricerca_esiti_lista_view_recycleview);

        this.btnSalvaRicerca.setOnCheckedChangeListener(this::btnSalvaRicercaOnClick);
        this.btnFiltri.setOnClickListener(this::btnFiltriOnClick);
        this.btnOrdina.setOnClickListener(this::btnOrdinaOnClick);
        
    }

    private void btnOrdinaOnClick(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.ricerca_esiti_ordina_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(this::popupMenuItemOnClick);
    }

    private boolean popupMenuItemOnClick(MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        // todo menu options
        switch (itemId){
            case R.id.ricerca_esiti_menu_ordinaRilevanza:
                break;
            case R.id.ricerca_esiti_menu_ordinaCostosi:
                break;
            case R.id.ricerca_esiti_menu_ordinaEconomici:
                break;
            case R.id.ricerca_esiti_menu_ordinaGrandi:
                break;
            case R.id.ricerca_esiti_menu_ordinaPiccoli:
                break;
            case R.id.ricerca_esiti_menu_ordinaPiuLocali:
                break;
            case R.id.ricerca_esiti_menu_ordinaMenoLocali:
                break;
            default:
                Log.d(TAG, "context menu option not valid");
        }

        return true;
    }

    private void btnSalvaRicercaOnClick(CompoundButton compoundButton, boolean b) {
        if(b){
            Log.d(TAG, "salva ricerca tap");
            DialogFragment dialog = new SalvaRicercaDialogFragment();
            dialog.show(getSupportFragmentManager(), "tag");
        } else {
            Log.d(TAG, "cancella ricerca tap");
        }
    }

    private void btnFiltriOnClick(View view) {
        DialogFragment dialog = new RicercaFiltriDialogFragment(this);
        dialog.show(getSupportFragmentManager(), "filtri");
    }

    private void setRecyclerViewAdapter(){
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);
    }

    private void updateList(Map<String, String> filters) {


        Log.i(TAG, filters.toString());
        filters.put("latitudineCentro", ""+this.latitudeCenter);
        filters.put("longitudineCentro", ""+this.longitudeCenter);
        filters.put("attivo", ""+true);
        filters.put("username_creatorenot", this.user.getSub());

        RemoteAPI.getAnnounceList(filters, (announces, exception) -> {

            if( exception != null ){

                Toast.makeText(this, "Errore durante il caricamento dei dati", Toast.LENGTH_SHORT)
                        .show();
                exception.printStackTrace();

            } else if ( announces != null ){

                this.announceList = announces;
                this.adapter = new AnnounceListAdapter(this.announceList, this);
                this.recyclerView.setAdapter(this.adapter);
                Log.i(TAG, announces.toString());
                Log.i(TAG, "Data retrivered");

            }

        });

    }

    @Override
    public void onFilterChooseComplete(Map<String, String> filters) {

        updateList(filters);

    }
}