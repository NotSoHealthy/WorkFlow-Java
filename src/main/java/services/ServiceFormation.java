package services;

import entity.Employee;
import entity.Formation;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements IService<Formation> {
    Connection cnx ;
    public ServiceFormation() {
        cnx = DBConnection.getInstance().getConnection();
    }
    @Override
    public void add(Formation formation) throws SQLException {
        String req ="insert into formation  (titre,description,date_debut,date_fin,participants_max) values(?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, formation.getTitre());
        ps.setString(2, formation.getDescription());
        ps.setDate(3, formation.getDate_Debut());
        ps.setDate(4, formation.getDate_Fin());
        ps.setInt(5, formation.getParticipants_Max());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void update(Formation formation) throws SQLException {
        String req ="update formation set titre=?, description=?, date_Debut=?, date_Fin=?, participants_max=? where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, formation.getTitre());
        ps.setString(2, formation.getDescription());
        ps.setDate(3, formation.getDate_Debut());
        ps.setDate(4, formation.getDate_Fin());
        ps.setInt(5, formation.getParticipants_Max());
        ps.setInt(6, formation.getFormation_ID());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void delete(Formation formation) throws SQLException {
        String req ="delete from formation where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, formation.getFormation_ID());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Formation> readAll() throws SQLException {
        String req ="select * from formation";
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Formation> formations = new ArrayList<Formation>();
        while (rs.next()) {
            formations.add(new Formation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getDate("date_debut"),rs.getDate("date_fin"),rs.getInt("participants_max")));
        }
        return formations;
    }

    @Override
    public Formation readById(int id) throws SQLException {
        String req ="select * from formation where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return new Formation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getDate("date_debut"),rs.getDate("date_fin"),rs.getInt("participants_max"));
        }
        return null;
    }
}
