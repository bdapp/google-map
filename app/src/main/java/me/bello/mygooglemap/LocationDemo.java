package me.bello.mygooglemap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;


/**
 * @Info
 * @Auth Bello
 * @Time 19-5-5 下午5:37
 * @Ver
 */
public class LocationDemo {
    private Context mContext;
    private MyLocationListener listener;
    private final static String TAG = "LocationDemo";
    private android.location.LocationManager locationManager;
    private MyLocationListener listeners[] = {
            new MyLocationListener(),
            new MyLocationListener()
    };

    public LocationDemo(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();

    }

    @SuppressLint("MissingPermission")
    public void startRequestLocationUpdates() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1f, listeners[0]);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1F, listeners[1]);
    }

    public void stopRequestLocationUpdates() {
        locationManager.removeUpdates(listeners[0]);
        locationManager.removeUpdates(listeners[1]);
    }

    public Location getCurrentLocation() {

        // go in best to worst order
        for (int i = 0; i < listeners.length; i++) {
            Location l = listeners[i].current();
            if (l != null) return l;
        }
        Log.d(TAG, "No location received yet.");
        return null;
    }

    private class MyLocationListener implements LocationListener {
        Location mLastLocation ;
        boolean mValid = false;

        @Override
        public void onLocationChanged(Location newLocation) {
            if (newLocation.getLatitude() == 0.0
                    && newLocation.getLongitude() == 0.0) {
                // Hack to filter out 0.0,0.0 locations
                return;
            }
            if (!mValid) {
                Log.d(TAG, "Got first location.");
            }
//            mLastLocation.set(newLocation);
            mLastLocation = newLocation;
            Log.d(TAG, "the newLocation is " + newLocation.getLongitude() + "x" + newLocation.getLatitude());
            mValid = true;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                case LocationProvider.TEMPORARILY_UNAVAILABLE: {
                    mValid = false;
                    break;
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, " support current " + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "no support current " + provider);
            mValid = false;
        }

        public Location current() {
            return mValid ? mLastLocation : null;
        }
    }


}
