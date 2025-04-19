package services;

import entity.Employee;
import entity.JobOffer;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobOfferService implements IService<JobOffer> {
    private Connection con;

    public JobOfferService() {
        con = DBConnection.getInstance().getConnection();
    }

    @Override
    public void add(JobOffer jobOffer) throws SQLException {
        String query = "INSERT INTO job_offer (Title, Description, Publication_Date, Expiration_Date, Contract_Type, Salary, user) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, jobOffer.getTitle());
            ps.setString(2, jobOffer.getDescription());
            ps.setDate(3, new java.sql.Date(jobOffer.getPublicationDate().getTime()));
            ps.setDate(4, new java.sql.Date(jobOffer.getExpirationDate().getTime()));
            ps.setString(5, jobOffer.getContractType());
            ps.setDouble(6, jobOffer.getSalary());
            ps.setInt(7, jobOffer.getEmployeeId().getId());
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted into job_offer.");
        }
    }

    @Override
    public void update(JobOffer jobOffer) throws SQLException {
        String query = "UPDATE job_offer SET Title = ?, Description = ?, Publication_Date = ?, Expiration_Date = ?, Contract_Type = ?, Salary = ?, user = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, jobOffer.getTitle());
            ps.setString(2, jobOffer.getDescription());
            ps.setDate(3, new java.sql.Date(jobOffer.getPublicationDate().getTime()));
            ps.setDate(4, new java.sql.Date(jobOffer.getExpirationDate().getTime()));
            ps.setString(5, jobOffer.getContractType());
            ps.setDouble(6, jobOffer.getSalary());
            ps.setInt(7, jobOffer.getEmployeeId().getId()); // setting the foreign key
            ps.setInt(8, jobOffer.getJobId());
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated in job_offer.");
        }
    }

    @Override
    public void delete(JobOffer jobOffer) throws SQLException {
        String query = "DELETE FROM job_offer WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, jobOffer.getJobId());
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted from job_offer.");
        }
    }

    @Override
    public JobOffer readById(int id) throws SQLException {
        String query = "SELECT * FROM job_offer WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ServiceEmployee serviceEmployee = new ServiceEmployee();

                    Employee employee = serviceEmployee.readById(rs.getInt("user"));

                    return new JobOffer(
                            rs.getInt("id"),
                            rs.getString("Title"),
                            rs.getString("Description"),
                            rs.getDate("Publication_Date"),
                            rs.getDate("Expiration_Date"),
                            rs.getString("Contract_Type"),
                            rs.getDouble("Salary"),
                            employee
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<JobOffer> readAll() throws SQLException {
        String query = "SELECT * FROM job_offer";
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            List<JobOffer> jobOffers = new ArrayList<>();
            while (rs.next()) {
                ServiceEmployee serviceEmployee = new ServiceEmployee();

                Employee employee = serviceEmployee.readById(rs.getInt("user"));

                JobOffer jobOffer = new JobOffer(
                        rs.getInt("id"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getDate("Publication_Date"),
                        rs.getDate("Expiration_Date"),
                        rs.getString("Contract_Type"),
                        rs.getDouble("Salary"),
                        employee
                );
                jobOffers.add(jobOffer);
            }
            return jobOffers;
        }
    }
}
