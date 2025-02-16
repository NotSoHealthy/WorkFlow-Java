package services;

import entity.Employee;
import entity.Formation;
import entity.Inscription;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceInscription implements IService<Inscription> {
    Connection cnx;
    public ServiceInscription() {
        cnx = DBConnection.getInstance().getConnection();
    }
    @Override
    public void add(Inscription inscription) throws SQLException {
        String req="insert into inscription (date_registration,status,formation_id,user_id) values (?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setDate(1, inscription.getDate_registration());
        ps.setString(2, inscription.getStatus());
        ps.setInt(3,inscription.getFormation().getFormation_ID());
        ps.setInt(4, inscription.getEmployee().getId());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void update(Inscription inscription) throws SQLException {
        String req ="update inscription set date_registration=?,status=?,formation_id=?,user_id=? where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setDate(1, inscription.getDate_registration());
        ps.setString(2, inscription.getStatus());
        ps.setInt(3,inscription.getFormation().getFormation_ID());
        ps.setInt(4, inscription.getEmployee().getId());
        ps.setInt(5,inscription.getId());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void delete(Inscription inscription) throws SQLException {
        String req ="delete from inscription where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1,inscription.getId());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Inscription> readAll() throws SQLException {
        String req ="select * from inscription";
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Inscription> inscriptions = new ArrayList<>();
        while (rs.next()) {

            int formationId = rs.getInt("formation_id");
            int userId = rs.getInt("user_id");
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),rs.getDate("date_registration"),rs.getString("status"),formation,employee));
        }
        return inscriptions;
    }

    @Override
    public Inscription readById(int id) throws SQLException {
        String req ="select * from inscription where id=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            int formationId = rs.getInt("formation_id");
            int userId = rs.getInt("user_id");
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);

            return new Inscription(rs.getInt("id"),rs.getDate("date_registration"),rs.getString("status"),formation,employee);
        }
        return null;
    }
    public List<Inscription> search(String str) throws SQLException {
        String req ="SELECT i.*, e.last_name FROM inscription i JOIN employees e ON i.user_id = e.id WHERE i.status LIKE ? OR i.id = ? OR e.last_name LIKE ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1,"%"+str+"%");
        int intValue = 1;
        try {
            intValue = Integer.parseInt(str);
        } catch (NumberFormatException e) {
        }
        ps.setInt(2, intValue);
        ps.setString(3, "%"+str+"%");
        ResultSet rs = ps.executeQuery();
        List<Inscription> inscriptions = new ArrayList<>();
        while (rs.next()) {
            int formationId = rs.getInt("formation_id");
            int userId = rs.getInt("user_id");
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),rs.getDate("date_registration"),rs.getString("status"),formation,employee));
        }
        ps.close();
        return inscriptions;
    }
    public List<Inscription> searchByDate(int year) throws SQLException {
        String req ="select * from inscription where YEAR(date_registration) = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, year);
        ResultSet rs = ps.executeQuery();
        List<Inscription> inscriptions = new ArrayList<>();
        while (rs.next()) {
            int formationId = rs.getInt("formation_id");
            int userId = rs.getInt("user_id");
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),rs.getDate("date_registration"),rs.getString("status"),formation,employee));
        }
        ps.close();
        return inscriptions;
    }
    public List<Inscription> sortDate() throws SQLException {
        String req ="select * from inscription order by date_registration ";
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Inscription> inscriptions = new ArrayList<>();
        while (rs.next()) {
            int formationId = rs.getInt("formation_id");
            int userId = rs.getInt("user_id");
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),rs.getDate("date_registration"),rs.getString("status"),formation,employee));
        }
        ps.close();
        return inscriptions;

    }
    public List<Inscription> sortStatus() throws SQLException {
        String req ="select * from inscription order by status ";
        PreparedStatement ps = cnx.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        List<Inscription> inscriptions = new ArrayList<>();
        while (rs.next()) {
            int formationId = rs.getInt("formation_id");
            int userId = rs.getInt("user_id");
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),rs.getDate("date_registration"),rs.getString("status"),formation,employee));
        }
        ps.close();
        return inscriptions;
    }
}
