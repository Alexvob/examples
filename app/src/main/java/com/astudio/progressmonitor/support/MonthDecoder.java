package com.astudio.progressmonitor.support;

import android.content.Context;

import com.astudio.progressmonitor.R;

public class MonthDecoder {

    private Context context;

    public MonthDecoder(Context context) {
        this.context = context;
    }

    public String decodeMonth(int month){

        switch (month){
            case 1: return context.getResources().getString(R.string.january);
            case 2: return context.getResources().getString(R.string.february);
            case 3: return context.getResources().getString(R.string.march);
            case 4: return context.getResources().getString(R.string.april);
            case 5: return context.getResources().getString(R.string.may);
            case 6: return context.getResources().getString(R.string.june);
            case 7: return context.getResources().getString(R.string.july);
            case 8: return context.getResources().getString(R.string.august);
            case 9: return context.getResources().getString(R.string.september);
            case 10: return context.getResources().getString(R.string.october);
            case 11: return context.getResources().getString(R.string.november);
            case 12: return context.getResources().getString(R.string.december);
            default: return " ";
        }
    }



}
