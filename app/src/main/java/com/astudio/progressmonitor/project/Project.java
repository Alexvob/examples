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
public class Project extends BaseObservable implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("owner_id")
    @Expose
    private int ownerId;

    @SerializedName("group_token")
    @Expose
    private String groupToken;

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("visibility")
    @Expose
    private int visibility;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    public String updatedAt = "";


    public Project(int ownerId, String desc, int status, int visibility, String groupToken) {
        this.ownerId = ownerId;
        this.desc = desc;
        this.status = status;
        this.visibility = visibility;
        this.groupToken = groupToken;
    }


    protected Project(Parcel in) {
        id = in.readInt();
        ownerId = in.readInt();
        desc = in.readString();
        status = in.readInt();
        visibility = in.readInt();
        groupToken = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(ownerId);
        dest.writeString(desc);
        dest.writeInt(status);
        dest.writeInt(visibility);
        dest.writeString(groupToken);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
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

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
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
        return "Project{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", desc='" + desc + '\'' +
                ", status=" + status +
                ", visibility=" + visibility +
                ", groupToken='" + groupToken + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
