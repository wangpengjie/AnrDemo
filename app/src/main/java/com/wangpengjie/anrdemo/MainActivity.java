package com.wangpengjie.anrdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ANRDEMO";
    private ScreenStatusReceiver mScreenStatusReceiver;
    private Lock mlock;
    private  Thread mthread;
    private static long spentTime= 30*1000;
    private int gDelaySwith = 0;
    private int ANRBROADCAST = 1;
    private int ANRSERVICE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreenStatusReceiver mScreenStatusReceiver = new ScreenStatusReceiver();
        IntentFilter filterIF = new IntentFilter();
        filterIF.addAction("android.intent.action.SCREEN_ON");
        filterIF.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(mScreenStatusReceiver, filterIF);
        mlock = new ReentrantLock();
        mthread = new Thread(new Runnable() {
            public void run() {
                mlock.lock();
                try {
                    Log.d(TAG,"thread run get lock");
                    SystemClock.sleep(spentTime);
                } finally {
                    mlock.unlock();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private class ScreenStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                if (gDelaySwith == ANRBROADCAST){
                    Log.d(TAG,"show broadcast delay..");
                    SystemClock.sleep(spentTime);
                }
            }
        }
    }

    public void onDestroy() {
        unregisterReceiver(mScreenStatusReceiver);
        mScreenStatusReceiver = null;
        super.onDestroy();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void InvokeInputAnrDemo(View v){
        Log.d(TAG,"input anr demo...");
        mthread.start();
        mlock.lock();
        try {
            Log.d(TAG,"InvokeInputAnrDemo get lock..");
        } finally {
            mlock.unlock();
        }
    }

    public void InvokeServiceAnrDemo(View v){
        Log.d(TAG,"InvokeServiceAnrDemo anr demo...");
        Intent iService = new Intent(MainActivity.this,ANRDemoService.class);
        iService.putExtra("AnrType",ANRSERVICE);

        startService(iService);
   }

    public void InvokeBroadCastAnrDemo(View v){
        Log.d(TAG,"InvokeBroadCastAnrDemo anr demo...");
        gDelaySwith = ANRBROADCAST;
    }

}
