package com.example.lenovo.puzzlegame.Beans;

public class StepRecord {
    public int getBlankfrom() {
        return blankfrom;
    }

    public void setBlankfrom(int blankfrom) {
        this.blankfrom = blankfrom;
    }

    public int blankfrom ;

    public int getBlankgo() {
        return blankgo;
    }

    public void setBlankgo(int blankgo) {
        this.blankgo = blankgo;
    }

    @Override
    public String toString() {
        return "StepRecord{" +
                "blankfrom=" + blankfrom +
                ", blankgo=" + blankgo +
                '}';
    }

    public int blankgo;
    public StepRecord ( ){
    }
    public StepRecord (int blankfrom,int blankgo){
        this.blankfrom=blankfrom;
        this.blankgo = blankgo;
    }
}
