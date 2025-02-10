package services;


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
        String req ="insert into formation  (title,description,date_begin,date_end,participants_max) values(?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, formation.getTitle());
        ps.setString(2, formation.getDescription());
        ps.setDate(3, formation.getDate_Begin());
        ps.setDate(4, formation.getDate_End());
        ps.setInt(5, formation.getParticipants_Max());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void update(Formation formation) throws SQLException {
        String req ="update formation set title=?, description=?, date_begin=?, date_end=?, participants_max=? where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, formation.getTitle());
        ps.setString(2, formation.getDescription());
        ps.setDate(3, formation.getDate_Begin());
        ps.setDate(4, formation.getDate_End());
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
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),rs.getDate("date_begin"),rs.getDate("date_end"),rs.getInt("participants_max")));
        }
        ps.close();
        return formations;

    }

    @Override
    public Formation readById(int id) throws SQLException {
        String req ="select * from formation where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),rs.getDate("date_begin"),rs.getDate("date_end"),rs.getInt("participants_max"));
        }
        ps.close();
        return null;
    }
    public List<Formation> search(String str) throws SQLException {
        String req ="select * from formation where title like ? or description like ? ";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, "%"+str+"%");
        ps.setString(2, "%"+str+"%");
        ResultSet rs = ps.executeQuery();
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),rs.getDate("date_begin"),rs.getDate("date_end"),rs.getInt("participants_max")));
        }
        ps.close();
        return formations;
    }
    public List<Formation> searchByDate(java.sql.Date date_start,java.sql.Date date_fin) throws SQLException {
        String req="select * from formation where date_begin >= ? and date_end <= ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setDate(1, date_start);
        ps.setDate(2, date_fin);
        ResultSet rs = ps.executeQuery();
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),rs.getDate("date_begin"),rs.getDate("date_end"),rs.getInt("participants_max")));
        }
        ps.close();
        return formations;
    }
    public List<Formation> sortId() throws SQLException {
        String req ="select * from formation order by id";
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),rs.getDate("date_begin"),rs.getDate("date_end"),rs.getInt("participants_max")));
        }
        ps.close();
        return formations;
    }
    public List<Formation> sortTitle() throws SQLException {
        String req ="select * from formation order by title ";
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),rs.getDate("date_begin"),rs.getDate("date_end"),rs.getInt("participants_max")));
        }
        ps.close();
        return formations;
    }
}
