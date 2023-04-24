package com.softwareengineeringproject.studymonster.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FlashcardCollectionDAO {
    @Query("SELECT * FROM flashcardCollections")
    List<FlashcardCollectionEntity> GetFlashcardCollections();

    @Query("SELECT * FROM flashcardCollections WHERE name = :name LIMIT 1")
    FlashcardCollectionEntity GetFlashcardCollectionByName(String name);

    @Insert
    long InsertFlashcardCollection(FlashcardCollectionEntity flashcardCollection);

    @Update
    void UpdateFlashcardCollection(FlashcardCollectionEntity flashcardCollection);

    @Query("DELETE FROM flashcardCollections WHERE id = :id")
    void DeleteFlashcardCollection(long id);
}
