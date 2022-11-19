/*
 *  Document   : Date Picker Dialog Class For Harvest Date
 *  Created on : 11.17.22
 *  @author incomingWill
 *  CPS 435 Final Program
 */

/*
 *  onCreate View creates a view from the select_date xml
 *  get default date and display in view
 *  create calendar view object
 *  when select button is pressed, store user selected date in cv object
 *  when cancel button is pressed, step back and leave default
 */

package com.incomingwill.myforagingassistant.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.incomingwill.myforagingassistant.R;

import java.util.Calendar;

public class DatePickerDialog extends DialogFragment {
    Calendar selectedDate;

    public interface SaveDateListener{
        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    public DatePickerDialog()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        //create the view from the the xml
        final View view = inflater.inflate(R.layout.select_date, container);

        //get default date and set
        getDialog().setTitle(R.string.title_dialog);
        selectedDate = Calendar.getInstance();

        //create object to store user selected data
        final CalendarView cv = view.findViewById(R.id.calendarView);

        //method to set date in cv object
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,
                                            int year, int month, int dayOfMonth) {
                selectedDate.set(year, month, dayOfMonth);
            }
        });

        //get buttons from View object
        Button selectButton = view.findViewById(R.id.buttonSelect);
        Button cancelButton = view.findViewById(R.id.buttonCancel);

        //select button action
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem(selectedDate);
            }
        });

        //cancel button action
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void saveItem(Calendar selectedDate) {
        SaveDateListener activity = (SaveDateListener)getActivity();
        activity.didFinishDatePickerDialog(selectedDate);
        getDialog().dismiss();
    }
}

