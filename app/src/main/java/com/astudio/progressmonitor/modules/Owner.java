package com.astudio.progressmonitor.modules;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
@Entity
public class Owner implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @Expose
    private int id;

    @Expose
    private String name;

    @SerializedName("name_first")
    @Expose
    private String nameFirst;

    @SerializedName("name_last")
    @Expose
    private String nameLast;

    @Expose
    private String phone;

    @Expose
    private String email;

    @Expose
    private String role;

    @SerializedName("group_token")
    @Expose
    private String groupToken;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @Expose
    private int rating;

    @SerializedName("parent_id")
    @Expose
    private int parentId;

    @Expose
    private String post;

    public Owner(int id, String name, String nameFirst, String nameLast, String phone, String email, String role, String groupToken,
                 String createdAt, String updatedAt, int rating, int parentId, String post) {
        this.id = id;
        this.name = name;
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.groupToken = groupToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.rating = rating;
        this.parentId = parentId;
        this.post = post;
    }

    // For @Delete then App.logout()
    @Ignore
    public Owner(int id){
        this.id = id;
    }

    protected Owner(Parcel in) {
        id = in.readInt();
        name = in.readString();
        nameFirst = in.readString();
        nameLast = in.readString();
        phone = in.readString();
        email = in.readString();
        role = in.readString();
        groupToken = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        rating = in.readInt();
        parentId = in.readInt();
        post = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(nameFirst);
        dest.writeString(nameLast);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(role);
        dest.writeString(groupToken);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(rating);
        dest.writeInt(parentId);
        dest.writeString(post);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Owner> CREATOR = new Creator<Owner>() {
        @Override
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        @Override
        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setGroupToken(String groupToken) {
        this.groupToken = groupToken;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public String getGroupToken() {
        return groupToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameFirst='" + nameFirst + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", groupToken='" + groupToken + '\'' +
                ", rating='" + rating + '\'' +
                ", parentId='" + parentId + '\'' +
                ", post='" + post + '\'' +
                '}';
    }
}
