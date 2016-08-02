package edu.uwm.diabetesapp;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by jrhay on 7/25/2016.
 */
public class BGLElement extends AddDataElement {


    DatabaseHelper diabeticdb ; //retrun reference to master db
    BGLLevel bgl = new BGLLevel();
    public BGLElement(Context context){
        super(context);
        inflate(context, R.layout.bgl_input_view, this);
        diabeticdb = DatabaseHelper.getInstance(context);
        bgl = new BGLLevel();
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

}
