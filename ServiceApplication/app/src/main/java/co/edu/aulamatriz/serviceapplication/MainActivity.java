package co.edu.aulamatriz.serviceapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final String MY_ACTION = "co.example.receiver.LOCAL";
    private MyBroadcast myBroadcast;
    private TextView textView;
    //private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyService.isInstance())
                    startService(new Intent(MainActivity.this, MyService.class));
            }
        });
        textView = (TextView) findViewById(R.id.textView);

        myBroadcast = new MyBroadcast();

        /*mAdView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice("AdRequest.DEVICE_ID_EMULATOR")
                .addTestDevice("207B846CF80A463F4B3150638CF7D2D0") // I don't know if it's okay to show.
                .build();
        mAdView.loadAd(request);*/
    }

    public void stopMyService(View view){
        if (MyService.isInstance())
            stopService(new Intent(MainActivity.this, MyService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(myBroadcast,
                        new IntentFilter(MY_ACTION)
                        );
        // Resume the AdView.
        //mAdView.resume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(myBroadcast);
        //mAdView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAdView.destroy();
    }

    class MyBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String mem = "Memoria disponible del dispositivo: " +
                    extras.getString("memmory");
            Log.d("MAIN ACTIVITY", mem);
            textView.setText(mem);

        }
    }
}
