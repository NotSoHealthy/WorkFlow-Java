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
        //sr.update(new Reclamation(12,"Reclamation 3","Imprimante cassée",sqlDate,sqlTime,"pending",sqlDate,serviceEmployee.readById(1),serviceEmployee.readById(3)));
        Reclamation r = new Reclamation(13,"Reclamation 2","Une probleme avec les moniteurs",sqlDate,sqlTime,"approved",sqlDate,serviceEmployee.readById(2),serviceEmployee.readById(3));
        sr.delete(r);

        //System.out.println(sr.readById(12));
        //System.out.println(sr.search("Reclamation 3"));
        //System.out.println(sr.sortTitre());
        System.out.println(sr.searchByDate(sqlDate));
        //System.out.println(sr.searchByState("pending"));



        //System.out.println(serviceEmployee.readAll());
        System.out.println(sr.readAll().toString());
        System.out.println(sqlDate);
        System.out.println(sqlTime);

    }
}
