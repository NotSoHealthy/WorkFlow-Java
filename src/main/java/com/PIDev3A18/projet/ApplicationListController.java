package com.PIDev3A18.projet;

import entity.Applications;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import services.ApplicationService;
import javafx.fxml.FXML;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;



public class ApplicationListController {

    @FXML
    private ListView<Applications> listView;

    private ApplicationService applicationService;



    public ApplicationListController() {
        this.applicationService = new ApplicationService();
    }

    @FXML
    public void initialize() {
        try {
            // Fetch all applications
            List<Applications> applications = applicationService.readAll();

            // Populate the ListView with applications
            listView.getItems().setAll(applications);

            // Custom cell factory to display application details with formatting
            listView.setCellFactory(param -> new javafx.scene.control.ListCell<Applications>() {
                @Override
                protected void updateItem(Applications application, boolean empty) {
                    super.updateItem(application, empty);
                    if (empty || application == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Create a layout for each list item (application)
                        HBox hBox = new HBox(10);
                        Label nameLabel = new Label(application.getFirst_Name() + " " + application.getLast_Name());
                        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                        Label statusLabel = new Label(application.getStatus());
                        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
                        hBox.getChildren().addAll(nameLabel, statusLabel);
                        hBox.setPadding(new Insets(10));
                        setGraphic(hBox);
                    }
                }
            });

            // Handle item click on ListView
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    viewApplicationDetails(newValue);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load applications from the database.", AlertType.ERROR);
        }
    }

    private void viewApplicationDetails(Applications selectedApplication) {
        try {
            // Load the ApplicationInformation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationInformation.fxml"));
            Node applicationInfoView = loader.load();

            // Get the controller and pass the selected application
            ApplicationInfoController applicationInfoController = loader.getController();
            applicationInfoController.setApplication(selectedApplication);

            // Get the current scene's root (which should be your BorderPane layout)
            BorderPane mainLayout = (BorderPane) listView.getScene().getRoot();

            // Set the ApplicationInformation view in the center, preserving the left navigation
            mainLayout.setCenter(applicationInfoView);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the application details.", Alert.AlertType.ERROR);
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
