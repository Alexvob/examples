package com.astudio.progressmonitor.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.user.SyncServiceUser;

public class UpdateTokenDeviceWorker extends Worker {

    private static final String TAG = "UpdateTokenDeviceWorker";


    public UpdateTokenDeviceWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        //Log.e(TAG, "Start SyncServiceUserWorker");
        int userId = App.getInstance().getCurrentUserId();
        String tokenDevice = App.getInstance().getDeviceToken();

        boolean result = App.getInstance().getRepositoryGroup().sendServerUpdateTokenFcm(userId, tokenDevice);

        if(result){
            return Result.success();
        } else {
            return Result.retry();
        }

    }


}