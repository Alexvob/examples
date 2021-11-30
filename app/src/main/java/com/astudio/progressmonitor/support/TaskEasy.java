package com.astudio.progressmonitor.support;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

@Keep
public class TaskEasy {

    @ColumnInfo(name = "id")
    private int id;

//    @ColumnInfo(name = "status")
//    private String status;

    @ColumnInfo(name = "updatedAt")
    private String updatedAt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    //    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }


    @NonNull
    @Override
    public String toString() {
        return "TaskEasy{" +
                "id=" + id +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
