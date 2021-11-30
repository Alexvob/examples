package com.astudio.progressmonitor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import com.astudio.progressmonitor.statistic.Statistic;
import com.astudio.progressmonitor.support.QuestionEasy;
import com.astudio.progressmonitor.voting.Question;

import java.util.List;
import java.util.Map;


@Dao
public interface StatisticDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void syncGroupStatistic(List<Statistic> statistics);

    // пересмотреть критерий по которому выбирать строку
    //@Query("SELECT * FROM statistic WHERE groupToken = :groupToken ORDER BY updated_at DESC LIMIT 1;")
    @Query("SELECT * FROM statistic WHERE groupToken = :groupToken AND year = :year AND month = :month")
    List<Statistic> getLocalStatisticCurrentMonth(String groupToken, int year, int month);

    @Query("SELECT count(*) FROM statistic WHERE groupToken = :groupToken")
    Integer getRowStatistic(String groupToken);


    @Query("SELECT count(*) FROM task WHERE groupToken = :groupToken AND createdAt between :start and :end")
    LiveData <Integer> getPeriod(String groupToken, String start, String end);


    // RepositoryStatistic

    @Query("SELECT * FROM statistic WHERE groupToken = :groupToken ORDER BY year ASC, month ASC")
    LiveData <Statistic[]> getGroupStatistic(String groupToken);

    @Query("SELECT count(*) FROM user WHERE groupToken = :groupToken")
    LiveData <Integer> getUsersInGroup(String groupToken);

    @Query("SELECT name FROM user WHERE groupToken = :groupToken AND role = :role")
    LiveData <String> getAdminName(String groupToken, String role);

    @Query("SELECT count(*) FROM task WHERE groupToken = :groupToken AND status = :status")
    LiveData <Integer> getActiveGroup(String groupToken, String status);

    @Query("SELECT count(*) FROM task WHERE groupToken = :groupToken AND forId = :userId AND status = :status")
    LiveData <Integer> getActiveForMe(String groupToken, int userId, String status);

    @Query("SELECT count(*) FROM task WHERE groupToken = :groupToken AND fromId = :userId AND status = :status")
    LiveData <Integer> getActiveFromMe(String groupToken, int userId, String status);


    // StatisticSupport

    @Query("SELECT * FROM statistic WHERE groupToken = :groupToken AND year = :year AND month = :month")
    Statistic checkCurrentMonthRow(String groupToken, int year, int month);

    @Insert
    void insert(Statistic statistic);

    @Query ("INSERT INTO statistic (groupToken, year, month) VALUES (:groupToken, :year, :month);")
    void insertNewRow(String groupToken, int year, int month);  // Смена админа


    @Query ("UPDATE statistic SET created = created + 1 WHERE groupToken = :groupToken AND year = :year AND month = :month")
    void incrementCreatedTask(String groupToken, int year, int month);

    @Query ("UPDATE statistic SET completed = completed + 1 WHERE groupToken = :groupToken AND year = :year AND month = :month")
    void incrementCompletedTask(String groupToken, int year, int month);

    @Query ("UPDATE statistic SET canceled = canceled + 1 WHERE groupToken = :groupToken AND year = :year AND month = :month")
    void incrementCanceledTask(String groupToken, int year, int month);

    @Query ("UPDATE statistic SET canceled = canceled - 1 WHERE groupToken = :groupToken AND year = :year AND month = :month")
    void decrementCanceledTask(String groupToken, int year, int month);



}
