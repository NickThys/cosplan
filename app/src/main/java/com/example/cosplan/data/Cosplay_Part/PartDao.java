package com.example.cosplan.data.Cosplay_Part;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Relation;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.cosplan.data.Coplay.Cosplay;

import java.util.List;

@Dao
public interface PartDao {
    @Insert
    void insert(Part part);

    @Query("SELECT * FROM CosplayPart_table WHERE CosplayPartBuyMake='Make'")
    LiveData<List<Part>> getPartsToMake();
    @Query("SELECT * FROM CosplayPart_table WHERE CosplayPartBuyMake='Buy'")
    LiveData<List<Part>> getPartsToBuy();

    @Delete
    void delete(Part part);

    @Update
    void update(Part part);

}