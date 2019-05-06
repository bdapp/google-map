package me.bello.mygooglemap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Context mContext;
    private String TAG = "MapsActivity";
    private GoogleMap mMap;
    private LocationHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = MapsActivity.this;

        init();


       /* LocationHelper helper = new LocationHelper(mContext);
        helper.startLocation(new LocationHelper.MyLocationListener() {
            @Override
            public void updateLastLocation(Location location) {
                Log.e(TAG, "updateLastLocation => " + location.toString());
                double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(location.getLongitude(), location.getLatitude());
                Log.e("location google ==> ", gcj02[0] + "," + gcj02[1]);
                Toast.makeText(mContext, location.toString(), Toast.LENGTH_SHORT).show();
            }

        });*/


    }

    private void init(){

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10008);
        } else {

            setContentView(R.layout.activity_maps);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        helper = new LocationHelper(mContext);
        helper.startLocation(new LocationHelper.MyLocationListener() {
            @Override
            public void updateLastLocation(Location location) {
                Log.e(TAG, "updateLastLocation => " + location.toString());
                double[] gcj02 = CoordinateTransformUtil.wgs84togcj02(location.getLongitude(), location.getLatitude());
                Log.e("location google ==> ", gcj02[0] + "," + gcj02[1]);
                Toast.makeText(mContext, location.toString(), Toast.LENGTH_SHORT).show();

                if (null != location) {
                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title(""));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
                } else {
                    Log.e("location google ==> ", "is wrong!");
                }

            }

        });

    }


    @Override
    protected void onDestroy() {
        helper.stopLocation();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10008) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                }
            }
            init();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
