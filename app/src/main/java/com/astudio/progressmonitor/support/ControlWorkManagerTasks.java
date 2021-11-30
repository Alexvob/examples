package com.astudio.progressmonitor.support;

import android.content.Context;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.astudio.progressmonitor.workers.SyncServiceGroupWorker;
import com.astudio.progressmonitor.workers.SyncServicePlanWorker;
import com.astudio.progressmonitor.workers.SyncServiceProjectWorker;
import com.astudio.progressmonitor.workers.SyncServicePromoWorker;
import com.astudio.progressmonitor.workers.SyncServiceStatisticWorker;
import com.astudio.progressmonitor.workers.SyncServiceTaskWorker;
import com.astudio.progressmonitor.workers.SyncServiceUserWorker;
import com.astudio.progressmonitor.workers.SyncServiceQuestionWorker;
import com.astudio.progressmonitor.workers.UpdateServerTokenWorker;
import com.astudio.progressmonitor.workers.UpdateTokenDeviceWorker;

import java.util.concurrent.TimeUnit;

public class ControlWorkManagerTasks {

    private static final String TAG = "ControlWorkManagerTasks";
    private Context context;


    public ControlWorkManagerTasks(Context context) {
        this.context = context;
    }


    public void setPeriodicSyncTasks(int initialDelay){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // наличие интернета Wi-fi или Mobile data
                .build();
        PeriodicWorkRequest periodicSync = new PeriodicWorkRequest.Builder(SyncServiceTaskWorker.class, 15, TimeUnit.MINUTES)
                .addTag("periodic-sync-tasks")
                //.setConstraints(constraints) // xiaomi в режиме сна блокирует доступ к сети
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        //Log.e(TAG, "setPeriodicSyncTask");
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("SyncServiceTask", ExistingPeriodicWorkPolicy.REPLACE, periodicSync);
    }


    public void setOneTimeSyncTasks(int initialDelay){
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceTaskWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag("onetime-sync-tasks")
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).beginUniqueWork("SyncServiceTaskOneTime", ExistingWorkPolicy.REPLACE, work).enqueue();
    }


    public void setOneTimeSyncUsers(int initialDelay){
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceUserWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag("onetime-sync-users")
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).beginUniqueWork("SyncServiceUser", ExistingWorkPolicy.REPLACE, work).enqueue();
    }


    public void setOneTimeSyncPlans(int initialDelay){
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServicePlanWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag("onetime-sync-plans")
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).beginUniqueWork("SyncServicePlan", ExistingWorkPolicy.REPLACE, work).enqueue();
    }


    public void setOneTimeSyncProjects(int initialDelay){
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceProjectWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag("onetime-sync-projects")
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).beginUniqueWork("SyncServiceProject", ExistingWorkPolicy.REPLACE, work).enqueue();
    }


    public void setOneTimeSyncGroup(int initialDelay){
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceGroupWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag("onetime-sync-groups")
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).beginUniqueWork("SyncServiceGroup", ExistingWorkPolicy.REPLACE, work).enqueue();
    }


    public void setOneTimeSyncPromo(int initialDelay){
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServicePromoWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag("onetime-sync-promos")
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).beginUniqueWork("SyncServicePromo", ExistingWorkPolicy.REPLACE, work).enqueue();
    }


    public void setOneTimeSyncStatistics(int initialDelay){
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceStatisticWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag("onetime-sync-statistics")
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).beginUniqueWork("SyncServiceStatistic", ExistingWorkPolicy.REPLACE, work).enqueue();
    }


    public void setOneTimeSyncQuestions(int initialDelay){
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceQuestionWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag("onetime-sync-questions")
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).beginUniqueWork("SyncServiceQuestion", ExistingWorkPolicy.REPLACE, work).enqueue();
    }


    public void setOneTimeUpdateTokenDevice(int initialDelay){
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(UpdateTokenDeviceWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag("onetime-update-token-device")
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, // if Result.retry
                        10, // retry after 10 seconds
                        TimeUnit.SECONDS)
                .build();
        WorkManager.getInstance(context).beginUniqueWork("UpdateTokenDeviceOneTime", ExistingWorkPolicy.REPLACE, work).enqueue();
    }



    //NOT USED 18-05-2021
    public void setPeriodicSyncUsers(int initialDelay){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest periodicSync = new PeriodicWorkRequest.Builder(SyncServiceUserWorker.class, 16, TimeUnit.MINUTES)
                .addTag("periodic-sync-users")
                //.setConstraints(constraints)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL,
                        10,
                        TimeUnit.SECONDS)
                .build();
        //Log.e(TAG, "setPeriodicSyncUser");
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("SyncServiceUser", ExistingPeriodicWorkPolicy.REPLACE, periodicSync);
    }


    //NOT USED 18-05-2021
    public void setPeriodicSyncStatistic(int initialDelay){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest periodicSync = new PeriodicWorkRequest.Builder(SyncServiceStatisticWorker.class, 17, TimeUnit.MINUTES)
                .addTag("periodic-sync-statistics")
                //.setConstraints(constraints)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL,
                        10,
                        TimeUnit.SECONDS)
                .build();
        //Log.e(TAG, "setPeriodicSyncStatistic");
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("SyncServiceStatistic", ExistingPeriodicWorkPolicy.REPLACE, periodicSync);
    }


    //NOT USED 18-05-2021
    public void setPeriodicSyncQuestion(int initialDelay){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest periodicSync = new PeriodicWorkRequest.Builder(SyncServiceQuestionWorker.class, 15, TimeUnit.MINUTES)
                .addTag("periodic-sync-question")
                //.setConstraints(constraints)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL,
                        10,
                        TimeUnit.SECONDS)
                .build();
        //Log.e(TAG, "setPeriodicSyncQuestion");
        WorkManager.getInstance(context).enqueueUniquePeriodicWork("SyncServiceQuestion", ExistingPeriodicWorkPolicy.REPLACE, periodicSync);
    }



    //    //TODO: сделать чтобы эта задача и periodicSyncTask не конфликтовали - сделать тоже Periodic ?
//    public void manualSyncTask(){
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceTaskWorker.class)
//                .setInitialDelay(800, TimeUnit.MILLISECONDS)
//                .addTag("oneTimeWork-manualSyncTask")
//                .setBackoffCriteria(BackoffPolicy.LINEAR, // if Result.retry
//                        10, // retry after 10 seconds
//                        TimeUnit.SECONDS)
//                .build();
//        WorkManager.getInstance(getApplicationContext()).beginUniqueWork("manualSyncTask", ExistingWorkPolicy.REPLACE, work).enqueue();
//    }
//
//
//    //TODO: сделать чтобы эта задача и periodicSyncUser не конфликтовали
//    public void manualSyncUser(){
//        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncServiceUserWorker.class)
//                .setInitialDelay(1, TimeUnit.MILLISECONDS)
//                .addTag("oneTimeWork-manualSyncUser")
//                .setBackoffCriteria(BackoffPolicy.LINEAR, // if Result.retry
//                        10, // retry after 10 seconds
//                        TimeUnit.SECONDS)
//                .build();
//        WorkManager.getInstance(getApplicationContext()).beginUniqueWork("manualSyncUser", ExistingWorkPolicy.REPLACE, work).enqueue();
//    }


}
