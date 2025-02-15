package com.p_branch.todoapp.repository.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = TaskEntity.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao getTaskDao();
}