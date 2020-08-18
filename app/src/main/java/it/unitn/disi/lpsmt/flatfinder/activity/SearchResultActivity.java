package it.unitn.disi.lpsmt.flatfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.fragment.SalvaZonaDialogFragment;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;

import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private static final String TAG = "SearchResultActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    //private List<> list;
    // manca modello della zona/ricerca

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ricerca_esiti_lista);

        this.setupUI();

        this.setRecyclerViewAdapter();

        // todo menu


    }

    private void setupUI() {
        this.recyclerView = this.findViewById(R.id.ricerca_esiti_lista_view_recycleview);
    }

    private void setRecyclerViewAdapter(){
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        // TODO get list from database

        //this.adapter = new MyAnnounceListAdapter(this.list, this);
        this.recyclerView.setAdapter(this.adapter);
    }

    private void salvaRicercaOnClick(){
        DialogFragment dialog = new SalvaZonaDialogFragment();
        dialog.show(getSupportFragmentManager(), "tag");
    }
}