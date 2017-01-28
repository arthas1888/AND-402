package com.example.a68.login2application;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String username, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StringBuilder msg = new StringBuilder();
        msg.append("Tengo tus credencales");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            username = bundle.getString("username");
            pass = bundle.getString("pass");
            msg.append("\nUsuario: " + username + "\n");
            msg.append("\nPassword: " + pass + "\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Atencion")
                .setMessage(msg)
                .setPositiveButton("OK", null);
        builder.create().show();
    }
}
