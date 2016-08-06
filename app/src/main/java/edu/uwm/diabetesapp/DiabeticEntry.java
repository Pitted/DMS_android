package edu.uwm.diabetesapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Lenovo on 7/14/2016.
 */
public class DiabeticEntry {

    //MEMBER ATTRIBUTES
    private long index;
    private String timestamp;
    private int code;
    private int BGL;
    private String diet;
    private String exercise;
    private String medication;

    public DiabeticEntry(){}

    public DiabeticEntry(long idx, String time, int id, int bgl, String nutrition, String fitness, String meds){
        index = idx;
        timestamp = time;
        code = id;
        BGL = bgl;
        diet = nutrition;
        exercise = fitness;
        medication = meds;
    }

    public void setIndex(long id){
        index = id;
    }
    public long getIndex(){
        return index;
    }

    public void setTime(String time){
        timestamp = time;
    }
    public String getTime(){
        return timestamp;
    }

    public void setCode(int id){
        code = id;
    }
    public int getCode(){
        return code;
    }

    public void setBGL(int bgl){
        BGL = bgl;
    }
    public int getBGL(){
        return BGL;
    }

    public void setDiet(String nutrition){
        diet = nutrition;
    }
    public String getDiet(){
        return diet;
    }

    public void setExercise(String fitness){
        exercise = fitness;
    }
    public String getExercise(){
        return exercise;
    }

    public void setMedication(String meds){
        medication = meds;
    }
    public String getMedication(){
        return medication;
    }

    public DataEvent createEvent() throws ParseException {
        /*
    Code: BGL-0
          Nutrition-1
          Fitness-2
          Medication-3
     */
        AppHelpers helper = new AppHelpers();
        DataEvent obj = null;
        SimpleDateFormat format = new SimpleDateFormat();
        Calendar time = Calendar.getInstance();
        try{
        time.setTime(new SimpleDateFormat("hh:mm aa EEE, MMM dd, yyyy").parse(timestamp));
        }catch (ParseException e) {
            throw new ParseException("Date from DB is not in AppHelperFormat", e.getErrorOffset());

        }
        switch (code){
            case 0: obj = new BGLLevel();
                ((BGLLevel)obj).setBGL(BGL); ((BGLLevel)obj).setEventDateTime(time);
                break;
            case 1: obj = new NutritionEvent(); ((NutritionEvent)obj).setEventDateTime(time);
                    ((NutritionEvent)obj).fromString(diet);
                break;
            case 2: obj = new FitnessEvent(); ((FitnessEvent)obj).setEventDateTime(time);
                ((FitnessEvent)obj).fromString(exercise);
                break;
            case 3: obj = new MedicationEvent(); ((MedicationEvent)obj).setEventDateTime(time);
                ((MedicationEvent)obj).fromString(medication);
                break;
        }
        return obj;
    }
}
