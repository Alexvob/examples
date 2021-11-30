package com.astudio.progressmonitor.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.plan.SyncServicePlan;
import com.astudio.progressmonitor.promo.SyncServicePromo;

public class SyncServicePromoWorker extends Worker {

    private static final String TAG = "SyncServicePromoWorker";


    public SyncServicePromoWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        //Log.e(TAG, "Start SyncServiceUserWorker");

        SyncServicePromo syncServicePromo = App.getInstance().getSyncServicePromo();
        boolean resultSync = syncServicePromo.startSync();

        if(resultSync){
            return Result.success();
        } else {
            return Result.retry();
        }

    }


}