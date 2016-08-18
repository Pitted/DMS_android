package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by jrhay on 7/25/2016.
 */
public class DietElement  extends AddDataElement
        implements TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener {

    private DatabaseHelper diabeticdb;
    private NutritionEvent meal;
    private Button datebtn;
    private Button timebtn;
    private AppHelpers helper;
    public DietElement(Context context, Activity parent, int i) {
        super(context, parent, i);
        inflate(context, R.layout.meal_input_view, this);
        diabeticdb = DatabaseHelper.getInstance(context);
        meal = new NutritionEvent();
        helper = new AppHelpers();

        //~~set up the to Date button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        datebtn = (Button) findViewById(R.id.date_btn);
        datebtn.setText(helper.getDate()); //init with current date
        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        //~~set up the time from button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        timebtn = (Button) findViewById(R.id.time_btn);
        timebtn.setText(helper.getTime()); //init with current time
        timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });
        meal.setEventDateTime(Calendar.getInstance());
    }

    @Override
    public boolean save() {
        if (((CheckBox) findViewById(R.id.meal_checkbox)).isChecked()) {

            try {
                meal.setNutritionEvent(Integer.parseInt(((TextView) findViewById(R.id.meal_input_qty)).getText().toString()), ((EditText)findViewById(R.id.meal_input_desc)).getText().toString());
            } catch (NumberFormatException e) {
                return false;
            }
            //boolean saved = diabeticdb.saveEvent(1,0,meal.getNutritionEvent(),null,null);
            boolean saved = diabeticdb.saveEvent(meal);
            if (saved == true)
                Toast.makeText(this.getContext(), "Medication Saved", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this.getContext(), "Error: Medication Not Saved", Toast.LENGTH_LONG).show();

            return saved;
        } else
            return false;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newDateFragment = new DatePickerFragment();
        newDateFragment.show(super.getParentActivity().getFragmentManager(), "datePicker");
    }

    //~~method to launch the time picker~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void showTimePickerDialog(View v) {
        DialogFragment newTimeFragment = new TimePickerFragment();
        newTimeFragment.show(super.getParentActivity().getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        timebtn.setText(helper.formatTime(hour,minute));
        meal.setEventDateTime(hour, minute);
    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        datebtn.setText(helper.formatDate(year, month + 1, day));
        meal.setEventDateTime(year, month, day);
    }
    @Override
    public DataEvent getEvent(){
        return meal;
    }
}