package services;

import entity.Employee;
import entity.Reclamation;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceReclamation implements IService<Reclamation> {

        Connection cnx ;
        public ServiceReclamation() {
            cnx = DBConnection.getInstance().getConnection();
        }


        @Override
        public void add(Reclamation reclamation) throws SQLException {
            String req ="insert into reclamations  (titre,description,date,heure,etat,date_resolution,id_responsable,id_employee) values(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, reclamation.getTitle());
            ps.setString(2, reclamation.getDescription());
            ps.setDate(3, reclamation.getDate());
            ps.setTime(4, reclamation.getHeure());
            ps.setString(5, reclamation.getEtat());
            ps.setDate(6, reclamation.getDate_resolution());
            ps.setInt(7, reclamation.getResponsable().getId());
            ps.setInt(8, reclamation.getEmployee().getId());
            ps.executeUpdate();
            ps.close();
        }

        @Override
        public void update(Reclamation reclamation) throws SQLException {
            String req ="update formation set title=?, description=?, date_begin=?, heure=?, etat=?, date_resolution=?, id_responsable=? where id=?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, reclamation.getTitle());
            ps.setString(2, reclamation.getDescription());
            ps.setDate(3, reclamation.getDate());
            ps.setTime(4, reclamation.getHeure());
            ps.setString(5, reclamation.getEtat());
            ps.setDate(6, reclamation.getDate_resolution());
            ps.setInt(7, reclamation.getResponsable().getId());
            ps.executeUpdate();
            ps.close();
        }

        @Override
        public void delete(Reclamation reclamation) throws SQLException {
            String req ="delete from formation where id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, reclamation.getReclamation_ID());
            ps.executeUpdate();
            ps.close();
        }

        @Override
        public List<Reclamation> readAll() throws SQLException {
            String req ="select * from reclamations";
            PreparedStatement ps = cnx.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            List<Reclamation> reclamations = new ArrayList<>();
            while (rs.next()) {
                reclamations.add(new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),new ServiceEmployee().readById(rs.getInt("id_responsable")),new ServiceEmployee().readById(rs.getInt("id_employee"))));
            }
            ps.close();
            return reclamations;

        }

        @Override
        public Reclamation readById(int id) throws SQLException {
            String req ="select * from reclamation where id=?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),new ServiceEmployee().readById(rs.getInt("id_responsable")),new ServiceEmployee().readById(rs.getInt("id_employee")));
            }
            ps.close();
            return null;
        }
      /*  public List<Reclamation> search(String str) throws SQLException {
            String req ="select * from formation where title like ? or description like ? ";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, "%"+str+"%");
            ps.setString(2, "%"+str+"%");
            ResultSet rs = ps.executeQuery();
            List<Reclamation> formations = new ArrayList<>();
            while (rs.next()) {
                formations.add(new Reclamation(rs.getInt("id"),rs.getString("title"),rs.getString("description"),rs.getDate("date_begin"),rs.getDate("date_end"),rs.getInt("participants_max")));
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
        }*/
    }

