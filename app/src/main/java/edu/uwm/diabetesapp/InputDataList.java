package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by jrhay on 7/26/2016.
 */
public class InputDataList extends ListView
        implements BGLEventFrag.OnSaveListener, MedicationEventFrag.OnSaveListener, NutritionEventFrag.OnSaveListener, FitnessEventFrag.OnSaveListener,
        BGLEventFrag.OnFragmentInteractionListener, MedicationEventFrag.OnFragmentInteractionListener, NutritionEventFrag.OnFragmentInteractionListener, FitnessEventFrag.OnFragmentInteractionListener
{

//    private final Context context;
    private LinearLayout header;
    //ArrayList<AddDataElement> data_elements;
    //private InputDataAdapter adapter;
    private List<DataEvent> events;
    private ArrayAdapter<DataEvent> adapter;
    private DialogFragment frag;
    private FragmentManager manager;
    public InputDataList(final Context context) {
        super(context);
//        this.context = context;
        header = new AddDataHeader(context);
        manager = ((Activity) context).getFragmentManager();
        addHeaderView(header);
        this.setDividerHeight(5);
        events = new ArrayList<>();
        //addHeaderView((LinearLayout) inflate(context, R.layout.add_data_header, this));
        //data_elements = new ArrayList<AddDataElement>();
        //adapter = new InputDataAdapter(context, data_elements);
        adapter = new ArrayAdapter<DataEvent>(context,R.layout.blank_element, R.id.blank_text, events){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                if(convertView == null){
                    TextView v = new TextView(getContext());
                    v.setText(events.get(position).toString());
                    v.setTextSize(15f);
                    v.setPadding(10,10,10,10);
                    return (v);
                }
                return convertView;
            }
        };
        this.setAdapter(adapter);
        ImageButton createBGL =  (ImageButton) findViewById(R.id.create_bgl_button);
        ImageButton createMedication = (ImageButton) findViewById(R.id.create_medication_button);
        ImageButton createMeal = (ImageButton) findViewById(R.id.create_meal_button);
        ImageButton createExercise = (ImageButton) findViewById(R.id.create_exercise_button);
        createBGL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //adapter.add(((AddDataHeader) header).createBGLElement(context, (Activity)context));
                frag = BGLEventFrag.newInstance((long)0);
                frag.show(manager, "Edit BGL");
            }
        });
        createMedication.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //adapter.add(((AddDataHeader) header).createMedicationElement(context, (Activity)context));
                frag = MedicationEventFrag.newInstance((long)0);
                frag.show(manager, "Edit Medication");
            }
        });
        createMeal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //adapter.add(((AddDataHeader) header).createMealElement(context, (Activity)context));
                frag = NutritionEventFrag.newInstance((long)0);
                frag.show(manager, "Edit Nutrition");
            }
        });
        createExercise.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //adapter.add(((AddDataHeader) header).createFitnessElemnet(context, (Activity)context));
                frag = FitnessEventFrag.newInstance((long) 0);
                frag.show(manager, "Edit Fitness");
            }
        });
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DataEvent obj = adapter.getItem(position-1);

                    if (obj instanceof BGLLevel) {
                        frag = BGLEventFrag.newInstance(position);
                        ((BGLEventFrag) frag).setBGL((BGLLevel) obj);
                        frag.show(manager, "Edit BGL");
                    } else if (obj instanceof MedicationEvent) {
                        frag = MedicationEventFrag.newInstance(position);
                        ((MedicationEventFrag) frag).setMedication((MedicationEvent) obj);
                        frag.show(manager, "Edit Medication");
                    } else if (obj instanceof NutritionEvent) {
                        frag = NutritionEventFrag.newInstance(position);
                        ((NutritionEventFrag) frag).setNutrition((NutritionEvent) obj);
                        frag.show(manager, "Edit Nutrition");
                    } else {
                        frag = FitnessEventFrag.newInstance(position);
                        ((FitnessEventFrag) frag).setFitness((FitnessEvent) obj);
                        frag.show(manager, "Edit Fitness");
                    }

            }
        });

    }
    public void save(){
        List<DataEvent> rem_list = new ArrayList<DataEvent>();
        for(DataEvent ele: events){
            if(DatabaseHelper.getInstance(getContext()).saveEvent(ele)){
                Toast.makeText(this.getContext(), "Event Saved", Toast.LENGTH_LONG).show();
                rem_list.add(ele);
            }else
                Toast.makeText(this.getContext(), "Error: Event Not Saved", Toast.LENGTH_LONG).show();
        }
        for(DataEvent ele: rem_list){
            adapter.remove(ele);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSave(MedicationEvent obj, long _id) {
        if(events.contains(obj)){
            adapter.notifyDataSetChanged();
            frag.getDialog().hide();
        }else {
            adapter.add(obj);
            adapter.notifyDataSetChanged();
            frag.getDialog().hide();
        }
    }

    @Override
    public void onSave(NutritionEvent obj, long _id) {
        if(events.contains(obj)) {
            adapter.notifyDataSetChanged();
            frag.getDialog().hide();
        }else{
        adapter.add(obj);
        adapter.notifyDataSetChanged();
        frag.getDialog().hide();
        }
    }

    @Override
    public void onSave(FitnessEvent obj, long _id) {
        if(events.contains(obj)) {
            adapter.notifyDataSetChanged();
            frag.getDialog().hide();
        }else {
            adapter.add(obj);
            adapter.notifyDataSetChanged();
            frag.getDialog().hide();
        }
    }

    @Override
    public void onSave(BGLLevel obj, long _id) {
        if(events.contains(obj)) {
            adapter.notifyDataSetChanged();
            frag.getDialog().hide();
        }else {
            adapter.add(obj);
            adapter.notifyDataSetChanged();
            frag.getDialog().hide();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //nothing
    }
}
