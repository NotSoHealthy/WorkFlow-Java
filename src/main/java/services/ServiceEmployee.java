package services;

import entity.Employee;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ServiceEmployee implements Service<Employee> {
    private Connection con;

    public ServiceEmployee() {
        con = DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(Employee employee) throws SQLException {
        if (con==null){
            System.out.println("connection is null");
        }
        String query = "insert into employees (first_name, last_name, email, phone, type) values(?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, employee.getFirstName());
        ps.setString(2, employee.getLastName());
        ps.setString(3, employee.getEmail());
        ps.setString(4, employee.getPhone());
        ps.setString(5, employee.getType());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void update(Employee employee) throws SQLException {

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
    public void update(Employee employee, int oldCIN) throws SQLException {
        String query = "update emplyees set first_name = ?, last_name = ?, email = ?, phone = ?, type = ? where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, employee.getFirstName());
        ps.setString(2, employee.getLastName());
        ps.setString(3, employee.getEmail());
        ps.setString(4, employee.getPhone());
        ps.setString(5, employee.getType());
        ps.setInt(6, employee.getId());
        ps.executeUpdate();
        ps.close();
    }

    public Employee getEmployeeById(int id) throws SQLException {
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
}
