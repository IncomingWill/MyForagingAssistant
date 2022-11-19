/*
 *  Document   : Forage Data Source Class
 *  Created on : 11.17.22
 *  @author incomingWill
 *  CPS 435 Final Program
 */

/*
 *  Methods to create db Helper object,
 *  open and close database,
 *  insert / update / delete db row,
 *  get last id in db for new row id,
 *  create list of names,
 *  create list of objects from rows,
 *  and to get specific object
 */

package com.incomingwill.myforagingassistant.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ForageDataSource {
    private SQLiteDatabase database;
    private ForageDBHelper dbHelper;

    public ForageDataSource(Context context) {
        dbHelper = new ForageDBHelper(context);
    }

    //open database, calls from dbHelper
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    //close in base class, not in helper
    public void close() {
        dbHelper.close();
    }

    public boolean insertForage(Forage f) {
        //default set to false, is succeed, didSucceed true
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("foragename", f.getForageName());
            initialValues.put("foragetype", f.getForageType());
            initialValues.put("forageyield", f.getForageYield());
            initialValues.put("harvestdate", String.valueOf(f.getHarvestDate().getTimeInMillis()));
            initialValues.put("latitude", f.getLatitude());
            initialValues.put("longitude", f.getLongitude());

            //byte array stored in blob field
            if (f.getPicture() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                f.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] photo = baos.toByteArray();
                initialValues.put("foragephoto", photo);
            }

            //will be 1 or 0, if greater than 0, true
            didSucceed = database.insert("forage",
                    null,
                    initialValues) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateForage(Forage f) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) f.getForageID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("foragename", f.getForageName());
            updateValues.put("foragetype", f.getForageType());
            updateValues.put("forageyield", f.getForageYield());
            updateValues.put("harvestdate", String.valueOf(f.getHarvestDate().getTimeInMillis()));
            updateValues.put("latitude", f.getLatitude());
            updateValues.put("longitude", f.getLongitude());

            if (f.getPicture() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                f.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] photo = baos.toByteArray();
                updateValues.put("foragephoto", photo);
            }

            didSucceed = database.update("forage",
                    updateValues,
                    "_id=" + rowId,
                    null) > 0;
        }
        catch (Exception e) {
            //Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean deleteForage(int forageId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete(
                    "forage",
                    "_id=" + forageId,
                    null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

    public int getLastForageId() {
        int lastId;
        try {
            String query = "Select MAX(_id) from forage";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    //create empty array list, and fill with Forages from db
    public ArrayList<Forage> getForages(String sortField, String sortOrder) {
        ArrayList<Forage> forages = new ArrayList<Forage>();
        try {
            //parameter can be used to sort
            String query = "SELECT * FROM forage ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Forage newForage;
            cursor.moveToFirst();
            //while not the last one
            while (!cursor.isAfterLast()) {
                newForage = new Forage();
                newForage.setForageID(cursor.getInt(0));
                newForage.setForageName(cursor.getString(1));
                newForage.setForageType(cursor.getString(2));
                newForage.setForageYield(cursor.getFloat(3));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(4)));
                newForage.setHarvestDate(calendar);
                newForage.setLatitude(cursor.getFloat(5));
                newForage.setLongitude(cursor.getFloat(6));

                byte[] photo = cursor.getBlob(7);
                if (photo != null) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(photo);
                    Bitmap thePicture = BitmapFactory.decodeStream(bais);
                    newForage.setPicture(thePicture);
                }

                forages.add(newForage);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            forages = new ArrayList<Forage>();
        }
        return forages;
    }

    //return single Forage DB entry
    public Forage getSpecificForage(int forageId) {
        Forage f = new Forage();
        String query = "SELECT  * FROM forage WHERE _id =" + forageId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            f.setForageID(cursor.getInt(0));
            f.setForageName(cursor.getString(1));
            f.setForageType(cursor.getString(2));
            f.setForageYield(cursor.getFloat(3));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(4)));
            f.setHarvestDate(calendar);
            f.setLatitude(cursor.getFloat(5));
            f.setLongitude(cursor.getFloat(6));

            byte[] photo = cursor.getBlob(7);
            if (photo != null) {
                ByteArrayInputStream bais = new ByteArrayInputStream(photo);
                Bitmap thePicture= BitmapFactory.decodeStream(bais);
                f.setPicture(thePicture);
            }

            cursor.close();
        }
        return f;
    }
}
