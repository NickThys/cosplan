package com.example.cosplan.data.cosplay;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CosplayDao {
    @Insert
    void insert(Cosplay cosplay);

    @Query("SELECT *FROM cosplay_table")
    LiveData<List<Cosplay>> getAllCosplays();

    @Delete
    void deleteCosplay(Cosplay cosplay);
    @Update
    void updateCosplay(Cosplay cosplay);
}
