package edu.uwm.diabetesapp;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class EditDataActivity extends Activity {

    private DatabaseHelper db;
    private Cursor cursor;
    private Button viewBglBtn;
    private Button viewMedBtn;
    private Button viewMealBtn;
    private Button viewFitBtn;
    private Button fromDate;
    private Button toDate;
    private Button Refresh;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
    }

}
