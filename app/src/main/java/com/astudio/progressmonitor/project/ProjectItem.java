package com.astudio.progressmonitor.project;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
@Entity
public class ProjectItem extends BaseObservable implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("project_id")
    @Expose
    private int projectId;

    @SerializedName("group_token")
    @Expose
    private String groupToken;

    @SerializedName("item")
    @Expose
    private String item;

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("executor_id")
    @Expose
    private int executorId;

    @SerializedName("executor_name")
    @Expose
    private String executorName;

    @SerializedName("deadline")
    @Expose
    private String deadline;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    public String updatedAt = "";


    public ProjectItem (int projectId, String groupToken, String item, String desc, int status, int executorId, String executorName, String deadline ) {
        this.projectId = projectId;
        this.groupToken = groupToken;
        this.item = item;
        this.desc = desc;
        this.status = status;
        this.executorId = executorId;
        this.executorName = executorName;
        this.deadline = deadline;
    }


    protected ProjectItem(Parcel in) {
        id = in.readInt();
        projectId = in.readInt();
        groupToken = in.readString();
        item = in.readString();
        desc = in.readString();
        status = in.readInt();
        executorId = in.readInt();
        executorName = in.readString();
        deadline = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<ProjectItem> CREATOR = new Creator<ProjectItem>() {
        @Override
        public ProjectItem createFromParcel(Parcel in) {
            return new ProjectItem(in);
        }

        @Override
        public ProjectItem[] newArray(int size) {
            return new ProjectItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(projectId);
        dest.writeString(groupToken);
        dest.writeString(item);
        dest.writeString(desc);
        dest.writeInt(status);
        dest.writeInt(executorId);
        dest.writeString(executorName);
        dest.writeString(deadline);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getExecutorId() {
        return executorId;
    }

    public void setExecutorId(int executorId) {
        this.executorId = executorId;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getGroupToken() {
        return groupToken;
    }

    public void setGroupToken(String groupToken) {
        this.groupToken = groupToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Bindable
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Bindable
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        notifyPropertyChanged(BR.updatedAt);
    }


    @NonNull
    @Override
    public String toString() {
        return "ProjectItem{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", item='" + item + '\'' +
                ", desc='" + desc + '\'' +
                ", status=" + status +
                ", executorId=" + executorId +
                ", executorName=" + executorName +
                ", deadline=" + deadline +
                ", groupToken='" + groupToken + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
