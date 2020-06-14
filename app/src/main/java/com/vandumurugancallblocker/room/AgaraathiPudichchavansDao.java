package com.vandumurugancallblocker.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AgaraathiPudichchavansDao {
    @Query("select  * from AgaraathiPudichchavan order by AgaraathiPudichchavan.id desc")
    LiveData<List<AgaraathiPudichchavan>> getAgaraathiPudichavansAsLiveData();

    @Query("select  * from AgaraathiPudichchavan")
    List<AgaraathiPudichchavan> getAgaraathiPudichavansAsList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AgaraathiPudichchavan> agaraathiPudichchavans);

    @Delete
    void delete(AgaraathiPudichchavan agaraathiPudichchavan);

}
