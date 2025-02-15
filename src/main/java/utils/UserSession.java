package utils;

import entity.Employee;
import services.ServiceEmployee;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public final class UserSession {

    private static UserSession instance;
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    private UserSession() {
    }

    public UserSession(int id) throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        this.employee = serviceEmployee.readById(id);
    }

    public static UserSession getInstance(int id) throws SQLException {
        if (instance == null) {
            instance = new UserSession(id);
        }
        return instance;
    }

    public static UserSession getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserSession has not been initialized.");
        }
        return instance;
    }
}