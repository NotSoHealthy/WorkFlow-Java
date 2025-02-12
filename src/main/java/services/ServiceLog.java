package services;

import entity.Log;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceLog implements IService<Log>{
    private Connection con;

    public ServiceLog() {this.con = DBConnection.getInstance().getConnection();}

    @Override
    public void add(Log log) throws SQLException {
        String query = "INSERT INTO logs (employee_id, action, description, timestamp) VALUES(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,log.getEmployee().getId());
        ps.setString(2,log.getAction());
        ps.setString(3,log.getDescription());
        ps.setTimestamp(4, Timestamp.valueOf(getTimestamp(log)));
        ps.executeUpdate();
        System.out.println("Log added");
    }

    private static LocalDateTime getTimestamp(Log log) {
        return log.getTimestamp();
    }

    @Override
    public void update(Log log) throws SQLException {
        String query = "update logs set employee_id=?, action=?, description=?, timestamp=? where id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,log.getEmployee().getId());
        ps.setString(2,log.getAction());
        ps.setString(3,log.getDescription());
        ps.setTimestamp(4,Timestamp.valueOf(log.getTimestamp()));
        ps.setInt(5,log.getId());
        int r = ps.executeUpdate();
        System.out.println(r+" logs updated");
    }

    @Override
    public void delete(Log log) throws SQLException {
        String query = "delete from logs where id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,log.getId());
        int r = ps.executeUpdate();
        System.out.println(r+" logs deleted");
    }

    @Override
    public List<Log> readAll() throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        List<Log> logs = new ArrayList<Log>();
        String query = "select * from logs";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Log log = new Log(rs.getInt("int"),serviceEmployee.readById(rs.getInt("employee_id")), rs.getString("action"),rs.getString("description"),rs.getTimestamp("timestamp").toLocalDateTime());
            logs.add(log);
        }
        return logs;
    }

    @Override
    public Log readById(int id) throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        List<Log> logs = new ArrayList<Log>();
        String query = "select * from logs where id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Log(rs.getInt("int"),serviceEmployee.readById(rs.getInt("employee_id")), rs.getString("action"),rs.getString("description"),rs.getTimestamp("timestamp").toLocalDateTime());
        }
        return null;
    }
}
