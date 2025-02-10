package entity;

import java.sql.Date;

public class Inscription {
    private int id;
    private java.sql.Date date_registration;
    private String status;
    Formation formation;
    Employee employee;
    public Inscription(int id, java.sql.Date date_registration, String status, Formation formation, Employee employee) {
        this.id = id;
        this.date_registration = date_registration;
        this.status = status;
        this.formation = formation;
        this.employee = employee;
    }

    public Inscription(Date date_registration, String status, Formation formation, Employee employee) {
        this.date_registration = date_registration;
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
    public java.sql.Date getDate_registration() {
        return date_registration;
    }
    public void setDate_registration(java.sql.Date date_registration) {
        this.date_registration = date_registration;
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
        return "Inscription{" +
                "id = " + id +
                ", date_inscription = " + date_registration +
                ", statut = '" + status + '\'' +
                ", formation = " + formation +
                ", employee = " + employee +
                "}\n";
    }
}
