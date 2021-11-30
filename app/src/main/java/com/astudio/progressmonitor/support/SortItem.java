package com.astudio.progressmonitor.support;

import androidx.annotation.NonNull;

public class SortItem {

    private String itemForBD;
    private String itemForLayout;
    private int period;


    public SortItem(String iBD, String iLt){
        this.itemForBD = iBD;
        this.itemForLayout = iLt;
    }


    public SortItem(int period, String iLt){
        this.period = period;
        this.itemForLayout = iLt;
    }


    public String getItemForBD(){
        return itemForBD;
    }


    public int getPeriod(){
        return period;
    }

//    public String getItemForLayout(){
//        return itemForLayout;
//    }

    @NonNull
    @Override
    public String toString() {
        return itemForLayout;
    }

}


