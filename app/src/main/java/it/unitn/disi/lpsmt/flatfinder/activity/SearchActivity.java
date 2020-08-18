package it.unitn.disi.lpsmt.flatfinder.activity;

import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.fragment.RicercaFragment;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Fragment fragment = new RicercaFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_lyt_layout, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();
    }
}
