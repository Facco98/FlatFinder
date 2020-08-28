package it.unitn.disi.lpsmt.flatfinder.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.FavoritesAdapter;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnounceListAdapter;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import it.unitn.disi.lpsmt.flatfinder.model.announce.Announce;

import java.util.*;

public class FavoritesActivity extends AppCompatActivity {
    private static final String TAG = "FavoritesActivity";
    private static final String FAVORITES_ARRAY = "favorite_announces";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Announce> announceList;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.pagina_dei_preferiti);

        this.user = User.getCurrentUser();
        if ( user == null ){

            //Log.i(TAG, "USER IS NOT LOGGED IN");
            Intent i = new Intent(this, LoginActivity.class);
            this.startActivity(i);
            this.finish();
        }

        this.setupUI();
        this.setRecyclerViewAdapter();
    }

    private void setRecyclerViewAdapter() {
        SharedPreferences sharedPreferences = getSharedPreferences("annunci_preferiti", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        // TODO get announceList from database
        announceList = new ArrayList<>();

        Set<String> favorites = sharedPreferences.getStringSet(FAVORITES_ARRAY, null);

        if(favorites != null){
            Iterator iterator = favorites.iterator();
            while(iterator.hasNext()){
                int id = Integer.parseInt((String) iterator.next());
                // get announce from id
                //announceList.add();
            }
        }

        this.adapter = new FavoritesAdapter(this.announceList, this);
        this.recyclerView.setAdapter(this.adapter);
    }

    private void setupUI() {
        this.recyclerView = this.findViewById(R.id.preferiti_view_recycleview);

        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = this.findViewById(R.id.preferiti_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
}
