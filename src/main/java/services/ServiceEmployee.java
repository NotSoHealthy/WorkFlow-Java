package services;

import entity.Employee;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceEmployee implements IService<Employee> {
    private Connection con;

    public ServiceEmployee() {
        con = DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(Employee employee) throws SQLException {
        if (con==null){
            System.out.println("connection is null");
        }
        String query = "insert into employees (first_name, last_name, email, password, phone, type) values(?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, employee.getFirstName());
        ps.setString(2, employee.getLastName());
        ps.setString(3, employee.getEmail());
        ps.setString(3, employee.getPassword());
        ps.setString(4, employee.getPhone());
        ps.setString(5, employee.getType());
        int r = ps.executeUpdate();
        ps.close();
        System.out.println(r + " rows affected");
    }

    @Override
    public void delete(Employee employee) throws SQLException {
        String query = "delete from employees where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, employee.getId());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void update(Employee employee) throws SQLException {
        String query = "update emplyees set first_name = ?, last_name = ?, email = ?, password = ?, phone = ?, type = ? where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, employee.getFirstName());
        ps.setString(2, employee.getLastName());
        ps.setString(3, employee.getEmail());
        ps.setString(3, employee.getPassword());
        ps.setString(4, employee.getPhone());
        ps.setString(5, employee.getType());
        ps.setInt(6, employee.getId());
        ps.executeUpdate();
        ps.close();
    }

    public Employee readById(int id) throws SQLException {
        String query = "select * from employees where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Employee(id, rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("email"), rs.getString("phone"), rs.getString("type"));
        }
        return null;
    }

    public List<Employee> readAll() throws SQLException {
        String query = "select * from employees";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Employee> employees = new ArrayList<>();
        while (rs.next()) {
            employees.add(new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("email"), rs.getString("phone"), rs.getString("type")));
        }
        return employees;
    }

    public Boolean verifPassword(String email, String password) throws SQLException {
        String query = "select * from employees where email = ? and password = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public Boolean verifEmail(String email) throws SQLException {
        String query = "select * from employees where email = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public Boolean verifPhone(String phone) throws SQLException {
        String query = "select * from employees where phone = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, phone);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public Employee readByEmailAndPassword(String email, String password) throws SQLException {
        String query = "select * from employees where email = ? and password = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                    email, password, rs.getString("phone"), rs.getString("type"));
        }
        return null;
    }
}
