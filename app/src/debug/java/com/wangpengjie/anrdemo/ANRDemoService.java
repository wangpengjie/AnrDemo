package com.wangpengjie.anrdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class ANRDemoService extends Service {

    private static long spentTime= 30*1000;

    private static final String TAG = "ANRDemo";
    public ANRDemoService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int anrType = intent.getIntExtra("AnrType", 0);
        if (anrType == 2){
            Log.d(TAG,"show service demo");
            SystemClock.sleep(spentTime);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
