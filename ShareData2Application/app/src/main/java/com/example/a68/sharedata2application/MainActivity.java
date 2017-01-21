package com.example.a68.sharedata2application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context friendContext = null;
        try {
            Context context = createPackageContext(
                    "com.example.a68.sharedata1application", 0);
            SharedPreferences pref = context.getSharedPreferences(
                    "my.preferences", Context.CONTEXT_IGNORE_SECURITY);
            String event = pref.getString("events", "No event for you");

            Log.i("sharedPref event", event);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
