package com.PIDev3A18.projet;


import entity.Reservation;
import services.ServiceEmployee;
import services.ServiceEvent;
import entity.Event;
import services.ServiceReservation;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class test {
    public static void main(String[] args) throws SQLException {
        ServiceEvent se = new ServiceEvent();
        ServiceEmployee emp = new ServiceEmployee();
        LocalDateTime dateTime = LocalDateTime.of(2025, 3, 22, 10, 00);
        Event e=new Event(5,"thirdevent","quatroDescription",dateTime,"Tunis","Hackathon",30,emp.readById(4));
        ServiceReservation sr=new ServiceReservation();
        Reservation res=new Reservation(14,"Accees normale",10,emp.readById(4),se.readById(4));
        //se.add(e);
        //se.delete(e);
        //sr.add(res);


    }
}
