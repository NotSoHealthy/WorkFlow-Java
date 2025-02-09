package entity;

import java.util.Date;

public class Formation {
    private int formation_ID;
    private String titre;
    private String description;
    private java.sql.Date date_Debut;
    private java.sql.Date date_Fin;
    private int participants_Max;

    public Formation(String titre, String description, java.sql.Date date_Debut, java.sql.Date date_Fin, int participants_Max) {
        this.titre = titre;
        this.description = description;
        this.date_Debut = date_Debut;
        this.date_Fin = date_Fin;
        this.participants_Max = participants_Max;
    }

    public Formation(int formation_ID, String titre, String description, java.sql.Date date_Debut, java.sql.Date date_Fin, int participants_Max) {
        this.formation_ID = formation_ID;
        this.titre = titre;
        this.description = description;
        this.date_Debut = date_Debut;
        this.date_Fin = date_Fin;
        this.participants_Max = participants_Max;
    }

    public int getFormation_ID() {
        return formation_ID;
    }
    public void setFormation_ID(int formation_ID) {
        this.formation_ID = formation_ID;
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
    public void setDescription(String Description) {
        this.description = Description;
    }
    public java.sql.Date getDate_Debut() {
        return date_Debut;
    }
    public void setDate_Debut(java.sql.Date Date_Debut) {
        this.date_Debut = Date_Debut;
    }
    public java.sql.Date getDate_Fin() {
        return date_Fin;
    }
    public void setDate_Fin(java.sql.Date Date_Fin) {
        this.date_Fin = Date_Fin;
    }
    public int getParticipants_Max() {
        return participants_Max;
    }
    public void setParticipants_Max(int Participants_Max) {
        this.participants_Max = Participants_Max;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "formation_ID=" + formation_ID +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date_Debut='" + date_Debut + '\'' +
                ", date_Fin='" + date_Fin + '\'' +
                ", participants_Max=" + participants_Max +
                "}\n";
    }
}
