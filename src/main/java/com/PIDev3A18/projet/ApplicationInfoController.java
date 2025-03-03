package com.PIDev3A18.projet;

import entity.Applications;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import services.ApplicationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private ComboBox<String> statusLabel;

    @FXML
    private Button submitButton;

    @FXML
    private Button viewCvButton;

    @FXML
    private Button viewCoverLetterButton;

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
        if (firstNameLabel != null) {
            initialize();
        }
    }

    @FXML
    private void submitButton(ActionEvent event) {
        String newStatus = statusLabel.getValue();
        selectedApplication.setStatus(newStatus);

        try {
            applicationService.update(selectedApplication);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Application status updated successfully!");

            Node source = (Node) event.getSource();
            BorderPane mainLayout = (BorderPane) source.getScene().getRoot();

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
        String cvPath = "D:\\3eme\\pidev\\WorkFlow\\WorkFlow-Java\\uploads\\cv\\" + selectedApplication.getCv();
        openPdfFile(cvPath);
    }

    @FXML
    private void handleCoverLetterAction(ActionEvent event) {
        String coverLetterPath = "D:\\3eme\\pidev\\WorkFlow\\WorkFlow-Java\\uploads\\coverletters\\" + selectedApplication.getCoverLetter();
        openPdfFile(coverLetterPath);
    }

    @FXML
    private void handleInterviewButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterviewInfo.fxml"));
            Parent root = loader.load();
            InterviewInfoController controller = loader.getController();
            controller.setApplication(selectedApplication);

            Stage stage = new Stage();
            stage.setTitle("Interview Information");
            stage.setScene(new Scene(root, 400, 300)); // Smaller window size
            stage.initModality(Modality.APPLICATION_MODAL); // Makes it a modal popup
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open the interview information window.");
        }
    }

    private void openPdfFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
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