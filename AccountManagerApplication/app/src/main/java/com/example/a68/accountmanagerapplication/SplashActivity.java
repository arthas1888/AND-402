package com.example.a68.accountmanagerapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.GoogleAuthUtil;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int RC_HANDLE_PERM = 3;
    Context mContext = SplashActivity.this;
    AccountManager mAccountManager;
    String token;
    int serverCode;
    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        syncGoogleAccount();
    }

    private void requestPermission() {
        final String[] permissions = new String[]{
                android.Manifest.permission.GET_ACCOUNTS};

        ActivityCompat.requestPermissions(this, permissions,
                RC_HANDLE_PERM);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_PERM) {
            Log.d(TAG, "Se obtuvo un inseperado resultado de permiso: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permisos concedidos");
            // we have permission
            getAccountNames();
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
                .setMessage("Permisos no autorizados")
                .setPositiveButton("Ok", listener)
                .setCancelable(false)
                .show();
    }

    private String[] getAccountNames() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }else {
            mAccountManager = AccountManager.get(this);
            Account[] accounts = mAccountManager
                    .getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            String[] names = new String[accounts.length];
            for (int i = 0; i < names.length; i++) {
                names[i] = accounts[i].name;
            }
            return names;
        }
        return new String[]{};
    }

    private AbstractGetNameTask getTask(SplashActivity activity, String email,
                                        String scope) {
        return new GetNameInForeground(activity, email, scope);

    }

    public void syncGoogleAccount() {
        if (isNetworkAvailable()) {
            Log.d(TAG, "entra aca syncGoogleAccount");
            String[] accountarrs = getAccountNames();
            if (accountarrs.length > 0) {
                //you can set here account for login
                for (int i=0; i<accountarrs.length;i++) {
                    getTask(SplashActivity.this, accountarrs[i], SCOPE).execute();
                }
            } else {
                Toast.makeText(SplashActivity.this, "No Google Account Sync!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SplashActivity.this, "No Network Service!",
                    Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.e("network", "Available");
            return true;
        }
        Log.e("network", "Not Available");
        return false;
    }
}
