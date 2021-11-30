package com.astudio.progressmonitor.project;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;

@Keep
public class ProjectEasy {

    @ColumnInfo(name = "id")
    private int id;

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


    @Override
    public String toString() {
        return "ProjectEasy{" +
                "id=" + id +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
