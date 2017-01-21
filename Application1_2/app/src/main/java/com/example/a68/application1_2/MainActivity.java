package com.example.a68.application1_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int requestCode_intent = 2;
    private TextView textViewLog;
    private boolean error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewLog = (TextView) findViewById(R.id.log);
    }

    public void openApp(View view) {
        Intent intent = new Intent();
        intent.setAction("Android.ATC.example.Application1");
        intent.addCategory("android.intent.category.DEFAULT");
        //startActivity(intent);
        try
        {
            startActivityForResult(intent, requestCode_intent);
        }
        catch (Exception e)
        {
            textViewLog.append("\n" + e.getMessage() + "\n");
            Toast.makeText(MainActivity.this, "Permission Not declared error!", Toast.LENGTH_LONG).show();
            error = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestCode_intent && !error)
        {
            textViewLog.append("\n You opened Application 1  Activity");
            Toast.makeText(this, "You opened Application 1  Activity",
                    Toast.LENGTH_LONG).show();
            error = false;
        }
    }
}
