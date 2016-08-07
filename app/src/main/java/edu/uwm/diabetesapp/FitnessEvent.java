package edu.uwm.diabetesapp;
import java.util.Calendar;

public class FitnessEvent extends DataEvent {




    private int qty;
    private String exercise;
    private String fitnessEvent;
    //private String dateTime;
    private Calendar eventDateTime;

    public FitnessEvent() {
        fitnessEvent = null;
    }

    public void setExercise(String exercises){
        exercise = exercises;
    }

    public String getExercise(){return exercise;}

    public void setEventDateTime(Calendar eventdatetime){eventDateTime = eventdatetime;}
    public void setEventDateTime(int hour,int minute){
        eventDateTime.set(Calendar.HOUR_OF_DAY,hour);
        eventDateTime.set(Calendar.MINUTE,minute);
    }
    public void setEventDateTime(int year,int month, int day){
        eventDateTime.set(Calendar.YEAR,year);
        eventDateTime.set(Calendar.MONTH,month);
        eventDateTime.set(Calendar.DAY_OF_MONTH,day);
    }
    public Calendar getEventDateTime(){
        return eventDateTime;
    }

    public void setFitnessEvent(int i, String s) {
        qty = i;
        exercise = s;
        fitnessEvent = ("" + qty + " calories burned doing " + exercise);
    }
    public String getFitnessEvent(){
        return fitnessEvent;
    }
    public String toString(){
        String time = eventDateTime.HOUR_OF_DAY + ":" + eventDateTime.MINUTE ;
        String date = (eventDateTime.MONTH + 1) + "/" + eventDateTime.DAY_OF_MONTH + "/" + eventDateTime.YEAR;

        return  qty + " calories burned at " + time + " on " + date + "\nDescription: " + exercise;
    }
    public void fromString(String s){
        //format of fitness Event
        String[] values = s.split(" calories burned doing ");
        qty = Integer.parseInt(values[0]);
        exercise = values[1];
    }

    public int getQTY() {
        return qty;
    }
}
