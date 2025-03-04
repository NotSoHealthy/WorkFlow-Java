package entity;

import java.sql.Date;
import java.sql.Time;

public class Reclamation {



        private int reclamation_ID;
        private String title;
        private String description;
        private String category;
        private String type;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttachedfile() {
        return attachedfile;
    }

    public void setAttachedfile(String attachedfile) {
        this.attachedfile = attachedfile;
    }

    private String attachedfile;
        private java.sql.Date date;
        private java.sql.Time heure;
        private String etat;
        private java.sql.Date date_resolution;
        private Employee responsable;
        private Employee employee;


    public Reclamation(String title, String description, String category, String type, String attachedfile, Date date, Time heure, String etat, Date date_resolution, Employee responsable, Employee employee) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.type = type;
        this.attachedfile = attachedfile;
        this.date = date;
        this.heure = heure;
        this.etat = etat;
        this.date_resolution = date_resolution;
        this.responsable = responsable;
        this.employee = employee;
    }


    public Reclamation(int id,String title, String description, String category, String type, String attachedfile, Date date, Time heure, String etat, Date date_resolution, Employee responsable, Employee employee) {
        this.reclamation_ID = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.type = type;
        this.attachedfile = attachedfile;
        this.date = date;
        this.heure = heure;
        this.etat = etat;
        this.date_resolution = date_resolution;
        this.responsable = responsable;
        this.employee = employee;
    }

    public Reclamation(int reclamation_ID, String title, String description, Date date, Time heure, String etat, Date date_resolution, Employee responsable, Employee employee) {
        this.reclamation_ID = reclamation_ID;
        this.title = title;
        this.description = description;
        this.date = date;
        this.heure = heure;
        this.etat = etat;
        this.date_resolution = date_resolution;
        this.responsable = responsable;
        this.employee = employee;
    }

    public Reclamation(String title, String description, Date date, Time heure, String etat, Date date_resolution, Employee responsable, Employee employee) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.heure = heure;
        this.etat = etat;
        this.date_resolution = date_resolution;
        this.responsable = responsable;
        this.employee = employee;
    }



    public Employee getEmployee() {return employee;}
    public Employee getResponsable() {return responsable;}
    public Date getDate_resolution() {return date_resolution;}
    public String getEtat() {return etat;}
    public Time getHeure() {return heure;}
    public Date getDate() {return date;}
    public String getDescription() {return description;}
    public String getTitle() {return title;}
    public int getReclamation_ID() {return reclamation_ID;}

    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setDate(Date date) {this.date = date;}
    public void setHeure(Time heure) {this.heure = heure;}
    public void setEtat(String etat) {this.etat = etat;}
    public void setDate_resolution(Date date_resolution) {this.date_resolution = date_resolution;}
    public void setResponsable(Employee responsable) {this.responsable = responsable;}
    public void setEmployee(Employee employee) {this.employee = employee;}

    @Override
    public String toString() {
        return "Reclamation{" +
                "reclamation_ID=" + reclamation_ID +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", heure=" + heure +
                ", etat='" + etat + '\'' +
                ", date_resolution=" + date_resolution +
                ", responsable=" + responsable +
                ", employee=" + employee +
                "}\n";
    }
}


