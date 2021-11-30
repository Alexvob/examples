package com.astudio.progressmonitor.modules;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.room.Embedded;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Keep
public class GlobalData implements Parcelable {

    // Owner
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("user_name_first")
    @Expose
    public String userNameFirst;
    @SerializedName("user_name_last")
    @Expose
    public String userNameLast;
    @SerializedName("email")
    @Expose
    public String email; // TODO: разбираться с final полей, их нельзя будет перезаписать при работе приложения!
    @SerializedName("phone")
    @Expose
    public String userPhone;
    @SerializedName("role")
    @Expose
    public String role;
    @SerializedName("rating")
    @Expose
    public int rating;
    @SerializedName("parent_id")
    @Expose
    public int parentId;
    @SerializedName("post")
    @Expose
    public String post;
    @SerializedName("user_created_at")
    @Expose
    public String userCreatedAt;
    @SerializedName("user_updated_at")
    @Expose
    public String userUpdatedAt;

    // Group
    @SerializedName("group_token")
    @Expose
    public String groupToken;
    @SerializedName("group_id")
    @Expose
    public int groupId;
    @SerializedName("group_name")
    @Expose
    public String groupName;
    @SerializedName("number_employees")
    @Expose
    public int numberEmployees;
    @SerializedName("date_last_pay")
    @Expose
    public String dateLastPay;
    @SerializedName("date_block")
    @Expose
    public String dateBlock;
    @SerializedName("group_created_at")
    @Expose
    public String groupCreatedAt;
    @SerializedName("group_updated_at")
    @Expose
    public String groupUpdatedAt;
    //@Ignore
    //public String settings = "TASKS_ACCESS_TO_OTHER:allow;TASKS_ACCESS_TO_USERS:all;PROJECTS_ACCESS_TO_USERS:child";
    @SerializedName("group_settings")
    @Expose
    public String settings;


    // когда Room создает этот объект, то игнорирует конструктор и вызовы приватных методов в нем.
    //@Ignore
    //public Map<String, String> settings = new HashMap<>();


    public GlobalData(int userId, String userName, String userNameFirst, String userNameLast, String email, String userPhone, String role,
                      int rating, int parentId, String post, String userCreatedAt, String userUpdatedAt,
                      String groupToken, int groupId, String groupName, int numberEmployees,
                      String dateLastPay, String dateBlock, String groupCreatedAt, String groupUpdatedAt, String settings) {
        this.userId = userId;
        this.userName = userName;
        this.userNameFirst = userNameFirst;
        this.userNameLast = userNameLast;
        this.email = email;
        this.userPhone = userPhone;
        this.role = role;
        this.rating = rating;
        this.parentId = parentId;
        this.post = post;
        this.userCreatedAt = userCreatedAt;
        this.userUpdatedAt = userUpdatedAt;

        this.groupToken = groupToken;
        this.groupId = groupId;
        this.groupName = groupName;
        this.numberEmployees = numberEmployees;
        this.dateLastPay = dateLastPay;
        this.dateBlock = dateBlock;
        this.groupCreatedAt = groupCreatedAt;
        this.groupUpdatedAt = groupUpdatedAt;
        this.settings = settings;
    }


    public HashMap<String, String> parseSettings(){
        HashMap<String, String> map = new HashMap<>();
        String[] pairs = settings.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length > 1) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        //Log.e("GlobalDataClass", "MAP: " + map);
        return map;
    }


    protected GlobalData(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        userNameFirst = in.readString();
        userNameLast = in.readString();
        email = in.readString();
        userPhone = in.readString();
        role = in.readString();
        rating = in.readInt();
        parentId = in.readInt();
        post = in.readString();
        userCreatedAt = in.readString();
        userUpdatedAt = in.readString();

        groupToken = in.readString();
        groupId = in.readInt();
        groupName = in.readString();
        numberEmployees = in.readInt();
        dateLastPay = in.readString();
        dateBlock = in.readString();
        groupCreatedAt = in.readString();
        groupUpdatedAt = in.readString();
        settings = in.readString();
    }

    public static final Creator<GlobalData> CREATOR = new Creator<GlobalData>() {
        @Override
        public GlobalData createFromParcel(Parcel in) {
            return new GlobalData(in);
        }

        @Override
        public GlobalData[] newArray(int size) {
            return new GlobalData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(userNameFirst);
        dest.writeString(userNameLast);
        dest.writeString(email);
        dest.writeString(userPhone);
        dest.writeString(role);
        dest.writeInt(rating);
        dest.writeInt(parentId);
        dest.writeString(post);
        dest.writeString(userCreatedAt);
        dest.writeString(userUpdatedAt);

        dest.writeString(groupToken);
        dest.writeInt(groupId);
        dest.writeString(groupName);
        dest.writeInt(numberEmployees);
        dest.writeString(dateLastPay);
        dest.writeString(dateBlock);
        dest.writeString(groupCreatedAt);
        dest.writeString(groupUpdatedAt);
        dest.writeString(settings);
    }

    @Override
    public String toString() {
        return "GlobalData{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", rating='" + rating + '\'' +
                ", parentId='" + parentId + '\'' +
                ", post='" + post + '\'' +
                ", groupToken='" + groupToken + '\'' +
                ", groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", numberEmployees='" + numberEmployees + '\'' +
                ", dateLastPay='" + dateLastPay + '\'' +
                ", dateBlock='" + dateBlock + '\'' +
                ", groupCreatedAt='" + groupCreatedAt + '\'' +
                ", settings='" + settings + '\'' +
                '}';
    }
}
