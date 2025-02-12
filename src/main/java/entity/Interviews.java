package entity;

import java.util.Date;

public class Interviews {
    private int interviewId;
    private Applications applicationId;
    private Employee employeeId;  // Reference to the employee handling the interview
    private Date interviewDate;
    private String location;
    private String feedback;
    private String status;

    // Constructors
    public Interviews(int interviewId, Applications applicationId, Employee employeeId, Date interviewDate, String location, String feedback, String status) {
        this.interviewId = interviewId;
        this.applicationId = applicationId;
        this.employeeId = employeeId;
        this.interviewDate = interviewDate;
        this.location = location;
        this.feedback = feedback;
        this.status = status;
    }

    public Interviews(Applications applicationId, Employee employeeId, Date interviewDate, String location, String feedback, String status) {
        this.applicationId = applicationId;
        this.employeeId = employeeId;
        this.interviewDate = interviewDate;
        this.location = location;
        this.feedback = feedback;
        this.status = status;
    }

    // Getters and Setters
    public int getInterviewId() {
        return interviewId;
    }

    public void setInterviewId(int interviewId) {
        this.interviewId = interviewId;
    }

    public Applications getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Applications applicationId) {
        this.applicationId = applicationId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "interviewId=" + interviewId +
                ", applicationId=" + applicationId +
                ", employeeId=" + employeeId +
                ", interviewDate=" + interviewDate +
                ", location='" + location + '\'' +
                ", feedback='" + feedback + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
