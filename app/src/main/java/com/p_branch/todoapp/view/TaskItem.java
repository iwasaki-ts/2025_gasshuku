package com.p_branch.todoapp.view;

public class TaskItem {
    Long id;
    private String title;
    private String endDate;
    private int progress;
    private int priority;

    public TaskItem(long id, String title, String endDate, int progress, int priority) {
        this.id = id;
        this.title = title;
        this.endDate = endDate;
        this.progress = progress;
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getEndDate() {
        return endDate;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getPriority() {
        return priority;
    }
}
