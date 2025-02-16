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
     * レコードをDBに登録する
     * @param taskInfo タスク情報
     * @return DBで自動採番されたID
     */
    public Long insertTask(TaskInfo taskInfo) {
        TaskEntity entity = TodoAppUtil.convertTaskInfoToTaskEntity(taskInfo);
        Long id = dao.insertTask(entity);
        Log.i(TAG, "insertTask insert id = " + id);
        return id;
    }

    /**
     * DBのレコードを更新
     * @param taskInfo タスク情報
     */
    public void updateTask(TaskInfo taskInfo) {
        /**
         * ---------------------------
         * 課題② DBへのタスクの登録 / 更新 / 削除を実装する
         * ---------------------------
         */
        //  --- 引数のタスク情報をタスクエンティティに変換する ---
        //  --- DAOの更新メソッドを呼び出す ---
    }

    /**
     * DBのレコードを削除
     * @param id ID
     */
    public void deleteTask(Long id) {
        /**
         * ---------------------------
         * 課題② DBへのタスクの登録 / 更新 / 削除を実装する
         * ---------------------------
         */
        //  --- DBから対象のIDでタスクエンティティを取得する ---
        //  --- タスクエンティティが存在する場合は、DAOの削除メソッドを呼び出す ---
    }

    /**
     * DBのレコードを全件取得する
     * @return タスク情報リスト
     */
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
