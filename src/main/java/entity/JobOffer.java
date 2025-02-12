package entity;

import java.util.Date;

public class JobOffer {
    private int jobId;
    private String title;
    private String description;
    private Date publicationDate;
    private Date expirationDate;
    private String contractType;
    private double salary;
    private Employee employeeId;

    // Constructors
    public JobOffer(int jobId, String title, String description, Date publicationDate, Date expirationDate, String contractType, double salary, Employee employeeId) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.expirationDate = expirationDate;
        this.contractType = contractType;
        this.salary = salary;
        this.employeeId = employeeId;
    }

    public JobOffer(String title, String description, Date publicationDate, Date expirationDate, String contractType, double salary, Employee employeeId) {
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.expirationDate = expirationDate;
        this.contractType = contractType;
        this.salary = salary;
        this.employeeId = employeeId;
    }

    // Getters and Setters
    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "JobOffer{" +
                "jobId=" + jobId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publicationDate=" + publicationDate +
                ", expirationDate=" + expirationDate +
                ", contractType='" + contractType + '\'' +
                ", salary=" + salary +
                ", employeeId=" + employeeId +
                '}';
    }
}
