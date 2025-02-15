package com.p_branch.todoapp.repository.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * タスクエンティティ
 */
@Entity(tableName = "tasks")
public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    public long id = 0L;

    public String title;

    public String endDate;

    public int progress;

    public int priority;
}
