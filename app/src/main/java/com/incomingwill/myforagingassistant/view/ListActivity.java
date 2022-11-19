
/*
 *  Document   : Forage List Activity
 *  Created on : 11.17.22
 *  @author incomingWill
 *  CPS 435 Final Program
 */

/*
 * List Activity to display all Forages from database
 * click listener will be passed to adapter
 * onCLick gets reference to ViewHolder that produced the click
 * and then uses it to get the position of the viewHolder in list
 * (This particular function not active yet to edit)
 * onCreate sets page title, initializes new forage button,
 * sets shared preferences for sort order, opens the database, pulls the foragess,
 * and places them into recycler view object for display.
 */

package com.incomingwill.myforagingassistant.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.incomingwill.myforagingassistant.R;
import com.incomingwill.myforagingassistant.model.Forage;
import com.incomingwill.myforagingassistant.model.ForageDataSource;
import com.incomingwill.myforagingassistant.presenter.ForageAdapter;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    //public static final String TAG = "ListActivity";
    ArrayList<Forage> forages;
    ForageAdapter forageAdapter;

    //On Click Listener to pass forage to map activity
    //get forage by id from database, pass through intent
    private View.OnClickListener onItemClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //cast RecycleView
                    RecyclerView.ViewHolder viewHolder =
                            (RecyclerView.ViewHolder) view.getTag();

                    //need to know integer position
                    int position = viewHolder.getAdapterPosition();
                    int forageId = forages.get(position).getForageID();
                    float latitude = forages.get(position).getLatitude();
                    float longitude = forages.get(position).getLongitude();

                    //get to map activity
                    Intent intent = new Intent(
                            ListActivity.this,
                            MapActivity.class);
                    //passing info to next screen
                    intent.putExtra("forageId", forageId);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        this.setTitle(R.string.forage_list);

        initNewForageButton();

        //display by sortfield, forage name is default
        String sortBy = getSharedPreferences(
                "MyForageListPreferences", Context.MODE_PRIVATE)
                .getString("sortfield", "foragename");

        //display ascending sortorder
        String sortOrder = getSharedPreferences(
                "MyForageList", Context.MODE_PRIVATE)
                .getString("sortorder", "ASC");

        ForageDataSource ds = new ForageDataSource(this);

        try {
            //open database, get forages, close
            ds.open();
            forages = ds.getForages(sortBy, sortOrder);
            ds.close();

            //if 0 greater than 0, store forage rows from database in forages
                //then display in recycler view
                //else, go to main activity to edit empty forage
            if (forages.size() > 0) {
                RecyclerView forageList = findViewById(R.id.rvForages);
                RecyclerView.LayoutManager layoutManager
                        = new LinearLayoutManager(this);
                forageList.setLayoutManager(layoutManager);
                ForageAdapter forageAdapter
                        = new ForageAdapter(forages, this);
                forageAdapter.setOnItemClickListener(onItemClickListener);
                forageList.setAdapter(forageAdapter);
            }
            else {
                Intent intent = new Intent(
                        ListActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(
                        this,
                        "No Forages to display",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(
                    this,
                    "Error retrieving forages",
                    Toast.LENGTH_LONG).show();
        }

        initDeleteSwitch();

    }

    @Override
    public void onResume(){
        super.onResume();

        //display by sortfield, forage name is default
        String sortBy = getSharedPreferences(
                "MyForageListPreferences", Context.MODE_PRIVATE)
                .getString("sortfield", "foragename");

        //display ascending sortorder
        String sortOrder = getSharedPreferences(
                "MyForageList", Context.MODE_PRIVATE)
                .getString("sortorder", "ASC");

        ForageDataSource ds = new ForageDataSource(this);

        try {
            //open database, get forages, close
            ds.open();
            forages = ds.getForages(sortBy, sortOrder);
            ds.close();

            //if 0 greater than 0, store forage rows from database in forages
            //then display in recycler view
            //else, go to main activity to edit empty forage
            if (forages.size() > 0) {
                RecyclerView forageList = findViewById(R.id.rvForages);
                RecyclerView.LayoutManager layoutManager
                        = new LinearLayoutManager(this);
                forageList.setLayoutManager(layoutManager);
                ForageAdapter forageAdapter
                        = new ForageAdapter(forages, this);
                forageAdapter.setOnItemClickListener(onItemClickListener);
                forageList.setAdapter(forageAdapter);
            }
            else {
                Intent intent = new Intent(
                        ListActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(
                        this,
                        "No Forages to display",
                        Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(
                    this,
                    "Error retrieving forages",
                    Toast.LENGTH_LONG).show();
        }

    }


    private void initNewForageButton() {
        Button buttonNewForage = findViewById(R.id.buttonMainView);
        buttonNewForage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(
                        ListActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //enable and display delete button
        //switch is considered on if the isChecked method returns true
        //status of delete switch is passed to forageAdapter
        //notify DataSetChanged is called, tells adapter to redraw list
    private void initDeleteSwitch()
    {
        //ContactAdapter contactAdapter = new ContactAdapter(contacts, this);
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                boolean status = compoundButton.isChecked();
                forageAdapter.setDelete(status);
                forageAdapter.notifyDataSetChanged();
            }
        });
    }
}