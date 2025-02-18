package entity;

public class Department {
    private int Department_id;
    private String Name;
    private float Year_Budget;
    private Employee Department_Manager;

    public Department(String name, float year_Budget, Employee department_Manager) {
        Name = name;
        Year_Budget = year_Budget;
        Department_Manager = department_Manager;
    }

    public Department(int department_id, String name, float year_Budget, Employee department_Manager) {
        Department_id = department_id;
        Name = name;
        Year_Budget = year_Budget;
        Department_Manager = department_Manager;
    }

    public Department(int department_id, String name) {
        Department_id = department_id;
        Name = name;
    }

    public Department() {}

    public int getDepartment_id() {return Department_id;}
    public void setDepartment_id(int department_id) {Department_id = department_id;}
    public String getName() {return Name;}
    public void setName(String name) {Name = name;}
    public float getYear_Budget() {return Year_Budget;}
    public void setYear_Budget(float year_Budget) {Year_Budget = year_Budget;}
    public Employee getDepartment_Manager() {return Department_Manager;}
    public void setDepartment_Manager(Employee department_Manager) {Department_Manager = department_Manager;}

    @Override
    public String toString() {
        return String.format("Nom Du Department:     %s | Budget:     %.2f | Manager:     %s",
                this.getName(),
                this.getYear_Budget(),
                this.getDepartment_Manager().getFirstName());
    }
}
