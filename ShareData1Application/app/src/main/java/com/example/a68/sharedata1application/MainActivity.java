package com.example.a68.sharedata1application;

import android.app.backup.BackupManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    private BackupManager backupManager;
    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(
                "my.preferences", Context.CONTEXT_IGNORE_SECURITY);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("events", "event #1");
        editor.apply();
        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        Button buttonRestore = (Button) findViewById(R.id.buttonRestore);
        buttonSave.setOnClickListener(this);
        buttonRestore.setOnClickListener(this);

        backupManager = new BackupManager(this);
        prefs = getSharedPreferences(CustomBackupAgent.File_Name_Of_Prefrences,
                Context.MODE_PRIVATE);
        edit = prefs.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSave:
                String value = ((EditText)findViewById(R.id.editText))
                        .getText().toString();
                edit.putString("save", value);
                edit.commit();
                Log.d("Test", "Calling backup...");
                backupManager.dataChanged();
                break;
            case R.id.buttonRestore:
                String savedData = prefs.getString("save", "");
                ((TextView)findViewById(R.id.textView2)).setText(savedData);
                break;
        }
    }
}
