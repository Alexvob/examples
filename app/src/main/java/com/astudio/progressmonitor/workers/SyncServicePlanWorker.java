package com.astudio.progressmonitor.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.plan.SyncServicePlan;
import com.astudio.progressmonitor.user.SyncServiceUser;

public class SyncServicePlanWorker extends Worker {

    private static final String TAG = "SyncServicePlanWorker";


    public SyncServicePlanWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        //Log.e(TAG, "Start SyncServiceUserWorker");

        SyncServicePlan syncServicePlan = App.getInstance().getSyncServicePlan();
        boolean resultSync = syncServicePlan.startSync();

        if(resultSync){
            return Result.success();
        } else {
            return Result.retry();
        }

    }


}