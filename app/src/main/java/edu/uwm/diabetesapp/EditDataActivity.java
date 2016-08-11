package edu.uwm.diabetesapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;

public class EditDataActivity extends Activity
        implements BGLEventFrag.OnSaveListener, MedicationEventFrag.OnSaveListener, NutritionEventFrag.OnSaveListener, FitnessEventFrag.OnSaveListener,
BGLEventFrag.OnFragmentInteractionListener, MedicationEventFrag.OnFragmentInteractionListener, NutritionEventFrag.OnFragmentInteractionListener, FitnessEventFrag.OnFragmentInteractionListener{

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
    private android.app.FragmentManager manager;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        manager = this.getFragmentManager();
        setContentView(R.layout.activity_edit_data);
        list = (ListView) findViewById(R.id.edit_data_list);
        db = DatabaseHelper.getInstance(this);
        cursor = db.getBGLEvents();
        adapter = new CursorAdapter(this, cursor, false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return new TextView(context);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                DiabeticEntry entry = new DiabeticEntry(cursor.getLong(0),cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4),cursor.getString(5), cursor.getString(6));
                TextView txtvw = (TextView) view;
                txtvw.setTextSize(14);
                try {
                    txtvw.setText(entry.createEvent().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        };
        list.setAdapter(adapter);
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
                    adapter.swapCursor(db.getFitnessEvents());
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
                    adapter.swapCursor(db.getNutritionEvents());
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
                    adapter.swapCursor(db.getMedicationEvents());
                    listType = new MedicationEvent();
                    adapter.notifyDataSetChanged();

                }
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) adapter.getItem(position);
                DiabeticEntry entry = new DiabeticEntry(cursor.getLong(0),cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4),cursor.getString(5), cursor.getString(6));
                try {
                    DataEvent obj = entry.createEvent();
                    if(obj instanceof BGLLevel){
                        BGLEventFrag frag = BGLEventFrag.newInstance(c.getLong(0));
                        frag.setBGL((BGLLevel) obj);
                        frag.show(manager, "Edit BGL");
                    }
                    else if(obj instanceof  MedicationEvent){
                        MedicationEventFrag frag = MedicationEventFrag.newInstance(c.getLong(0));
                        frag.setMedication((MedicationEvent) obj);
                        frag.show(manager, "Edit Medication");
                    }
                    else if(obj instanceof NutritionEvent){
                        NutritionEventFrag frag = NutritionEventFrag.newInstance(c.getLong(0));
                        frag.setNutrition((NutritionEvent) obj);
                        frag.show(manager, "Edit Nutrition");
                    }
                    else{
                        FitnessEventFrag frag = FitnessEventFrag.newInstance(c.getLong(0));
                        frag.setFitness((FitnessEvent)obj);
                        frag.show(manager, "Edit Fitness");
                    }
                    int width = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
                    int height = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //do nothing
    }
    @Override
    public void onSave(BGLLevel obj, long _id) {
        ContentValues cv = new ContentValues();
        cv.put("DateTime", (new AppHelpers()).formatDateTime(obj.getEventDateTime()));
        cv.put("BGL", (obj.getBGL()));
        DatabaseHelper.getInstance(this).getWritableDatabase().update("DiabeticTable", cv, "_id="+_id, null);
    }

    @Override
    public void onSave(MedicationEvent obj, long _id) {
        ContentValues cv = new ContentValues();
        cv.put("DateTime", (new AppHelpers()).formatDateTime(obj.getEventDateTime()));
        cv.put("Medication", (obj.getMedicationEvent()));
        DatabaseHelper.getInstance(this).getWritableDatabase().update("DiabeticTable", cv, "_id="+_id, null);
        }

    @Override
    public void onSave(NutritionEvent obj, long _id) {
        ContentValues cv = new ContentValues();
        cv.put("DateTime", (new AppHelpers()).formatDateTime(obj.getEventDateTime()));
        cv.put("Diet", obj.getNutritionEvent());
        DatabaseHelper.getInstance(this).getWritableDatabase().update("DiabeticTable", cv, "_id="+_id, null);
    }

    @Override
    public void onSave(FitnessEvent obj, long _id) {
        ContentValues cv = new ContentValues();
        cv.put("DateTime", (new AppHelpers()).formatDateTime(obj.getEventDateTime()));
        cv.put("Exercise", obj.getFitnessEvent());
        DatabaseHelper.getInstance(this).getWritableDatabase().update("DiabeticTable", cv, "_id="+_id, null);
        }
}
