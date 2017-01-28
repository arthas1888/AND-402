package com.example.a68.contentproviderapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView status = (TextView) findViewById(R.id.tv_status);
        // Inserting data into content provider
        ContentValues tuple = new ContentValues();
        tuple.put(Constants.TEXT, Constants.TEXT_DATA);
        getContentResolver().insert(Constants.CONTENT_URI, tuple);

        // Reading from content provider
        String cols[] = new String[] { Constants.ID, Constants.TEXT };
        Uri u = Constants.CONTENT_URI;
        Cursor c = getContentResolver().query(u, cols, null, null, null);
        if (c.moveToFirst())
            status.setText("Data read from content provider: "
                    + c.getString(c.getColumnIndex(Constants.TEXT)));
        else
            status.setText("Access denied!");
    }
}
