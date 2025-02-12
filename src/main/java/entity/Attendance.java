package entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private int id;
    private Employee employee;
    private LocalDate date;
    private LocalTime entry_time;
    private LocalTime exit_time;

    public Attendance(int id, Employee employee, LocalDate date, LocalTime entry_time, LocalTime exit_time) {
        this.id = id;
        this.employee = employee;
        this.date = date;
        this.entry_time = entry_time;
        this.exit_time = exit_time;
    }

    public Attendance(Employee employee, LocalDate date, LocalTime entry_rime, LocalTime exit_time) {
        this.employee = employee;
        this.date = date;
        this.entry_time = entry_rime;
        this.exit_time = exit_time;
    }

    public Attendance(int id){
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(LocalTime entry_rime) {
        this.entry_time = entry_rime;
    }

    public LocalTime getExit_time() {
        return exit_time;
    }

    public void setExit_time(LocalTime exit_time) {
        this.exit_time = exit_time;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", employee=" + employee +
                ", date=" + date +
                ", entry_rime=" + entry_time +
                ", exit_time=" + exit_time +
                '}';
    }
}
