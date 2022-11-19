/*
 *  Document   : Main Activity My Foraging Assistant
 *  Created on : 11.17.22
 *  @author incomingWill
 *  CPS 435 Final Program
 */

/*
 *  Main Activity
 */

package com.incomingwill.myforagingassistant.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.incomingwill.myforagingassistant.R;
import com.incomingwill.myforagingassistant.model.Forage;
import com.incomingwill.myforagingassistant.model.ForageDataSource;

import java.util.Calendar;

public class MainActivity
        extends AppCompatActivity
        implements DatePickerDialog.SaveDateListener{

    //permissions
    final int PERMISSION_REQUEST_CAMERA = 103;
    final int CAMERA_REQUEST = 1888;
    final int PERMISSION_REQUEST_LOCATION = 101;

    LocationManager locationManager;
    LocationListener gpsListener;
    private Forage currentForage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate private instance to provide association between views
        currentForage = new Forage();

        //initialize each button onCreate
        initTextChangedEvents();
        initDateButton();
        initRecordLocationButton();
        initImageButton();
        initSaveButton();
        initViewListButton();
    }

    //methods to set data from text fields into Forage Object currentForage
    private void initTextChangedEvents() {
        final EditText etName = findViewById(R.id.editTextMainName);
        final EditText etType = findViewById(R.id.editTextMainType);
        final EditText etYield = findViewById(R.id.editTextMainYield);

        //name
        etName.addTextChangedListener(new TextWatcher() {
            //listener and textwatcher to make changes after, before, and on text change
            public void afterTextChanged(Editable s) {
                currentForage.setForageName(etName.getText().toString());
            }
            public void beforeTextChanged(
                    CharSequence arg0, int arg1, int arg2, int arg3) {
                //auto-generated method stub
            }
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                //auto-generated method stub
            }
        });

        //type
        etType.addTextChangedListener(new TextWatcher() {
            //listener and textwatcher to make changes after, before, and on text change
            public void afterTextChanged(Editable s) {
                currentForage.setForageType(etType.getText().toString());
            }
            public void beforeTextChanged(
                    CharSequence arg0, int arg1, int arg2, int arg3) {
                //auto-generated method stub
            }
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                //auto-generated method stub
            }
        });

        //yield
        etYield.addTextChangedListener(new TextWatcher() {
            //listener and textwatcher to make changes after, before, and on text change
            public void afterTextChanged(Editable s) {
                currentForage.setForageYield(Float.parseFloat(etYield.getText().toString()));
            }

            public void beforeTextChanged(
                    CharSequence arg0, int arg1, int arg2, int arg3) {
                //auto-generated method stub
            }

            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                //auto-generated method stub
            }
        });
    }

    //call fragment for date picker
    private void initDateButton(){
        Button changeDate = findViewById(R.id.buttonHarvestDate);

        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }

    //need to implement for datePickerDialog
    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        //put date into textview label
        TextView forageDate = findViewById(R.id.textViewMainDisplayDate);
        currentForage.setHarvestDate(selectedTime);
        forageDate.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
    }

    //Method getting the current location
        //turn on the GPS
    //Location updates will be posted in the TextViews
    private void initRecordLocationButton() {
        Button buttonRecordLocation = findViewById(R.id.buttonRecordLocation);

        buttonRecordLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContextCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) !=
                                PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    MainActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                                Snackbar.make(findViewById(R.id.topLayout),
                                                "Meal Rater requires this permission to locate " +
                                                        "your location", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ActivityCompat.requestPermissions(
                                                        MainActivity.this,
                                                        new String[]{
                                                                Manifest.permission.ACCESS_FINE_LOCATION},
                                                        PERMISSION_REQUEST_LOCATION);

                                            }
                                        }).show();
                            } else {
                                ActivityCompat.requestPermissions(
                                        MainActivity.this,
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSION_REQUEST_LOCATION);
                            }
                        } else {
                            startLocationUpdates();
                            turnOffgpsListener();
                        }
                    } else {
                        startLocationUpdates();
                        turnOffgpsListener();
                    }
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Error requesting permission", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {



        switch (requestCode)   {
            case PERMISSION_REQUEST_LOCATION:{
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocationUpdates();
                    turnOffgpsListener();
                }
                else
                {
                    Toast.makeText(MainActivity.this,
                            "Forage Assistant will not track location.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    //location listener
    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    //put in textDisplay fields
                //    TextView txtLatitude = findViewById((R.id.textDisplayLatitude));
                //    TextView txtLongitude = findViewById((R.id.textDisplayLongitude));
                //    TextView txtAccuracy = findViewById((R.id.textDisplayAccuracy));

                    //get methods of location class to set curretForage coordinates
                    currentForage.setLatitude((float) location.getLatitude());
                    currentForage.setLongitude((float) location.getLongitude());

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, gpsListener);
        }
        catch(Exception e)
        {
            Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();
        }
    }

    private void turnOffgpsListener() {
        gpsListener = null;
    }

    //image button to take a picture
    //listener to watch for click, check permission, take a photo
    private void initImageButton() {
        ImageButton ib = findViewById(R.id.imageForage);
        ib.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                MainActivity.this, android.Manifest.permission.CAMERA)) {
                            Snackbar.make(findViewById(R.id.topLayout),
                                            "The app needs permission to take pictures.",
                                            Snackbar.LENGTH_INDEFINITE).setAction(
                                            "Ok", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    ActivityCompat.requestPermissions(
                                                            MainActivity.this,
                                                            new String[]{ android.Manifest.permission.CAMERA},
                                                            PERMISSION_REQUEST_CAMERA);
                                                }
                                            })
                                    .show();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{android.Manifest.permission.CAMERA},
                                    PERMISSION_REQUEST_CAMERA);
                        }
                    }
                    else {
                        takePhoto();
                    }
                } else {
                    takePhoto();
                }
            }
        });
    }

    public void takePhoto(){
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    //activity result for when picture is taken, place into currentForage image button
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(
                        photo, 144, 144, true);
                ImageButton ibForage = (ImageButton) findViewById(R.id.imageForage);
                ibForage.setImageBitmap(scaledPhoto);
                currentForage.setPicture(scaledPhoto);
            }
        }
    }

    //modified for saving via API
    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                hideKeyboard();

                ForageDataSource ds = new ForageDataSource(MainActivity.this);
                try {
                    ds.open();
                    //if forageID is -1, add to forage table in database
                    if (currentForage.getForageID() == -1) {
                        wasSuccessful = ds.insertForage(currentForage);
                        //if successful, get last id + 1
                        if (wasSuccessful) {
                            int newId = ds.getLastForageId();
                            currentForage.setForageID(newId);
                            Toast.makeText(
                                    MainActivity.this,
                                    "Successfully Saved Your Forage!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    //update if already exists on API
                    else {
                        wasSuccessful = ds.updateForage(currentForage);

                        Toast.makeText(
                                MainActivity.this,
                                "Successfully Updated Your Forage!",
                                Toast.LENGTH_LONG).show();
                    }
                    ds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                    Toast.makeText(
                            MainActivity.this,
                            "Couldn't Save This Forage",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        EditText editTextName = findViewById(R.id.editTextMainName);
        imm.hideSoftInputFromWindow(editTextName.getWindowToken(), 0);
        EditText editTextType = findViewById(R.id.editTextMainType);
        imm.hideSoftInputFromWindow(editTextType.getWindowToken(), 0);
        EditText editTextYield = findViewById(R.id.editTextMainYield);
        imm.hideSoftInputFromWindow(editTextYield.getWindowToken(), 0);
    }

    private void initViewListButton() {
        Button listButton = findViewById(R.id.buttonListView);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,
                        ListActivity.class);
                intent.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP));

                startActivity(intent);
            }
        });
    }

}