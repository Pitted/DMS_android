package edu.uwm.dms_android;

/**
 * Created by jrhay on 7/17/2016.
 */
public class Medication {
    private String type;
    private int quantity;
    private String desc;
    private final int id;
    public Medication(int id, String type, String desc, int quantity){
        this.id = id;
        this.type = type;
        this.desc = desc;
        this.quantity = quantity;
    }
    public void setType(String t){
        this.type =t;
    }
    public void setDesc(String d){
        this.desc = d;
    }
    public void setQuantity(int q){
        this.quantity = q;
    }
    public String getDesc(){
        return desc;
    }
    public String getType(){
        return type;
    }
    public int getQuantity(){
        return quantity;
    }
}
