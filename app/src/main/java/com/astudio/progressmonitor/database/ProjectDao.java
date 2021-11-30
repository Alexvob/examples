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
import com.astudio.progressmonitor.project.CounterItem;
import com.astudio.progressmonitor.project.Project;
import com.astudio.progressmonitor.project.ProjectEasy;
import com.astudio.progressmonitor.project.ProjectItem;

import java.util.List;


@Dao
public interface ProjectDao {


    // Repository

    @Query("SELECT COUNT(*) as count_all_item, COUNT(CASE WHEN status == 100 THEN 1 END) as count_exec_item from projectitem WHERE groupToken = :groupToken AND projectId = :projectId" )
    LiveData <CounterItem> countProjectItems(String groupToken, int projectId);


    @Query("UPDATE project SET status = :projectProgress WHERE id = :id")
    void setProjectProgress(int id, int projectProgress);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM project WHERE groupToken = :groupToken AND ownerId = :userId AND status != 101 ORDER BY createdAt DESC")
    LiveData <List<Project>> getProjects(String groupToken, int userId);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM project WHERE groupToken = :groupToken AND ownerId = :userId AND status = :status ORDER BY createdAt DESC")
    LiveData <List<Project>> getProjects(String groupToken, int userId, int status);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM project WHERE groupToken = :groupToken AND ownerId = :userId AND status != 101 AND visibility == 0 ORDER BY createdAt DESC")
    LiveData <List<Project>> getProjectsWithoutPrivate(String groupToken, int userId);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM project WHERE groupToken = :groupToken AND ownerId = :userId AND status = :status AND visibility == 0 ORDER BY createdAt DESC")
    LiveData <List<Project>> getProjectsWithoutPrivate(String groupToken, int userId, int status);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProject(Project project);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProject(Project project);

    @Query("DELETE FROM project WHERE id = :id")
    int deleteProject(Integer id);


    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    //@Query("SELECT * FROM ProjectItem WHERE groupToken = :groupToken AND projectId = :projectId ORDER BY ABS(item)")
    @Query("SELECT * FROM ProjectItem WHERE groupToken = :groupToken AND projectId = :projectId")
    LiveData <List<ProjectItem>> getProjectItems(String groupToken, int projectId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProjectItem(ProjectItem projectItem);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProjectItem(ProjectItem projectItem);


    @Query("DELETE FROM projectitem WHERE id = :id")
    int deleteProjectItem(Integer id);



    // SyncService Project

    @Query("SELECT count(*) FROM project WHERE groupToken = :groupToken")
    Integer getRowProject(String groupToken);

    @Query("SELECT updatedAt FROM project WHERE groupToken = :groupToken ORDER BY updatedAt DESC LIMIT 1")
    String getLastUpdatedLocalProject(String groupToken);

    @Query("SELECT id, updatedAt FROM project WHERE groupToken = :groupToken")
    List<ProjectEasy> getAllUpdatedAt(String groupToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProjects(List<Project> projects);

    @Query("DELETE FROM project WHERE id IN (:listId)")
    void deleteProjects(List<Integer> listId);


    // ----------------------------------

    // SyncService ProjectItem

    @Query("SELECT count(*) FROM ProjectItem WHERE groupToken = :groupToken")
    Integer getRowProjectItem(String groupToken);

    @Query("SELECT updatedAt FROM ProjectItem WHERE groupToken = :groupToken ORDER BY updatedAt DESC LIMIT 1")
    String getLastUpdatedLocalProjectItem(String groupToken);

    @Query("SELECT id, updatedAt FROM ProjectItem WHERE groupToken = :groupToken")
    List<ProjectEasy> getAllUpdatedAtItem(String groupToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProjectItems(List<ProjectItem> projectItems);

    @Query("DELETE FROM ProjectItem WHERE id IN (:listId)")
    void deleteProjectItems(List<Integer> listId);




}
