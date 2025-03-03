package services;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import entity.Employee;
import entity.Formation;
import entity.Inscription;
import utils.DBConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ServiceInscription implements IService<Inscription> {
    Connection cnx;
    private static final String FLAG_FILE = "Files/smsFlags.properties";
    public static final String ACCOUNT_SID = "AC4110bdfcaad2020468865d70dc5590de";
    public static final String AUTH_TOKEN = "bd3846619cc82ee6a9df5b980f44efe6";
    public ServiceInscription() {
        cnx = DBConnection.getInstance().getConnection();

    }
    @Override
    public void add(Inscription inscription) throws SQLException {
        String req="insert into inscription (date_registration,status,formation_id,user_id) values (?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setDate(1, java.sql.Date.valueOf(inscription.getDateRegistration()));
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
        ps.setDate(1, java.sql.Date.valueOf(inscription.getDateRegistration()));
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
            LocalDate inscri = rs.getDate("date_registration").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),inscri,rs.getString("status"),formation,employee));
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
            LocalDate inscri = rs.getDate("date_registration").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);

            return new Inscription(rs.getInt("id"),inscri,rs.getString("status"),formation,employee);
        }
        return null;
    }
    public List<Inscription> search(String str) throws SQLException {
        String req ="SELECT i.*, e.last_name FROM inscription i JOIN employees e ON i.user_id = e.id WHERE e.last_name LIKE ? OR e.first_name LIKE ? OR YEAR(date_registration) LIKE ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, "%"+str+"%");
        ps.setString(2, "%"+str+"%");
        ps.setString(3, "%"+str+"%");
        ResultSet rs = ps.executeQuery();
        List<Inscription> inscriptions = new ArrayList<>();
        while (rs.next()) {
            int formationId = rs.getInt("formation_id");
            int userId = rs.getInt("user_id");
            LocalDate inscri = rs.getDate("date_registration").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),inscri,rs.getString("status"),formation,employee));
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
            LocalDate inscri = rs.getDate("date_registration").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),inscri,rs.getString("status"),formation,employee));
        }
        ps.close();
        return inscriptions;

    }
    public List<Inscription> sortStatus(String str) throws SQLException {
        String req ="select * from inscription where status like ? ";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1,"%"+str+"%");
        ResultSet rs = ps.executeQuery();
        List<Inscription> inscriptions = new ArrayList<>();
        while (rs.next()) {
            int formationId = rs.getInt("formation_id");
            int userId = rs.getInt("user_id");
            LocalDate inscri = rs.getDate("date_registration").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),inscri,rs.getString("status"),formation,employee));
        }
        ps.close();
        return inscriptions;
    }
    public boolean isRegistered(Formation formation, Employee loggedinEmployee) throws SQLException {
        String query = "SELECT * FROM inscription WHERE formation_id = ? AND user_id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1,formation.getFormation_ID());
        ps.setInt(2,loggedinEmployee.getId());
        ResultSet rs = ps.executeQuery();
        return rs.next();

    }
    public List<Inscription> SortByEmployee(int id) throws SQLException {
        String req ="select * from inscription where user_id = ? ";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        List<Inscription> inscriptions = new ArrayList<>();
        while (rs.next()) {
            int formationId = rs.getInt("formation_id");
            int userId = rs.getInt("user_id");
            LocalDate inscri = rs.getDate("date_registration").toLocalDate();
            ServiceEmployee e=new ServiceEmployee();
            Employee employee= e.readById(userId);
            ServiceFormation s=new ServiceFormation();
            Formation formation = s.readById(formationId);
            inscriptions.add(new Inscription(rs.getInt("id"),inscri,rs.getString("status"),formation,employee));
        }
        ps.close();
        return inscriptions;
    }
    public Map<String, Integer> statisticInscription() throws SQLException {
        String query = "SELECT f.title as title, COUNT(*) as count FROM inscription i JOIN formation f ON i.formation_id = f.id GROUP BY f.title";
        PreparedStatement ps = cnx.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        Map<String, Integer> InscriptionCount = new HashMap<>();
        while (rs.next()) {
            String title = rs.getString("title");
            int count = rs.getInt("count");
            InscriptionCount.put(title, count);
        }
        ps.close();
        return InscriptionCount;
    }
    public static boolean isSmsSent(int employeeId) {
        Properties props = new Properties();
        File file = new File(FLAG_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String val = props.getProperty(String.valueOf(employeeId));
        if (val != null) {
            String[] parts = val.split("=");
            if (parts.length > 0) {
                return Boolean.parseBoolean(parts[0]);
            }
        }
        return false;
    }
    public static void markSmsSent(int employeeId, boolean value) {
        Properties props = new Properties();
        File file = new File(FLAG_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String val=value+"="+LocalDate.now();
        props.setProperty(String.valueOf(employeeId), val);
        try (FileWriter writer = new FileWriter(file)) {
            for (String key : props.stringPropertyNames()) {
                writer.write(key + "=" + props.getProperty(key) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static LocalDate getSmsSentDate(int employeeId) {
        Properties props = new Properties();
        File file = new File(FLAG_FILE);

        if (!file.exists()) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String value = props.getProperty(String.valueOf(employeeId));

        if (value != null && value.contains("=")) {
            String[] parts = value.split("=");
            if (parts.length == 2) {
                return LocalDate.parse(parts[1]);

            }
        }
        return null;
    }
    public void sendSMS(Employee loggedinEmployee) throws SQLException {
        LocalDate date=getSmsSentDate(loggedinEmployee.getId());
        if(date !=null && date.isBefore(LocalDate.now())) {
            Properties props = new Properties();
            File file = new File(FLAG_FILE);
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            props.remove(String.valueOf(loggedinEmployee.getId()));
            try (FileOutputStream fos = new FileOutputStream(file)) {
                props.store(fos, "SMS Flag Data");
                System.out.println("Removed expired SMS record for employee ID: " + loggedinEmployee.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (isSmsSent(loggedinEmployee.getId())) {
            System.out.println("SMS already sent for employee " + loggedinEmployee.getId());
            return;
        }
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String fromNumber = "+18782905902";
        String toNumber = "+21698264250";
        String body = "";
        String query ="SELECT f.title,e.last_name,e.first_name FROM formation f JOIN inscription i ON f.ID = i.formation_id JOIN employees e ON i.user_id = e.id  WHERE f.date_begin = CURDATE() AND i.user_id = ? AND i.status='approuver'";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1,loggedinEmployee.getId());
        ResultSet rs = ps.executeQuery();
        String firstName = "";
        String lastName = "";
        List<String> formations = new ArrayList<>();

        while (rs.next()) {
            firstName = rs.getString("first_name");
            lastName = rs.getString("last_name");
            formations.add(rs.getString("title"));
        }
        if (!formations.isEmpty()) {
            body = "Bonjour Monsieur/Madame, " + lastName + " " + firstName + ", n'oubliez pas vos formations aujourd'hui: " + String.join(", ", formations) + ".";
            Message message = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(fromNumber), body).create();
            System.out.println("Message Status: " + message.getStatus());
            markSmsSent(loggedinEmployee.getId(), true);
        }
        rs.close();
        ps.close();
    }
}
