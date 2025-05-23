package entity;

public class Reservation {
    int id;
    double price;
    String type;
    int NombreDePlaces;
    String qr_url;
    Employee employee;
    Event e;
    public Reservation() {}
    public Reservation(int id, double price, String type, int NombreDePlaces,Employee employee, Event e) {
        this.id = id;
        this.price = price;
        this.type = type;
        this.NombreDePlaces = NombreDePlaces;
        this.employee = employee;
        this.e = e;
    }
    public Reservation(int id, double price, String type, int NombreDePlaces,String qr_url,Employee employee, Event e) {
        this.id = id;
        this.price = price;
        this.type = type;
        this.NombreDePlaces = NombreDePlaces;
        this.qr_url = qr_url;
        this.employee = employee;
        this.e = e;
    }
    public Reservation(double price, String type, int NombreDePlaces ,Employee employee, Event e) {
        this.price = price;
        this.type = type;
        this.NombreDePlaces = NombreDePlaces;
        this.employee = employee;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Event getEvent() {
        return e;
    }

    public void setEvent(Event e) { this.e = e; }

    public String getQr_url() {
        return qr_url;
    }

    public void setQr_url(String qr_url) {
        this.qr_url = qr_url;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", NombreDePlaces=" + NombreDePlaces +
                ", UID=" + employee +
                ", Event=" + e +
                '}';
    }
}
