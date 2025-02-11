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
            String req ="update reclamations set titre=?, description=?, date=?, heure=?, etat=?, date_resolution=?, id_responsable=? where id=?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, reclamation.getTitle());
            ps.setString(2, reclamation.getDescription());
            ps.setDate(3, reclamation.getDate());
            ps.setTime(4, reclamation.getHeure());
            ps.setString(5, reclamation.getEtat());
            ps.setDate(6, reclamation.getDate_resolution());
            ps.setInt(7, reclamation.getResponsable().getId());
            ps.setInt(8, reclamation.getReclamation_ID());
            ps.executeUpdate();
            ps.close();
        }

        @Override
        public void delete(Reclamation reclamation) throws SQLException {
            String req ="delete from reclamations where id = ?";
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
            String req ="select * from reclamations where id=?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),new ServiceEmployee().readById(rs.getInt("id_responsable")),new ServiceEmployee().readById(rs.getInt("id_employee")));
            }
            ps.close();
            return null;
        }
       public List<Reclamation> search(String str) throws SQLException {
            String req ="select * from reclamations where titre like ? or description like ? ";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, "%"+str+"%");
            ps.setString(2, "%"+str+"%");
            ResultSet rs = ps.executeQuery();
            List<Reclamation> reclamations = new ArrayList<>();
            while (rs.next()) {
                reclamations.add(new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),new ServiceEmployee().readById(rs.getInt("id_responsable")),new ServiceEmployee().readById(rs.getInt("id_employee"))));
            }
            ps.close();
            return reclamations;
        }
        public List<Reclamation> searchByDate(java.sql.Date date) throws SQLException {
            String req="select * from reclamations where date = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            List<Reclamation> reclamations = new ArrayList<>();
            while (rs.next()) {
                reclamations.add(new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),new ServiceEmployee().readById(rs.getInt("id_responsable")),new ServiceEmployee().readById(rs.getInt("id_employee"))));
            }
            ps.close();
            return reclamations;
        }

    public List<Reclamation> searchByState(String Etat) throws SQLException {
        String req="select * from reclamations where etat = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, Etat);
        ResultSet rs = ps.executeQuery();
        List<Reclamation> reclamations = new ArrayList<>();
        while (rs.next()) {
            reclamations.add(new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),new ServiceEmployee().readById(rs.getInt("id_responsable")),new ServiceEmployee().readById(rs.getInt("id_employee"))));
        }
        ps.close();
        return reclamations;
    }

        public List<Reclamation> sortTitre() throws SQLException {
            String req ="select * from reclamations order by titre ";
            PreparedStatement ps = cnx.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            List<Reclamation> reclamations = new ArrayList<>();
            while (rs.next()) {
                reclamations.add(new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),new ServiceEmployee().readById(rs.getInt("id_responsable")),new ServiceEmployee().readById(rs.getInt("id_employee"))));
            }
            ps.close();
            return reclamations;
        }
    }

