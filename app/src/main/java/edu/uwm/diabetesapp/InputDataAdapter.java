package edu.uwm.diabetesapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by jrhay on 7/26/2016.
 */
public class InputDataAdapter extends ArrayAdapter<AddDataElement>{

    private ArrayList<AddDataElement> dataElements;


    public InputDataAdapter(Context context, List<AddDataElement> objects) {
        super(context,R.layout.blank_element, objects);
        dataElements = (ArrayList<AddDataElement>) objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            return ((View) dataElements.get(position));
        }
        return convertView;
    }
    public void removeAll(List<AddDataElement> list){
        for(AddDataElement ele: list){
            this.remove(ele);
        }
    }


    public ArrayList<AddDataElement> getList(){
        return dataElements;
    }
    public void addElement(AddDataElement e){
        this.add(e);
    }

}
