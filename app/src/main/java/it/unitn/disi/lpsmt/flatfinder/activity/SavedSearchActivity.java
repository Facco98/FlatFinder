package it.unitn.disi.lpsmt.flatfinder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.SavedSearchListAdapter;
import it.unitn.disi.lpsmt.flatfinder.model.Search;
import it.unitn.disi.lpsmt.flatfinder.model.User;

import java.util.ArrayList;
import java.util.List;

public class SavedSearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Search> searchList = null;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_search);

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

    private void setupToolbar() {
        Toolbar toolbar = this.findViewById(R.id.saved_search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void setRecyclerViewAdapter() {
        this.layoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.layoutManager);

        // TODO get searchList from database

        if(searchList == null){
            searchList = new ArrayList<>();
        }

        this.adapter = new SavedSearchListAdapter(this.searchList, this);
        this.recyclerView.setAdapter(this.adapter);
    }

    private void setupUI() {
        setupToolbar();
        this.recyclerView = this.findViewById(R.id.saved_search_view_recycleview);
    }
}