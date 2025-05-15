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
        String query = "insert into department (Name, Year_Budget, manager) values(?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, department.getName());
        ps.setFloat(2, department.getYear_Budget());
        ps.setInt(3, department.getDepartment_Manager().getId());

        int r = ps.executeUpdate();
        ps.close();
        System.out.println(r + " rows added");
    }

    @Override
    public void update(Department department) throws SQLException {
        String query = "update department set Name = ?, Year_Budget = ?, manager = ? where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, department.getName());
        ps.setFloat(2, department.getYear_Budget());
        ps.setInt(3, department.getDepartment_Manager().getId());
        ps.setInt(4, department.getDepartment_id()); // Add this line to set the Department_Id
        int r = ps.executeUpdate();
        ps.close();
        System.out.println(r + " rows Updated");
    }

    @Override
    public void delete(Department department) throws SQLException {
        String query = "delete from department where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, department.getDepartment_id());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Department> readAll() throws SQLException {
        String query = "select * from department";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Department> departments = new ArrayList<>();
        while (rs.next()) {
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            ServiceProject  serviceProject = new ServiceProject();
            Employee employee = serviceEmployee.readById(rs.getInt("manager"));
            departments.add(new Department(rs.getInt("id"), rs.getString("Name"), rs.getFloat("Year_Budget"),
                    employee));
        }
        return departments;
    }

    @Override
    public Department readById(int id) throws SQLException {
        String query = "select * from department where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            ServiceProject  serviceProject = new ServiceProject();
            Employee employee = serviceEmployee.readById(rs.getInt("manager"));
            return new Department(id, rs.getString("Name"), rs.getFloat("Year_Budget"),
                    employee);
        }
        return null;
    }

    public Department readByIdWithoutManager(int id) throws SQLException {
        String query = "select * from department where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            ServiceProject  serviceProject = new ServiceProject();
            return new Department(id, rs.getString("Name"));
        }
        return null;
    }

    public List<Department> searchByName(String name) throws SQLException {
        String req = "SELECT * FROM department WHERE LOWER(Name) LIKE ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setString(1, "%" + name.toLowerCase() + "%");
        ResultSet rs = ps.executeQuery();
        List<Department> departments = new ArrayList<>();
        while (rs.next()) {
            int Department_Manager = rs.getInt("manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Department_Manager);
            departments.add(new Department(rs.getInt("id"), rs.getString("Name"), rs.getFloat("Year_Budget"),
                    employee));
        }
        ps.close();
        return departments;
    }
    public List<Department> sortBudget() throws SQLException {
        String req = "SELECT * FROM department ORDER BY Year_Budget ASC";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Department> departments = new ArrayList<>();
        while (rs.next()) {
            int Department_Manager = rs.getInt("manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Department_Manager);
            departments.add(new Department(rs.getInt("id"), rs.getString("Name"), rs.getFloat("Year_Budget"),
                    employee));
        }
        ps.close();
        return departments;
    }

    public List<Department> sortName() throws SQLException {
        String req = "SELECT * FROM department ORDER BY Name ASC";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Department> departments = new ArrayList<>();
        while (rs.next()) {
            int Department_Manager = rs.getInt("manager");
            ServiceEmployee e = new ServiceEmployee();
            Employee employee = e.readById(Department_Manager);
            departments.add(new Department(rs.getInt("id"), rs.getString("Name"), rs.getFloat("Year_Budget"),
                    employee));
        }
        ps.close();
        return departments;
    }
}
