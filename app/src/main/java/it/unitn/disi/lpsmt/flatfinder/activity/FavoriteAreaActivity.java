package it.unitn.disi.lpsmt.flatfinder.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.FavoriteAreaListAdapter;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.model.Search;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;

import java.util.List;

public class FavoriteAreaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Search> searchList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_area);

        this.setupUI();

        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        // TODO get searchList from database
        // searchList = ;

        this.adapter = new FavoriteAreaListAdapter(this.searchList, this);
        this.recyclerView.setAdapter(this.adapter);


    }

    private void setupUI() {
        this.recyclerView = this.findViewById(R.id.favorite_area_view_recycleview);
    }
}