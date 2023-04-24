package com.softwareengineeringproject.studymonster.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "flashcards",
        indices = { @Index(value = { "term", "collection_id" }, unique = true) })
public class FlashcardEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "term")
    public String term;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "collection_id")
    public int collectionId;
}
