package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;

public class EditDataActivity extends Activity {

    private DatabaseHelper db;
    private Cursor cursor;
    private ImageButton viewBglBtn;
    private ImageButton viewMedBtn;
    private ImageButton viewMealBtn;
    private ImageButton viewFitBtn;
    private Button fromDate;
    private Button toDate;
    private Button Refresh;
    private ListView list;
    private CursorAdapter adapter;
    private DataEvent listType = new BGLLevel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        list = (ListView) findViewById(R.id.edit_data_list);
        db = DatabaseHelper.getInstance(this);
        cursor = db.getBGLEvents();
        adapter = new CursorAdapter(list.getContext(), cursor, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return new TextView(context);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                DiabeticEntry entry = new DiabeticEntry(cursor.getLong(0),cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4),cursor.getString(5), cursor.getString(6));
                TextView txtvw = (TextView) view;
                try {
                    txtvw.setText(entry.createEvent().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };

        viewBglBtn = (ImageButton) findViewById(R.id.view_bgl_btn);
        viewBglBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listType instanceof BGLLevel) return;
                else {
                    cursor = db.getBGLEvents();
                    listType = new BGLLevel();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        viewFitBtn = (ImageButton) findViewById(R.id.view_fitness_btn);
        viewFitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listType instanceof FitnessEvent) return;
                else{
                    cursor = db.getFitnessEvents();
                    listType = new FitnessEvent();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        viewMealBtn = (ImageButton) findViewById(R.id.view_diet_btn);
        viewMealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listType instanceof FitnessEvent) return;
                else{
                    cursor = db.getNutritionEvents();
                    listType = new NutritionEvent();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        viewMedBtn = (ImageButton) findViewById(R.id.view_med_btn);
        viewMedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listType instanceof MedicationEvent) return;
                else{
                    cursor = db.getMedicationEvents();
                    listType = new MedicationEvent();
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

}
