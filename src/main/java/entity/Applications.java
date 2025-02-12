package entity;

import java.util.Date;

public class Applications {
    private int applicationId;
    private JobOffer jobId;
    private Employee employeeId;
    private String cv;
    private String coverLetter;
    private Date submissionDate;
    private String status;

    // Constructors
    public Applications(int applicationId, JobOffer jobId, Employee employeeId, String cv, String coverLetter, Date submissionDate, String status) {
        this.applicationId = applicationId;
        this.jobId = jobId;
        this.employeeId = employeeId;
        this.cv = cv;
        this.coverLetter = coverLetter;
        this.submissionDate = submissionDate;
        this.status = status;
    }

    public Applications(JobOffer jobId, Employee employeeId, String cv, String coverLetter, Date submissionDate, String status) {
        this.jobId = jobId;
        this.employeeId = employeeId;
        this.cv = cv;
        this.coverLetter = coverLetter;
        this.submissionDate = submissionDate;
        this.status = status;
    }

    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public JobOffer getJobId() {
        return jobId;
    }

    public void setJobId(JobOffer jobId) {
        this.jobId = jobId;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", jobId=" + jobId +
                ", employeeId=" + employeeId +
                ", cv='" + cv + '\'' +
                ", coverLetter='" + coverLetter + '\'' +
                ", submissionDate=" + submissionDate +
                ", status='" + status + '\'' +
                '}';
    }
}
