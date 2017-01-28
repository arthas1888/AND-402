package com.example.a68.getdatacontentproviderapplication;

import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView status = (TextView) findViewById(R.id.status);

        // Reading from content provider
        String cols[] = new String[] { "_ID", "_name"};
        try {
            Uri uri = Uri.parse("content://co.example.testContentProvider/Test");
            Cursor c = getContentResolver().query(uri, cols, null, null, null);
            if (c.moveToFirst())
                status.setText("Data read from content provider: "
                        + c.getString(c.getColumnIndex("_name")));

        }catch (Exception e){
            status.setText("Access denied! " + e.getMessage());
        }
    }
}
