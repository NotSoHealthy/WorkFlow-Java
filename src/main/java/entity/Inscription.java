package entity;

import java.sql.Date;

public class Inscription {
    private int id;
    private java.sql.Date date_inscription;
    private String statut;
    Formation formation;
    public Inscription(int id, java.sql.Date date_inscription, String statut, Formation formation) {
        this.id = id;
        this.date_inscription = date_inscription;
        this.statut = statut;
        this.formation = formation;
    }

    public Inscription(Date date_inscription, String statut, Formation formation) {
        this.date_inscription = date_inscription;
        this.statut = statut;
        this.formation = formation;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public java.sql.Date getDate_inscription() {
        return date_inscription;
    }
    public void setDate_inscription(java.sql.Date date_inscription) {
        this.date_inscription = date_inscription;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public Formation getFormation() {
        return formation;
    }
    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    @Override
    public String toString() {
        return "Inscription{" +
                "id=" + id +
                ", date_inscription=" + date_inscription +
                ", statut='" + statut + '\'' +
                ", formation=" + formation +
                "}\n";
    }
}
