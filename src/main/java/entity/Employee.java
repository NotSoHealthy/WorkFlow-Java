package entity;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private Department department;
    private String adresse;
    private String gouvernorat;
    private String imageUrl;
    private String role;
    private String status;
    private String two_factor_secret;

    //All attributes
    public Employee(int id, String firstName, String lastName, String email, String phone, String password, Department department, String adresse, String gouvernorat, String imageUrl, String role, String status, String two_factor_secret) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.department = department;
        this.adresse = adresse;
        this.gouvernorat = gouvernorat;
        this.imageUrl = imageUrl;
        this.role = role;
        this.status = status;
        this.two_factor_secret = two_factor_secret;
    }

    //All attributes except two_factor_secret
    public Employee(int id, String firstName, String lastName, String email, String phone, String password, Department department, String adresse, String gouvernorat, String imageUrl, String role, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.department = department;
        this.adresse = adresse;
        this.gouvernorat = gouvernorat;
        this.imageUrl = imageUrl;
        this.role = role;
        this.status = status;
    }

    //All attributes except id, status and two_factor_secret
    public Employee(String firstName, String lastName, String email, String phone, String password, Department department, String adresse, String gouvernorat, String imageUrl, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.department = department;
        this.adresse = adresse;
        this.gouvernorat = gouvernorat;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    //All attributes except id, department, imageUrl, status and two_factor_secret
    public Employee(String firstName, String lastName, String email, String phone, String password, String adresse, String gouvernorat, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.adresse = adresse;
        this.gouvernorat = gouvernorat;
        this.role = role;
    }

    //Only firstname, lastname, email, phone and two_factor_secret
    public Employee(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    //All attributes except id and two_factor_secret
    public Employee(String firstName, String lastName, String email, String phone, String password, Department department, String adresse, String gouvernorat, String imageUrl, String role, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.department = department;
        this.adresse = adresse;
        this.gouvernorat = gouvernorat;
        this.imageUrl = imageUrl;
        this.role = role;
        this.status = status;
    }

    //All attributes except password and two_factor_secret
    public Employee(int id, String firstName, String lastName, String email, String phone, Department department, String adresse, String gouvernorat, String imageUrl, String role, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.adresse = adresse;
        this.gouvernorat = gouvernorat;
        this.imageUrl = imageUrl;
        this.role = role;
        this.status = status;
    }

    //Only id
    public Employee(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Department getDepartment() {return department;}

    public void setDepartment(Department department) {this.department = department;}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getGouvernorat() {
        return gouvernorat;
    }

    public void setGouvernorat(String gouvernorat) {
        this.gouvernorat = gouvernorat;
    }

    public String getTwo_factor_secret() {
        return two_factor_secret;
    }

    public void setTwo_factor_secret(String two_factor_secret) {
        this.two_factor_secret = two_factor_secret;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", department=" + department +
                ", adresse='" + adresse + '\'' +
                ", gouvernorat='" + gouvernorat + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", two_factor_secret='" + two_factor_secret + '\'' +
                '}';
    }
}
