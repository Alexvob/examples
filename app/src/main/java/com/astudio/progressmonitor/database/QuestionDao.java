package com.astudio.progressmonitor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.astudio.progressmonitor.statistic.Statistic;
import com.astudio.progressmonitor.support.QuestionEasy;
import com.astudio.progressmonitor.voting.Question;

import java.util.List;


@Dao
public interface QuestionDao {

    // VotingRepository

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(Question question);

    @Query("SELECT * FROM question WHERE groupToken = :groupToken AND status = :status ORDER BY created_at DESC ")
    LiveData<List<Question>> getQuestions(String groupToken, String status);

    @Query("UPDATE question SET voted = :flag WHERE id = :id")
    void markVoted(int id, int flag);

//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    void markVoted(Question question);


    // SyncService - Question

    @Query("SELECT count(*) FROM question WHERE groupToken = :groupToken")
    Integer getRowQuestion(String groupToken);

    @Query("SELECT updated_at FROM question WHERE groupToken = :groupToken ORDER BY updated_at DESC LIMIT 1")
    String getLastUpdatedLocalQuestion(String groupToken);

    @Query("SELECT id, updated_at FROM question WHERE groupToken = :groupToken")
    List<QuestionEasy> getAllUpdatedAt(String groupToken);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(List<Question> questions);

    @Query("DELETE FROM question WHERE id IN (:listId)")
    void deleteQuestion(List<Integer> listId);



}
