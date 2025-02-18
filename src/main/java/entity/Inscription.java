package entity;

import java.sql.Date;
import java.time.LocalDate;

public class Inscription {
    private int id;
    private LocalDate dateRegistration;
    private String status;
    Formation formation;
    Employee employee;
    public Inscription(int id, LocalDate date_registration, String status, Formation formation, Employee employee) {
        this.id = id;
        this.dateRegistration = date_registration;
        this.status = status;
        this.formation = formation;
        this.employee = employee;
    }

    public Inscription(LocalDate date_registration, String status, Formation formation, Employee employee) {
        this.dateRegistration = date_registration;
        this.status = status;
        this.formation = formation;
        this.employee = employee;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public LocalDate getDateRegistration() {
        return dateRegistration;
    }
    public void setDate_registration(LocalDate date_registration) {
        this.dateRegistration = date_registration;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Formation getFormation() {
        return formation;
    }
    public void setFormation(Formation formation) {
        this.formation = formation;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Inscription {" +
                "id = " + id +
                ", date_inscription = " + dateRegistration +
                ", statut = '" + status + '\'' +
                ", formation = " + formation +
                ", employee = " + employee +
                "}\n";
    }
}
