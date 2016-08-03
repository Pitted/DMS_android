package edu.uwm.diabetesapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by jrhay on 7/26/2016.
 */
public class InputDataList extends ListView {

//    private final Context context;
    private LinearLayout header;
    ArrayList<AddDataElement> data_elements;
    private InputDataAdapter adapter;
    public InputDataList(final Context context) {
        super(context);
//        this.context = context;
        header = new AddDataHeader(context);
        addHeaderView(header);
        this.setDividerHeight(5);
        //addHeaderView((LinearLayout) inflate(context, R.layout.add_data_header, this));
        data_elements = new ArrayList<AddDataElement>();
        adapter = new InputDataAdapter(context, data_elements);
        this.setAdapter(adapter);
        ImageButton createBGL =  (ImageButton) findViewById(R.id.create_bgl_button);
        ImageButton createMedication = (ImageButton) findViewById(R.id.create_medication_button);
        ImageButton createMeal = (ImageButton) findViewById(R.id.create_meal_button);
        ImageButton createExercise = (ImageButton) findViewById(R.id.create_exercise_button);
        createBGL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 adapter.add(((AddDataHeader) header).createBGLElement(context, (Activity)context));
            }
        });
        createMedication.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(((AddDataHeader) header).createMedicationElement(context, (Activity)context));
            }
        });
        createMeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(((AddDataHeader) header).createMealElement(context, (Activity)context));
            }
        });
        createExercise.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add(((AddDataHeader) header).createFitnessElemnet(context, (Activity)context));
            }
        });
    }
    public void save(){
        List<AddDataElement> rem_list = new ArrayList<AddDataElement>();
        for(AddDataElement ele: this.adapter.getList()){
            if(ele.save()){
                Log.d("SAVE", "ele.save");
                rem_list.add(ele);
            }
        }
        adapter.removeAll(rem_list);
        adapter.notifyDataSetChanged();
    }
}
