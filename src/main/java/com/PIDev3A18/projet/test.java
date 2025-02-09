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
        Date date_inscri=Date.valueOf("2025-06-10");
        Date date_debut=Date.valueOf("2025-07-10");
        Date date_fin=Date.valueOf("2025-08-20");
        Formation f=new Formation(4,"no","yesyesyes",date_debut,date_fin,11);
        Employee e= new Employee(8,"Fares","Guermazi","1234","fares.guermazi@esprit.tn","98264250","employee");
        Inscription i = new Inscription(date_inscri,"en attente",f,e);
        ServiceInscription si= new ServiceInscription();
        System.out.println(si.readAll());

    }

}
