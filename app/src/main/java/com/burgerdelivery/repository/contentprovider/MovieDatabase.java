package com.burgerdelivery.repository.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies";
    private static final int DATABASE_VERSION = 1;

    MovieDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + OrderContract.MovieEntry.TABLE_NAME + " (" +
                OrderContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                OrderContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NULL, " +
                OrderContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                OrderContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                OrderContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                OrderContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                OrderContract.MovieEntry.COLUMN_GENRE_ID_LIST + " TEXT NOT NULL, " +
                OrderContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
