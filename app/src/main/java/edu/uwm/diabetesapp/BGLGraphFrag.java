package edu.uwm.diabetesapp;


import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BGLGraphFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BGLGraphFrag extends DialogFragment {

    //param1 fromDate string
    //param2 toDate string
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LineChart chart;
    private View fragView;
    private Calendar fromDate = Calendar.getInstance();
    private Calendar toDate = Calendar.getInstance();
    private DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
    private Cursor cursor;
    private List<Entry> values = new ArrayList<>();
    private LineDataSet dataSet;
    ArrayList<String> label;

    public BGLGraphFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BGLGraphFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static BGLGraphFrag newInstance(String param1, String param2) {
        BGLGraphFrag fragment = new BGLGraphFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragView = inflater.inflate(R.layout.fragment_bgl_graph, container, false);
        try {
            fromDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mParam1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            toDate.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mParam2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        chart = (LineChart) fragView.findViewById(R.id.chart);
        cursor = db.getBGLEventBetween(fromDate.getTime(), toDate.getTime());
        for(int i = 0; i<cursor.getCount(); i++){
            cursor.moveToNext();
            try {
                values.add(new Entry(xValueFactory(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(1)), fromDate.getTime(), toDate.getTime()), (float) cursor.getInt(2)));
                //values.add(new Entry((float)(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(1))).getTime(), (float)cursor.getInt(2)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        sort(values);
        LineDataSet data = new LineDataSet(values, "BGLGraph");
        LineData lineData = new LineData(data);
        data.setDrawFilled(true);
        data.setColor(getResources().getColor(R.color.graphline));
        data.setDrawCircles(false);
        data.setDrawValues(false);
        chart.setData(new LineData(data));
        chart.setVisibleYRange(300f, 50f, null);
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setGranularity(1f);
//        label = labelFactory(fromDate.getTime(), toDate.getTime());
//        xAxis.setValueFormatter(new AxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return label.get((int) value);
//            }
//
//            @Override
//            public int getDecimalDigits() {
//                return 0;
//            }
//        });
        chart.invalidate();
        return fragView;
    }

    private float xValueFactory(Date valueDate, Date fromDate, Date toDate){
        if(valueDate.compareTo(fromDate)>=0 && valueDate.compareTo(toDate)<=0) {
            long difference = toDate.getTime() - fromDate.getTime();
            long valueDifference = valueDate.getTime() - fromDate.getTime();
            return (float) (valueDifference/difference) * chart.getXChartMax();
            //ratio of where value belongs between from and to
        }
        return -1;
    }
    private ArrayList<String> labelFactory(Date fromDate, Date toDate){
        SimpleDateFormat hoursFormat = new SimpleDateFormat("HH");
        SimpleDateFormat daysFormat = new SimpleDateFormat("mm/DD/yy");
        long day = 86000;
        long difference = toDate.getTime() - fromDate.getTime();
        ArrayList<String> lables = new ArrayList<>();
        if(difference <= day){
            lables.add(hoursFormat.format(new Date(fromDate.getTime()+difference/4)));
            lables.add(hoursFormat.format(new Date(fromDate.getTime()+difference/4*2)));
            lables.add(hoursFormat.format(new Date(fromDate.getTime()+difference/4*3)));
            lables.add(hoursFormat.format(toDate));
        }else{
            lables.add(daysFormat.format(new Date(fromDate.getTime()+difference/4)));
            lables.add(daysFormat.format(new Date(fromDate.getTime()+difference/4*2)));
            lables.add(daysFormat.format(new Date(fromDate.getTime()+difference/4*3)));
            lables.add(daysFormat.format(toDate));
        }
        return lables;
    }
    private void sort(List<Entry> v){
        Collections.sort(v, new Comparator<Entry>() {
            @Override
            public int compare(Entry lhs, Entry rhs) {
                if(lhs.getX()==rhs.getX()) return 0;
                else if(lhs.getX()<rhs.getX()) return -1;
                else return 1;
            }
        });
    }

}
