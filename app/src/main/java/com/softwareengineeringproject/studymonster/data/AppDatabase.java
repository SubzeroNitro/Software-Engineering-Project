package com.softwareengineeringproject.studymonster.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = { FlashcardCollectionEntity.class, FlashcardEntity.class },
        version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FlashcardCollectionDAO FlashcardCollectionDAO();
    public abstract FlashcardDAO FlashcardDAO();

    public static synchronized AppDatabase GetInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
        }

        return instance;
    }

    private static final String DATABASE_NAME = "studymonster_db";
    private static AppDatabase instance;
}
