package com.astudio.progressmonitor.workers;

import androidx.annotation.NonNull;

import com.astudio.progressmonitor.interfaces.CalledWorker;
import com.astudio.progressmonitor.modules.App;

public class UpdateServerTokenWorker implements CalledWorker {

    private static final String TAG = "UpdateServerTokenWorker";
    private String token;



    public UpdateServerTokenWorker(String tokenFCM) {
        this.token = tokenFCM;
    }


    @Override
    public void doWork() {

        App.getInstance().getControlWorkManagerTasks().setOneTimeUpdateTokenDevice(0);
        //App.getInstance().getRepositoryGroup().sendServerUpdateTokenFcm(userId, token);

    }


    @NonNull
    @Override
    public String toString() {
        return "UpdateServerTokenWorker{" +
                //"userId=" + userId +
                '}';
    }
}
