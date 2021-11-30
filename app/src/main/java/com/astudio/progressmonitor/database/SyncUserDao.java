package com.astudio.progressmonitor.database;

import android.database.SQLException;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;


import com.astudio.progressmonitor.user.User;
import com.astudio.progressmonitor.user.UserEasy;

import java.util.List;


@Dao
public abstract class SyncUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<User> users);

    @Query("DELETE FROM user WHERE id IN (:listId)")
    abstract void delete(List<Integer> listId);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, updatedAt, parentId FROM user WHERE groupToken = :groupToken")
    abstract List<UserEasy> getAllUpdatedAt(String groupToken);

    @Query("DELETE FROM task WHERE fromId = :userId AND groupToken = :groupToken")
    abstract void deleteUserTasks(int userId, String groupToken);

    @Query("DELETE FROM user WHERE id = :userId")
    abstract int deleteUser(int userId);


    @Transaction
    public List<UserEasy> syncRowsInTransactionOnlyAdd(List<User> users, String groupToken){
        insert(users);
        return getAllUpdatedAt(groupToken);
    }


    @Transaction
    public List<UserEasy> syncRowsInTransactionOnlyDel(List<Integer> listId, String groupToken){
        delete(listId);
        return getAllUpdatedAt(groupToken);
    }


    @Transaction
    public List<UserEasy> syncRowsInTransaction(List<User> users, List<Integer> listId, String groupToken){
        insert(users);
        delete(listId);
        return getAllUpdatedAt(groupToken);
    }


    // RepositoryUser-UserControl - Manual Deleting
    @Transaction
    public int deleteUserAndTasks(int userId, String groupToken){
        try {
            deleteUserTasks(userId, groupToken);
            return deleteUser(userId);
        }catch (SQLException e){
            return 0;
        }
    }


    // App-logoutForDeleteUser (delete all tasks and all users)
    @Transaction
    public void deleteAllUserAndTasks(String groupToken){
        deleteAllTasks(groupToken);
        deleteAllUsers(groupToken);
    }


    @Query("DELETE FROM task WHERE groupToken = :groupToken")
    abstract void deleteAllTasks(String groupToken);

    @Query("DELETE FROM user WHERE groupToken = :groupToken")
    abstract void deleteAllUsers(String groupToken);


    // RepositoryUser
    @Transaction
    public boolean changeAdminAndOwner(int newAdmin, int oldAdmin){
        try {
            changeAdmin(newAdmin, "a");
            changeOwner(newAdmin, "a");
            changeAdmin(oldAdmin, "u");
            changeOwner(oldAdmin, "u");
            return true;
        }catch (SQLException e){
            return false;
        }
    }


    @Query ("UPDATE user SET role = :role WHERE id = :userId")
    abstract void changeAdmin(int userId, String role);  // Смена админа

    @Query ("UPDATE owner SET role = :role WHERE id = :userId")
    abstract void changeOwner(int userId, String role);  // Смена админа

}
