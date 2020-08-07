package it.unitn.disi.lpsmt.flatfinder.activity;

import android.widget.TableLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.adapter.MyAnnouncesAdapter;
import it.unitn.disi.lpsmt.flatfinder.fragment.MyAnnouncesListFragment;
import it.unitn.disi.lpsmt.flatfinder.fragment.RicercaFragment;

public class MyAnnouncesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.i_miei_annunci);

        this.setupUI();


        MyAnnouncesAdapter myAnnouncesAdapter = new MyAnnouncesAdapter(getSupportFragmentManager());
        myAnnouncesAdapter.addToList(new MyAnnouncesListFragment(), "PREFERITI");
        myAnnouncesAdapter.addToList(new MyAnnouncesListFragment(), "ATTIVI");
        myAnnouncesAdapter.addToList(new MyAnnouncesListFragment(), "RITIRATI");
        this.viewPager.setAdapter(myAnnouncesAdapter);
        tabLayout.setupWithViewPager(this.viewPager);
    }

    private void setupUI(){
        this.tabLayout = this.findViewById(R.id.miei_annunci_lyt_tablayout);
        this.viewPager = this.findViewById(R.id.miei_annunci_viewpager);
    }
}
