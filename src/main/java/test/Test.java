package test;

import entity.Attendance;
import entity.Conge;
import entity.Employee;
import services.ServiceAttendance;
import services.ServiceConge;
import services.ServiceEmployee;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Test {
    public static void main(String[] args) throws SQLException {
        ServiceAttendance serviceAttendance = new ServiceAttendance();
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        System.out.println(serviceAttendance.readAll());
    }
}
