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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FitnessEventFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FitnessEventFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitnessEventFrag extends DialogFragment
        implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener, TimePickerFragment.OnTimePickedListener, DatePickerFragment.OnDatePickedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    public interface OnSaveListener{
        public void onSave(FitnessEvent obj, long _id);
    }

    private OnSaveListener mCallBack;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private int mParam1;
    private FitnessEvent fit;
    private Button dateBtn;
    private Button timeBtn;
    private ImageButton save;
    private TextView qty;
    private TextView desc;

    private OnFragmentInteractionListener mListener;
    private AppHelpers helper;

    public FitnessEventFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @return A new instance of fragment FitnessEventFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static FitnessEventFrag newInstance(long param1) {
        FitnessEventFrag fragment = new FitnessEventFrag();
        Bundle args = new Bundle();
        args.putLong("_id", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt("_id", 0);
        }
        dateBtn = (Button) getActivity().findViewById(R.id.bgl_date_button);
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        timeBtn = (Button) getActivity().findViewById(R.id.bgl_time_button);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        qty = (EditText) getActivity().findViewById(R.id.fitness_qty);
        desc = (EditText) getActivity().findViewById(R.id.fitness_desc);
        save = (ImageButton) getActivity().findViewById(R.id.fit_frag_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fit.setFitnessEvent(Integer.parseInt(qty.getText().toString()), desc.getText().toString());
            }
        });
    }

    public void setFitness(FitnessEvent f){
        fit = f;
        setFields();
    }
    private void setFields(){
        qty.setText(fit.getQTY());
        desc.setText(fit.getExercise());
        dateBtn.setText(new SimpleDateFormat("MM/dd/YY").format(fit.getEventDateTime()));
        timeBtn.setText(new SimpleDateFormat("hh:mm aa").format(fit.getEventDateTime()));
    }

    private void showTimePickerDialog(View v) {
        if (fit.getEventDateTime() != null){
            Calendar c = fit.getEventDateTime();
            TimePickerDialog timePicker = new TimePickerDialog(getActivity(), this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);
            timePicker.show();
        }else{
            DialogFragment newTimeFragment = new TimePickerFragment();
            newTimeFragment.show(this.getFragmentManager(), "timePicker");
        }
    }

    private void showDatePickerDialog(View v) {
        if(fit.getEventDateTime() != null){
            Calendar c = fit.getEventDateTime();
            DatePickerDialog datePicker = new DatePickerDialog(getActivity(), this,c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }else{
            DialogFragment newTimeFragment = new TimePickerFragment();
            newTimeFragment.show(this.getFragmentManager(), "timePicker");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fitness_event, container, false);
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
        }
        else{
            throw new RuntimeException(context.toString()
                    + " must implement OnSaveListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDatePicked(int picker, int year, int month, int dayOfMonth) {
        dateBtn.setText(helper.formatDate(year, month + 1, dayOfMonth));
        fit.setEventDateTime(year, month, dayOfMonth);
    }
    @Override
    public void onTimePicked(int picker, int hour, int minute) {
        timeBtn.setText(helper.formatTime(hour,minute));
        fit.setEventDateTime(hour,minute);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dateBtn.setText(helper.formatDate(year, monthOfYear+1, dayOfMonth));
        fit.setEventDateTime(year, monthOfYear+1, dayOfMonth);
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeBtn.setText(helper.formatTime(hourOfDay, minute));
        fit.setEventDateTime(hourOfDay, minute);
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
    public void onSave(FitnessEvent o, long _id){
        this.mCallBack.onSave(o, getArguments().getLong("_id"));
    }
}
