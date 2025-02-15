package com.p_branch.todoapp.repository.database;

import android.content.Context;

import androidx.room.Room;

/**
 * データベースアクセス用クラス
 */
public class DbAccessor {
    private static DbAccessor instance = null;
    private final AppDatabase db;

    private DbAccessor(Context context) {
        this.db = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
    }

    public static DbAccessor getInstance(Context context) {
        if (instance == null) {
            instance = new DbAccessor(context);
        }
        return instance;
    }

    public TaskDao getTaskDao() {
        return db.getTaskDao();
    }
}
