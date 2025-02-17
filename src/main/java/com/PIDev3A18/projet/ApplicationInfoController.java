package com.PIDev3A18.projet;

import entity.Applications;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import services.ApplicationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ApplicationInfoController {

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label cvLabel;

    @FXML
    private Label coverLetterLabel;

    @FXML
    private Label submissionDateLabel;

    @FXML
    private ComboBox<String> statusLabel;  // Change Label to ComboBox

    @FXML
    private Button updateStatusButton;

    @FXML
    private Button deleteApplicationButton;

    @FXML
    private Button viewCvButton; // New button to view CV

    @FXML
    private Button viewCoverLetterButton; // New button to view Cover Letter

    private Applications selectedApplication;

    private ApplicationService applicationService;

    public ApplicationInfoController() {
        this.applicationService = new ApplicationService();
    }

    @FXML
    public void initialize() {
        if (selectedApplication != null) {
            firstNameLabel.setText(selectedApplication.getFirst_Name());
            lastNameLabel.setText(selectedApplication.getLast_Name());
            emailLabel.setText(selectedApplication.getMail());
            cvLabel.setText(selectedApplication.getCv());
            coverLetterLabel.setText(selectedApplication.getCoverLetter());
            submissionDateLabel.setText(selectedApplication.getSubmissionDate().toString());

            // Set ComboBox items and selection
            statusLabel.getItems().setAll("Pending", "Reviewed");
            statusLabel.setValue(selectedApplication.getStatus());
        }
    }

    public void setApplication(Applications application) {
        this.selectedApplication = application;
        // Populate the labels after the application is set
        if (firstNameLabel != null) {  // Check if labels are initialized before setting text
            initialize();
        }
    }

    @FXML
    private void submitButton(ActionEvent event) {
        String newStatus = statusLabel.getValue(); // Get the selected value from ComboBox
        selectedApplication.setStatus(newStatus);

        try {
            applicationService.update(selectedApplication);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Application status updated successfully!");

            // Use the event's source to get the current scene's root.
            Node source = (Node) event.getSource();
            BorderPane mainLayout = (BorderPane) source.getScene().getRoot();

            // Load the ApplicationList view.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationList.fxml"));
            Node applicationListView = loader.load();
            mainLayout.setCenter(applicationListView);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update the application status.");
        }
    }



    @FXML
    private void handleCvAction(ActionEvent event) {
        // Full path to the CV file
        String cvPath = "D:\\3eme\\pidev\\WorkFlow\\WorkFlow-Java\\uploads\\cv\\" + selectedApplication.getCv();
        openPdfFile(cvPath);
    }

    @FXML
    private void handleCoverLetterAction(ActionEvent event) {
        // Full path to the Cover Letter file
        String coverLetterPath = "D:\\3eme\\pidev\\WorkFlow\\WorkFlow-Java\\uploads\\coverletters\\" + selectedApplication.getCoverLetter();
        openPdfFile(coverLetterPath);
    }

    // Method to open PDF file using the system's default PDF viewer
    private void openPdfFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);  // Opens the file with the system's default application
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not open the file.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "File not found.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




}
