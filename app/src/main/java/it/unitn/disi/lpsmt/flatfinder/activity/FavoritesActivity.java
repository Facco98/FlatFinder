package it.unitn.disi.lpsmt.flatfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Announce> announceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.i_miei_annunci);

        this.setupUI();

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        // TODO get announceList from database
        // announceList = ;

        this.adapter = new MyAnnounceListAdapter(this.announceList, this);
        this.recyclerView.setAdapter(this.adapter);
    }

    private void setupUI() {
        this.recyclerView = this.findViewById(R.id.preferiti_view_recycleview);
    }
}
