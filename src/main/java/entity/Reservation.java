package entity;

public class Reservation {
    int id;
    double price;
    String type;
    int NombreDePlaces;
    int UID;
    Event e;
    public Reservation() {}
    public Reservation(int id, double price, String type, int NombreDePlaces,int UID, Event e) {
        this.id = id;
        this.price = price;
        this.type = type;
        this.NombreDePlaces = NombreDePlaces;
        this.UID = UID;
        this.e = e;
    }
    public Reservation(double price, String type, int NombreDePlaces ,int UID, Event e) {
        this.price = price;
        this.type = type;
        this.NombreDePlaces = NombreDePlaces;
        this.UID = UID;
        this.e = e;
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

    public Event getEvent() {
        return e;
    }

    public void setEvent(Event e) { this.e = e; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", NombreDePlaces=" + NombreDePlaces +
                ", UID=" + UID +
                ", Event=" + e +
                '}';
    }
}
