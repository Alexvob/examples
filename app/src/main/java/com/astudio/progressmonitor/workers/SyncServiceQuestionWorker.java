package com.astudio.progressmonitor.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.astudio.progressmonitor.modules.App;
import com.astudio.progressmonitor.statistic.SyncServiceStatistic;
import com.astudio.progressmonitor.voting.SyncServiceQuestion;

public class SyncServiceQuestionWorker extends Worker {

    private static final String TAG = "SyncServiceQuestWorker";


    public SyncServiceQuestionWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        //Log.e(TAG, "Start SyncServiceQuestionWorker");

        SyncServiceQuestion syncServiceQuestion = App.getInstance().getSyncServiceQuestion();
        boolean resultSync = syncServiceQuestion.startSync();

        if(resultSync){
            return Result.success();
        } else {
            return Result.retry();
        }

    }


}