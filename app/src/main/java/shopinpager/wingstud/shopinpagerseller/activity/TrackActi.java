package shopinpager.wingstud.shopinpagerseller.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.ActivityTrackBinding;

public class TrackActi extends AppCompatActivity {

    private Toolbar toolbar;

    private Context mContext;

    private ActivityTrackBinding binding;


    private GoogleMap googleMap;


    private int locationRequestCode = 1003;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_track);

        mContext = TrackActi.this;

        binding.mapView.onCreate(savedInstanceState);

        binding.mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setMap();
    }

    private void setMap() {
        binding.mapView.getMapAsync(mMap -> {
            googleMap = mMap;

            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);

            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(26.9124,75.7873) , 14.0f) );

            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            // For showing a move to my location button
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(TrackActi.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        locationRequestCode);
            } else {
                googleMap.setMyLocationEnabled(true);
            }
        });
    }

}
