package test;

import entity.Department;
import entity.Employee;
import services.ServiceDepartment;
import services.ServiceEmployee;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        ServiceDepartment serviceDepartment = new ServiceDepartment();
        Employee employee = serviceEmployee.readById(1);
        Department department = serviceDepartment.readById(3);
        if (department != null && employee != null) {
            department.setName("kingg");
            department.setYear_Budget(166600);
            department.setDepartment_Manager(employee);
            serviceDepartment.update(department);
            System.out.println(serviceDepartment.readAll());
        } else {
            System.out.println("Error: Department or Employee not found.");
        }
    }
}
