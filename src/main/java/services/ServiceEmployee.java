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
        String query = "insert into employees (first_name, last_name, email, phone, password, adresse, gouvernorat, role, status) values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, employee.getFirstName());
        ps.setString(2, employee.getLastName());
        ps.setString(3, employee.getEmail());
        ps.setString(4, employee.getPhone());
        ps.setString(5, employee.getPassword());
        ps.setString(6, employee.getAdresse());
        ps.setString(7, employee.getGouvernorat());
        ps.setString(8, employee.getRole());
        ps.setString(9, "pending");
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
        String query = "update employees set first_name = ?, last_name = ?, email = ?, phone = ?, password = ?, department_id = ?, adresse = ?, gouvernorat = ?, image_url = ?, role = ?, status = ? where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, employee.getFirstName());
        ps.setString(2, employee.getLastName());
        ps.setString(3, employee.getEmail());
        ps.setString(4, employee.getPhone());
        ps.setString(5, employee.getPassword());
        ps.setInt(6, employee.getDepartment().getDepartment_id());
        ps.setString(7, employee.getAdresse());
        ps.setString(8, employee.getGouvernorat());
        ps.setString(9, employee.getImageUrl());
        ps.setString(10, employee.getRole());
        ps.setString(11, employee.getStatus());
        ps.setInt(12, employee.getId());
        ps.executeUpdate();
        ps.close();
    }

    public Employee readById(int id) throws SQLException {
        ServiceDepartment serviceDepartment = new ServiceDepartment();
        String query = "select * from employees where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Employee(id, rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("email"), rs.getString("phone"),rs.getString("password"),serviceDepartment.readByIdWithoutManager(rs.getInt("department_id")), rs.getString("adresse"), rs.getString("gouvernorat"), rs.getString("image_url"), rs.getString("role"), rs.getString("status"));
        }
        System.out.println("no employee found");
        return null;
    }

    public List<Employee> readAll() throws SQLException {
        ServiceDepartment serviceDepartment = new ServiceDepartment();
        String query = "select * from employees";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Employee> employees = new ArrayList<>();
        while (rs.next()) {
            employees.add(new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("email"), rs.getString("phone"),rs.getString("password"),serviceDepartment.readById(rs.getInt("department_id")), rs.getString("adresse"), rs.getString("gouvernorat"), rs.getString("image_url"),rs.getString("role"), rs.getString("status")));
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
        ServiceDepartment serviceDepartment = new ServiceDepartment();
        String query = "select * from employees where email = ? and password = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                    email, rs.getString("phone"),password,serviceDepartment.readById(rs.getInt("department_id")), rs.getString("adresse"), rs.getString("gouvernorat"), rs.getString("image_url"), rs.getString("role"), rs.getString("status"), rs.getString("two_factor_secret"));
        }
        return null;
    }

    public Employee readByEmail(String email) throws SQLException {
        ServiceDepartment serviceDepartment = new ServiceDepartment();
        String query = "select * from employees where email = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("email"), rs.getString("phone"),rs.getString("password"),serviceDepartment.readById(rs.getInt("department_id")), rs.getString("adresse"), rs.getString("gouvernorat"), rs.getString("image_url"),rs.getString("role"), rs.getString("status"));

        }
        return null;
    }
    public Employee readByNumber(String number) throws SQLException {
        ServiceDepartment serviceDepartment = new ServiceDepartment();
        String query = "select * from employees where phone = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, number);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return new Employee(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                    rs.getString("email"), rs.getString("phone"),rs.getString("password"),serviceDepartment.readById(rs.getInt("department_id")), rs.getString("adresse"), rs.getString("gouvernorat"), rs.getString("image_url"),rs.getString("role"), rs.getString("status"));

        }
        return null;
    }

    public void set2FASecret(Employee employee) throws SQLException {
        String query = "update employees set two_factor_secret = ? where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, employee.getTwo_factor_secret());
        ps.setInt(2, employee.getId());
        ps.executeUpdate();
    }

    public void remove2FASecret(Employee employee) throws SQLException {
        String query = "update employees set two_factor_secret = null where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, employee.getId());
        ps.executeUpdate();
    }
}
