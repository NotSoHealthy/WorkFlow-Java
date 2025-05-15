package services;

import entity.Message;
import entity.Reclamation;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceMessage implements IService<Message> {




    Connection cnx ;
    public ServiceMessage() {
        cnx = DBConnection.getInstance().getConnection();
    }


    @Override
    public void add(Message message) throws SQLException {
        String req ="insert into message  (contenu,date,heure,reclamation,user) values(?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, message.getContenu());
        ps.setDate(2, message.getDate());
        ps.setTime(3, message.getHeure());
        ps.setInt(4, message.getReclamation().getReclamation_ID());
        ps.setInt(5, message.getUser().getId());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void update(Message messages) throws SQLException {
        String req ="update message set contenu=?,date=?,heure=? where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, messages.getContenu());
        ps.setDate(2, messages.getDate());
        ps.setTime(3, messages.getHeure());
        ps.setInt(4, messages.getMessage_ID());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void delete(Message message) throws SQLException {
        String req ="delete from message where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, message.getMessage_ID());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Message> readAll() throws SQLException {
        String req ="select * from message";
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Message> messages = new ArrayList<>();
        while (rs.next()) {
            messages.add(new Message(rs.getInt("id"),rs.getString("contenu"),rs.getDate("date"),rs.getTime("heure"),new ServiceReclamation().readById(rs.getInt("reclamation")),new ServiceEmployee().readById(rs.getInt("user"))));
        }
        ps.close();
        return messages;

    }

    @Override
    public Message readById(int id) throws SQLException {
        String req ="select * from message where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return new Message(rs.getInt("id"),rs.getString("contenu"),rs.getDate("date"),rs.getTime("heure"),new ServiceReclamation().readById(rs.getInt("reclamation")),new ServiceEmployee().readById(rs.getInt("user")));
        }
        ps.close();
        return null;
    }
}
