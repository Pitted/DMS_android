package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GraphActivity extends Activity implements DatePickerDialog.OnDateSetListener{

    private Button toBtn;
    private Button fromBtn;
    private Button dateButton;
    private Button createGraph;
    private Calendar fromTime = new GregorianCalendar();
    private Calendar toTime = new GregorianCalendar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        toBtn = (Button) findViewById(R.id.graph_to_date);
        fromBtn = (Button) findViewById(R.id.graph_from_date);
        toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateButton = toBtn;
                showDatePicker(v);
            }
        });
        fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateButton = fromBtn;
                showDatePicker(v);
            }
        });
        createGraph = (Button) findViewById(R.id.create_graph_button);
        createGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGraphDialog(v);
            }
        });
    }

    private void showGraphDialog(View v) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BGLGraphFrag frag = BGLGraphFrag.newInstance(dateFormat.format(fromTime.getTime()), dateFormat.format(toTime.getTime()));
        frag.show(this.getFragmentManager(),"Graph");
    }

    public void showDatePicker(View v){
        if(dateButton == toBtn){
            if(toTime != null){
                DatePickerDialog picker = new DatePickerDialog(this, this, toTime.get(Calendar.YEAR), toTime.get(Calendar.MONTH), toTime.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }else{
                Calendar c = Calendar.getInstance();
                DatePickerDialog picker = new DatePickerDialog(this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
        }else if(dateButton == fromBtn){
            if(fromTime != null){
                DatePickerDialog picker = new DatePickerDialog(this, this, fromTime.get(Calendar.YEAR), fromTime.get(Calendar.MONTH), fromTime.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }else{
                Calendar c = Calendar.getInstance();
                DatePickerDialog picker = new DatePickerDialog(this, this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
        }else{
        //shit
         }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(dateButton == toBtn){
            toTime.set(year, monthOfYear, dayOfMonth);
            toBtn.setText(new SimpleDateFormat("MM/dd/yyyy").format(toTime.getTime()));
        }else{
            fromTime.set(year, monthOfYear, dayOfMonth);
            fromBtn.setText(new SimpleDateFormat("MM/dd/yyyy").format(fromTime.getTime()));
        }
    }
}
