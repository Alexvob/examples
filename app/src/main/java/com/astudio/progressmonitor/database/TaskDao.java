package com.astudio.progressmonitor.database;

import androidx.annotation.Keep;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.astudio.progressmonitor.support.SqlConverter;
import com.astudio.progressmonitor.support.TaskEasy;
import com.astudio.progressmonitor.task.Task;


import java.util.List;
import java.util.Map;


@Dao
public interface TaskDao {

    //RepositoryTask

    @Query("SELECT * FROM task WHERE fromId = :userId AND status = :status AND groupToken = :groupToken ORDER BY updatedAt DESC")
    LiveData<List<Task>> getOutTask(int userId, String status, String groupToken);

    @Query("SELECT * FROM task WHERE forId = :userId AND status = :status AND groupToken = :groupToken ORDER BY updatedAt DESC")
    LiveData<List<Task>> getInTask(int userId, String status, String groupToken);

    @Query("SELECT * FROM task WHERE forId <> :userId AND fromId <> :userId AND status = :status AND groupToken = :groupToken ORDER BY updatedAt DESC")
    LiveData<List<Task>> getOtherTask(int userId, String status, String groupToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Task task);

    @Query("UPDATE task SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    void updateStatus(int id, String status, String updatedAt);

    @Query("UPDATE task SET read = :flag WHERE id = :id")
    void markReadTask(int id, int flag);

    @Query("DELETE FROM task WHERE id IN (:listId)")
    void delete(List<Integer> listId);




    // Old SyncService

//    @Query("SELECT id, updatedAt FROM task WHERE groupToken = :groupToken")
//    List<TaskEasy> getAllUpdatedAtOld(String groupToken);

    @Query("SELECT id FROM task WHERE groupToken = :groupToken")
    List <Integer> getListId(String groupToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Task> task);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateModifiedTasks(List<Task> mutableTasks);

    @Query("SELECT count(*) FROM task WHERE groupToken = :groupToken")
    Integer getNumberTableRows(String groupToken);

    @Query("SELECT updatedAt FROM task WHERE groupToken = :groupToken ORDER BY updatedAt DESC LIMIT 1")
    String getLastUpdatedLocalTable(String groupToken);


    // New SyncService

    @Query("SELECT count(*) FROM task WHERE groupToken = :groupToken")
    Integer getRowTask(String groupToken);

    @Query("SELECT updatedAt FROM task WHERE groupToken = :groupToken ORDER BY updatedAt DESC LIMIT 1")
    String getLastUpdatedLocalTask(String groupToken);

    @Query("SELECT id, updatedAt FROM task WHERE groupToken = :groupToken")
    List<TaskEasy> getAllUpdatedAt(String groupToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTasks(List<Task> tasks);

    @Query("DELETE FROM task WHERE id IN (:listId)")
    void deleteTasks(List<Integer> listId);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertQuestion(List<Question> questions);
//
//    @Query("DELETE FROM question WHERE id IN (:listId)")
//    void deleteQuestion(List<Integer> listId);

    // NOT USED

//    @Query("SELECT read FROM task WHERE id = :id")
//    int getAllTasks(int id);
//
//    @Query("SELECT id, status FROM task ORDER BY id DESC")
//        //@TypeConverters({SqlConverter.class})Map <Integer, String> getAllStatus();
//    List<TaskEasy> getAllStatus();
//
//    @Delete
//    void delete(Task task);
//
//    @Update
//    void update(Task task);
//
//    @Query("SELECT * FROM task WHERE status = :status ORDER BY createdAt DESC")
//    LiveData<List<Task>> getAll(String status);
//
//    @Query("SELECT * FROM task ORDER BY createdAt DESC")
//    LiveData<List<Task>> getAll();
//
//    @Query("SELECT * FROM task WHERE id = :id")
//    LiveData <Task> getById(long id);



}
