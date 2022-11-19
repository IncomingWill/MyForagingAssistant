/*
 *  Document   : Forage Adapter
 *  Created on : 11.17.22
 *  @author incomingWill
 *  CPS 435 Final Program
 */

/*
 *  adapter facilitates user manipulation of data in view
 *  The adapter class is declared and sub classes RV adapter
 *  declare the data to be displayed
 *  ViewHolder behavior creates, gets, and displays the various references
 *      such as forage entry name, type, yield, data, and image
 *  declare constructor for adapter that is used to associate
 *      data to be displayed via adapter
 *  override generic ViewHolder, which is called for each item in the data set
 *  override parent getItemCount, need to return number of items in the data set
 */

package com.incomingwill.myforagingassistant.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.incomingwill.myforagingassistant.R;
import com.incomingwill.myforagingassistant.model.Forage;
import com.incomingwill.myforagingassistant.model.ForageDataSource;

import java.util.ArrayList;

public class ForageAdapter extends RecyclerView.Adapter{
    //array of names to be displayed
    private final ArrayList<Forage> forageData;
    //Want to track if item from list is clicked
    private View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    //activity context that is using list
    private final Context parentContext;

    public class ForageViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvType;
        public TextView tvYield;
        public TextView tvDate;
        public ImageView ivPhoto;
        public Button buttonDeleteForage;

        public ForageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewListDisplayName);
            tvType = itemView.findViewById(R.id.textViewListDisplayType);
            tvYield = itemView.findViewById(R.id.textViewListDisplayYield);
            tvDate = itemView.findViewById(R.id.textViewListDisplayDate);
            ivPhoto = itemView.findViewById(R.id.imageViewPhoto);

            buttonDeleteForage = itemView.findViewById(R.id.buttonDeleteForage);

            //set tag so that item click may be identified
            itemView.setTag(this);
            //listen for click
            itemView.setOnClickListener(mOnItemClickListener);
        }

        public TextView getNameTextView() { return tvName; }
        public TextView getTypeTextView() { return tvType; }
        public TextView getYieldTextView() { return tvYield; }
        public TextView getDateTextView() { return tvDate; }
        public ImageView getIvPhoto() { return ivPhoto; }
        public Button getDeleteButton() { return buttonDeleteForage; }
    }

    public ForageAdapter(
            ArrayList<Forage> arrayList, Context context) {
        forageData = arrayList;
        parentContext = context;
    }

    //method to pass listener from activity to adapter
    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ForageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder,
            final int position) {
        ForageViewHolder fvh = (ForageViewHolder) holder;
        fvh.getNameTextView()
                .setText(forageData
                        .get(position)
                        .getForageName());
        fvh.getTypeTextView()
                .setText(forageData
                        .get(position)
                        .getForageType());
        fvh.getYieldTextView()
                .setText(forageData
                        .get(position)
                        .getYieldString());
        fvh.getDateTextView()
                .setText(forageData
                        .get(position)
                        .getDateString());
        fvh.getIvPhoto()
                .setImageBitmap(forageData
                        .get(position)
                        .getPicture());

        if (isDeleting) {
            fvh.getDeleteButton().setVisibility(View.VISIBLE);
            fvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        }
        else {
            fvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return forageData.size();
    }

    public void setDelete(boolean b) {
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Forage f = forageData.get(position);
        ForageDataSource ds = new ForageDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteForage(f.getForageID());
            ds.close();
            if (didDelete) {
                forageData.remove(position);
                notifyDataSetChanged();
                Toast.makeText(parentContext,
                        "Delete Successful!",
                        Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(parentContext,
                        "Delete Failed!",
                        Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception e) {
            Toast.makeText(parentContext,
                    "Delete Failed!",
                    Toast.LENGTH_LONG).show();
        }
    }


}

