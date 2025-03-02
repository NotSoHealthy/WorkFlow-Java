package com.PIDev3A18.projet;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.util.Base64;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

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

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

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
            String from = "your-email@gmail.com"; // Replace with your Gmail address
            String subject = "Interview Details";
            String bodyText = "Dear Applicant,\n\nHere are your interview details:\n" +
                    "Date: " + interview.getInterviewDate() + "\n" +
                    "Location: " + interview.getLocation() + "\n" +
                    "Feedback: " + interview.getFeedback() + "\n" +
                    "Status: " + interview.getStatus() + "\n\n" +
                    "Please see the attached QR code for a quick summary.\n\n" +
                    "Best regards,\nYour Company";

            // Generate QR code image bytes containing the interview details
            byte[] qrCodeBytes = generateQRCodeImage(
                    "Date: " + interview.getInterviewDate() + "\n" +
                            "Location: " + interview.getLocation() + "\n" +
                            "Feedback: " + interview.getFeedback() + "\n" +
                            "Status: " + interview.getStatus()
            );

            // Create an email with text and QR code attachment
            MimeMessage email = createEmailWithQRCode(recipient, from, subject, bodyText, qrCodeBytes);
            Message message = createMessageWithEmail(email);
            service.users().messages().send("me", message).execute();
            System.out.println("Email sent to: " + recipient);
        } catch (Exception e) {
            throw new IOException("Email sending failed: " + e.getMessage(), e);
        }
    }

    // Generate QR code image bytes using ZXing
    private byte[] generateQRCodeImage(String text) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    // Create an email with both text and the QR code image as an inline attachment
    private MimeMessage createEmailWithQRCode(String to, String from, String subject, String bodyText, byte[] qrCodeImageBytes) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        // Create text part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(bodyText, "utf-8");

        // Create image part for the QR code
        MimeBodyPart imagePart = new MimeBodyPart();
        DataSource ds = new ByteArrayDataSource(qrCodeImageBytes, "image/png");
        imagePart.setDataHandler(new DataHandler(ds));
        imagePart.setFileName("qrcode.png");
        imagePart.setHeader("Content-ID", "<qrcode>");
        imagePart.setDisposition(MimeBodyPart.INLINE);

        // Combine parts into a multipart
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(imagePart);

        email.setContent(multipart);
        return email;
    }

    // Convert MimeMessage to Gmail Message
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
