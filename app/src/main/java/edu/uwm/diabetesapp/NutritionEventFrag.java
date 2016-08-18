package edu.uwm.diabetesapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NutritionEventFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NutritionEventFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NutritionEventFrag extends DialogFragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener, TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public interface OnSaveListener{
        public void onSave(NutritionEvent obj, long _id);
    }
    private OnSaveListener mCallback;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private NutritionEvent nut = new NutritionEvent();


    // TODO: Rename and change types of parameters
    private int mParam1;

    private Button dateBtn;
    private Button timeBtn;
    private ImageButton save;
    private EditText qty;
    private EditText desc;
    private AppHelpers helper;
    private View fragView;
    private OnFragmentInteractionListener mListener;

    public NutritionEventFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @return A new instance of fragment NutritionEventFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static NutritionEventFrag newInstance(long param1) {
        NutritionEventFrag fragment = new NutritionEventFrag();
        Bundle args = new Bundle();
        args.putLong("_id", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt("_id");
        }

    }

    private void showTimePickerDialog(View v) {
        if(nut.getEventDateTime() != null){
            Calendar c = nut.getEventDateTime();
            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
            timePicker.show();
        }else{
            DialogFragment newTimeFragment = new TimePickerFragment();
            newTimeFragment.show(this.getFragmentManager(), "timePicker");
        }
    }

    private void showDatePickerDialog(View v) {
        if(nut.getEventDateTime() != null){
            Calendar c = nut.getEventDateTime();
            DatePickerDialog datePicker = new DatePickerDialog(getActivity(),this,c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        }else{
            DialogFragment newDateFragment = new DatePickerFragment();
            newDateFragment.show(this.getFragmentManager(), "datePicker");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_nutrition_event, container, false);
        dateBtn = (Button) fragView.findViewById(R.id.nutrition_date_btn);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        timeBtn = (Button) fragView.findViewById(R.id.nutrition_time_btn);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        qty = (EditText) fragView.findViewById(R.id.nutrition_qty);
        desc = (EditText) fragView.findViewById(R.id.nutrition_desc);
        save = (ImageButton) fragView.findViewById(R.id.nutr_frag_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nut.setNutritionEvent(Integer.parseInt(qty.getText().toString()), desc.getText().toString());
                onSave(nut, getArguments().getLong("_id"));
            }
        });
        if(nut != null){
            setFields();
        }
        return fragView;
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
        if(context instanceof  OnSaveListener){
            mCallback = (OnSaveListener) context;
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

    }

    @Override
    public void onDatePicked(int picker, int year, int month, int dayOfMonth) {
        nut.setEventDateTime(year, month + 1, dayOfMonth);
        dateBtn.setText(new SimpleDateFormat("MM/dd/yy").format(nut.getEventDateTime().getTime()));
    }

    public void setNutrition(NutritionEvent o){
        nut = o;
        setFields();
    }
    private void setFields() {
        qty.setText(String.valueOf(nut.getQty()));
        desc.setText(nut.getNutrition());
        if(nut.getEventDateTime() != null){
            timeBtn.setText(new SimpleDateFormat("hh:mm aa").format(nut.getEventDateTime().getTime()));
            dateBtn.setText(new SimpleDateFormat("MM/dd/yy").format(nut.getEventDateTime().getTime()));
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        nut.setEventDateTime(year, monthOfYear, dayOfMonth);
        dateBtn.setText(new SimpleDateFormat("MM/dd/yy").format(nut.getEventDateTime().getTime()));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        nut.setEventDateTime(hour, minute);
        timeBtn.setText(new SimpleDateFormat("hh:mm aa").format(nut.getEventDateTime().getTime()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        nut.setEventDateTime(hourOfDay, minute);
        timeBtn.setText(new SimpleDateFormat("hh:mm aa").format(nut.getEventDateTime().getTime()));
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

    public void onSave(NutritionEvent o, long _id){
        this.mCallback.onSave(nut,_id);
    }
}
