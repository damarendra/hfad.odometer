package com.example.odometer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import java.util.Random;

public class OdometerService extends Service {

    private final Random random = new Random();

    private final IBinder binder = new OdometerBinder();
    public class OdometerBinder extends Binder {
        OdometerService getOdometerService() {
            return OdometerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public Double getDistance() {
        return random.nextDouble();
    }
}