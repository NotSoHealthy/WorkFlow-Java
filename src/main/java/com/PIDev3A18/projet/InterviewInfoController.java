package com.PIDev3A18.projet;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import entity.Applications;
import entity.Interviews;
import entity.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.GmailService;
import services.InterviewService;
import utils.UserSession;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message.RecipientType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Base64;

public class InterviewInfoController {

    @FXML private DatePicker interviewDatePicker;
    @FXML private TextField locationField;
    @FXML private TextArea feedbackArea;
    @FXML private ComboBox<String> statusComboBox;

    private Applications selectedApplication;
    private InterviewService interviewService = new InterviewService();

    public void setApplication(Applications application) {
        this.selectedApplication = application;
    }

    @FXML
    public void initialize() {
        statusComboBox.getItems().addAll("Scheduled", "Completed", "Cancelled");
        statusComboBox.setValue("Scheduled");
    }

    @FXML
    private void handleSendAction(ActionEvent event) {
        LocalDate interviewDate = interviewDatePicker.getValue();
        String location = locationField.getText();
        String feedback = feedbackArea.getText();
        String status = statusComboBox.getValue();

        if (interviewDate == null || location == null || location.isEmpty() || status == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please fill in all required fields.");
            return;
        }

        UserSession userSession = UserSession.getInstance();
        Employee loggedinEmployee = userSession.getLoggedInEmployee();

        if (loggedinEmployee == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No logged-in employee found. Please log in again.");
            return;
        }

        if (selectedApplication == null || selectedApplication.getMail() == null || selectedApplication.getMail().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Application or email address is missing.");
            return;
        }

        Interviews interview = new Interviews(
                selectedApplication,
                loggedinEmployee,
                java.sql.Date.valueOf(interviewDate),
                location,
                feedback,
                status
        );

        try {
            interviewService.add(interview);
            sendEmail(selectedApplication.getMail(), interview);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Interview information saved and email sent.");
            ((Stage) interviewDatePicker.getScene().getWindow()).close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save interview information: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to send email: " + e.getMessage());
        }
    }

    private void sendEmail(String recipient, Interviews interview) throws MessagingException, IOException {
        try {
            Gmail service = GmailService.getGmailService();
            String from = "your-email@gmail.com";
            String subject = "Interview Details";
            String bodyText = "Dear Applicant,\n\nHere are your interview details:\n" +
                    "Date: " + interview.getInterviewDate() + "\n" +
                    "Location: " + interview.getLocation() + "\n" +
                    "Feedback: " + interview.getFeedback() + "\n" +
                    "Status: " + interview.getStatus() + "\n\n" +
                    "Best regards,\nYour Company";

            MimeMessage email = createEmail(recipient, from, subject, bodyText);
            Message message = createMessageWithEmail(email);
            service.users().messages().send("me", message).execute();
            System.out.println("Email sent to: " + recipient);
        } catch (Exception e) {
            throw new IOException("Email sending failed: " + e.getMessage(), e);
        }
    }

    private MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}