package com.PIDev3A18.projet;

import entity.JobOffer;
import services.JobOfferService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

public class JobOfferListController {

    @FXML
    private ListView<JobOffer> listView;

    @FXML
    private TextField searchTextField;

    // ComboBox for sorting job offers by Publication_Date.
    @FXML
    private ComboBox<String> dateSortComboBox;

    @FXML
    private ImageView returnBtn;

    private JobOfferService jobOfferService;
    // Hold the original list for filtering and sorting.
    private List<JobOffer> allJobOffers;

    public JobOfferListController() {
        this.jobOfferService = new JobOfferService();
    }

    @FXML
    public void initialize() {
        try {
            // Load all job offers from the database.
            allJobOffers = jobOfferService.readAll();
            listView.getItems().setAll(allJobOffers);

            // Set up the ListView cell factory.
            listView.setCellFactory(param -> new ListCell<JobOffer>() {
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

            // Add listeners for dynamic search and sorting.
            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> updateFilteredList());
            dateSortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> updateFilteredList());

            // Set up sort options.
            dateSortComboBox.getItems().clear();
            dateSortComboBox.getItems().addAll("Ascending", "Descending");
            dateSortComboBox.setValue("Ascending");

            // When a job offer is selected, proceed to apply.
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    applyForJob(newValue);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load job offers from the database.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Updates the ListView based on the search text (by title) and the sort order.
     */
    private void updateFilteredList() {
        String searchText = searchTextField.getText().toLowerCase().trim();
        String sortOption = dateSortComboBox.getValue();

        List<JobOffer> filtered = allJobOffers.stream()
                .filter(offer -> searchText.isEmpty() || offer.getTitle().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        // Sort the filtered list by Publication_Date.
        if (sortOption != null) {
            if (sortOption.equals("Ascending")) {
                filtered.sort(Comparator.comparing(JobOffer::getPublicationDate));
            } else if (sortOption.equals("Descending")) {
                filtered.sort(Comparator.comparing(JobOffer::getPublicationDate).reversed());
            }
        }

        listView.getItems().setAll(filtered);
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
            showAlert("Error", "Failed to load the application form.", Alert.AlertType.ERROR);
        }
    }

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

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
