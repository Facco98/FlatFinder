package it.unitn.disi.lpsmt.flatfinder.activity;

import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.AnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.fragment.RicercaFiltriDialogFragment;
import it.unitn.disi.lpsmt.flatfinder.fragment.SalvaRicercaDialogFragment;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultActivity";

    private Toolbar toolbar;
    private Button btnFiltri, btnOrdina;
    private ToggleButton btnSalvaRicerca;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Announce> announceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ricerca_esiti_lista);

        this.setupUI();

        this.setupToolbar();

        this.setRecyclerViewAdapter();

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
        DialogFragment dialog = new RicercaFiltriDialogFragment();
        dialog.show(getSupportFragmentManager(), "filtri");
    }

    private void setRecyclerViewAdapter(){
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        // TODO get list from database

        if(announceList == null){
            announceList = new ArrayList<>();
        }

        this.adapter = new AnnounceListAdapter(this.announceList, this);
        this.recyclerView.setAdapter(this.adapter);
    }
}