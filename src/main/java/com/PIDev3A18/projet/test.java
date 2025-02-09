package com.PIDev3A18.projet;

import entity.Formation;
import entity.Inscription;
import services.ServiceFormation;
import services.ServiceInscription;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) throws SQLException {
        Date date_inscri=Date.valueOf("2025-07-10");
        Date date_debut=Date.valueOf("2025-07-10");
        Date date_fin=Date.valueOf("2025-08-20");
        Formation f=new Formation(3,"yes","yesyesyes",date_debut,date_fin,10);
        Inscription i = new Inscription(date_inscri,"terminé",f);
        ServiceInscription si = new ServiceInscription();
        si.update(i);
    }

}
