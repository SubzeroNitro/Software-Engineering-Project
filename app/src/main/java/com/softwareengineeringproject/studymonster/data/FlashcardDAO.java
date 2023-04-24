package com.softwareengineeringproject.studymonster.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FlashcardDAO {
    @Query("SELECT * FROM flashcards")
    List<FlashcardEntity> GetFlashcards();

    @Query("SELECT * FROM flashcards WHERE collection_id IS :collectionId")
    List<FlashcardEntity> GetFlashcardsInCollection(long collectionId);

    @Query("SELECT * FROM flashcards WHERE term = :term AND collection_id = :collectionId LIMIT 1")
    FlashcardEntity GetFlashcardByTerm(String term, long collectionId);

    @Insert
    long InsertFlashcard(FlashcardEntity flashcard);

    @Update
    void UpdateFlashcard(FlashcardEntity flashcard);

    @Query("DELETE FROM flashcards WHERE id = :id")
    void DeleteFlashcard(long id);

    @Query("DELETE FROM flashcards WHERE collection_id = :collectionId")
    void DeleteFlashcardByCollectionId(long collectionId);
}
