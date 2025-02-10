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
        List<Inscription> inscriptions = new ArrayList<Inscription>();
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
}
