package co.edu.aulamatriz.serviceapplication;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static co.edu.aulamatriz.serviceapplication.MainActivity.MY_ACTION;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();

    private static MyService instance;
    private Handler handler;
    private Runnable runnable;

    public static boolean isInstance(){

        return instance != null;
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Se inicio el servicio");

        callTimer();
        return START_STICKY;
    }

    private void callTimer() {

        runnable = new Runnable() {
            @Override
            public void run() {
                new UpdateMemmory().execute();
                handler.postDelayed(this, 1000);
            }
        };
        runnable.run();
    }

    class UpdateMemmory extends AsyncTask<Void, Void, String>{

        private ActivityManager.MemoryInfo memoryInfo;
        private ActivityManager activityManager;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            memoryInfo = new ActivityManager.MemoryInfo();
            activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        }

        @Override
        protected String doInBackground(Void... params) {
            activityManager.getMemoryInfo(memoryInfo);
            return memoryInfo.availMem / 1024 + " KB";
        }

        @Override
        protected void onPostExecute(String memmory) {
            //Log.d(TAG, "Memoria disponible del dispositivo: " + memmory);
            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("memmory", memmory);
            LocalBroadcastManager.getInstance(
                    getApplicationContext()).sendBroadcast(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        handler.removeCallbacks(runnable);
        Log.e(TAG, "Servicio destruido");
    }
}
