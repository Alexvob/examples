package com.astudio.progressmonitor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.astudio.progressmonitor.promo.Promo;
import com.astudio.progressmonitor.promo.PromoEasy;
import com.astudio.progressmonitor.promo.SyncServicePromo;


import java.util.List;


@Dao
public interface PromoDao {


    // Repository

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, content FROM promo")
    LiveData<List<PromoEasy>> getPromoList();


    @Query("DELETE FROM promo WHERE id = :id")
    int deletePromo(Integer id);


    // SyncService Promo


    @Query("SELECT * FROM promo")
    List<Promo> getPromos();

    @Query("SELECT count(*) FROM promo")
    int getRowPromo();

    @Query("SELECT updatedAt FROM promo ORDER BY updatedAt DESC LIMIT 1")
    String getLastUpdatedLocalPromo();

    @Query("SELECT id, updatedAt FROM promo")
    List<PromoEasy> getAllUpdatedAt();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPromos(List<Promo> promos);

    @Query("DELETE FROM promo WHERE id IN (:listId)")
    void deletePromos(List<Integer> listId);



}
