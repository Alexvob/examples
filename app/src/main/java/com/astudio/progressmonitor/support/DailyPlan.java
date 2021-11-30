package com.astudio.progressmonitor.support;

import androidx.annotation.Keep;

import com.astudio.progressmonitor.task.Task;
import com.astudio.progressmonitor.user.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Keep
public class DailyPlan {

    @SerializedName("user")
    @Expose
    public User user;

    @SerializedName("task")
    @Expose
    public Task task;


    public DailyPlan(){
        user = new User("Name1", "FirstName1", "", "Phone1", "", "pass1", "u", "token1", 0, 9999, "");
        task = new Task(1, "from1", 2, "for1", "desc1", "2021-01-01","a", "token1");
    }


}
