package me.bello.mygooglemap;

/**
 * @Info
 * @Auth Bello
 * @Time 19-5-5 下午5:48
 * @Ver
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("MissingPermission")
public class LocationHelper implements LocationListener {

    private static final String TAG = "LocationHelper";

    private LocationManager mLocationManager;
    private MyLocationListener mListener;
    private Context mContext;

    public LocationHelper(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }


    public void startLocation(MyLocationListener listener) {

        this.mListener = listener;

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(mContext, "请打开定位权限", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isOpenGPS()){
            showGPSDialog();
        }
        getLocationFromGPS();


    }


    /**
     * 关闭定位
     */
    public void stopLocation() {
        mLocationManager.removeUpdates(this);
    }


    /**
     * 通过GPS获取定位
     */
    private void getLocationFromGPS() {
        try {

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30 * 1000, 0, this);
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (null != location) {
                mListener.updateLastLocation(location);
            } else {
                getLocationFromNetWork();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 通过网络获取定位
     */
    private void getLocationFromNetWork() {
        try {

            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30 * 1000, 0, this);
            Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (null != location) {
                mListener.updateLastLocation(location);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged => " + location.toString());
        mListener.updateLastLocation(location);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged => " + provider);
    }


    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled => " + provider);
    }


    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled => " + provider);
    }


    /**
     * 获取定位后的回调
     */
    interface MyLocationListener {
        void updateLastLocation(Location location);
    }


    /**
     * 判断GPS是否开启
     */
    private boolean isOpenGPS() {
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }

        return false;
    }


    private void showGPSDialog() {
        if (!((Activity)mContext).isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("本应用需要获取地理位置，请打开获取位置的开关");
            builder.setNegativeButton("OPEN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });
            builder.create().show();
        }
    }

}