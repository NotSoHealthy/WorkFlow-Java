package test;

import entity.Employee;
import services.ServiceEmployee;
import services.ServiceReclamation;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        Employee employee = new Employee("Amine","Kerfai","aminekerfai75","12457852","employee");
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        //serviceEmployee.add(employee);
        ServiceReclamation sr = new ServiceReclamation();

        //sr.add("Reclamation 1","Une probleme avec le nouveau logiciel","");

        System.out.println(serviceEmployee.readAll());
    }
}
