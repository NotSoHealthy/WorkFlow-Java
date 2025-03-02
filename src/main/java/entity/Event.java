package entity;

import java.time.LocalDateTime;

public class Event {
    int id;
    String titre;
    String description;
    LocalDateTime dateetheure;
    String lieu;
    String type;
    int nombredeplace;
    boolean isOnline;
    Employee e;
    public Event() {}
    public Event(int id,String titre,String description,LocalDateTime dateetheure,String lieu,String type,int nombredeplace,boolean isOnline,Employee e) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateetheure = dateetheure;
        this.lieu = lieu;
        this.type = type;
        this.nombredeplace = nombredeplace;
        this.isOnline = isOnline;
        this.e = e;
    }
    public Event(String titre,String description,LocalDateTime dateetheure,String lieu,String type,int nombredeplace,boolean isOnline,Employee e) {
        this.titre = titre;
        this.description = description;
        this.dateetheure = dateetheure;
        this.lieu = lieu;
        this.type = type;
        this.nombredeplace = nombredeplace;
        this.isOnline = isOnline;
        this.e = e;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateetheure() {
        return dateetheure;
    }

    public void setDateetheure(LocalDateTime dateetheure) {
        this.dateetheure = dateetheure;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNombredeplace() {
        return nombredeplace;
    }

    public void setNombredeplace(int nombredeplace) {
        this.nombredeplace = nombredeplace;
    }

    public Employee getEmployee() {
        return e;
    }

    public void setEmployee(Employee e) {
        this.e = e;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateetheure=" + dateetheure +
                ", lieu='" + lieu + '\'' +
                ", type='" + type + '\'' +
                ", nombredeplace=" + nombredeplace +
                ", Employee=" + e +
                '}';
    }
}
