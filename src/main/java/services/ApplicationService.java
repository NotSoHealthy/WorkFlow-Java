package services;

import entity.Applications;
import entity.Employee;
import entity.JobOffer;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationService implements IService<Applications> {
    private Connection con;

    public ApplicationService() {
        con = DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(Applications application) throws SQLException {
        String query = "INSERT INTO application (job, CV, Cover_Letter, Submission_Date, Status, user, First_Name, Last_Name, mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            if (isJobOfferValid(application.getJobId().getJobId())) {
                ps.setInt(1, application.getJobId().getJobId());
                ps.setString(2, application.getCv());
                ps.setString(3, application.getCoverLetter());
                ps.setDate(4, new java.sql.Date(application.getSubmissionDate().getTime()));
                ps.setString(5, application.getStatus());
                // Use the employer's id from the job offer as the foreign key.
                ps.setInt(6, application.getJobId().getEmployeeId().getId());
                // Use the candidate's first and last name from the Applications object's own fields.
                ps.setString(7, application.getFirst_Name());
                ps.setString(8, application.getLast_Name());
                ps.setString(9, application.getMail());
                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted into applications.");
            } else {
                System.out.println("Invalid Job ID: " + application.getJobId());
            }
        }
    }

    @Override
    public void update(Applications application) throws SQLException {
        String query = "UPDATE application SET job = ?, CV = ?, Cover_Letter = ?, Submission_Date = ?, Status = ?, user = ?, First_Name = ?, Last_Name = ?, mail = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            if (isJobOfferValid(application.getJobId().getJobId())) {
                ps.setInt(1, application.getJobId().getJobId());
                ps.setString(2, application.getCv());
                ps.setString(3, application.getCoverLetter());
                ps.setDate(4, new java.sql.Date(application.getSubmissionDate().getTime()));
                ps.setString(5, application.getStatus());
                ps.setInt(6, application.getEmployeeId().getId());
                ps.setString(7, application.getFirst_Name());
                ps.setString(8, application.getLast_Name());
                ps.setString(9, application.getMail());
                ps.setInt(10, application.getApplicationId());
                int rowsAffected = ps.executeUpdate();
                System.out.println(rowsAffected + " row(s) updated in applications.");
            } else {
                System.out.println("Invalid Job ID: " + application.getJobId());
            }
        }
    }

    private boolean isJobOfferValid(int jobId) throws SQLException {
        String query = "SELECT COUNT(*) FROM job_offer WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public void delete(Applications application) throws SQLException {
        String query = "DELETE FROM application WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, application.getApplicationId());
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted from applications.");
        }
    }

    public List<Applications> readAll() throws SQLException {
        String query = "SELECT * FROM application";
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            List<Applications> applications = new ArrayList<>();
            while (rs.next()) {
                JobOfferService serviceJobOffer = new JobOfferService();
                ServiceEmployee serviceEmployee = new ServiceEmployee();

                JobOffer jobOffer = serviceJobOffer.readById(rs.getInt("job"));
                Employee employee = serviceEmployee.readById(rs.getInt("user"));

                Applications app = new Applications(
                        rs.getInt("id"),
                        jobOffer,
                        employee,
                        rs.getString("CV"),
                        rs.getString("Cover_Letter"),
                        rs.getDate("Submission_Date"),
                        rs.getString("Status"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getString("mail")
                );
                applications.add(app);
            }
            return applications;
        }
    }

    @Override
    public Applications readById(int id) throws SQLException {
        String query = "SELECT * FROM application WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JobOfferService serviceJobOffer = new JobOfferService();
                    ServiceEmployee serviceEmployee = new ServiceEmployee();

                    JobOffer jobOffer = serviceJobOffer.readById(rs.getInt("job"));
                    Employee employee = serviceEmployee.readById(rs.getInt("user"));

                    Applications app = new Applications(
                            rs.getInt("id"),
                            jobOffer,
                            employee,
                            rs.getString("CV"),
                            rs.getString("Cover_Letter"),
                            rs.getDate("Submission_Date"),
                            rs.getString("Status"),
                            rs.getString("First_Name"),
                            rs.getString("Last_Name"),
                            rs.getString("mail")
                    );
                    return app;
                }
            }
        }
        return null;
    }

    public List<Applications> filterByJob(int jobId) throws SQLException {
        String query = "SELECT * FROM application WHERE job = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Applications> applications = new ArrayList<>();
                JobOfferService serviceJobOffer = new JobOfferService();
                ServiceEmployee serviceEmployee = new ServiceEmployee();

                while (rs.next()) {
                    JobOffer jobOffer = serviceJobOffer.readById(rs.getInt("job"));
                    Employee employee = serviceEmployee.readById(rs.getInt("user"));

                    Applications app = new Applications(
                            rs.getInt("id"),
                            jobOffer,
                            employee,
                            rs.getString("CV"),
                            rs.getString("Cover_Letter"),
                            rs.getDate("Submission_Date"),
                            rs.getString("Status"),
                            rs.getString("First_Name"),
                            rs.getString("Last_Name"),
                            rs.getString("mail")
                    );
                    applications.add(app);
                }
                return applications;
            }
        }
    }
}
