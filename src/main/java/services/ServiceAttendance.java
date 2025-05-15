package services;

import entity.Attendance;
import entity.Employee;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAttendance implements IService<Attendance> {
    private Connection con;

    public ServiceAttendance() {this.con = DBConnection.getInstance().getConnection();}

    @Override
    public void add(Attendance attendance) throws SQLException {
        String query = "insert into attendance (user, date, entry_time, exit_time) values(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,attendance.getEmployee().getId());
        ps.setDate(2, Date.valueOf(attendance.getDate()));
        ps.setTime(3, Time.valueOf(attendance.getEntry_time()));
        ps.setTime(4, Time.valueOf(attendance.getExit_time()));
        ps.executeUpdate();
        System.out.println("Attendance added");
    }

    @Override
    public void update(Attendance attendance) throws SQLException {
        String query = "update attendance set user = ?, date = ?, entry_time = ?, exit_time = ? where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,attendance.getEmployee().getId());
        ps.setDate(2, Date.valueOf(attendance.getDate()));
        ps.setTime(3, Time.valueOf(attendance.getEntry_time()));
        ps.setTime(4, Time.valueOf(attendance.getExit_time()));
        ps.setInt(5,attendance.getId());
        int r =ps.executeUpdate();
        System.out.println(r + " Attendances updated");
    }

    @Override
    public void delete(Attendance attendance) throws SQLException {
        String query = "delete from attendance where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,attendance.getId());
        int r =ps.executeUpdate();
        System.out.println(r + " Attendances deleted");
    }

    @Override
    public List<Attendance> readAll() throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        List<Attendance> attendances = new ArrayList<Attendance>();
        String query = "select * from attendance";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Attendance attendance = new Attendance(rs.getInt("id"),serviceEmployee.readById(rs.getInt("user")),rs.getDate("date").toLocalDate(),rs.getTime("entry_time").toLocalTime(),rs.getTime("exit_time").toLocalTime());
            attendances.add(attendance);
        }
        return attendances;
    }

    @Override
    public Attendance readById(int id) throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        String query = "select * from attendance where id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Attendance(rs.getInt("id"),serviceEmployee.readById(rs.getInt("user")),rs.getDate("date").toLocalDate(),rs.getTime("entry_time").toLocalTime(),rs.getTime("exit_time").toLocalTime());
        }
        System.out.println("Attendance not found");
        return null;
    }

    public List<Attendance> readAllByEmployee(Employee employee) throws SQLException {
        List<Attendance> attendances = new ArrayList<>();
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        String query = "select * from attendance where user = ? ORDER BY date DESC";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,employee.getId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            attendances.add(new Attendance(rs.getInt("id"),serviceEmployee.readById(rs.getInt("user")),rs.getDate("date").toLocalDate(),rs.getTime("entry_time").toLocalTime(),rs.getTime("exit_time").toLocalTime()));
        }
        return attendances;
    }
}
