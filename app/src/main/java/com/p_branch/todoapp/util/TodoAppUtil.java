package com.p_branch.todoapp.util;

import com.p_branch.todoapp.repository.TaskInfo;
import com.p_branch.todoapp.repository.database.TaskEntity;
import com.p_branch.todoapp.view.TaskItem;

public class TodoAppUtil {

    /**
     * 引数のタスクアイテムをタスク情報に変換する
     *
     * @param taskInfo タスク情報
     * @return taskItem タスクアイテム
     */
    public static TaskItem convertTaskInfoToTaskItem(TaskInfo taskInfo) {
        return new TaskItem(
                taskInfo.getId(),
                taskInfo.getTitle(),
                taskInfo.getEndDate(),
                taskInfo.getProgress(),
                taskInfo.getPriority()
        );
    }

    /**
     * 引数のタスクアイテムをタスク情報に変換する
     *
     * @param taskItem タスクアイテム
     * @return taskInfo タスク情報
     */
    public static TaskInfo convertTaskItemToTaskInfo(TaskItem taskItem) {
        return new TaskInfo(
                taskItem.getId(),
                taskItem.getTitle(),
                taskItem.getEndDate(),
                taskItem.getProgress(),
                taskItem.getPriority()
        );
    }

    /**
     * 引数のタスク情報をタスクエンティティに変換する
     *
     * @param taskInfo タスク情報
     * @return entity タスクエンティティ
     */
    public static TaskEntity convertTaskInfoToTaskEntity(TaskInfo taskInfo) {
        TaskEntity entity = new TaskEntity();
        entity.id = taskInfo.getId();
        entity.title = taskInfo.getTitle();
        entity.endDate = taskInfo.getEndDate();
        entity.progress = taskInfo.getProgress();
        entity.priority = taskInfo.getPriority();
        return entity;
    }

    /**
     * 引数のタスクエンティティをタスク情報に変換する
     *
     * @param taskEntity タスクエンティティ
     * @return taskInfo タスク情報
     */
    public static TaskInfo convertTaskEntityToTaskInfo(TaskEntity taskEntity) {
        return new TaskInfo(
                taskEntity.id,
                taskEntity.title,
                taskEntity.endDate,
                taskEntity.progress,
                taskEntity.priority
        );
    }
}
