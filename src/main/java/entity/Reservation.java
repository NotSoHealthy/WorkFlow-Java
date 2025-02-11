package entity;

public class Reservation {
    int id;
    double price;
    String type;
    int NombreDePlaces;
    int UID;
    int ID_Event;
    public Reservation() {}
    public Reservation(int id, double price, String type, int NombreDePlaces,int UID, int ID_Event) {
        this.id = id;
        this.price = price;
        this.type = type;
        this.NombreDePlaces = NombreDePlaces;
        this.UID = UID;
        this.ID_Event = ID_Event;
    }
    public Reservation(double price, String type, int NombreDePlaces ,int UID, int ID_Event) {
        this.price = price;
        this.type = type;
        this.NombreDePlaces = NombreDePlaces;
        this.UID = UID;
        this.ID_Event = ID_Event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNombreDePlaces() { return NombreDePlaces; }

    public void setNombreDePlaces(int NombreDePlaces) { this.NombreDePlaces = NombreDePlaces; }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public int getID_Event() {
        return ID_Event;
    }

    public void setID_Event(int ID_Event) {
        this.ID_Event = ID_Event;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", NombreDePlaces=" + NombreDePlaces +
                ", UID=" + UID +
                ", ID_Event=" + ID_Event +
                '}';
    }
}
