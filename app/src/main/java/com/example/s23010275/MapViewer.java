package com.example.s23010275;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapViewer extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private EditText searchText;
    private Button searchBtn;
private Button tempButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_viewer);

        searchText = findViewById(R.id.mapSearchInput);
        searchBtn = findViewById(R.id.searchButton);
        tempButton = findViewById(R.id.tempBtn);

        // Get map fragment and initialize
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        tempButton.setOnClickListener(v -> {
            // Navigate to SensorUI activity
            Intent intent = new Intent(MapViewer.this, SensorUI.class);
            startActivity(intent);
        });

        searchBtn.setOnClickListener(v -> {
            String searchedQuery = searchText.getText().toString();
            if (searchedQuery.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addressList = geocoder.getFromLocationName(searchedQuery, 1);
                Log.d("MapViewer", "Address List: " + addressList);

                if (addressList == null || addressList.isEmpty()) {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                Address address = addressList.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                String locationName = address.getLocality() != null ? address.getLocality() : searchedQuery;

                // Update the map only if map is ready
                if (gMap != null) {
                    LatLng searchedLocation = new LatLng(latitude, longitude);
                    gMap.clear(); // Remove existing markers
                    gMap.addMarker(new MarkerOptions().position(searchedLocation).title(locationName));
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, 15f));
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Geocoding failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setZoomControlsEnabled(true);

        // Optionally show a default location
        LatLng defaultLocation = new LatLng(7.8731, 80.7718); // Sri Lanka
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 6.0f));
    }
}
