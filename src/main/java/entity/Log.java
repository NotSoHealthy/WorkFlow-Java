package entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Log {
    private int id;
    private Employee employee;
    private String action;
    private String description;
    private LocalDateTime timestamp;

    public Log(Employee employee, String action, String description, LocalDateTime timestamp) {
        this.employee = employee;
        this.action = action;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Log(int id, Employee employee, String action, String description, LocalDateTime timestamp) {
        this.id = id;
        this.employee = employee;
        this.action = action;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Log(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", employee=" + employee +
                ", action='" + action + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
