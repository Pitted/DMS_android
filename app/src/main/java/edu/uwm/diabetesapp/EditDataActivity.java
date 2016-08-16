package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditDataActivity extends Activity
        implements BGLEventFrag.OnSaveListener, MedicationEventFrag.OnSaveListener, NutritionEventFrag.OnSaveListener, FitnessEventFrag.OnSaveListener,
BGLEventFrag.OnFragmentInteractionListener, MedicationEventFrag.OnFragmentInteractionListener, NutritionEventFrag.OnFragmentInteractionListener, FitnessEventFrag.OnFragmentInteractionListener,
        DatePickerDialog.OnDateSetListener{

    private DatabaseHelper db;
    private Cursor cursor;
    private ImageButton viewBglBtn;
    private ImageButton viewMedBtn;
    private ImageButton viewMealBtn;
    private ImageButton viewFitBtn;
    private Button fromDate;
    private Button toDate;
    private ImageButton refresh;
    private Calendar toDateTime;
    private Calendar fromDateTime;
    private android.app.DialogFragment currentFragment;
    private ListView list;
    private Button datePicked;
    private Calendar datePickedCal;
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
                    adapter.swapCursor(db.getBGLEvents());
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
                android.app.DialogFragment frag;
                DiabeticEntry entry = new DiabeticEntry(c.getLong(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4), c.getString(5), c.getString(6));
                try {
                    DataEvent obj = entry.createEvent();
                    if (obj instanceof BGLLevel) {
                        frag = BGLEventFrag.newInstance(c.getLong(0));
                        ((BGLEventFrag) frag).setBGL((BGLLevel) obj);
                        frag.show(manager, "Edit BGL");
                    } else if (obj instanceof MedicationEvent) {
                        frag = MedicationEventFrag.newInstance(c.getLong(0));
                        ((MedicationEventFrag) frag).setMedication((MedicationEvent) obj);
                        frag.show(manager, "Edit Medication");
                    } else if (obj instanceof NutritionEvent) {
                        frag = NutritionEventFrag.newInstance(c.getLong(0));
                        ((NutritionEventFrag) frag).setNutrition((NutritionEvent) obj);
                        frag.show(manager, "Edit Nutrition");
                    } else {
                        frag = FitnessEventFrag.newInstance(c.getLong(0));
                        ((FitnessEventFrag) frag).setFitness((FitnessEvent) obj);
                        frag.show(manager, "Edit Fitness");
                    }
                    currentFragment = frag;
                    int width = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
                    int height = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
                    currentFragment = frag;

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });
        fromDateTime = Calendar.getInstance();
        toDateTime = Calendar.getInstance();
        toDate = (Button)findViewById(R.id.toDateBtn);
        toDate.setText(new SimpleDateFormat("MM/dd/yy").format(toDateTime.getTime()));
        fromDate = (Button)findViewById(R.id.fromDateBtn);
        fromDate.setText(new SimpleDateFormat("MM/dd/yy").format(fromDateTime.getTime()));
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicked = toDate;
                datePickedCal = toDateTime;
                showDatePicker(v);
            }
        });
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicked = fromDate;
                datePickedCal = fromDateTime;
                showDatePicker(v);
            }
        });

    }

    private void refreshList() {
        if(toDateTime.getTime().compareTo(fromDateTime.getTime())>=0) {
            if(listType instanceof BGLLevel){
                adapter.swapCursor(db.getBGLEventBetween(fromDateTime.getTime(), toDateTime.getTime()));
            }else if(listType instanceof MedicationEvent){
                adapter.swapCursor(db.getMedEventBetween(fromDateTime.getTime(), toDateTime.getTime()));
            }else if(listType instanceof NutritionEvent){
                adapter.swapCursor(db.getNutEventBetween(fromDateTime.getTime(), toDateTime.getTime()));
            }else if(listType instanceof FitnessEvent){
                adapter.swapCursor(db.getFitEventBetween(fromDateTime.getTime(), toDateTime.getTime()));
            }
            else{}
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //do nothing
    }
    @Override
    public void onSave(BGLLevel obj, long _id){
        DatabaseHelper.getInstance(this).update(obj, _id);
        currentFragment.getDialog().hide();
//        manager.beginTransaction().remove(currentFragment);
        adapter.swapCursor(db.getBGLEvents());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSave(MedicationEvent obj, long _id){
        DatabaseHelper.getInstance(this).update(obj, _id);
        currentFragment.getDialog().hide();
        adapter.swapCursor(db.getMedicationEvents());
//        manager.beginTransaction().remove(currentFragment.this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSave(NutritionEvent obj, long _id){
        DatabaseHelper.getInstance(this).update(obj, _id);
        currentFragment.getDialog().hide();
        adapter.swapCursor(db.getNutritionEvents());
//        manager.beginTransaction().remove(currentFragment.this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSave(FitnessEvent obj, long _id){
        DatabaseHelper.getInstance(this).update(obj, _id);
        currentFragment.getDialog().hide();
        adapter.swapCursor(db.getFitnessEvents());
//        manager.beginTransaction().remove(currentFragment.this);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        datePickedCal.set(year, monthOfYear, dayOfMonth);
        datePicked.setText(new SimpleDateFormat("MM/dd/yy").format(datePickedCal.getTime()));
    }
    public void showDatePicker(View v){
        DatePickerDialog datePicker = new DatePickerDialog(this,this,datePickedCal.get(Calendar.YEAR),datePickedCal.get(Calendar.MONTH), datePickedCal.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }
}
