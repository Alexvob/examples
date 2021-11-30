package com.astudio.progressmonitor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.astudio.progressmonitor.plan.Plan;
import com.astudio.progressmonitor.plan.PlanEasy;

import java.util.List;


@Dao
public interface PlanDao {


    // Repository

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPlan(Plan plan);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM `plan` WHERE groupToken = :groupToken AND ownerId = :userId AND planDate = :planDate")
    LiveData <List<Plan>> getPlan(String groupToken, int userId, String planDate);

    //@Query("SELECT * FROM `plan` WHERE groupToken = :groupToken AND ownerId = :userId AND createdAt between :startDay and :endDay")
    //LiveData <List<Plan>> getPlan(String groupToken, int userId, String startDay, String endDay);


    @Query("SELECT DISTINCT `desc` FROM `plan` WHERE ownerId = :userId")
    LiveData<List<String>> getDescPlans(int userId);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updatePlan(Plan plan);


    @Query("DELETE FROM `plan` WHERE id = :id")
    int deletePlan(Integer id);


    // Statistic Plan

    @Query("SELECT SUM(duration) FROM `plan` WHERE groupToken = :groupToken AND createdAt between :start and :end")
    Integer getSumDurationByGroup(String groupToken, String start, String end);

    @Query("SELECT * FROM `plan` WHERE groupToken = :groupToken AND ownerId = :userId AND createdAt between :start and :end")
    List<Plan> getPlanForSelfStatistic(String groupToken, int userId, String start, String end);

    @Query("SELECT * FROM `plan` WHERE groupToken = :groupToken AND ownerId IN (:userIds) AND createdAt between :start and :end")
    List<Plan> getPlanForChildStatistic(String groupToken, List<Integer> userIds, String start, String end);

    @Query("SELECT count(*) FROM `plan` WHERE groupToken = :groupToken AND ownerId IN (:userIds) AND createdAt between :start and :end")
    Integer getNumberPlannedChild (String groupToken, List<Integer> userIds, String start, String end);

    @Query("SELECT count(*) FROM `plan` WHERE groupToken = :groupToken AND ownerId IN (:userIds) AND status = :status AND createdAt between :start and :end")
    Integer getNumberNotAcceptedPlan (String groupToken, List<Integer> userIds, int status, String start, String end);


    // SyncService

    @Query("SELECT count(*) FROM `plan` WHERE groupToken = :groupToken")
    Integer getRowPlan(String groupToken);

    @Query("SELECT updatedAt FROM `plan` WHERE groupToken = :groupToken ORDER BY updatedAt DESC LIMIT 1")
    String getLastUpdatedLocalPlan(String groupToken);

    @Query("SELECT id, updatedAt FROM `plan` WHERE groupToken = :groupToken")
    List<PlanEasy> getAllUpdatedAt(String groupToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPlans(List<Plan> plans);

    @Query("DELETE FROM `plan` WHERE id IN (:listId)")
    void deletePlans(List<Integer> listId);


}
