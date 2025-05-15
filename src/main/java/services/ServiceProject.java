package services;

import entity.Department;
import entity.Employee;
import entity.Project;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceProject implements IService<Project> {
    private Connection con;

    public ServiceProject() {
        con = DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(Project project) throws SQLException {
        if (con == null) {
            System.out.println("Connection Error");
            return;
        }
        String query = "INSERT INTO project (Name, Description, Start_Date, End_Date, Budget, Manager, Department, State) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, project.getName());
        ps.setString(2, project.getDescription());
        ps.setDate(3, new java.sql.Date(project.getStart_Date().getTime()));
        ps.setDate(4, new java.sql.Date(project.getEnd_Date().getTime()));
        ps.setFloat(5, project.getBudget());
        ps.setInt(6, project.getProject_Manager().getId());
        ps.setInt(7, project.getDepartment_id().getDepartment_id());
        ps.setString(8, project.getState());
        int r = ps.executeUpdate();
        ps.close();
        System.out.println(r + " rows affected");
    }

    public List<Project> getProjectsByManagerId(int managerId) throws SQLException {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT id, name, description, start_date, end_date, budget, state, Manager, Department " +
                "FROM project WHERE Manager = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, managerId);
            ResultSet rs = pstmt.executeQuery();
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            ServiceDepartment serviceDepartment = new ServiceDepartment();
            while (rs.next()) {
                int projectId = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                Date startDate = rs.getDate("start_date");
                Date endDate = rs.getDate("end_date");
                float budget = rs.getFloat("budget");
                String state = rs.getString("state");
                int managerIdFromDB = rs.getInt("Manager");
                int deptId = rs.getInt("Department");

                // Fetch Employee (Project_Manager) from the database
                Employee manager = serviceEmployee.readById(managerIdFromDB);
                if (manager == null) {
                    manager = new Employee(managerIdFromDB); // Fallback if not found (minimal data)
                }

                // Fetch Department from the database
                Department dept = serviceDepartment.readById(deptId);
                if (dept == null) {
                    dept = new Department(); // Fallback if not found (minimal data)
                }

                projects.add(new Project(projectId, name, description, startDate, endDate, budget, manager, dept, state));
            }
        }
        return projects;
    }

    @Override
    public void update(Project project) throws SQLException {
        String query = "UPDATE project SET Name = ?, Description = ?, Start_Date = ?, End_Date = ?, Budget = ?, Manager = ?, Department = ?, State = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, project.getName());
        ps.setString(2, project.getDescription());
        ps.setDate(3, new java.sql.Date(project.getStart_Date().getTime()));
        ps.setDate(4, new java.sql.Date(project.getEnd_Date().getTime()));
        ps.setFloat(5, project.getBudget());
        ps.setInt(6, project.getProject_Manager().getId());
        ps.setInt(7, project.getDepartment_id().getDepartment_id());
        ps.setString(8, project.getState());
        ps.setInt(9, project.getProject_id());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void delete(Project project) throws SQLException {
        String query = "DELETE FROM project WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, project.getProject_id());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Project> readAll() throws SQLException {
        String query = "SELECT * FROM project";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        ServiceDepartment serviceDepartment = new ServiceDepartment();
        while (rs.next()) {
            Employee employee = serviceEmployee.readById(rs.getInt("Manager"));
            Department department = serviceDepartment.readById(rs.getInt("Department"));
            projects.add(new Project(rs.getInt("id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    @Override
    public Project readById(int id) throws SQLException {
        String query = "SELECT * FROM project WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            ServiceDepartment serviceDepartment = new ServiceDepartment();
            Employee employee = serviceEmployee.readById(rs.getInt("Manager"));
            Department department = serviceDepartment.readById(rs.getInt("Department"));
            Project project = new Project(id, rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State"));
            ps.close();
            return project;
        }
        ps.close();
        return null;
    }

    public List<Project> searchByName(String name) throws SQLException {
        String req = "SELECT * FROM project WHERE Name = ?"; // Fixed query to use a single parameter
        PreparedStatement ps = con.prepareStatement(req);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department");
            int Project_Manager = rs.getInt("Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    public List<Project> searchByDate(int year) throws SQLException {
        String req = "SELECT * FROM project WHERE YEAR(Start_Date) = ? OR YEAR(End_Date) = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, year);
        ps.setInt(2, year); // Fixed missing parameter
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department");
            int Project_Manager = rs.getInt("Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    public List<Project> sortDate() throws SQLException {
        String req = "SELECT * FROM project ORDER BY Start_Date ASC";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department");
            int Project_Manager = rs.getInt("Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    public List<Project> sortState() throws SQLException {
        String req = "SELECT * FROM project ORDER BY State ASC"; // Now valid with State column
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department");
            int Project_Manager = rs.getInt("Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    public List<Project> sortBudget() throws SQLException {
        String req = "SELECT * FROM project ORDER BY Budget DESC";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department");
            int Project_Manager = rs.getInt("Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }
}