package test;

import entity.Employee;
import services.ServiceEmployee;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        Employee employee = new Employee("Amin","Ben Hamouda","aminbenhamouda16@gmail.com","53267314","employee");
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        serviceEmployee.add(employee);
    }
}
