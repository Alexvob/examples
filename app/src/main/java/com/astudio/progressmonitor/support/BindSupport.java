package com.astudio.progressmonitor.support;

import android.content.res.Resources;

import com.astudio.progressmonitor.R;

public class BindSupport {

    private String status;
    private String curStatus;
    private Resources mResources;

    public BindSupport(Resources resources) {
        mResources = resources;
        curStatus = mResources.getString(R.string.status_task_active);
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getStatus(){
        switch (status){
            //case "a": curStatus = "Активная";
            case "a": curStatus = mResources.getString(R.string.status_task_active);
                break;
            case "s": curStatus = mResources.getString(R.string.status_task_inactive);
                break;
            case "c": curStatus = mResources.getString(R.string.status_task_canceled);
                break;
            case "o": curStatus = "Open";
                break;
        }
        return curStatus;
    }



}
