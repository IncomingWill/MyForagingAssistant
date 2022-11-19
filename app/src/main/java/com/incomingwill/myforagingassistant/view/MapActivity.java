package com.incomingwill.myforagingassistant.view;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.incomingwill.myforagingassistant.R;
import com.incomingwill.myforagingassistant.model.Forage;
import com.incomingwill.myforagingassistant.model.ForageDataSource;

public class MapActivity
        extends AppCompatActivity
        implements OnMapReadyCallback {


    GoogleMap gMap;
    FusedLocationProviderClient fusedLocationClient;
    private MapView mvForage;
    private Forage currentForage;
    float ZOOM_LEVEL = 14.0F;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //private variable to provide association between activities
        currentForage = new Forage();

        initForageListButton();
        initNewForageButton();

        //methods for populating the map with current restaurant
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mvForage = findViewById(R.id.mapViewForage);
        mvForage.onCreate(mapViewBundle);
        mvForage.getMapAsync(this::onMapReady);

    }

    private void initForageListButton() {
        Button listButton = findViewById(R.id.buttonListView);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MapActivity.this,
                        ListActivity.class);
                intent.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP));

                startActivity(intent);
            }
        });
    }

    private void initNewForageButton() {
        Button buttonNewForage = findViewById(R.id.buttonMaptoMain);
        buttonNewForage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(
                        MapActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mvForage.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvForage.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mvForage.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mvForage.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        int measuredWidth = size.x;
        int measuredHeight = size.y;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            float latitude = extras.getFloat("latitude");
            float longitude = extras.getFloat("longitude");

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            LatLng point = new LatLng(
                    latitude,
                    longitude);
            builder.include(point);

            gMap.addMarker(new MarkerOptions().position(point).title(
                    currentForage.getForageName()));

            //Animate the camera to the marker
            //can .moveCamera or .animateCamera
            gMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(builder.build(),
                            measuredWidth,
                            measuredHeight,
                            450));
            gMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(point, ZOOM_LEVEL));

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvForage.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvForage.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mvForage.onLowMemory();
    }
}
