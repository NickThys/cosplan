package com.example.cosplan.data.cosplay.checkList;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.cosplan.data.cosplay.Cosplay;

@Entity(tableName = "CosplayChecklist_table",foreignKeys = @ForeignKey(onDelete = ForeignKey.CASCADE,entity = Cosplay.class,
        parentColumns = "Id",
        childColumns = "CosplayId"))
public class ChecklistPart {
    @ColumnInfo(name = "CosplayId",index = true)
    public int mCosplayId;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "CosplayCheckListPartId")
    public int mCosplayCheckListPartId;
    @ColumnInfo(name = "CosplayCheckListPartName")
    public String mCosplayCheckListPartName;
    @ColumnInfo(name = "CosplayCheckListPartChecked")
    public boolean mCosplayCheckListPartChecked;

    @Ignore
    public ChecklistPart(){}

    public ChecklistPart(int mCosplayId, int mCosplayCheckListPartId, String mCosplayCheckListPartName, boolean mCosplayCheckListPartChecked) {
        this.mCosplayId = mCosplayId;
        this.mCosplayCheckListPartId = mCosplayCheckListPartId;
        this.mCosplayCheckListPartName = mCosplayCheckListPartName;
        this.mCosplayCheckListPartChecked = mCosplayCheckListPartChecked;
    }
}
