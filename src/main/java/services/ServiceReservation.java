package services;

import entity.Event;
import entity.Reservation;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation implements IService<Reservation> {
    private Statement ste;
    private Connection cnx;
    private PreparedStatement pst;
    private ResultSet rs;
    public ServiceReservation() {
        cnx= DBConnection.getInstance().getConnection();
    }
    @Override
    public void add(Reservation reservation) {
        ServiceEvent es=new ServiceEvent();
        Event e=es.readById(reservation.getEvent().getId());
        int nb=e.getNombredeplace();
        if(nb==0){
            System.out.println("Event is full");
        }
        else{
            String requete="insert into reservation(Price,Type,NombreDePlaces,qr_url,UID,ID_Event) values(?,?,?,?,?,?)";
            try {
                pst= cnx.prepareStatement(requete);
                pst.setDouble(1,reservation.getPrice());
                pst.setString(2,reservation.getType());
                pst.setInt(3,reservation.getNombreDePlaces());
                pst.setString(4,reservation.getQr_url());
                pst.setInt(5,reservation.getEmployee().getId());
                pst.setInt(6,reservation.getEvent().getId());
                pst.executeUpdate();
                es.decrementNumber(reservation);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void delete(Reservation r) {
        ServiceEvent es=new ServiceEvent();
        String requete="delete from reservation where ID_Reservation=?";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setInt(1, r.getId());
            pst.executeUpdate();
            es.incrementtNumber(r);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Reservation reservation) {
        String requete="update reservation set Price=?,Type=?,NombreDePlaces = ? ,UID=? where ID_Reservation=?";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setDouble(1,reservation.getPrice());
            pst.setString(2,reservation.getType());
            pst.setInt(3,reservation.getNombreDePlaces());
            pst.setInt(4,reservation.getEmployee().getId());
            pst.setInt(5,reservation.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reservation> readAll() {
        ServiceEvent es=new ServiceEvent();
        ServiceEmployee es1=new ServiceEmployee();
        String requete="select * from reservation";
        List<Reservation> reservations=new ArrayList<Reservation>();
        try {
            ste=cnx.createStatement();
            rs=ste.executeQuery(requete);
            while(rs.next()){
                reservations.add(new Reservation(rs.getInt("ID_Reservation"),rs.getDouble("price"),rs.getString("Type"),rs.getInt("NombreDePlaces"),rs.getString("qr_url"),es1.readById(rs.getInt("UID")),es.readById(rs.getInt("ID_Event"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    @Override
    public Reservation readById(int id) {
        ServiceEvent es=new ServiceEvent();
        ServiceEmployee es1=new ServiceEmployee();
        String requete="select * from reservation where ID_Reservation = '"+id+"'";
        Reservation r=new Reservation();
        try {
            ste=cnx.createStatement();
            rs=ste.executeQuery(requete);
            rs.next();
            r.setId(rs.getInt("ID_Reservation"));
            r.setPrice(rs.getFloat("price"));
            r.setType(rs.getString("Type"));
            r.setNombreDePlaces(rs.getInt("NombreDePlaces"));
            r.setQr_url(rs.getString("qr_url"));
            r.setEmployee(es1.readById(rs.getInt("UID")));
            r.setEvent(es.readById(rs.getInt("ID_Event")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return r;
    }
    public List<Reservation> readAllById(int id) {
        ServiceEvent es=new ServiceEvent();
        ServiceEmployee es1=new ServiceEmployee();
        String requete="select * from reservation where UID = '"+id+"'";
        List<Reservation> reservations=new ArrayList<Reservation>();
        try {
            ste=cnx.createStatement();
            rs=ste.executeQuery(requete);
            while(rs.next()){
                reservations.add(new Reservation(rs.getInt("ID_Reservation"),rs.getDouble("price"),rs.getString("Type"),rs.getInt("NombreDePlaces"),rs.getString("qr_url"),es1.readById(rs.getInt("UID")),es.readById(rs.getInt("ID_Event"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }
}
