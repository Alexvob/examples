package com.astudio.progressmonitor.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.astudio.progressmonitor.group.SyncServiceGroup;
import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.plan.SyncServicePlan;

public class SyncServiceGroupWorker extends Worker {

    private static final String TAG = "SyncServiceGroupWorker";


    public SyncServiceGroupWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {

        SyncServiceGroup syncServiceGroup = App.getInstance().getSyncServiceGroup();
        boolean resultSync = syncServiceGroup.startSync();

        if(resultSync){
            return Result.success();
        } else {
            return Result.retry();
        }

    }


}