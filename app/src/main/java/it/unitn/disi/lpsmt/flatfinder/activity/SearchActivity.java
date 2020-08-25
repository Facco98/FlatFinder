package it.unitn.disi.lpsmt.flatfinder.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.fragment.RicercaMappaFragment;
import it.unitn.disi.lpsmt.flatfinder.model.User;

public class SearchActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.user = User.getCurrentUser();
        if ( user == null ){

            //Log.i(TAG, "USER IS NOT LOGGED IN");
            Intent i = new Intent(this, LoginActivity.class);
            this.startActivity(i);
            this.finish();
        }


        setupToolbar();

        Fragment fragment = new RicercaMappaFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_lyt_layout, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    private void setupToolbar() {
        Toolbar toolbar = this.findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }
}
