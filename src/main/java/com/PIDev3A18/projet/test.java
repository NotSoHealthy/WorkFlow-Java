package com.PIDev3A18.projet;

import entity.Employee;
import entity.Formation;
import entity.Inscription;
import services.ServiceEmployee;
import services.ServiceFormation;
import services.ServiceInscription;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) throws SQLException {

        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);
        Date date_fin=Date.valueOf("2026-12-20");
        Employee e= new Employee(12,"Fares","Abdnadher","1234","fares.Abdnadher@esprit.tn","98264251","employee","pending");
        Formation f=new Formation("C++","Learn how to program",sqlDate,date_fin,25,e);
        ServiceInscription si= new ServiceInscription();
        ServiceFormation sf=new ServiceFormation();
        ServiceEmployee se=new ServiceEmployee();
        sf.readAll().forEach(System.out::println);





    }

}
