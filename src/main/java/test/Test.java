package test;

import entity.Employee;
import entity.Reclamation;
import services.ServiceEmployee;
import services.ServiceReclamation;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Test {
    public static void main(String[] args) throws SQLException {
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);
        LocalTime localTime = LocalTime.now();
        Time sqlTime = Time.valueOf(localTime);

        Employee employee = new Employee("Amine","Kerfai","aminekerfai75","12457852","employee");
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        //serviceEmployee.add(employee);
        ServiceReclamation sr = new ServiceReclamation();

        //sr.add(new Reclamation("Reclamation 2","Une probleme avec les moniteurs",sqlDate,sqlTime,"approved",sqlDate,serviceEmployee.readById(2),serviceEmployee.readById(3)));





        //System.out.println(serviceEmployee.readAll());
        System.out.println(sr.readAll().toString());
        System.out.println(sqlDate);
        System.out.println(sqlTime);

    }
}
