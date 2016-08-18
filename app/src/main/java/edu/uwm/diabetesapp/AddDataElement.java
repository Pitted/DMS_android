package edu.uwm.diabetesapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import edu.uwm.diabetesapp.TimePickerFragment.OnTimePickedListener;

/**
 * Created by jrhay on 7/25/2016.
 */
public class AddDataElement extends LinearLayout {

    private int id;
    private Activity parent;
    public AddDataElement(Context context, Activity parent,int i) {
        super(context);
        this.parent = parent;
        id = i;
    }
    public Activity getParentActivity(){return parent;}
    public boolean save(){return false;}
    public DataEvent getEvent(){return null;}
}
