package com.example.odometer;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

//import java.util.Random;

public class OdometerService extends Service {

//    private final Random random = new Random();

    private final IBinder binder = new OdometerBinder();
    public class OdometerBinder extends Binder {
        OdometerService getOdometerService() {
            return OdometerService.this;
        }
    }

    private LocationManager locationManager;
    public static final String PERMISSION_ACCESS_FINE_LOCATION =
            Manifest.permission.ACCESS_FINE_LOCATION;
    private LocationListener locationListener;

    private static double distanceInMeters;
    private static Location lastLocation = null;

    @Override
    public void onCreate() {
        super.onCreate();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if(lastLocation == null) {
                    lastLocation = location;
                }
                distanceInMeters += location.distanceTo(lastLocation);
                lastLocation = location;
            }
        };
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, PERMISSION_ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            String provider = locationManager.getBestProvider(new Criteria(), true);
            if(provider != null) {
                locationManager.requestLocationUpdates(
                        provider, 1000, 1, locationListener);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        if(locationManager != null && locationListener != null) {
            if(ContextCompat.checkSelfPermission(this,
                    PERMISSION_ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationManager.removeUpdates(locationListener);
            }
            locationManager = null;
            locationListener = null;
        }
    }

    public Double getDistance() {
//        return random.nextDouble();
        return distanceInMeters / 1609.344;
    }

}