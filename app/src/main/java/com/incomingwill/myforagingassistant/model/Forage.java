package com.incomingwill.myforagingassistant.model;

/*
 *  Document   : Forage Object
 *  Created on : 11.17.22
 *  @author incomingWill
 *  CPS 435 Final Program
 */

/*
 *  basic object class for operations on Forage object
 */

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Calendar;

public class Forage {

    public final String TAG = "Contacts";

    private int forageID;
    private String forageName;
    private String forageType;
    private float forageYield;
    private Calendar harvestDate;
    private float latitude;
    private float longitude;
    private Bitmap picture;

    public Forage() {
        forageID = -1;
    }

    public void setForageID(int r) {
        this.forageID = r;
    }

    public int getForageID() {
        return forageID;
    }

    public void setForageName(String n) {
        this.forageName = n;
    }

    public String getForageName() {
        return forageName;
    }

    public void setForageType(String n) {
        this.forageType = n;
    }

    public String getForageType() {
        return forageType;
    }

    public void setForageYield(float y) {
        this.forageYield = y;
    }

    public float getForageYield() {
        return forageYield;
    }

    public Calendar getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(Calendar d) {
        this.harvestDate = d;
    }

    public void setLatitude(float la) { this.latitude = la; }

    public float getLatitude() {return latitude; }

    public void setLongitude(float lo) { this.longitude = lo; }

    public float getLongitude() {return longitude; }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getYieldString() {
        String yieldString = String.valueOf(this.forageYield);
        return yieldString;
    }

    public String getLatitudeString() {
        String latString = String.valueOf(this.latitude);
        return latString;
    }

    public String getLongitudeString() {
        String longString = String.valueOf(this.longitude);
        return longString;
    }
}

