package com.PIDev3A18.projet;


import entity.Reservation;
import services.ServiceEvent;
import entity.Event;
import services.ServiceReservation;

import java.time.LocalDateTime;
import java.util.List;

public class test {
    public static void main(String[] args) {
        ServiceEvent se = new ServiceEvent();
        LocalDateTime dateTime = LocalDateTime.of(2025, 2, 22, 10, 00);
        Event e=new Event("secondevent","thedescription",dateTime,"Ben Arous","WorkShop",30,4);
        ServiceReservation sr=new ServiceReservation();
        Reservation res=new Reservation(5,14,"Accees normale",4,4);
        sr.add(res);


    }
}