package entity;


import java.time.LocalDate;

public class Formation {
    private int formation_ID;
    private String title;
    private String description;
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private int participants_Max;
    private Employee employee;

    public Formation(String title, String description, LocalDate date_begin, LocalDate date_end, int participants_Max,Employee employee) {
        this.title = title;
        this.description = description;
        this.dateBegin = date_begin;
        this.dateEnd = date_end;
        this.participants_Max = participants_Max;
        this.employee = employee;
    }

    public Formation(int formation_ID, String title, String description, LocalDate date_begin, LocalDate date_end, int participants_Max, Employee employee) {
        this.formation_ID = formation_ID;
        this.title = title;
        this.description = description;
        this.dateBegin = date_begin;
        this.dateEnd = date_end;
        this.participants_Max = participants_Max;
        this.employee = employee;
    }

    public int getFormation_ID() {
        return formation_ID;
    }
    public void setFormation_ID(int formation_ID) {
        this.formation_ID = formation_ID;
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
    public void setDescription(String Description) {
        this.description = Description;
    }
    public LocalDate getDateBegin() {
        return dateBegin;
    }
    public void setDateBegin(LocalDate date_begin) {
        this.dateBegin = date_begin;
    }
    public LocalDate getDateEnd() {
        return dateEnd;
    }
    public void setDateEnd(LocalDate date_end) {
        this.dateEnd = date_end;
    }
    public int getParticipants_Max() {
        return participants_Max;
    }
    public void setParticipants_Max(int Participants_Max) {
        this.participants_Max = Participants_Max;
    }
    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Formation {" +
                "formation_ID = " + formation_ID +
                ", titre = '" + title + '\'' +
                ", description = '" + description + '\'' +
                ", date_Debut = '" + dateBegin + '\'' +
                ", date_Fin = '" + dateEnd + '\'' +
                ", participants_Max = " + participants_Max +
                ", employee = " + employee +
                "}\n";
    }
}
