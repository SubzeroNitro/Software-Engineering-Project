package com.softwareengineeringproject.studymonster.data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.io.Serializable;

@Entity(
        tableName = "flashcardCollections",
        indices = { @Index(value = "name", unique = true) })
public class FlashcardCollectionEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;
}
