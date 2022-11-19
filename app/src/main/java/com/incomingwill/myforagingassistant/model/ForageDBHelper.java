package com.incomingwill.myforagingassistant.model;

/*
 *  Document   : Forage Database Helper
 *  Created on : 11.17.22
 *  @author incomingWill
 *  CPS 435 Final Program
 */

/*
 *  database version 1
 *      try statement to add forage photo blob column to forage table
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ForageDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myforages.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String CREATE_TABLE_FORAGE =
            "create table forage (_id integer primary key autoincrement, "
                    + "foragename text not null, foragetype text, "
                    + "forageyield float(8), harvestdate text, latitude float(8), "
                    + "longitude float(8), foragephoto blob);";

    public ForageDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FORAGE);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {
        Log.w(ForageDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS forage");
        onCreate(db);

        //try statement to implement future change in database
        /*
        try {
            db.execSQL("ALTER TABLE forage add column columnname datatype");
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }
}

