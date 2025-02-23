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
        String query = "INSERT INTO projects (Name, Description, Start_Date, End_Date, Budget, Project_Manager, Department_Id, State) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

    @Override
    public void update(Project project) throws SQLException {
        String query = "UPDATE projects SET Name = ?, Description = ?, Start_Date = ?, End_Date = ?, Budget = ?, Project_Manager = ?, Department_Id = ?, State = ? WHERE Project_Id = ?";
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
        String query = "DELETE FROM projects WHERE Project_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, project.getProject_id());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Project> readAll() throws SQLException {
        String query = "SELECT * FROM projects";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            ServiceDepartment serviceDepartment = new ServiceDepartment();
            Employee employee = serviceEmployee.readById(rs.getInt("Project_Manager"));
            Department department = serviceDepartment.readById(rs.getInt("Department_Id"));
            projects.add(new Project(rs.getInt("Project_Id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    @Override
    public Project readById(int id) throws SQLException {
        String query = "SELECT * FROM projects WHERE Project_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            ServiceDepartment serviceDepartment = new ServiceDepartment();
            Employee employee = serviceEmployee.readById(rs.getInt("Project_Manager"));
            Department department = serviceDepartment.readById(rs.getInt("Department_Id"));
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
        String req = "SELECT * FROM projects WHERE Name = ?"; // Fixed query to use a single parameter
        PreparedStatement ps = con.prepareStatement(req);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department_Id");
            int Project_Manager = rs.getInt("Project_Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("Project_Id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    public List<Project> searchByDate(int year) throws SQLException {
        String req = "SELECT * FROM projects WHERE YEAR(Start_Date) = ? OR YEAR(End_Date) = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, year);
        ps.setInt(2, year); // Fixed missing parameter
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department_Id");
            int Project_Manager = rs.getInt("Project_Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("Project_Id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    public List<Project> sortDate() throws SQLException {
        String req = "SELECT * FROM projects ORDER BY Start_Date ASC";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department_Id");
            int Project_Manager = rs.getInt("Project_Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("Project_Id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    public List<Project> sortState() throws SQLException {
        String req = "SELECT * FROM projects ORDER BY State ASC"; // Now valid with State column
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department_Id");
            int Project_Manager = rs.getInt("Project_Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("Project_Id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }

    public List<Project> sortBudget() throws SQLException {
        String req = "SELECT * FROM projects ORDER BY Budget DESC";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Project> projects = new ArrayList<>();
        while (rs.next()) {
            int Department_Id = rs.getInt("Department_Id");
            int Project_Manager = rs.getInt("Project_Manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Project_Manager);
            ServiceDepartment s = new ServiceDepartment();
            Department department = s.readById(Department_Id);
            projects.add(new Project(rs.getInt("Project_Id"), rs.getString("Name"), rs.getString("Description"),
                    rs.getDate("Start_Date"), rs.getDate("End_Date"), rs.getFloat("Budget"),
                    employee, department, rs.getString("State")));
        }
        ps.close();
        return projects;
    }
}