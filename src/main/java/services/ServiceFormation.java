package services;


import entity.Employee;
import entity.Formation;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceFormation implements IService<Formation> {
    Connection cnx ;
    public ServiceFormation() {
        cnx = DBConnection.getInstance().getConnection();
    }
    @Override
    public void add(Formation formation) throws SQLException {
        String req ="insert into formation  (title,description,date_begin,date_end,participants_max,responsable) values(?,?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, formation.getTitle());
        ps.setString(2, formation.getDescription());
        ps.setDate(3, java.sql.Date.valueOf(formation.getDateBegin()));
        ps.setDate(4, java.sql.Date.valueOf(formation.getDateEnd()));
        ps.setInt(5, formation.getParticipants_Max());
        ps.setInt(6,formation.getEmployee().getId());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void update(Formation formation) throws SQLException {
        String req ="update formation set title=?, description=?, date_begin=?, date_end=?, participants_max=?,responsable=? where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, formation.getTitle());
        ps.setString(2, formation.getDescription());
        ps.setDate(3, java.sql.Date.valueOf(formation.getDateBegin()));
        ps.setDate(4, java.sql.Date.valueOf(formation.getDateEnd()));
        ps.setInt(5, formation.getParticipants_Max());
        ps.setInt(6, formation.getEmployee().getId());
        ps.setInt(7, formation.getFormation_ID());
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
            int responsable_id = rs.getInt("responsable");
            LocalDate begin = rs.getDate("date_begin").toLocalDate();
            LocalDate end = rs.getDate("date_end").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(responsable_id);
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),begin,end,rs.getInt("participants_max"),employee));
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
            int responsable_id = rs.getInt("responsable");
            LocalDate begin = rs.getDate("date_begin").toLocalDate();
            LocalDate end = rs.getDate("date_end").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(responsable_id);
            return new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),begin,end,rs.getInt("participants_max"),employee);
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
            int responsable_id = rs.getInt("responsable");
            ServiceEmployee e=new ServiceEmployee();
            LocalDate begin = rs.getDate("date_begin").toLocalDate();
            LocalDate end = rs.getDate("date_end").toLocalDate();
            Employee employee= e.readById(responsable_id);
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),begin,end,rs.getInt("participants_max"),employee));
        }
        ps.close();
        return formations;
    }
    public List<Formation> searchByDate(LocalDate date_start,LocalDate date_fin) throws SQLException {
        String req="select * from formation where date_begin >= ? and date_end <= ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setDate(1, java.sql.Date.valueOf(date_start));
        ps.setDate(2, java.sql.Date.valueOf(date_fin));
        ResultSet rs = ps.executeQuery();
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            int responsable_id = rs.getInt("responsable");
            LocalDate begin = rs.getDate("date_begin").toLocalDate();
            LocalDate end = rs.getDate("date_end").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(responsable_id);
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),begin,end,rs.getInt("participants_max"),employee));
        }
        ps.close();
        return formations;
    }
    public List<Formation> sortId(String order) throws SQLException {
        String req ="select * from formation order by id "+order;
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            int responsable_id = rs.getInt("responsable");
            LocalDate begin = rs.getDate("date_begin").toLocalDate();
            LocalDate end = rs.getDate("date_end").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(responsable_id);
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),begin,end,rs.getInt("participants_max"),employee));
        }
        ps.close();
        return formations;
    }
    public List<Formation> sortTitle(String order) throws SQLException {
        String req ="select * from formation order by title "+order;
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Formation> formations = new ArrayList<>();
        while (rs.next()) {
            int responsable_id = rs.getInt("responsable");
            LocalDate begin = rs.getDate("date_begin").toLocalDate();
            LocalDate end = rs.getDate("date_end").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(responsable_id);
            formations.add(new Formation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),begin,end,rs.getInt("participants_max"),employee));
        }
        ps.close();
        return formations;
    }
    public Map<Integer, Integer> statisticFormation() throws SQLException {
        String query = "SELECT YEAR(date_begin) as year, COUNT(*) as count FROM formation GROUP BY year";
        PreparedStatement ps = cnx.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        Map<Integer, Integer> formationCount = new HashMap<>();
        while (rs.next()) {
            int year = rs.getInt("year");
            int count = rs.getInt("count");
            formationCount.put(year, count);
        }
        ps.close();
        return formationCount;
    }
}
