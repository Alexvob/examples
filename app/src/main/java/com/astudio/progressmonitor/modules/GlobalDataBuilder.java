package com.astudio.progressmonitor.modules;

import android.os.AsyncTask;

import com.astudio.progressmonitor.database.AppDatabase;
import com.astudio.progressmonitor.interfaces.GroupContracts;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

// Вероятно этот класс - попытка сделать "модульным" запрос глобальных данных, а вообще этот функционал есть в РепозиториГроуп

public class GlobalDataBuilder implements GroupContracts.GlobalDataBuilder {

    private AppDatabase db;
    private CallbackCaller callbackCaller;

    public GlobalDataBuilder(AppDatabase database, CallbackCaller callbackCaller) {
        db = database;
        this.callbackCaller = callbackCaller;
    }


    public interface CallbackCaller{
        void onResponse(GlobalData globalData);
    }


    public void executeQuery(){
        AppGlobalDataQuery appGlobalDataQuery = new AppGlobalDataQuery(db, this);
        appGlobalDataQuery.execute();
    }


    @Override
    public void onResponse(GlobalData globalData) {
        callbackCaller.onResponse(globalData);
    }


    static class AppGlobalDataQuery extends AsyncTask<Void, Void, GlobalData> {

        private AppDatabase db;
        GroupContracts.GlobalDataBuilder callback;

        AppGlobalDataQuery(AppDatabase db, GroupContracts.GlobalDataBuilder callback) {
            this.db = db;
            this.callback = callback;
        }
        @Override
        protected GlobalData doInBackground(Void... voids) {
            try {
                return db.groupDao().getAppGlobalData();
            } catch (Exception e){
                FirebaseCrashlytics.getInstance().recordException(e);
                return null;
            }
        }
        @Override
        protected void onPostExecute(GlobalData globalData) {
            super.onPostExecute(globalData);
            callback.onResponse(globalData);
        }
    }

}
