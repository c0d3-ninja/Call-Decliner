package com.vandumurugancallblocker.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FlagDao {

    @Query("Select * from FlagEntity where FlagEntity.id=:id")
    LiveData<FlagEntity> getFlagAsLiveData(String id);

    @Query("Select * from FlagEntity where FlagEntity.id=:id")
    FlagEntity getFlagSync(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FlagEntity flagEntity);
}
