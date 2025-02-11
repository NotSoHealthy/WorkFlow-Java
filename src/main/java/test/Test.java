package test;

import entity.Department;
import entity.Employee;
import entity.Project;
import entity.Department;
import services.ServiceDepartment;
import services.ServiceEmployee;
import services.ServiceProject;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        Employee employee = serviceEmployee.readById(1);
        ServiceProject serviceProject = new ServiceProject();
        Project project = serviceProject.readById(2);
        Department department = new Department("financeee",166600, employee, project);
        ServiceDepartment serviceDepartment = new ServiceDepartment();
        serviceDepartment.add(department);
        System.out.println(serviceDepartment.readAll());
    }
}
