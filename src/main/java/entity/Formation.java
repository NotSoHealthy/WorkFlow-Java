package entity;



public class Formation {
    private int formation_ID;
    private String title;
    private String description;
    private java.sql.Date date_begin;
    private java.sql.Date date_end;
    private int participants_Max;

    public Formation(String title, String description, java.sql.Date date_begin, java.sql.Date date_end, int participants_Max) {
        this.title = title;
        this.description = description;
        this.date_begin = date_begin;
        this.date_end = date_end;
        this.participants_Max = participants_Max;
    }

    public Formation(int formation_ID, String title, String description, java.sql.Date date_begin, java.sql.Date date_end, int participants_Max) {
        this.formation_ID = formation_ID;
        this.title = title;
        this.description = description;
        this.date_begin = date_begin;
        this.date_end = date_end;
        this.participants_Max = participants_Max;
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
    public java.sql.Date getDate_Begin() {
        return date_begin;
    }
    public void setDate_Begin(java.sql.Date date_begin) {
        this.date_begin = date_begin;
    }
    public java.sql.Date getDate_End() {
        return date_end;
    }
    public void setDate_Fin(java.sql.Date date_end) {
        this.date_end = date_end;
    }
    public int getParticipants_Max() {
        return participants_Max;
    }
    public void setParticipants_Max(int Participants_Max) {
        this.participants_Max = Participants_Max;
    }

    @Override
    public String toString() {
        return "Formation {" +
                "formation_ID=" + formation_ID +
                ", titre='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date_Debut='" + date_begin + '\'' +
                ", date_Fin='" + date_end + '\'' +
                ", participants_Max=" + participants_Max +
                "}\n";
    }
}
