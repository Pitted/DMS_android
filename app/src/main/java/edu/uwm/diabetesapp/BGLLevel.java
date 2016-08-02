package edu.uwm.diabetesapp;

/**
 * Created by Lenovo on 7/9/2016.
 */
public class BGLLevel extends DataEvent {

    private int bglLevel;

    public BGLLevel() {
        bglLevel = 0;
    }

    public void setBGL(double level){
        bglLevel = (int)level;
    }

    public int getBGL() {
        return bglLevel;
    }

}
