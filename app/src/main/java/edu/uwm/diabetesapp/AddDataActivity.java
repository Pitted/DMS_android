package edu.uwm.diabetesapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AddDataActivity extends Activity {



    private LinearLayout lvHeader;
    private ListView data_list;
    private ImageButton save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        data_list = new InputDataList(this);
        this.addContentView(data_list, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (400*getResources().getDisplayMetrics().density)));
        save = (ImageButton) findViewById(R.id.save_inputs);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){;
                ((InputDataList)data_list).save();
            }
        });

    }





}
