package entity;

import java.util.Date;

public class Project {
    private int Project_id;
    private String Name;
    private String Description;
    private Date Start_Date;
    private Date End_Date;
    private float Budget;
    private Employee Project_Manager;
    private Department Department_id;
    private String State;

    public Project(String name, String description, Date start_Date, Date end_Date, float budget, Employee project_Manager, Department department_id, String state) {
        Name = name;
        Description = description;
        Start_Date = start_Date;
        End_Date = end_Date;
        Budget = budget;
        Project_Manager = project_Manager;
        Department_id = department_id;
        State = state;
    }

    public Project(int project_id, String name, String description, Date start_Date, Date end_Date, float budget, Employee project_Manager, Department department_id, String state) {
        Project_id = project_id;
        Name = name;
        Description = description;
        Start_Date = start_Date;
        End_Date = end_Date;
        Budget = budget;
        Project_Manager = project_Manager;
        Department_id = department_id;
        State = state;
    }

    public Project() {
    }

    public int getProject_id() { return Project_id; }
    public void setProject_id(int project_id) { Project_id = project_id; }
    public String getName() { return Name; }
    public void setName(String name) { Name = name; }
    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }
    public Date getStart_Date() { return Start_Date; }
    public void setStart_Date(Date start_Date) { Start_Date = start_Date; }
    public Date getEnd_Date() { return End_Date; }
    public void setEnd_Date(Date end_Date) { End_Date = end_Date; }
    public float getBudget() { return Budget; }
    public void setBudget(float budget) { Budget = budget; }
    public Employee getProject_Manager() { return Project_Manager; }
    public void setProject_Manager(Employee project_Manager) { Project_Manager = project_Manager; }
    public Department getDepartment_id() { return Department_id; }
    public void setDepartment_id(Department department_id) { Department_id = department_id; }
    public String getState() { return State; }
    public void setState(String state) { State = state; }

    // Convert java.util.Date to java.time.LocalDate for modern date handling
    public java.time.LocalDate getLocalEndDate() {
        return End_Date != null ? new java.sql.Date(End_Date.getTime()).toLocalDate() : null;
    }

    public java.time.LocalDate getLocalStartDate() {
        return Start_Date != null ? new java.sql.Date(Start_Date.getTime()).toLocalDate() : null;
    }

    @Override
    public String toString() {
        return "Project{" +
                "Project_id=" + Project_id +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Start_Date=" + Start_Date +
                ", End_Date=" + End_Date +
                ", Budget=" + Budget +
                ", Project_Manager=" + Project_Manager +
                ", Department_id=" + Department_id +
                ", State='" + State + '\'' +
                '}';
    }
}