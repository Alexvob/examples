package com.astudio.progressmonitor.database;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.astudio.progressmonitor.user.User;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.Arrays;
import java.util.List;


@Dao
public abstract class GroupStatisticDao {



    @Query("SELECT id FROM user WHERE groupToken = :groupToken")
    abstract LiveData<Integer> getAllUpdatedAt1(String groupToken);

    @Query("SELECT id FROM user WHERE groupToken = :groupToken")
    abstract LiveData<Integer> getAllUpdatedAt2(String groupToken);


    public LiveData<Integer[]> testLiveData(String groupToken){
        LiveData<Integer> v1 = getAllUpdatedAt1(groupToken);

        LiveData<Integer> v2 = getAllUpdatedAt2(groupToken);
        LiveData<Integer[]> arr;

        MutableLiveData<Integer[]> mArr = new MutableLiveData<>();
        //mArr.setValue(v1);
        return mArr;
    }



}
