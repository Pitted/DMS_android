package edu.uwm.diabetesapp;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by jrhay on 7/25/2016.
 */





public class AddDataHeader extends LinearLayout {

    private int count;

    public AddDataHeader(Context context) {
        super(context);
        inflate(context, R.layout.add_data_header, this);
        count = 0;
    }

    public AddDataElement createBGLElement(Context context, Activity parent){
        count +=1;
        return new BGLElement(context,parent, count-1);
    }
    public AddDataElement createMedicationElement(Context context, Activity parent){
        count +=1;
        return new MedicationElement(context,parent, count-1);
    }
    public AddDataElement createMealElement(Context context, Activity parent){
        count +=1;
        return new DietElement(context, parent, count-1);
    }
    public AddDataElement createFitnessElemnet(Context context, Activity parent){
        count +=1;
        return new FitnessElement(context, parent, count-1);
    }
}
