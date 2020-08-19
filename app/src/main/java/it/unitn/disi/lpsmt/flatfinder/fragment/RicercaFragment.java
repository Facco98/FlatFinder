package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import it.unitn.disi.lpsmt.flatfinder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RicercaFragment extends Fragment {

    private SearchView searchView;
    private CardView crdDisegnaMappa;
    private CardView crdGPS;

    public RicercaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ricerca, container, false);

        initUI(view);

        return view;
    }

    private void initUI(View view){
        this.searchView = view.findViewById(R.id.ricerca_view_search);
        this.crdDisegnaMappa = view.findViewById(R.id.ricerca_view_disegnabtn);
        this.crdGPS = view.findViewById(R.id.ricerca_view_gpsbtn);

        crdDisegnaMappa.setOnClickListener(this::cardDisegnaMappaOnCLick);
        crdGPS.setOnClickListener(this::crdGPSOnClick);
    }

    private void crdGPSOnClick(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        int currentFragmentId = ((ViewGroup)getView().getParent()).getId();
        Fragment nextFragment = new RicercaMappaFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(currentFragmentId, nextFragment)
                .addToBackStack(null)
                .commit();
    }

    private void cardDisegnaMappaOnCLick(View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        int currentFragmentId = ((ViewGroup)getView().getParent()).getId();
        Fragment nextFragment = new RicercaMappaFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(currentFragmentId, nextFragment)
                .addToBackStack(null)
                .commit();
    }
}
