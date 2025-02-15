package com.p_branch.todoapp.repository;

import android.content.Context;
import android.util.Log;

import com.p_branch.todoapp.repository.database.DbAccessor;
import com.p_branch.todoapp.repository.database.TaskDao;
import com.p_branch.todoapp.repository.database.TaskEntity;
import com.p_branch.todoapp.util.TodoAppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * タスクリポジトリ
 */
public class TaskRepository {
    private final String TAG = this.getClass().getSimpleName();
    // データアクセスクラス
    private final TaskDao dao;

    public TaskRepository(Context context) {
        this.dao = DbAccessor.getInstance(context).getTaskDao();
    }

    /**
     * タスクをDBに登録する
     * @param taskInfo タスク
     * @return DBで自動採番されたID
     */
    public Long insertTask(TaskInfo taskInfo) {
        TaskEntity entity = TodoAppUtil.convertTaskInfoToTaskEntity(taskInfo);
        Long id = dao.insertTask(entity);
        Log.i(TAG, "insertTask insert id = " + id);
        return id;
    }

    public void updateTask(TaskInfo taskInfo) {
        TaskEntity entity = TodoAppUtil.convertTaskInfoToTaskEntity(taskInfo);
        dao.updateTask(entity);
        Log.i(TAG, "updateTask update id = " + taskInfo.getId());
    }

    public void deleteTask(Long id) {
        Log.i(TAG, "saveTask delete id = " + id);
        TaskEntity task = dao.getTask(id);
        if (task != null) {
            dao.delete(task);
        }
    }

    public ArrayList<TaskInfo> getAllTaskInfo() {
        Log.i(TAG, "getAllTaskInfo");
        ArrayList<TaskInfo> taskInfoList = new ArrayList<>();
        List<TaskEntity> taskEntityList = dao.getAllTask();
        for (TaskEntity taskEntity: taskEntityList) {
            taskInfoList.add(TodoAppUtil.convertTaskEntityToTaskInfo(taskEntity));
        }
        return taskInfoList;
    }
}
