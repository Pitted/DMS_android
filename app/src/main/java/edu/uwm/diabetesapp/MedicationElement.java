package edu.uwm.diabetesapp;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jrhay on 7/25/2016.
 */
public class MedicationElement extends AddDataElement{

    DatabaseHelper diabeticdb ;
    private MedicationEvent medication;
    public MedicationElement(Context context) {
        super(context);
        inflate(context, R.layout.medication_input_view, this);
        diabeticdb = DatabaseHelper.getInstance(context);
        medication = new MedicationEvent();

    }
    @Override
    public boolean save(){
        if(((CheckBox) findViewById(R.id.med_checkbox)).isChecked()) {
            try {
                medication.setMedicationEvent(Integer.parseInt(((TextView)findViewById(R.id.med_input_qty)).getText().toString()), findViewById(R.id.med_input_desc).toString());
            } catch (NumberFormatException e) {
                return false;
            }
            //boolean saved = diabeticdb.saveEvent(3,0,null,null,medication.getMedicationEvent());
            //int code, int bgl, String diet, String exercise, String medication
            boolean saved = diabeticdb.saveEvent(medication);
            if (saved == true)
                Toast.makeText(this.getContext(), "Medication Saved", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this.getContext(), "Error: Medication Not Saved", Toast.LENGTH_LONG).show();

            return saved;
        }else
            return false;

    }
}
