package edu.uwm.dms_android;

/**
 * Created by jrhay on 7/17/2016.
 */
public class Activity {
    private String type;
    private int calories;
    private String desc;
    private final int id;
    public Activity(int id, String type, String desc, int calories){
        this.id = id;
        this.type = type;
        this.desc = desc;
        this.calories = calories;
    }
    public void setType(String type){
        this.type = type;
    }
    public void setCalories(int calories){
        this.calories = calories;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getType(){
        return type;
    }
    public String getDesc(){
        return desc;
    }
    public int getCalories(){
        return calories;
    }
}
