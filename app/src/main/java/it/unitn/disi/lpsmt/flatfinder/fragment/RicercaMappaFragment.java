package it.unitn.disi.lpsmt.flatfinder.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.activity.SearchResultActivity;

public class RicercaMappaFragment extends Fragment {

    private static final String TAG = "RicercaMappaFragment";

    private SearchView searchView;
    private MapView mapView;
    private FloatingActionButton fabGPS;
    private Button btnConferma;

    private MapboxMap map;
    private Marker marker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Mapbox.getInstance(this.requireContext(), getString(R.string.mapbox_access_token));

        View inflateView = inflater.inflate(R.layout.ricerca_disegna_mappa, container, false);

        initUI(inflateView);
        this.mapView.onCreate(savedInstanceState);
        this.mapView.getMapAsync(mapboxMap -> {

            this.map = mapboxMap;
            this.map.setStyle(Style.MAPBOX_STREETS);

        });

        // return view
        return inflateView;
    }

    private void initUI(View inflateView){
        this.searchView = inflateView.findViewById(R.id.ricerca_view_search);
        this.mapView = inflateView.findViewById(R.id.ricerca_view_mapView);
        this.fabGPS = inflateView.findViewById(R.id.ricerca_fab_gps);
        this.btnConferma = inflateView.findViewById(R.id.ricerca_btn_conferma);

        this.fabGPS.setOnClickListener(this::btnGPSOnClick);
        this.btnConferma.setOnClickListener(this::btnConfermaOnClick);
    }

    private void btnGPSOnClick(View view) {

        if (ActivityCompat.checkSelfPermission(this.requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "ENTERD");
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    400);
            return;

        }

        LocationManager locationManager = (LocationManager) this.requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListenerWrapper() {
            @Override
            public void onLocationChanged(Location location) {
                moveCameraToPosition(location.getLatitude(), location.getLongitude());
            }
        }, Looper.getMainLooper());

    }

    private void btnConfermaOnClick(View view) {

        if( this.marker == null ){

            Toast.makeText(this.requireContext(), R.string.must_select_location, Toast.LENGTH_SHORT).show();
            return;

        }

        Intent intent = new Intent(this.getActivity(), SearchResultActivity.class);
        intent.putExtra("latitudineCentro", marker.getPosition().getLatitude());
        intent.putExtra("longitudineCentro", marker.getPosition().getLongitude());
        startActivity(intent);
    }

    private void moveCameraToPosition(double latitude, double longitude){

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                latitude, longitude
        )).zoom(17).build();
        if( marker != null )
            map.removeMarker(marker);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000);
        marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));

    }

}

abstract class LocationListenerWrapper implements LocationListener {


    @Override
    public abstract void onLocationChanged(Location location);

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
