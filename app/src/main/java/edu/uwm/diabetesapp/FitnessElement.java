package edu.uwm.diabetesapp;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jrhay on 7/25/2016.
 */
public class FitnessElement extends AddDataElement {
    DatabaseHelper diabeticdb;
    private FitnessEvent fit;
    public FitnessElement(Context context){
        super(context);
         inflate(context, R.layout.fitness_input_view, this);
        diabeticdb = DatabaseHelper.getInstance(context);
        fit = new FitnessEvent();
    }
    @Override
    public boolean save(){
        if(((CheckBox) findViewById(R.id.fitness_checkbox)).isChecked()) {
            try {
                fit.setFitnessEvent(Integer.parseInt(((TextView)findViewById(R.id.fitness_input_qty)).getText().toString()), findViewById(R.id.fitness_input_desc).toString());
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
}
