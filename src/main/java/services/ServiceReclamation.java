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
        ServiceEmployee se = new ServiceEmployee();

        @Override
        public void add(Reclamation reclamation) throws SQLException {
            String req ="insert into reclamation  (titre,description,category,type,attachedfile,date,heure,etat,date_resolution,responsable,user) values(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, reclamation.getTitle());
            ps.setString(2, reclamation.getDescription());
            ps.setString(3, reclamation.getCategory());
            ps.setString(4, reclamation.getType());
            ps.setString(5, reclamation.getAttachedfile());
            ps.setDate(6, reclamation.getDate());
            ps.setTime(7, reclamation.getHeure());
            ps.setString(8, reclamation.getEtat());
            ps.setDate(9, reclamation.getDate_resolution());
            if(reclamation.getResponsable()!=null){
            ps.setInt(10, reclamation.getResponsable().getId());}
            else    ps.setNull(10, java.sql.Types.INTEGER);
            ps.setInt(11, reclamation.getEmployee().getId());
            ps.executeUpdate();
            ps.close();
        }

        @Override
        public void update(Reclamation reclamation) throws SQLException {
            String req ="update reclamation set titre=?, description=?,category=?,type=?,attachedfile=?, date=?, heure=?, etat=?, date_resolution=?, responsable=? where id=?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, reclamation.getTitle());
            ps.setString(2, reclamation.getDescription());
            ps.setString(3, reclamation.getCategory());
            ps.setString(4, reclamation.getType());
            ps.setString(5, reclamation.getAttachedfile());
            ps.setDate(6, reclamation.getDate());
            ps.setTime(7, reclamation.getHeure());
            ps.setString(8, reclamation.getEtat());
            ps.setDate(9, reclamation.getDate_resolution());
            if(reclamation.getResponsable()!=null){
                ps.setInt(10, reclamation.getResponsable().getId());}
            else    ps.setNull(10, java.sql.Types.INTEGER);
            ps.setInt(11, reclamation.getReclamation_ID());
            ps.executeUpdate();
            ps.close();
        }

        @Override
        public void delete(Reclamation reclamation) throws SQLException {

            String req ="delete from reclamation where id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, reclamation.getReclamation_ID());
            ps.executeUpdate();
            ps.close();
        }

        @Override
        public List<Reclamation> readAll() throws SQLException {

            String req ="select * from reclamation";
            PreparedStatement ps = cnx.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            List<Reclamation> reclamations = new ArrayList<>();
            while (rs.next()) {
                Reclamation r = new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getString("category"),rs.getString("type"),rs.getString("attachedfile"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),null,null);
                if(rs.getInt("responsable")!= 0) r.setResponsable(se.readById(rs.getInt("responsable")));
                if(rs.getInt("user")!= 0) r.setEmployee(se.readById(rs.getInt("user")));
                reclamations.add(r);
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
                Reclamation r = new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getString("category"),rs.getString("type"),rs.getString("attachedfile"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),null,null);
                if(rs.getInt("responsable")!= 0) r.setResponsable(se.readById(rs.getInt("responsable")));
                if(rs.getInt("user")!= 0) r.setEmployee(se.readById(rs.getInt("user")));
                return r; }
            ps.close();
            return null;
        }
       public List<Reclamation> search(String str) throws SQLException {
            String req ="select * from reclamation where titre like ? or description like ? ";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, "%"+str+"%");
            ps.setString(2, "%"+str+"%");
            ResultSet rs = ps.executeQuery();
            List<Reclamation> reclamations = new ArrayList<>();
            while (rs.next()) {
                Reclamation r = new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getString("category"),rs.getString("type"),rs.getString("attachedfile"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),null,null);
                if(rs.getInt("responsable")!= 0) r.setResponsable(se.readById(rs.getInt("responsable")));
                if(rs.getInt("user")!= 0) r.setEmployee(se.readById(rs.getInt("user")));
                reclamations.add(r);   }
            ps.close();
            return reclamations;
        }
        public List<Reclamation> searchByDate(java.sql.Date date) throws SQLException {
            String req="select * from reclamation where date = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            List<Reclamation> reclamations = new ArrayList<>();
            while (rs.next()) {
                Reclamation r = new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getString("category"),rs.getString("type"),rs.getString("attachedfile"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),null,null);
                if(rs.getInt("responsable")!= 0) r.setResponsable(se.readById(rs.getInt("responsable")));
                if(rs.getInt("user")!= 0) r.setEmployee(se.readById(rs.getInt("user")));
                reclamations.add(r);    }
            ps.close();
            return reclamations;
        }

    public List<Reclamation> searchByState(String Etat) throws SQLException {
        String req="select * from reclamation where etat = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, Etat);
        ResultSet rs = ps.executeQuery();
        List<Reclamation> reclamations = new ArrayList<>();
        while (rs.next()) {
            Reclamation r = new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getString("category"),rs.getString("type"),rs.getString("attachedfile"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),null,null);
            if(rs.getInt("responsable")!= 0) r.setResponsable(se.readById(rs.getInt("responsable")));
            if(rs.getInt("user")!= 0) r.setEmployee(se.readById(rs.getInt("user")));
            reclamations.add(r);  }
        ps.close();
        return reclamations;
    }

        public List<Reclamation> sortTitre(int opt) throws SQLException {
            String req;
           if(opt == 1) req ="select * from reclamation order by titre ASC";
           else req ="select * from reclamation order by titre DESC";
           PreparedStatement ps = cnx.prepareStatement(req);
           ResultSet rs = ps.executeQuery();
           List<Reclamation> reclamations = new ArrayList<>();
           while (rs.next()) {
               Reclamation r = new Reclamation(rs.getInt("id"),rs.getString("titre"),rs.getString("description"),rs.getString("category"),rs.getString("type"),rs.getString("attachedfile"),rs.getDate("date"),rs.getTime("heure"),rs.getString("etat"),rs.getDate("date_resolution"),null,null);
               if(rs.getInt("responsable")!= 0) r.setResponsable(se.readById(rs.getInt("responsable")));
               if(rs.getInt("user")!= 0) r.setEmployee(se.readById(rs.getInt("user")));
               reclamations.add(r);   }
           ps.close();
           return reclamations;
        }
    }

