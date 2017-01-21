package com.example.a68.application1_1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_HANDLE_SMS_PERM = 3;
    private ListView listView;
    public static class SmsColumns{
        static String ID = "_id",
        ADDRESS = "address",
        DATE = "date",
        BODY = "body";
    }
    public static final Uri SMS = Uri.parse("content://sms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);

        int rc = ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.READ_SMS);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            callData();
        } else {
            requestSMSPermission();
        }
    }

    private void callData() {
        Cursor cursor = getContentResolver().query(SMS, new String[]{
                        SmsColumns.ID,
                        SmsColumns.ADDRESS,
                        SmsColumns.DATE,
                        SmsColumns.BODY},
                null, null, SmsColumns.DATE   + " DESC");
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(
                this, cursor, 0
        );
        listView.setAdapter(customCursorAdapter);
    }

    private void requestSMSPermission() {
        Log.e(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{
                android.Manifest.permission.READ_SMS};

        ActivityCompat.requestPermissions(this, permissions,
                RC_HANDLE_SMS_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_SMS_PERM) {
            Log.d(TAG, "Se obtuvo un inseperado resultado de permiso: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permisos concedidos");
            // we have permission
            callData();
            return;
        }

        Log.e(TAG, "Permisos no concedidos: resultados len = " + grantResults.length +
                " Resultado codigo = " + (grantResults.length > 0 ? grantResults[0] : "(vacio)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alerta")
                .setMessage(R.string.no_granted_permission)
                .setPositiveButton(R.string.ok, listener)
                .setCancelable(false)
                .show();
    }


    class CustomCursorAdapter extends CursorAdapter{

        public CustomCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return View.inflate(context, R.layout.activity_read_sms, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view.findViewById(R.id.textAddress)).setText(cursor.getString
                    (cursor.getColumnIndexOrThrow(String.valueOf(SmsColumns.ADDRESS))));
            ((TextView) view.findViewById(R.id.textBody)).setText(cursor.getString
                    (cursor.getColumnIndexOrThrow(String.valueOf(SmsColumns.BODY))));
            ((TextView) view.findViewById(R.id.textDate)).setText(new Date(cursor.getLong
                    (cursor.getColumnIndexOrThrow(String.valueOf(SmsColumns.DATE)))).toLocaleString());
        }
    }
}
