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

public class ServiceDepartment implements IService<Department> {
    private Connection con;
    public ServiceDepartment() {
        con = DBConnection.getInstance().getConnection();
    }
    @Override
    public void add(Department department) throws SQLException {
        if (con==null){
            System.out.println("connection Error");
        }
        String query = "insert into departments (Name, Year_Budget, Department_Manager, Projects) values(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, department.getName());
        ps.setFloat(2, department.getYear_Budget());
        ps.setInt(3, department.getDepartment_Manager().getId());
        ps.setInt(4, department.getProjects().getProject_id());


        int r = ps.executeUpdate();
        ps.close();
        System.out.println(r + " rows affected");

    }

    @Override
    public void update(Department department) throws SQLException {
        String query = "update departments set Name = ?, Year_Budget = ?, Department_Manager = ?, Projects = ? where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, department.getName());
        ps.setString(2, String.valueOf(department.getYear_Budget()));
        ps.setString(3, String.valueOf(department.getDepartment_Manager().getId()));
        ps.setString(4, String.valueOf(department.getProjects().getProject_id()));
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void delete(Department department) throws SQLException {
        String query = "delete from departments where Department_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, department.getDepartment_id());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Department> readAll() throws SQLException {
        String query = "select * from departments";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Department> departments = new ArrayList<>();
        while (rs.next()) {
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            ServiceProject  serviceProject = new ServiceProject();
            Employee employee = serviceEmployee.readById(rs.getInt("Department_Manager"));
            Project project = serviceProject.readById(rs.getInt("Projects"));
            departments.add(new Department(rs.getInt("Department_Id"), rs.getString("Name"), rs.getFloat("Year_Budget"),
                    employee, project));
        }
        return departments;
    }

    @Override
    public Department readById(int id) throws SQLException {
        String query = "select * from departments where Department_Id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            ServiceProject  serviceProject = new ServiceProject();
            Employee employee = serviceEmployee.readById(rs.getInt("Department_Manager"));
            Project project = serviceProject.readById(rs.getInt("Projects"));
            return new Department(id, rs.getString("Name"), rs.getFloat("Year_Budget"),
                    employee, project);
        }
        return null;
    }
}
