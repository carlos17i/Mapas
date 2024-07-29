package com.example.mapas;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner mapTypeSpinner;
    private ToggleButton toggleZoomControls;
    private Button btnCenterBogota, btnAnimateEgypt, btnShowCameraPosition, btnAddMarkerParis;
    private RadioGroup styleRadioGroup;

    private static final LatLng BOGOTA = new LatLng(4.7110, -74.0721);
    private static final LatLng EGYPT_PYRAMIDS = new LatLng(29.9792, 31.1342);
    private static final LatLng PARIS = new LatLng(48.8566, 2.3522);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapTypeSpinner = findViewById(R.id.mapTypeSpinner);
        toggleZoomControls = findViewById(R.id.toggleZoomControls);
        btnCenterBogota = findViewById(R.id.btnCenterBogota);
        btnAnimateEgypt = findViewById(R.id.btnAnimateEgypt);
        btnShowCameraPosition = findViewById(R.id.btnShowCameraPosition);
        btnAddMarkerParis = findViewById(R.id.btnAddMarkerParis);
        styleRadioGroup = findViewById(R.id.styleRadioGroup);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupMapTypeSpinner();
        setupToggleZoomControls();
        setupCenterBogotaButton();
        setupAnimateEgyptButton();
        setupShowCameraPositionButton();
        setupAddMarkerParisButton();
        setupStyleRadioGroup();
    }

    private void setupMapTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.map_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapTypeSpinner.setAdapter(adapter);
        mapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mMap != null) {
                    switch (position) {
                        case 0:
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case 1:
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                        case 2:
                            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case 3:
                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupToggleZoomControls() {
        toggleZoomControls.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mMap != null) {
                mMap.getUiSettings().setZoomControlsEnabled(isChecked);
            }
        });
    }

    private void setupCenterBogotaButton() {
        btnCenterBogota.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BOGOTA, 8));
            }
        });
    }

    private void setupAnimateEgyptButton() {
        btnAnimateEgypt.setOnClickListener(v -> {
            if (mMap != null) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(EGYPT_PYRAMIDS)
                        .zoom(15)
                        .bearing(50)
                        .tilt(65)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    private void setupShowCameraPositionButton() {
        btnShowCameraPosition.setOnClickListener(v -> {
            if (mMap != null) {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                String message = "Lat: " + cameraPosition.target.latitude +
                        ", Lng: " + cameraPosition.target.longitude +
                        ", Zoom: " + cameraPosition.zoom +
                        ", Bearing: " + cameraPosition.bearing +
                        ", Tilt: " + cameraPosition.tilt;
                Toast.makeText(MapsActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupAddMarkerParisButton() {
        btnAddMarkerParis.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.addMarker(new MarkerOptions().position(PARIS).title("Pais: Francia"));
            }
        });
    }

    private void setupStyleRadioGroup() {
        styleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (mMap != null) {
                int styleResource = checkedId == R.id.radioStyle1 ? R.raw.style1 : R.raw.style2;
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, styleResource));
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(latLng -> {
            String message = "Map clicked at: Lat: " + latLng.latitude + ", Lng: " + latLng.longitude;
            Toast.makeText(MapsActivity.this, message, Toast.LENGTH_SHORT).show();
        });

        mMap.setOnMarkerClickListener(marker -> {
            String message = "Marker: " + marker.getTitle() + ", Position: " + marker.getPosition().toString();
            Toast.makeText(MapsActivity.this, message, Toast.LENGTH_SHORT).show();
            return false;
        });
    }
}
