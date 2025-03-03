package services;

import entity.Conge;
import entity.Employee;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceConge implements IService<Conge> {
    private Connection con;

    public ServiceConge() {this.con = DBConnection.getInstance().getConnection();}

    @Override
    public void add(Conge conge) throws SQLException {
        String query = "insert into conges (employee_id, request_date, start_date, end_date, reason, status) values(?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,conge.getEmployee().getId());
        ps.setDate(2, Date.valueOf(conge.getRequest_date()));
        ps.setDate(3, Date.valueOf(conge.getStart_date()));
        ps.setDate(4, Date.valueOf(conge.getEnd_date()));
        ps.setString(5, conge.getReason());
        ps.setString(6, conge.getStatus());
        ps.executeUpdate();
        System.out.println("Conge added");
    }

    @Override
    public void update(Conge conge) throws SQLException {
        String query = "update conges set employee_id=?, request_date=?,start_date=?,end_date=?,reason=?,status=? where id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,conge.getEmployee().getId());
        ps.setDate(2, Date.valueOf(conge.getRequest_date()));
        ps.setDate(3, Date.valueOf(conge.getStart_date()));
        ps.setDate(4, Date.valueOf(conge.getEnd_date()));
        ps.setString(5, conge.getReason());
        ps.setString(6, conge.getStatus());
        ps.setInt(7, conge.getId());
        int r = ps.executeUpdate();
        System.out.println(r + " Conges updated");
    }

    @Override
    public void delete(Conge conge) throws SQLException {
        String query = "delete from conges where id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, conge.getId());
        int r = ps.executeUpdate();
        System.out.println(r + " Conges deleted");
    }

    @Override
    public List<Conge> readAll() throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        List<Conge> conges = new ArrayList<Conge>();
        String query = "select * from conges";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Conge conge = new Conge(rs.getInt("id"),serviceEmployee.readById(rs.getInt("employee_id")),rs.getDate("request_date").toLocalDate(),rs.getDate("start_date").toLocalDate(),rs.getDate("end_date").toLocalDate(), rs.getString("reason"),rs.getString("status"));
            conges.add(conge);
        }
        return conges;
    }

    @Override
    public Conge readById(int id) throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        String query = "select * from conges where id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Conge(rs.getInt("id"),serviceEmployee.readById(rs.getInt("employee_id")),rs.getDate("request_date").toLocalDate(),rs.getDate("start_date").toLocalDate(),rs.getDate("end_date").toLocalDate(), rs.getString("reason"),rs.getString("status"));
        }
        return null;
    }

    public int getCongeThisYear(Employee employee) throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        String query = "select * from conges where employee_id=? AND status='approved' AND YEAR(start_date) = YEAR(CURRENT_DATE);";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, employee.getId());
        ResultSet rs = ps.executeQuery();
        return rs.getFetchSize();
    }
}
