package com.PIDev3A18.projet;

import entity.Employee;
import entity.Formation;
import entity.Inscription;
import services.ServiceEmployee;
import services.ServiceFormation;
import services.ServiceInscription;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) throws SQLException {
        Date date_inscri=Date.valueOf("2027-12-10");
        Date date_debut=Date.valueOf("2025-11-10");
        Date date_fin=Date.valueOf("2026-12-20");
        Formation f=new Formation(8,"python","Learn how to program",date_debut,date_fin,25);
        Employee e= new Employee(12,"Fares","Abdnadher","1234","fares.Abdnadher@esprit.tn","98264251","employee");
        Inscription i = new Inscription(date_inscri,"accepté",f,e);
        ServiceInscription si= new ServiceInscription();
        ServiceFormation sf=new ServiceFormation();
        ServiceEmployee se=new ServiceEmployee();
        System.out.println(si.sortStatus());




    }

}
