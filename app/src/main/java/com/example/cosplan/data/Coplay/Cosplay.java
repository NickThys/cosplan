package com.example.cosplan.data.Coplay;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "cosplay_table")
public class Cosplay {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "CosplayId")
    public int mCosplayId;
    @NonNull
    @ColumnInfo(name = "CosplayName")
    public String mCosplayName;
    @NonNull
    @ColumnInfo(name = "CosplayStartDate")
    public String mCosplayStartDate;
    @NonNull
    @ColumnInfo(name = "CosplayEndDate")
    public String mCosplayEndDate;
    @NonNull
    @ColumnInfo(name = "CosplayBudget")
    public double mCosplayBudget;
    @NonNull
    @ColumnInfo(name = "CosplayIMG")
    public Uri mCosplayIMG;

    public String getCosplayName(){return this.mCosplayName;}
    public String getCosplayStartDate(){return this.mCosplayStartDate;}
    public String getCosplayEndDate(){return this.mCosplayEndDate;}
    public double getCosplayBudget(){return this.mCosplayBudget;}
    public Uri getCosplayIMG(){return this.mCosplayIMG;}
}
