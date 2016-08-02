package edu.uwm.diabetesapp;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by jrhay on 7/25/2016.
 */





public class AddDataHeader extends LinearLayout {



    public AddDataHeader(Context context) {
        super(context);
        inflate(context, R.layout.add_data_header, this);


    }

    public AddDataElement createBGLElement(Context context){
        return new BGLElement(context);
    }
    public AddDataElement createMedicationElement(Context context){
        return new MedicationElement(context);
    }
    public AddDataElement createMealElement(Context context){
        return new DietElement(context);
    }
    public AddDataElement createFitnessElemnet(Context context){
        return new FitnessElement(context);
    }
}
