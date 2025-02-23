package com.PIDev3A18.projet;

import entity.Applications;
import entity.JobOffer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import services.ApplicationService;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ApplicationListController {

    @FXML
    private ListView<Applications> listView;

    @FXML
    private ComboBox<JobOffer> jobFilterComboBox;

    // TextField for dynamic candidate search.
    @FXML
    private TextField searchTextField;

    // New ComboBox to sort applications by Submission_Date.
    @FXML
    private ComboBox<String> dateSortComboBox;

    private ApplicationService applicationService;
    private List<Applications> allApplications;

    public ApplicationListController() {
        this.applicationService = new ApplicationService();
    }

    @FXML
    public void initialize() {
        try {
            // Load all applications from the database.
            allApplications = applicationService.readAll();
            listView.getItems().setAll(allApplications);

            // Set up the ListView cell factory.
            listView.setCellFactory(param -> new ListCell<Applications>() {
                @Override
                protected void updateItem(Applications application, boolean empty) {
                    super.updateItem(application, empty);
                    if (empty || application == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
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

            // When an application is selected, display its details.
            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    viewApplicationDetails(newValue);
                }
            });

            // --------- Setup for Job Filter ComboBox -----------
            jobFilterComboBox.getItems().clear();
            JobOffer allJobs = new JobOffer(0, "All Jobs");
            jobFilterComboBox.getItems().add(allJobs);
            Set<JobOffer> jobOffers = new HashSet<>();
            for (Applications app : allApplications) {
                jobOffers.add(app.getJobId());
            }
            jobFilterComboBox.getItems().addAll(jobOffers);
            jobFilterComboBox.setValue(allJobs);
            jobFilterComboBox.setCellFactory(lv -> new ListCell<JobOffer>() {
                @Override
                protected void updateItem(JobOffer item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getTitle());
                }
            });
            jobFilterComboBox.setButtonCell(new ListCell<JobOffer>() {
                @Override
                protected void updateItem(JobOffer item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getTitle());
                }
            });

            // --------- Setup for Date Sort ComboBox -----------
            dateSortComboBox.getItems().clear();
            dateSortComboBox.getItems().addAll("Ascending", "Descending");
            dateSortComboBox.setValue("Ascending");

            // --------- Add listeners to update the filtered list ---------
            jobFilterComboBox.setOnAction(e -> updateFilteredList());
            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> updateFilteredList());
            dateSortComboBox.setOnAction(e -> updateFilteredList());

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load applications from the database.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Updates the ListView based on the current job filter, search text, and date sort order.
     */
    private void updateFilteredList() {
        JobOffer selectedJob = jobFilterComboBox.getValue();
        String searchText = searchTextField.getText().toLowerCase().trim();
        String sortOption = dateSortComboBox.getValue();

        List<Applications> filtered = allApplications.stream()
                .filter(app -> {
                    // Filter by job (if not "All Jobs").
                    boolean jobMatches = (selectedJob == null || selectedJob.getJobId() == 0) ||
                            (app.getJobId().getJobId() == selectedJob.getJobId());
                    // Filter by candidate first or last name.
                    boolean searchMatches = searchText.isEmpty() ||
                            (app.getFirst_Name().toLowerCase().contains(searchText) ||
                                    app.getLast_Name().toLowerCase().contains(searchText));
                    return jobMatches && searchMatches;
                })
                .collect(Collectors.toList());

        // Sort the filtered list by Submission_Date.
        if (sortOption != null) {
            if (sortOption.equals("Ascending")) {
                filtered.sort(Comparator.comparing(Applications::getSubmissionDate));
            } else if (sortOption.equals("Descending")) {
                filtered.sort(Comparator.comparing(Applications::getSubmissionDate).reversed());
            }
        }

        listView.getItems().setAll(filtered);
    }

    private void viewApplicationDetails(Applications selectedApplication) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationInformation.fxml"));
            Node applicationInfoView = loader.load();

            ApplicationInfoController applicationInfoController = loader.getController();
            applicationInfoController.setApplication(selectedApplication);

            BorderPane mainLayout = (BorderPane) listView.getScene().getRoot();
            mainLayout.setCenter(applicationInfoView);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the application details.", Alert.AlertType.ERROR);
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
