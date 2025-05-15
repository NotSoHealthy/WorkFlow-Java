package services;

import entity.Interviews;
import entity.Applications;
import entity.Employee;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterviewService implements IService<Interviews> {
    private Connection con;

    public InterviewService() {
        con = DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(Interviews interview) throws SQLException {
        if (isApplicationValid(interview.getApplicationId().getApplicationId())) {
            String query = "INSERT INTO interview (application, Interview_Date, Location, Feedback, Status, user) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, interview.getApplicationId().getApplicationId());
                ps.setDate(2, new java.sql.Date(interview.getInterviewDate().getTime()));
                ps.setString(3, interview.getLocation());
                ps.setString(4, interview.getFeedback());
                ps.setString(5, interview.getStatus());
                ps.setInt(6, interview.getEmployeeId().getId()); //
                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted into interviews.");
            }
        } else {
            System.out.println("Invalid Application ID: " + interview.getApplicationId());
        }
    }

    @Override
    public void update(Interviews interview) throws SQLException {
        if (isApplicationValid(interview.getApplicationId().getApplicationId())) {
            String query = "UPDATE interview SET application = ?, Interview_Date = ?, Location = ?, Feedback = ?, Status = ?, user = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, interview.getApplicationId().getApplicationId());
                ps.setDate(2, new java.sql.Date(interview.getInterviewDate().getTime()));
                ps.setString(3, interview.getLocation());
                ps.setString(4, interview.getFeedback());
                ps.setString(5, interview.getStatus());
                ps.setInt(6, interview.getEmployeeId().getId());
                ps.setInt(7, interview.getInterviewId());
                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " row(s) updated in interviews.");
            }
        } else {
            System.out.println("Invalid Application ID: " + interview.getApplicationId());
        }
    }

    private boolean isApplicationValid(int applicationId) throws SQLException {
        String query = "SELECT COUNT(*) FROM application WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public void delete(Interviews interview) throws SQLException {
        String query = "DELETE FROM interview WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, interview.getInterviewId());
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted from interviews.");
        }
    }

    public Interviews readById(int id) throws SQLException {
        String query = "SELECT * FROM interview WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ApplicationService applicationService = new ApplicationService();
                    ServiceEmployee serviceEmployee = new ServiceEmployee();

                    Applications application = applicationService.readById(rs.getInt("application"));
                    Employee employee = serviceEmployee.readById(rs.getInt("Employee_ID"));

                    return new Interviews(
                            rs.getInt("id"),
                            application,
                            employee,
                            rs.getDate("Interview_Date"),
                            rs.getString("Location"),
                            rs.getString("Feedback"),
                            rs.getString("Status")
                    );
                }
            }
        }
        return null;
    }

    public List<Interviews> readAll() throws SQLException {
        String query = "SELECT * FROM interview";
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            List<Interviews> interviews = new ArrayList<>();
            while (rs.next()) {
                ApplicationService applicationService = new ApplicationService();
                ServiceEmployee serviceEmployee = new ServiceEmployee();

                Applications application = applicationService.readById(rs.getInt("application"));
                Employee employee = serviceEmployee.readById(rs.getInt("user"));

                Interviews interview = new Interviews(
                        rs.getInt("id"),
                        application,
                        employee,
                        rs.getDate("Interview_Date"),
                        rs.getString("Location"),
                        rs.getString("Feedback"),
                        rs.getString("Status")
                );
                interviews.add(interview);
            }
            return interviews;
        }
    }
}
