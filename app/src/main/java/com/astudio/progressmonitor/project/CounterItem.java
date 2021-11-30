package com.astudio.progressmonitor.project;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class CounterItem {


    @ColumnInfo(name = "count_all_item")
    public int countAllItem;

    @ColumnInfo(name = "count_exec_item")
    public int countExecItem;


    public int countProgressProject(){
        int result = 0;
        if (countAllItem != 0){
            float tempResult = ( (float) countExecItem / (float) countAllItem ) * 100;
            result = Math.round(tempResult);
        }
        return result;
    }


    @NonNull
    @Override
    public String toString() {
        return "CounterItem{" +
                "countAllItem=" + countAllItem +
                ", countExecItem=" + countExecItem +
                '}';
    }
}
