package entity;

import java.sql.Date;
import java.sql.Time;

public class Message {

    private int message_ID;
    private String contenu;
    private java.sql.Date date;
    private java.sql.Time heure;
    private Reclamation reclamation;
    private Employee user;

    public Message( String contenu,Date date, Time heure,Reclamation reclamation,Employee user) {
        this.user = user;
        this.reclamation = reclamation;
        this.heure = heure;
        this.date = date;
        this.contenu = contenu;
    }

    public Message(int message_ID, String contenu, Date date, Time heure, Reclamation reclamation, Employee user) {
        this.message_ID = message_ID;
        this.contenu = contenu;
        this.date = date;
        this.heure = heure;
        this.reclamation = reclamation;
        this.user = user;
    }

    public int getMessage_ID() {return message_ID;}
    public String getContenu() {return contenu;}
    public Date getDate() {return date;}
    public Time getHeure() {return heure;}
    public Reclamation getReclamation() {return reclamation;}
    public Employee getUser() {return user;}

    public void setContenu(String contenu) {this.contenu = contenu;}
    public void setDate(Date date) {this.date = date;}
    public void setHeure(Time heure) {this.heure = heure;}
    public void setReclamation(Reclamation reclamation) {this.reclamation = reclamation;}
    public void setUser(Employee user) {this.user = user;}

    @Override
    public String toString() {
        return "Message{" +
                "message_ID=" + message_ID +
                ", contenu='" + contenu + '\'' +
                ", date=" + date +
                ", heure=" + heure +
                ", reclamation=" + reclamation +
                ", user=" + user +
                "}\n";
    }
}
