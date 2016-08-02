package edu.uwm.diabetesapp;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jrhay on 7/25/2016.
 */
public class DietElement  extends AddDataElement{

    DatabaseHelper diabeticdb;
    NutritionEvent meal;
    public DietElement(Context context) {
        super(context);
         inflate(context, R.layout.meal_input_view, this);
        diabeticdb = DatabaseHelper.getInstance(context);
        meal = new NutritionEvent();
    }
    @Override
    public boolean save() {
        if(((CheckBox) findViewById(R.id.meal_checkbox)).isChecked()) {

            try {
                meal.setNutritionEvent(Integer.parseInt(((TextView)findViewById(R.id.meal_input_qty)).getText().toString()), findViewById(R.id.meal_input_desc).toString());
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
        }else
            return false;
    }
}
