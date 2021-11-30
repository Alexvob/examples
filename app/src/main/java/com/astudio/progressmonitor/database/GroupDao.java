package com.astudio.progressmonitor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.astudio.progressmonitor.group.Group;
import com.astudio.progressmonitor.modules.GlobalData;


@Dao
public interface GroupDao {


    // App

    @Query("SELECT owner.id AS userId, owner.name AS userName, owner.nameFirst AS userNameFirst, owner.nameLast AS userNameLast," +
            " owner.email, owner.phone AS userPhone, owner.role, owner.rating, owner.parentId, owner.post, owner.createdAt, owner.updatedAt, `group`.groupToken, `group`.id AS groupId," +
            " `group`.name AS groupName, `group`.numberEmployees, `group`.dateLastPay, `group`.dateBlock, `group`.settings, `group`.createdAt AS groupCreatedAt, `group`.updatedAt AS groupUpdatedAt " +
            "FROM owner, `group` " +
            "WHERE `group`.groupToken == owner.groupToken")
    GlobalData getAppGlobalData();



    // RepositoryGroup

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Group group);


    @Query("SELECT * FROM `group` WHERE groupToken = :groupToken")
    Group getGroup(String groupToken);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGroup(Group group);


    @Delete
    void delete(Group group); // App.logout()


    @Query("SELECT owner.id AS userId, owner.name AS userName, owner.nameFirst AS userNameFirst, owner.nameLast AS userNameLast," +
            " owner.email, owner.phone AS userPhone, owner.role, owner.rating, owner.parentId, owner.post, owner.createdAt, owner.updatedAt, `group`.groupToken, `group`.id AS groupId," +
            " `group`.name AS groupName, `group`.numberEmployees, `group`.dateLastPay, `group`.dateBlock, `group`.settings, `group`.createdAt AS groupCreatedAt, `group`.updatedAt AS groupUpdatedAt " +
            "FROM owner, `group` " +
            "WHERE `group`.groupToken == owner.groupToken")
    LiveData<GlobalData> getLiveDataGlobalData();



    // не нужен этот метод, тк в Арр получаем данные группы при старте приложения
    //@Query("SELECT * FROM `group` WHERE groupToken = :groupToken")
    //LiveData <Group> getGroup(String groupToken);


}
