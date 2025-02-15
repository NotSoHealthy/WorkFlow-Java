package com.PIDev3A18.projet;


import entity.Reservation;
import services.ServiceEmployee;
import services.ServiceEvent;
import entity.Event;
import services.ServiceReservation;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import entity.Applications;
import entity.Employee;
import entity.JobOffer;
import entity.Interviews;
import services.ApplicationService;
import services.InterviewService;
import services.JobOfferService;
import services.ServiceEmployee;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class test {
    public static void main(String[] args) {
        try {
            ServiceEmployee serviceEmployee= new ServiceEmployee();
            Employee employee =serviceEmployee.readById(1);


            JobOfferService jobService = new JobOfferService();
            Date publicationDate = new Date();
            Date expirationDate = new Date(System.currentTimeMillis() + 86400000L); // 1 day later
            JobOffer jobOffer = new JobOffer(
                    "Software Engineer 21344",
                    "Develop and maintain software",
                    publicationDate,
                    expirationDate,
                    "Full-Time",
                    2400,
                    employee
            );
            jobService.add(jobOffer);
            System.out.println("Job Offer Added Successfully!");

            List<JobOffer> jobOffers = jobService.readAll();




            JobOffer jobOfferId = jobService.readById(2);

            ApplicationService appService = new ApplicationService();
            Applications application = new Applications(
                    jobOfferId,
                    employee,
                    "CV_Pathsaddsa.pdf",
                    "Cover Letter Text",
                    new Date(),
                    "Pending"
              );
            appService.add(application);
            System.out.println("Application Added Successfully!");

            List<Applications> applications = appService.readAll();
            System.out.println("All Applications: " + applications);


            Applications applicationId = appService.readById(4);

            InterviewService interviewService = new InterviewService();
            Interviews interview = new Interviews(
                    applicationId,
                    employee,
                    new Date(),
                    "Online",
                    "Good Candidate",
                    "Scheduled"
            );
            interviewService.add(interview);

            List<Interviews> interviews = interviewService.readAll();
            System.out.println("All Interviews: " + interviews);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
