package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by jrhay on 7/25/2016.
 */
public class BGLElement extends AddDataElement
        implements TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener{


    private DatabaseHelper diabeticdb ; //retrun reference to master db
    private BGLLevel bgl;
    private Button TimeBtn;
    private Button DateBtn;
    private AppHelpers helper;

    public BGLElement(Context context, Activity parent, int i){
        super(context, parent, i);
        inflate(context, R.layout.bgl_input_view, this);
        diabeticdb = DatabaseHelper.getInstance(context);
        bgl = new BGLLevel();
        helper = new AppHelpers();

        //~~set up the to Date button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        DateBtn = (Button) findViewById(R.id.date_btn);
        DateBtn.setText(helper.getDate()); //init with current date
        DateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        //~~set up the time from button~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        TimeBtn = (Button) findViewById(R.id.time_btn);
        TimeBtn.setText(helper.getTime()); //init with current time
        TimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });
        bgl.setEventDateTime(Calendar.getInstance());
    }
    @Override
    public boolean save(){
        if(((CheckBox) findViewById(R.id.checkBox)).isChecked()) {
            try {
                bgl.setBGL(Double.parseDouble(((EditText) findViewById(R.id.bgl_value)).getText().toString()));
            } catch (NumberFormatException e) {
                return false;
            }
            //boolean saved = diabeticdb.saveEvent(0,bgl.getBGL(),null,null,null);
            //int code, int bgl, String diet, String exercise, String medication
            boolean saved = diabeticdb.saveEvent(bgl);
            if (saved)
                Toast.makeText(this.getContext(), "BGL Saved", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this.getContext(), "Error: BGL Not Saved", Toast.LENGTH_LONG).show();
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
        newTimeFragment.show(super.getParentActivity().getFragmentManager(), "timePicker");
    }

    //~~method when the time is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        TimeBtn.setText(helper.formatTime(hour,minute));
        bgl.setEventDateTime(hour,minute);
    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        DateBtn.setText(helper.formatDate(year,month+1,day));
        bgl.setEventDateTime(year,month,day);

    }
}
