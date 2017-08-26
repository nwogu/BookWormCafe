package com.example.android.bwc.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bwc.R;

import static com.example.android.bwc.data.BWContract.BwcEntry.TABLE_NAME;

/**
 * Created by TEST on 7/19/2017.
 */

public class BWCDBHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "bwc.db";

    public BWCDBHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String fg = "CREATE TABLE " + BWContract.BwcEntry.TABLE_NAME +
                "( " + BWContract.BwcEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BWContract.BwcEntry.COLUMN_CHILD_NAME + " TEXT, " + BWContract.BwcEntry.COLUMN_PARENT_NAME +
                " TEXT, " +
                BWContract.BwcEntry.COLUMN_GENDER +
                " TEXT DEFAULT Unknown, " + BWContract.BwcEntry.COLUMN_AGE + " TEXT, " +
                BWContract.BwcEntry.COLUMN_PHONE_NUMBER + " TEXT, " +
                BWContract.BwcEntry.COLUMN_HUB_NUMBER + " TEXT, " +
                BWContract.BwcEntry.COLUMN_BOOKS + " TEXT)";

        sqLiteDatabase.execSQL(fg);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
