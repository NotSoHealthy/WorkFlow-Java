package entity;

import java.time.LocalDate;

public class Conge {
    private int id;
    private Employee employee;
    private LocalDate request_date;
    private LocalDate start_date;
    private LocalDate end_date;
    private String reason;
    private String status;

    public Conge(Employee employee, LocalDate request_date, LocalDate start_date, LocalDate end_date, String reason, String status) {
        this.employee = employee;
        this.request_date = request_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.reason = reason;
        this.status = status;
    }

    public Conge(int id, Employee employee, LocalDate request_date, LocalDate start_date, LocalDate end_date, String reason, String status) {
        this.id = id;
        this.employee = employee;
        this.request_date = request_date;
        this.start_date = start_date;
        this.end_date = end_date;
        this.reason = reason;
        this.status = status;
    }

    public Conge(int id){
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getRequest_date() {
        return request_date;
    }

    public void setRequest_date(LocalDate request_date) {
        this.request_date = request_date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Conge{" +
                "id=" + id +
                ", employee=" + employee +
                ", request_date=" + request_date +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
