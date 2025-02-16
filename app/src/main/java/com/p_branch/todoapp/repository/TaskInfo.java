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
    // 0: 0% / 1:10% / ... / 9: 90% / 10: 100%
    private int progress;
    // 優先度
    // 0: 低 / 1: 中 / 2: 高
    private int priority;

    public TaskInfo(long id, String title, String endDate, int progress, int priority) {
        this.id = id;
        this.title = title;
        this.endDate = endDate;
        this.progress = progress;
        this.priority = priority;
    }

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
