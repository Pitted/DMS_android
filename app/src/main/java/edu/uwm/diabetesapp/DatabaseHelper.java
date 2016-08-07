package edu.uwm.diabetesapp;

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
import java.util.List;

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


/*
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE +
                "(DateTime TEXT PRIMARY KEY, " +
                "EventCode INTEGER, " +
                "BGL INTEGER, " +
                "Diet TEXT," +
                "Exercise TEXT," +
                "Medication TEXT);");
     }*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DATABASE_TABLE +
                "(_id INTEGER PRIMARY KEY, " +
                "DateTime DATETIME, " +
                "EventCode INTEGER, " +
                "BGL INTEGER, " +
                "Diet TEXT, " +
                "Exercise TEXT, " +
                "Medication TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_TABLE);
        onCreate(db);
    }


    public boolean saveEvent(DataEvent event) {

        ContentValues cv = new ContentValues();
        if (event instanceof BGLLevel) {
            cv.put("DateTime", (new AppHelpers()).formatDateTime(((BGLLevel) event).getEventDateTime()));
            cv.put("_id", getEntryCount());
            cv.put("EventCode", 0);
            cv.put("BGL", (((BGLLevel) event).getBGL()));
            cv.put("Diet", "");
            cv.put("Exercise", "");
            cv.put("Medication", "");
        } else if (event instanceof MedicationEvent) {
            cv.put("DateTime", (new AppHelpers()).formatDateTime(((MedicationEvent) event).getEventDateTime()));
            cv.put("_id", getEntryCount());
            cv.put("EventCode", 3);
            cv.put("BGL", "");
            cv.put("Diet", "");
            cv.put("Exercise", "");
            cv.put("Medication", (((MedicationEvent) event).getMedicationEvent()));
        } else if (event instanceof NutritionEvent) {
            cv.put("DateTime", (new AppHelpers()).formatDateTime(((NutritionEvent) event).getEventDateTime()));
            cv.put("_id", getEntryCount());
            cv.put("EventCode", 1);
            cv.put("BGL", "");
            cv.put("Diet", ((NutritionEvent) event).getNutritionEvent());
            cv.put("Exercise", "");
            cv.put("Medication", "");
        } else {
            cv.put("DateTime", (new AppHelpers()).formatDateTime(((FitnessEvent) event).getEventDateTime()));
            cv.put("_id", getEntryCount());
            cv.put("EventCode", 2);
            cv.put("BGL", "");
            cv.put("Diet", "");
            cv.put("Exercise", ((FitnessEvent) event).getFitnessEvent());
            cv.put("Medication", "");
        }
        if (this.getWritableDatabase().insert(DATABASE_TABLE, null, cv) == -1)
            return false;
        else {
            Log.v("DB EVENT", cv.toString());
            return true;
        }
    }
    public Cursor getBGLEvents(){
        return this.getReadableDatabase().rawQuery("select * from " + DATABASE_TABLE + " where EventCode == 0", null);
    }
    public Cursor getMedicationEvents(){
        return this.getReadableDatabase().rawQuery("select * from " + DATABASE_TABLE + " where EventCode == 3", null);
    }
    public Cursor getFitnessEvents(){
        return this.getReadableDatabase().rawQuery("select * from " + DATABASE_TABLE + " where EventCode == 2", null);
    }
    public Cursor getNutritionEvents(){
        return this.getReadableDatabase().rawQuery("select * from " + DATABASE_TABLE + " where EventCode == 1", null);
    }
    public boolean update(DiabeticEntry de){
        //TODO
        return false;
    }

    public boolean saveEvent (String formattedDate, int code, int bgl, String diet, String exercise, String medication) {
        //String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        SQLiteDatabase db = this.getWritableDatabase();
        long index = getEntryCount();

        ContentValues cv = new ContentValues();

        cv.put("Event", index);
        cv.put("DateTime", formattedDate);
        cv.put("EventCode", code);
        cv.put("BGL", bgl);
        cv.put("Diet", diet);
        cv.put("Exercise", exercise);
        cv.put("Medication", medication);

        //Log.v("DB EVENT", index + "\t" + formattedDate + "\t" + code + "\t" +bgl + "\t" + diet + "\t" + exercise + "\t" + medication);

        long res = db.insert(DATABASE_TABLE, null, cv);

        if( res == -1 )
            return false;
        else {
            Log.v("DB EVENT", index + "\t" + formattedDate + "\t" + code + "\t" +bgl + "\t" + diet + "\t" + exercise + "\t" + medication);
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
                entry.setIndex(cursor.getLong(0));
                entry.setTime(cursor.getString(1));
                entry.setCode(cursor.getInt(2));
                entry.setBGL(cursor.getInt(3));
                entry.setDiet(cursor.getString(4));
                entry.setExercise(cursor.getString(5));
                entry.setMedication(cursor.getString(6));

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


