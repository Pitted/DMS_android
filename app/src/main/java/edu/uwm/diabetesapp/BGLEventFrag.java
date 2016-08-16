package edu.uwm.diabetesapp;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.view.ViewGroup.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BGLEventFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BGLEventFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BGLEventFrag extends DialogFragment
        implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener, TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    
    public interface OnSaveListener{
        public void onSave(BGLLevel obj, long _id);
    }
    private OnSaveListener mCallBack;
    
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BGLLevel bgl;
    private Button dateBtn;
    private Button timeBtn;
    private ImageButton save;
    private EditText bgl_lvl;
    private AppHelpers helper;
    private View fragView;

    // TODO: Rename and change types of parameters
    private long mParam1;

    private OnFragmentInteractionListener mListener;

    public BGLEventFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BGLEventFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static BGLEventFrag newInstance(long param1) {
        BGLEventFrag fragment = new BGLEventFrag();
        Bundle args = new Bundle();
        args.putLong("_id", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             mParam1 = getArguments().getLong("_id", 0);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        helper = new AppHelpers();
        fragView = inflater.inflate(R.layout.fragment_bglevent, container, false);
        dateBtn = (Button) fragView.findViewById(R.id.bgl_date_button);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        timeBtn = (Button) fragView.findViewById(R.id.bgl_time_button);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        bgl_lvl = (EditText) fragView.findViewById(R.id.bgl_field);
        save = (ImageButton) fragView.findViewById(R.id.bgl_frag_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgl.setBGL(Double.parseDouble(bgl_lvl.getText().toString()));
                onSave(bgl, getArguments().getLong("_id"));
            }
        });
        if(bgl != null){
            setFields();
        }
        return  fragView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if(context instanceof OnSaveListener){
            mCallBack = (OnSaveListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentSaveListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //do nothing
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //do nothing
    }
    public void setBGL(BGLLevel obj){
        bgl = obj;
        //setFields();
    }
    private void setFields(){
        bgl_lvl.setText(String.valueOf(bgl.getBGL()));
        dateBtn.setText(new SimpleDateFormat("MM/dd/yyyy").format(bgl.getEventDateTime().getTime()));
        timeBtn.setText(new SimpleDateFormat("hh:mm aa").format(bgl.getEventDateTime().getTime()));
    }

    //~~method when the time is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        timeBtn.setText(helper.formatTime(hour,minute));
        bgl.setEventDateTime(hour, minute);
    }

    //~~method when a date is picked~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public void onDatePicked(int picker, int year, int month, int day) {
        dateBtn.setText(helper.formatDate(year, month + 1, day));
        bgl.setEventDateTime(year, month, day);

    }
    public void showDatePickerDialog(View v) {
        if(bgl.getEventDateTime() != null){
            Calendar c =  bgl.getEventDateTime();
            DatePickerDialog datePicker = new DatePickerDialog(getActivity(),this,c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        }else{
            DialogFragment newDateFragment = new DatePickerFragment();
            newDateFragment.show(this.getFragmentManager(), "datePicker");
        }


    }

    //~~method to launch the time picker~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void showTimePickerDialog(View v) {
        if(bgl.getEventDateTime() != null){
            Calendar c = bgl.getEventDateTime();
            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
            timePicker.show();
        }else{
            DialogFragment newTimeFragment = new TimePickerFragment();
            newTimeFragment.show(this.getFragmentManager(), "timePicker");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dateBtn.setText(helper.formatDate(year, monthOfYear + 1, dayOfMonth));
        bgl.setEventDateTime(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeBtn.setText(helper.formatTime(hourOfDay,minute));
        bgl.setEventDateTime(hourOfDay, minute);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void onSave(BGLLevel o, long _id){
        this.mCallBack.onSave(o, _id);
    }
}
