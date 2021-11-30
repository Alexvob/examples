package com.astudio.progressmonitor.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PrimarySyncData {

    @SerializedName("tableRow")
    @Expose
    public int tableRow;


    @SerializedName("lastUpdated")
    @Expose
    public String lastUpdated;


    @Override
    public String toString() {
        return "PrimarySyncData{" +
                "tableRow=" + tableRow +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
