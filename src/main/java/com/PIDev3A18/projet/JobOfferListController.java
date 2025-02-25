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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class JobOfferListController {

    @FXML
    private ListView<JobOffer> listView;

    private JobOfferService jobOfferService;

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
            showAlert("Error", "Failed to load the login page.", Alert.AlertType.ERROR);
        }

    }

    public JobOfferListController() {
        this.jobOfferService = new JobOfferService();
    }

    @FXML
    public void initialize() {
        try {
            List<JobOffer> jobOffers = jobOfferService.readAll();

            listView.getItems().setAll(jobOffers);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Application.fxml"));
            Scene scene = new Scene(loader.load());

            ApplicationController applicationController = loader.getController();
            applicationController.setJobOffer(selectedJob);

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