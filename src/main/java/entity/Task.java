package entity;

import java.sql.Date;
import java.sql.Timestamp;

public class Task {
    private int taskId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Date startDate;
    private Date dueDate;
    private Date completionDate;
    private Employee assignedTo;  // Employee is in the same package
    private Project project;      // Project is in the same package
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Task() {
    }

    // Parameterized constructor (without IDs, for creating new tasks)
    public Task(String title, String description, String status, String priority, Date startDate, Date dueDate,
                Date completionDate, Employee assignedTo, Project project) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
        this.assignedTo = assignedTo;
        this.project = project;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // Parameterized constructor (with IDs, for existing tasks)
    public Task(int taskId, String title, String description, String status, String priority, Date startDate,
                Date dueDate, Date completionDate, Employee assignedTo, Project project,
                Timestamp createdAt, Timestamp updatedAt) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.completionDate = completionDate;
        this.assignedTo = assignedTo;
        this.project = project;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public Employee getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", startDate=" + startDate +
                ", dueDate=" + dueDate +
                ", completionDate=" + completionDate +
                ", assignedTo=" + (assignedTo != null ? assignedTo.getId() : "null") +
                ", project=" + (project != null ? project.getProject_id() : "null") +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}