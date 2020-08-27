package it.unitn.disi.lpsmt.flatfinder.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import it.unitn.disi.lpsmt.flatfinder.R;
import it.unitn.disi.lpsmt.flatfinder.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private User user;

    private MapView mapView;
    private FloatingActionButton fabGPS;
    private Button btnConferma;
    private Toolbar toolbar;

    private MapboxMap map;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_search);

        this.user = User.getCurrentUser();
        if ( user == null ){

            //Log.i(TAG, "USER IS NOT LOGGED IN");
            Intent i = new Intent(this, LoginActivity.class);
            this.startActivity(i);
            this.finish();
        }

        this.setupUI();

        this.mapView.onCreate(savedInstanceState);
        this.mapView.getMapAsync(mapboxMap -> {

            this.map = mapboxMap;
            this.map.setStyle(Style.MAPBOX_STREETS);

        });


    }



    private void setupUI() {
        setupToolbar();

        this.mapView = this.findViewById(R.id.search_view_mapView);
        this.fabGPS = this.findViewById(R.id.search_fab_gps);
        this.btnConferma = this.findViewById(R.id.search_btn_conferma);

        this.fabGPS.setOnClickListener(this::btnGPSOnClick);
        this.btnConferma.setOnClickListener(this::btnConfermaOnClick);
    }

    private void setupToolbar() {
        toolbar = this.findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        toolbar.setOnClickListener(this::toolbarOnClick);
    }

    private void toolbarOnClick(View view) {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.mapbox_access_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .language("it")
                        .country("it")
                        .build(PlaceOptions.MODE_FULLSCREEN))
                .build(SearchActivity.this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);

            // set address to toolbar
            toolbar.setTitle(feature.placeName());


            Point point = (Point) feature.geometry();
            if (map != null) {
                moveCameraToPosition(point.latitude(),
                        point.longitude());
            }

        }
    }

    private void btnGPSOnClick(View view) {

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "ENTERD");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    400);
            return;

        }

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListenerWrapper() {
            @Override
            public void onLocationChanged(Location location) {
                moveCameraToPosition(location.getLatitude(), location.getLongitude());
            }
        }, Looper.getMainLooper());

    }

    private void btnConfermaOnClick(View view) {

        if( this.marker == null ){

            Toast.makeText(this, R.string.must_select_location, Toast.LENGTH_SHORT).show();
            return;

        }

        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra("latitudineCentro", marker.getPosition().getLatitude());
        intent.putExtra("longitudineCentro", marker.getPosition().getLongitude());
        intent.putExtra("indirizzo", this.toolbar.getTitle().toString());
        startActivity(intent);
    }

    private void moveCameraToPosition(double latitude, double longitude){

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(
                latitude, longitude
        )).zoom(17).build();
        if( marker != null )
            map.removeMarker(marker);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000);

        MapboxGeocoding geocoder = MapboxGeocoding.builder().accessToken(getString(R.string.mapbox_access_token))
                .query(Point.fromLngLat(longitude, latitude))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS).build();
        geocoder.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if( response.body() != null && response.body().features().size() >= 1 ){

                    CarmenFeature feature = response.body().features().get(0);
                    toolbar.setTitle(feature.placeName());
                    marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));

                } else {
                    Toast.makeText(SearchActivity.this, R.string.error_generic, Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Toast.makeText(SearchActivity.this, R.string.error_generic, Toast.LENGTH_SHORT);
                t.printStackTrace();
            }
        });

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