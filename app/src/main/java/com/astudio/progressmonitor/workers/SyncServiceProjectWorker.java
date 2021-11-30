package com.astudio.progressmonitor.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.plan.SyncServicePlan;
import com.astudio.progressmonitor.project.SyncServiceProject;

public class SyncServiceProjectWorker extends Worker {

    private static final String TAG = "SyncServiceProjectWorker";


    public SyncServiceProjectWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        //Log.e(TAG, "Start SyncServiceUserWorker");

        SyncServiceProject syncServiceProject = App.getInstance().getSyncServiceProject();
        boolean resultSync = syncServiceProject.startSync();

        if(resultSync){
            return Result.success();
        } else {
            return Result.retry();
        }

    }


}