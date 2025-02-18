package com.PIDev3A18.projet;

import entity.JobOffer;
import services.JobOfferService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class JobOfferListController {

    @FXML
    private ListView<JobOffer> listView;

    private JobOfferService jobOfferService;

    public JobOfferListController() {
        this.jobOfferService = new JobOfferService();
    }

    @FXML
    public void initialize() {
        try {
            // Fetch all job offers
            List<JobOffer> jobOffers = jobOfferService.readAll();

            // Populate the ListView with job offers
            listView.getItems().setAll(jobOffers);

            // Custom cell factory to display job offer details with formatting
            listView.setCellFactory(param -> new javafx.scene.control.ListCell<JobOffer>() {
                @Override
                protected void updateItem(JobOffer jobOffer, boolean empty) {
                    super.updateItem(jobOffer, empty);
                    if (empty || jobOffer == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Create a layout for each list item (job offer)
                        HBox hBox = new HBox(10);
                        Label titleLabel = new Label(jobOffer.getTitle());
                        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                        Label descriptionLabel = new Label(jobOffer.getDescription());
                        descriptionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
                        hBox.getChildren().addAll(titleLabel, descriptionLabel);
                        hBox.setPadding(new Insets(10));
                        setGraphic(hBox);
                    }
                }
            });

            // Handle item click on ListView
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    applyForJob(newValue);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load job offers from the database.", AlertType.ERROR);
        }
    }

    private void applyForJob(JobOffer selectedJob) {
        try {
            // Load the Application.fxml file using an absolute resource path.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Application.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the controller of the Application.fxml and pass the selected job offer.
            ApplicationController applicationController = loader.getController();
            applicationController.setJobOffer(selectedJob);

            // Set the new scene on the current stage.
            Stage stage = (Stage) listView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the application form.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
