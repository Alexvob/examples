package com.astudio.progressmonitor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.astudio.progressmonitor.modules.Owner;
import com.astudio.progressmonitor.scheme.SchemeElement;
import com.astudio.progressmonitor.scheme.StructuralSchemeActivity;
import com.astudio.progressmonitor.user.User;
import com.astudio.progressmonitor.user.UserEasy;

import java.sql.SQLException;
import java.util.List;


@Dao
public interface UserDao {

    // RepositoryUser

    @Query("SELECT * FROM user WHERE groupToken = :groupToken ORDER BY name")
    LiveData <List<User>> getAllUsers(String groupToken);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, name, parentId, post FROM user WHERE groupToken = :groupToken ORDER BY name")
    LiveData <List<UserEasy>> getUserListForSpinner(String groupToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Query("DELETE FROM user WHERE id IN (:listId)")
    void delete(List<Integer> listId);

//    @Query ("UPDATE user SET role = :role WHERE id = :userId")
//    void changeAdmin(int userId, String role);  // Смена админа


    // For RepositoryPlan for statistic (range employees by execution)
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, name, parentId FROM user WHERE groupToken = :groupToken")
    List<UserEasy> getUsersEasy(String groupToken);



    // SyncService-User Old

    @Query("SELECT count(*) FROM user WHERE groupToken = :groupToken")
    Integer getNumberTableRows(String groupToken);

    @Query("SELECT updatedAt FROM user WHERE groupToken = :groupToken ORDER BY updatedAt DESC LIMIT 1")
    String getLastUpdatedLocalTable(String groupToken);

    @Query("SELECT id FROM user WHERE groupToken = :groupToken")
    List <Integer> getListId(String groupToken);

//    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
//    @Query("SELECT id, updatedAt FROM user WHERE groupToken = :groupToken")
//    List<UserEasy> getAllUpdatedAt(String groupToken);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateModifiedUsers(List<User> mutableUsers);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> users);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    int updateUser(User user);



    // New SyncService

    @Query("SELECT count(*) FROM user WHERE groupToken = :groupToken")
    Integer getRowUser(String groupToken);

    @Query("SELECT updatedAt FROM user WHERE groupToken = :groupToken ORDER BY updatedAt DESC LIMIT 1")
    String getLastUpdatedLocalUser(String groupToken);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, updatedAt, parentId FROM user WHERE groupToken = :groupToken")
    List<UserEasy> getAllUpdatedAt(String groupToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> users);

    @Query("DELETE FROM user WHERE id IN (:listId)")
    void deleteUsers(List<Integer> listId);


    // Owner

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Owner owner);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateOwner(Owner owner);

    @Query("SELECT * FROM owner")
    Owner getOwnerData() throws SQLException;

    @Query ("UPDATE owner SET role = :role WHERE id = :userId")
    void updateOwnerRole(int userId, String role);  // Смена админа

    @Delete
    void delete(Owner owner);

    @Query("SELECT role FROM owner")
    LiveData<String> observeOwnerRole();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, parentId, name, post FROM user WHERE groupToken = :groupToken")
    LiveData<List<SchemeElement>> getSchemeElements(String groupToken);


    // For Repository Post
    @Query ("UPDATE user SET parentId = :newParentId WHERE id IN (:userIds)")
    void changeParentForGroupEmployees(int newParentId, List<Integer> userIds);



    // Profile Statistic

//    @Query("SELECT count(*) FROM user WHERE groupToken = :groupToken")
//    LiveData <Integer> getUsersInGroup(String groupToken);
//
//    @Query("SELECT name FROM user WHERE groupToken = :groupToken AND role = :role")
//    LiveData <String> getAdminName(String groupToken, String role);
//
//    @Query("SELECT count(*) FROM task WHERE groupToken = :groupToken AND status = :status")
//    LiveData <Integer> getActiveGroup(String groupToken, String status);
//
//    @Query("SELECT count(*) FROM task WHERE groupToken = :groupToken AND forId = :userId AND status = :status")
//    LiveData <Integer> getActiveForMe(String groupToken, int userId, String status);
//
//    @Query("SELECT count(*) FROM task WHERE groupToken = :groupToken AND fromId = :userId AND status = :status")
//    LiveData <Integer> getActiveFromMe(String groupToken, int userId, String status);







    //Billing
//    @Query("SELECT createdAt FROM user WHERE createdAt IN (SELECT min(createdAt) FROM user) AND groupToken = :groupToken")
//    String getFirstDateCreateUser(String groupToken);
//
//    @Query("SELECT createdAt FROM user WHERE createdAt IN (SELECT max(createdAt) FROM user) AND groupToken = :groupToken")
//    String getLastDateCreateUser(String groupToken);



    //@Query("SELECT * FROM owner")
    //Owner checkOwnerData();

//    @Query("SELECT * FROM owner")
//    LiveData<Owner> getOwnerLiveData();

}
