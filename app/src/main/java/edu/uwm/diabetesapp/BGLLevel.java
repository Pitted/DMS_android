package edu.uwm.diabetesapp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BGLLevel extends DataEvent {

    private int bglLevel;
    private String dateTime;
    private Calendar eventDateTime;

    public BGLLevel() {
        bglLevel = 0;
        eventDateTime = Calendar.getInstance();
    }

    public void setBGL(double level){
        bglLevel = (int)level;
    }

    public int getBGL() {
        return bglLevel;
    }

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

    public String toString(){
        String time = new SimpleDateFormat("hh:mm aa").format(eventDateTime.getTime());
        String date = new SimpleDateFormat("MM/dd/yyyy").format(eventDateTime.getTime());
        return "" + bglLevel + "mg/dl at " + time + " on " + date;
    }
}
