package com.example.a68.contentproviderapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

public class MyContentProvider extends ContentProvider {

    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "_name";
    public static final String TABLE_NAME = "Test";
    public static final String AUTHORITY = "co.example.testContentProvider";
    private CustomDB dbHelper;
    private static final UriMatcher sUriMatcher;
    private static final int DATUM = 1;
    private static final int DATUM_ID = 2;
    private static HashMap<String, String> projMap;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, DATUM);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", DATUM_ID);
        projMap = new HashMap<String, String>();
        projMap.put(Constants.ID, Constants.ID);
        projMap.put(Constants.TEXT, Constants.TEXT);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri u) {
        if (sUriMatcher.match(u) == DATUM)
            return Constants.CONTENT_TYPE;
        else
            throw new IllegalArgumentException("Unknown URI " + u);
    }

    @Override
    public Uri insert(Uri u, ContentValues v) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rId = db.insert(TABLE_NAME, Constants.TEXT, v);
        if (rId > 0) {
            Uri uri = ContentUris.withAppendedId(Constants.CONTENT_URI, rId);
            getContext().getContentResolver().notifyChange(uri, null);
            return uri;
        }
        throw new SQLException("Failed to insert row into " + u);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new CustomDB(getContext());
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri u, String[] p, String s,
                        String[] args, String sort) throws IllegalArgumentException {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        qb.setProjectionMap(projMap);
        if (sUriMatcher.match(u) != DATUM) {
            if (sUriMatcher.match(u) == DATUM_ID)
                s = s + "_id = " + u.getLastPathSegment();
            else
                throw new IllegalArgumentException("Unknown URI " + u);
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, p, s, args, null, null, sort);
        c.setNotificationUri(getContext().getContentResolver(), u);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class CustomDB extends SQLiteOpenHelper{

        public CustomDB(Context context) {
            super(context, "DBTest", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME
                    + " ("
                    + COLUMN_ID + " integer PRIMARY KEY not null, "
                    + COLUMN_NAME + " TEXT "
                    + ")";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
