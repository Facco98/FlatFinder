package it.unitn.disi.lpsmt.flatfinder.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.AnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.fragment.FilterCompletion;
import it.unitn.disi.lpsmt.flatfinder.fragment.RicercaFiltriDialogFragment;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.Zone;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;
import it.unitn.disi.lpsmt.flatfinder.remote.RemoteAPI;
import it.unitn.disi.lpsmt.flatfinder.util.Util;

import java.util.*;

public class SearchResultActivity extends AppCompatActivity implements FilterCompletion {

    private static final String TAG = "SearchResultActivity";

    private Toolbar toolbar;
    private Button btnFiltri, btnOrdina;
    private Button btnSalvaRicerca;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Announce> announceList;

    private Double latitudeCenter, longitudeCenter, maxDistance;
    private String address;

    private User user;

    private AlertDialog alertDialog;

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

        this.alertDialog = Util.getDialog(this, TAG);

        this.latitudeCenter = getIntent().getDoubleExtra("latitudineCentro", 0);
        this.longitudeCenter = getIntent().getDoubleExtra("longitudineCentro", 0);
        this.address = getIntent().getStringExtra("indirizzo");

        this.setupUI();
        this.setupToolbar();
        this.setRecyclerViewAdapter();



        Map<String, String> filters = new HashMap<>();
        this.maxDistance = 1.0;
        filters.put("distanzaMax", "1");

        updateList(filters);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
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

        this.btnSalvaRicerca.setOnClickListener(this::btnSalvaRicercaOnClick);
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

        if(announceList != null){
            switch (itemId){
                case R.id.ricerca_esiti_menu_ordinaRecenti:
                    announceList.sort((a1, a2) -> a2.getDate().compareTo(a1.getDate()));
                    break;
                case R.id.ricerca_esiti_menu_ordinaCostosi:
                    announceList.sort((a1, a2)-> Math.round(a2.getRentPerMonth() - a1.getRentPerMonth()));
                    break;
                case R.id.ricerca_esiti_menu_ordinaEconomici:
                    announceList.sort((a1, a2)-> Math.round(a1.getRentPerMonth() - a2.getRentPerMonth()));
                    break;
                case R.id.ricerca_esiti_menu_ordinaGrandi:
                    announceList.sort((a1, a2)-> Math.round(a2.getSize() - a1.getSize()));
                    break;
                case R.id.ricerca_esiti_menu_ordinaPiccoli:
                    announceList.sort((a1, a2)-> Math.round(a1.getSize() - a2.getSize()));
                    break;
                case R.id.ricerca_esiti_menu_ordinaPiuLocali:
                    announceList.sort((a1, a2)-> a2.getnLocals() - a1.getnLocals());
                    break;
                case R.id.ricerca_esiti_menu_ordinaMenoLocali:
                    announceList.sort(Comparator.comparingInt(Announce::getnLocals));
                    break;
                default:
                    Log.d(TAG, "context menu option not valid");
            }
            this.adapter = new AnnounceListAdapter(this.announceList, this);
            this.recyclerView.setAdapter(this.adapter);

            Toast.makeText(this,"Lista ordinata con successo", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void btnSalvaRicercaOnClick(View v) {
        Util.showDialog(alertDialog, TAG);
        Log.d(TAG, "salva ricerca tap");
        Zone zone = new Zone(null, this.longitudeCenter, this.latitudeCenter, this.maxDistance, address);
        RemoteAPI.registerZone(this.user, zone, (string, ex)->{

            if( ex != null ) {
                ex.printStackTrace();
                Toast.makeText(this, R.string.zone_registered_error, Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, R.string.zone_registered, Toast.LENGTH_SHORT).show();

            }
        });
        Util.dismissDialog(alertDialog, TAG);

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
        Util.showDialog(alertDialog, TAG);


        Log.i(TAG, filters.toString());
        filters.put("latitudineCentro", ""+this.latitudeCenter);
        filters.put("longitudineCentro", ""+this.longitudeCenter);
        filters.put("attivo", ""+true);
        filters.put("username_creatorenot", this.user.getSub());
        if( filters.get("distanzaMax") != null)
            this.maxDistance = Double.parseDouble(filters.get("distanzaMax"));

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

        Util.dismissDialog(alertDialog, TAG);

    }

    @Override
    public void onFilterChooseComplete(Map<String, String> filters) {

        updateList(filters);

    }
}