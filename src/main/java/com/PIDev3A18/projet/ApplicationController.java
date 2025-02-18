package com.PIDev3A18.projet;

import entity.Applications;
import entity.JobOffer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ApplicationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ApplicationController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private Button uploadCVButton;

    @FXML
    private Button uploadCoverLetterButton;

    @FXML
    private Label cvFileNameLabel;

    @FXML
    private Label coverLetterFileNameLabel;

    @FXML
    private Button submitApplicationButton;

    private File cvFile;
    private File coverLetterFile;

    private JobOffer selectedJob;



    @FXML
    private ImageView returnBtn;


    @FXML
    void returnBtn(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) returnBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void setJobOffer(JobOffer selectedJob) {
        this.selectedJob = selectedJob;
    }

    @FXML
    void uploadCVButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        cvFile = fileChooser.showOpenDialog(null);
        if (cvFile != null) {
            cvFileNameLabel.setText(cvFile.getName());
        }
    }

    @FXML
    void uploadCoverLetterButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        coverLetterFile = fileChooser.showOpenDialog(null);
        if (coverLetterFile != null) {
            coverLetterFileNameLabel.setText(coverLetterFile.getName());
        }
    }

    @FXML
    void submitApplicationButton(ActionEvent event) {
        // Validate form fields
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || cvFile == null || coverLetterFile == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
            return;
        }

        try {
            // Upload files to designated directories
            String cvUploadedName = uploadFile(cvFile, "uploads/cv");
            String coverLetterUploadedName = uploadFile(coverLetterFile, "uploads/coverletters");


            Applications application = new Applications(
                    selectedJob,                              // The selected job offer.
                    selectedJob.getEmployeeId(),              // Employee from the job offer (employer).
                    cvUploadedName,                           // Uploaded CV file name (or path)
                    coverLetterUploadedName,                  // Uploaded cover letter file name (or path)
                    new Date(System.currentTimeMillis()),     // Submission date.
                    "Pending",                                // Status.
                    firstNameField.getText(),                 // Candidate's first name.
                    lastNameField.getText(),                  // Candidate's last name.
                    emailField.getText()                      // Candidate's email.
            );

            ApplicationService applicationService = new ApplicationService();
            applicationService.add(application);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Application submitted successfully!");

            firstNameField.clear();
            lastNameField.clear();
            emailField.clear();  // Clear the email field
            cvFileNameLabel.setText("No file chosen");
            coverLetterFileNameLabel.setText("No file chosen");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while submitting the application.");
        }
    }


    private String uploadFile(File file, String destinationFolder) throws IOException {
        Path destDir = Paths.get(destinationFolder);
        if (!Files.exists(destDir)) {
            Files.createDirectories(destDir);
        }

        String newFileName = System.currentTimeMillis() + "_" + file.getName();
        Path destPath = destDir.resolve(newFileName);
        Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
