package edu.uwm.dms_android;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;
/**
 * Created by jrhay on 7/16/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context){
        super(context, "diabetes.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean editBGL(int bgl){
        //TODO
        return true;
    }
    public boolean saveBGL(int bgl){
        //TODO
        return true;
    }
    public boolean editMedication(Medication medication){
        //TODO
        return true;
    }
    public boolean addMedication(Medication medication){
        //TODO
        return true;
    }
    public boolean editActivity(Activity activity){
        //TODO
        return true;
    }
    public boolean addActivity(Activity activity){
        //TODO
        return true;
    }
    public boolean editMeal(Meal meal){
        //TODO
        return true;
    }
    public boolean addMeal(Meal meal){
        //TODO
        return true;
    }

}
