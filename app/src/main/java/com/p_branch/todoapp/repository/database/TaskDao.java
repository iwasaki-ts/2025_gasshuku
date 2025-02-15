package com.p_branch.todoapp.repository.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * タスクDAO
 */
@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insertTask(TaskEntity task);

    @Update
    void updateTask(TaskEntity task);

    @Delete
    void delete(TaskEntity task);

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskEntity getTask(Long id);

    @Query("SELECT * FROM tasks")
    List<TaskEntity> getAllTask();
}
