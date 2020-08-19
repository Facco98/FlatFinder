package it.unitn.disi.lpsmt.flatfinder.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TableLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnouncesAdapter;
import it.unitn.disi.lpsmt.flatfinder.fragment.MyAnnouncesListFragment;
import it.unitn.disi.lpsmt.flatfinder.fragment.RicercaFragment;

public class MyAnnouncesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fabAddNewAnnounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.i_miei_annunci);

        this.setupUI();

        MyAnnouncesAdapter myAnnouncesAdapter = new MyAnnouncesAdapter(getSupportFragmentManager());
        myAnnouncesAdapter.addToList(new MyAnnouncesListFragment(), "ATTIVI");
        myAnnouncesAdapter.addToList(new MyAnnouncesListFragment(), "RITIRATI");
        this.viewPager.setAdapter(myAnnouncesAdapter);
        tabLayout.setupWithViewPager(this.viewPager);
    }

    private void setupUI(){
        this.tabLayout = this.findViewById(R.id.miei_annunci_lyt_tablayout);
        this.viewPager = this.findViewById(R.id.miei_annunci_viewpager);
        this.fabAddNewAnnounce = this.findViewById(R.id.miei_annunci_fab_addNewAnnounce);

        this.fabAddNewAnnounce.setOnClickListener(this::fabAddNewAnnounceOnClick);

        setupToolbar();
    }

    private void fabAddNewAnnounceOnClick(View view) {
        Intent intent = new Intent(this, NewAnnounceActivity.class);
        this.startActivity(intent);
    }

    private void setupToolbar() {
        Toolbar toolbar = this.findViewById(R.id.miei_annunci_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

}
