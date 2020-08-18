package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.MapView;
import it.unitn.disi.lpsmt.flatfinder.R;

public class RicercaMappaFragment extends Fragment {

    private static final String TAG = "RicercaMappaFragment";

    private SearchView searchView;
    private MapView mapView;
    private Button btnGPS;
    private Button btnConferma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflateView = inflater.inflate(R.layout.ricerca_disegna_mappa, container, false);

        initUI(inflateView);

        // return view
        return inflateView;
    }

    private void initUI(View inflateView){
        this.searchView = inflateView.findViewById(R.id.ricerca_view_search);
        this.mapView = inflateView.findViewById(R.id.ricerca_view_mapView);
        this.btnGPS = inflateView.findViewById(R.id.ricerca_fab_gps);
        this.btnConferma = inflateView.findViewById(R.id.ricerca_btn_conferma);

        this.btnGPS.setOnClickListener(this::btnGPSOnClick);
        this.btnConferma.setOnClickListener(this::btnConfermaOnClick);
    }

    private void btnGPSOnClick(View view) {
    }

    private void btnConfermaOnClick(View view) {
    }
}
