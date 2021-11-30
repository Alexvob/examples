package com.astudio.progressmonitor.workers;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.task.SyncServiceTask;

public class SyncServiceTaskWorker extends Worker {

    private static final String TAG = "SyncServiceTaskWorker";


    public SyncServiceTaskWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        //Log.e(TAG, "Start SyncServiceTaskWorker");
//        String groupToken = getInputData().getString("groupToken");
//        if(groupToken == null) {
//            return Result.failure(); // обработка события в вызывающем коде
//        }

        SyncServiceTask syncService = App.getInstance().getSyncServiceTask();
        boolean resultSync = syncService.startSync();

        if(resultSync){
            return Result.success();
        } else {
            return Result.retry();
        }
    }


}