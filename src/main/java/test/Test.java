package test;

import entity.Employee;
import services.ServiceEmployee;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        Employee employee = new Employee("Youssef","Mlaouhia","Youssef_mlaouhia@hotmail.fr","93714003","employee");
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        serviceEmployee.add(employee);
        System.out.println(serviceEmployee.readAll());
    }
}
