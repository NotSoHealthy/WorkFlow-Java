package services;

import entity.Event;

import java.util.ArrayList;
import java.util.List;

import entity.Reservation;
import utils.DBConnection;

import java.sql.*;

public class ServiceEvent implements IService<Event> {
    private Statement ste;
    private Connection cnx;
    private PreparedStatement pst;
    private ResultSet rs;
    public ServiceEvent() {
        cnx= DBConnection.getInstance().getConnection();
    }
    @Override
    public void add(Event evenement) {
        String requete="insert into events(Title,Description,DateAndTime,Location,Type,NumberOfPlaces,UID) values (?,?,?,?,?,?,?)";
        try {
            pst= cnx.prepareStatement(requete);
            pst.setString(1, evenement.getTitre());
            pst.setString(2, evenement.getDescription());
            pst.setTimestamp(3,Timestamp.valueOf(evenement.getDateetheure()));
            pst.setString(4, evenement.getLieu());
            pst.setString(5, evenement.getType());
            pst.setInt(6,evenement.getNombredeplace());
            pst.setInt(7, evenement.getUID());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Event event) {
        String requete="delete from events where ID_Event=?";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setInt(1, event.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Event evenement) {
        String requete="update events set Title=?,Description=?,DateAndTime=?,Location=?,Type=?,NumberOfPlaces=? where ID_Event=?";
        try {
            pst= cnx.prepareStatement(requete);
            pst.setString(1, evenement.getTitre());
            pst.setString(2, evenement.getDescription());
            pst.setTimestamp(3,Timestamp.valueOf(evenement.getDateetheure()));
            pst.setString(4, evenement.getLieu());
            pst.setString(5, evenement.getType());
            pst.setInt(6,evenement.getNombredeplace());
            pst.setInt(7, evenement.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Event> readAll() {
        String requete="select * from events";
        List<Event> evenements=new ArrayList<Event>();
        try {
            ste=cnx.createStatement();
            rs=ste.executeQuery(requete);
            while(rs.next()){
                evenements.add(new Event(rs.getInt("ID_Event"),rs.getString("Title"),rs.getString("Description"),rs.getTimestamp("DateAndTime").toLocalDateTime(),rs.getString("Location"),rs.getString("Type"),rs.getInt("NumberOfPlaces"),rs.getInt("UID")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return evenements;
    }

    @Override
    public Event readById(int id) {
        String requete="select * from events where ID_Event = '"+id+"'";
        Event event=new Event();
        try {
            ste=cnx.createStatement();
            rs=ste.executeQuery(requete);
            rs.next();
            event.setId( rs.getInt("ID_Event"));
            event.setTitre(rs.getString("Title"));
            event.setDescription(rs.getString("Description"));
            event.setDateetheure(rs.getTimestamp("DateAndTime").toLocalDateTime());
            event.setLieu(rs.getString("Location"));
            event.setType(rs.getString("Type"));
            event.setNombredeplace(rs.getInt("NumberOfPlaces"));
            event.setUID(rs.getInt("UID"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return event;
    }
    public void decrementNumber(Reservation r){
        String requete="update events set NumberOfPlaces=NumberOfPlaces-? where ID_Event = ?";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setInt(1,r.getNombreDePlaces());
            pst.setInt(2, r.getEvent().getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }public void incrementtNumber(Reservation r){
        String requete="update events set NumberOfPlaces=NumberOfPlaces+? where ID_Event = ?";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setInt(1,r.getNombreDePlaces());
            pst.setInt(2, r.getEvent().getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
