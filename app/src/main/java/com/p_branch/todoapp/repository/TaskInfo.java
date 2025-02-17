package com.p_branch.todoapp.repository;

/**
 * タスク情報
 */
public class TaskInfo {
    // ID
    private long id = 0L;
    // タイトル
    private String title;
    // 期日
    private String endDate;
    // 達成度
    private int progress;
    // 優先度
    private int priority;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
