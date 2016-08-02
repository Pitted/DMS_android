package edu.uwm.diabetesapp;

/**
 * Created by Lenovo on 7/13/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_TABLE = "DiabeticTable";
    private static DatabaseHelper sInstance;


    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, "diabetes.db", null, 1);
    }


    /*
    Code: BGL-0
          Nutrition-1
          Fitness-2
          Medication-3
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE +
                "(DateTime TEXT PRIMARY KEY, " +
                "EventCode INTEGER, " +
                "BGL INTEGER, " +
                "Diet TEXT," +
                "Exercise TEXT," +
                "Medication TEXT);");
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }

    public boolean saveEvent(DataEvent event){
       String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
        ContentValues cv = new ContentValues();
        if(event instanceof BGLLevel){
            cv.put("DateTime", timestamp);
            cv.put("EventCode", 0);
            cv.put("BGL", (((BGLLevel) event).getBGL()));
            cv.put("Diet", "");
            cv.put("Exercise", "");
            cv.put("Medication", "");
        }else if(event instanceof MedicationEvent){
            cv.put("DateTime", timestamp);
            cv.put("EventCode", 3);
            cv.put("BGL","");
            cv.put("Diet", "");
            cv.put("Exercise", "");
            cv.put("Medication", (((MedicationEvent) event).getMedicationEvent()));
        }else if(event instanceof NutritionEvent){
            cv.put("DateTime", timestamp);
            cv.put("EventCode", 1);
            cv.put("BGL","");
            cv.put("Diet", ((NutritionEvent) event).getNutritionEvent());
            cv.put("Exercise", "");
            cv.put("Medication", "");
        }
        else{
            cv.put("DateTime", timestamp);
            cv.put("EventCode", 2);
            cv.put("BGL","");
            cv.put("Diet", "");
            cv.put("Exercise", ((FitnessEvent) event).getFitnessEvent());
            cv.put("Medication", "");
        }
        if(this.getWritableDatabase().insert(DATABASE_TABLE, null, cv) == -1)
            return false;
        else{
            Log.v("DB EVENT", cv.toString());
            return true;
        }

    }

//    public boolean saveEvent (int code, int bgl, String diet, String exercise, String medication) {
//        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
//
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put("DateTime", formattedDate);
//        cv.put("EventCode", code);
//        cv.put("BGL", bgl);
//        cv.put("Diet", diet);
//        cv.put("Exercise", exercise);
//        cv.put("Medication", medication);
//
//        long res = db.insert(DATABASE_TABLE, null, cv);
//
//        if( res == -1 )
//            return false;
//        else {
//            Log.v("DB EVENT", formattedDate + "\t" + code + "\t" +bgl + "\t" + diet + "\t" + exercise + "\t" + medication);
//            return true;
//        }
//
//    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+ DATABASE_TABLE, null);
        return result;
    }


    public long getEntryCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT COUNT(*) FROM " + DATABASE_TABLE;
        SQLiteStatement statement = db.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public ArrayList<DiabeticEntry> getAllItems() {
        ArrayList<DiabeticEntry> dbList = new ArrayList<DiabeticEntry>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DATABASE_TABLE, null);

        //COLLECT EACH ROW IN THE TABLE
        if (cursor.moveToFirst()){
            do {
                DiabeticEntry entry = new DiabeticEntry();
                entry.setTime(cursor.getString(0));
                entry.setCode(cursor.getInt(1));
                entry.setBGL(cursor.getInt(2));
                entry.setDiet(cursor.getString(3));
                entry.setExercise(cursor.getString(4));
                entry.setMedication(cursor.getString(5));

                //ADD TO THE QUERY LIST
                dbList.add(entry);
            } while (cursor.moveToNext());
        }
        return dbList;
    }


    public void clearDatabase() {
        String clearDBQuery = "DELETE FROM "+ DATABASE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(clearDBQuery);
    }
}


