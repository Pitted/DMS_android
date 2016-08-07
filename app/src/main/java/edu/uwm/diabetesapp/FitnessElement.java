package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by jrhay on 7/25/2016.
 */
public class FitnessElement extends AddDataElement
        implements TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener{
    private DatabaseHelper diabeticdb;
    private FitnessEvent fit;
    private AppHelpers helper;
    private Button timebtn;
    private Button datebtn;

    public FitnessElement(Context context, Activity parent, int i){
        super(context, parent, i);
         inflate(context, R.layout.fitness_input_view, this);
        diabeticdb = DatabaseHelper.getInstance(context);
        fit = new FitnessEvent();
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
        fit.setEventDateTime(Calendar.getInstance());
    }
    @Override
    public boolean save(){
        if(((CheckBox) findViewById(R.id.fitness_checkbox)).isChecked()) {
            try {
                fit.setFitnessEvent(Integer.parseInt(((TextView) findViewById(R.id.fitness_input_qty)).getText().toString()), ((EditText)findViewById(R.id.fitness_input_desc)).getText().toString());
            } catch (NumberFormatException e) {
                return false;
            }
            //boolean saved = diabeticdb.saveEvent(2,0,null,fit.getFitnessEvent(),null);
            boolean saved = diabeticdb.saveEvent(fit);
            if (saved == true)
                Toast.makeText(this.getContext(), "Medication Saved", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this.getContext(), "Error: Medication Not Saved", Toast.LENGTH_LONG).show();

            return saved;
        }else
            return false;
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newDateFragment = new DatePickerFragment();
        newDateFragment.show(super.getParentActivity().getFragmentManager(), "datePicker");
    }

    //~~method to launch the time picker~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void showTimePickerDialog(View v) {
        DialogFragment newTimeFragment = new TimePickerFragment();
        newTimeFragment.show(getParentActivity().getFragmentManager(), "timePicker");
    }

    @Override
    public void onDatePicked(int picker, int year, int month, int dayOfMonth) {
        datebtn.setText(helper.formatDate(year, month+1, dayOfMonth));
        fit.setEventDateTime(year, month, dayOfMonth);
    }

    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        timebtn.setText(helper.formatTime(hour, minute));
        fit.setEventDateTime(hour, minute);
    }
}
