package com.astudio.progressmonitor.database;

import androidx.annotation.Keep;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.astudio.progressmonitor.support.TaskEasy;
import com.astudio.progressmonitor.task.Task;

import java.util.List;


@Dao
public abstract class SyncTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<Task> tasks);

    @Query("DELETE FROM task WHERE id IN (:listId)")
    abstract void delete(List<Integer> listId);

    @Query("SELECT id, updatedAt FROM task WHERE groupToken = :groupToken")
    abstract List<TaskEasy> getAllUpdatedAt(String groupToken);


    @Transaction
    public List<TaskEasy> syncRowsInTransactionOnlyAdd(List<Task> tasks, String groupToken){
        insert(tasks);
        return getAllUpdatedAt(groupToken);
    }

    @Transaction
    public List<TaskEasy> syncRowsInTransactionOnlyDel(List<Integer> listId, String groupToken){
        delete(listId);
        return getAllUpdatedAt(groupToken);
    }

    @Transaction
    public List<TaskEasy> syncRowsInTransaction(List<Task> tasks, List<Integer> listId, String groupToken){
        insert(tasks);
        delete(listId);
        return getAllUpdatedAt(groupToken);
    }


    @Query("DELETE FROM task WHERE groupToken = :groupToken")
    abstract void deleteTasks(String groupToken);

    @Query("DELETE FROM task WHERE groupToken = :groupToken")
    abstract void deleteUsers(String groupToken);

    @Query("DELETE FROM statistic WHERE groupToken = :groupToken")
    abstract void deleteStatistics(String groupToken);


    public void clearLocalTables(String groupToken){
        deleteTasks(groupToken);
        deleteUsers(groupToken);
        deleteStatistics(groupToken);
    }


}
