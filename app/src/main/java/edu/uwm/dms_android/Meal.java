package edu.uwm.dms_android;

/**
 * Created by jrhay on 7/17/2016.
 */
public class Meal {
    private String type;
    private String desc;
    private int calories;
    private final int id;
    public Meal(int id, String type, String desc, int calories){
        this.id = id;
        this.type = type;
        this.desc = desc;
        this.calories = calories;
    }
    public void setTypeType(String type){
        this.type = type;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public void setCalories(int calories){
        this.calories = calories;
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
