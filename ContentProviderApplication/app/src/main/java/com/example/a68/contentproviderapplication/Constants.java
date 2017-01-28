package com.example.a68.contentproviderapplication;

import android.net.Uri;


public class Constants {
    private Constants() {
    }

    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY + "/" + MyContentProvider.TABLE_NAME);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.android.contentproviderlab.Test";
    public static final String ID = "_ID";
    public static final String TEXT = "_name";
    public static final String TEXT_DATA = "Hello Content Providers!";
}
