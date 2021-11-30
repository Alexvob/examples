package com.astudio.progressmonitor.support;

import androidx.room.TypeConverter;

import java.util.HashMap;
import java.util.Map;

public class SqlConverter {



    @TypeConverter
    public Map<Integer, String> convertToMap(int id, String status){
        Map<Integer, String> myMap = new HashMap<Integer, String>();
        return  myMap;

    }

}
