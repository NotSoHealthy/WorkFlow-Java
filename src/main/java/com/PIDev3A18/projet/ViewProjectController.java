package com.PIDev3A18.projet;

import entity.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import services.ServiceProject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewProjectController {

    @FXML private ListView<Project> ShowProject;
    @FXML private Button DeleteBtn;
    @FXML private Button UpdateBtn;
    @FXML private Button AddBtn;

    private final ServiceProject serviceProject = new ServiceProject();

    @FXML
    void initialize() throws SQLException {
        loadProjects();
        ShowProject.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                DeleteBtn.setDisable(false);
                UpdateBtn.setDisable(false);
            }
        });

        // Set custom cell factory for the ListView
        ShowProject.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Project> call(ListView<Project> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Project project, boolean empty) {
                        super.updateItem(project, empty);
                        if (project == null || empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(project.getName() + "\n" +
                                    "Description: " + project.getDescription() + "\n" +
                                    "Start Date: " + project.getStart_Date() + "\n" +
                                    "End Date: " + project.getEnd_Date() + "\n" +
                                    "Budget: " + project.getBudget() + "\n" +
                                    "Manager: " + project.getProject_Manager().getFirstName() + " " + project.getProject_Manager().getLastName());
                        }
                    }
                };
            }
        });
    }

    private void loadProjects() throws SQLException {
        List<Project> projects = serviceProject.readAll();
        ObservableList<Project> projectList = FXCollections.observableArrayList(projects);
        ShowProject.setItems(projectList);
    }

    @FXML
    void deleteProject(ActionEvent event) {
        Project selectedProject = ShowProject.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            try {
                serviceProject.delete(selectedProject);
                loadProjects(); // Reload the list of projects after deletion
                showAlert(Alert.AlertType.INFORMATION, "Success", "Project deleted successfully.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete the project.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Project Selected", "Please select a project to delete.");
        }
    }

    @FXML
    void updateProject(ActionEvent event) {
        Project selectedProject = ShowProject.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            try {
                // Load the UpdateProject.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateProject.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the selected project
                UpdateProjectController updateController = loader.getController();
                updateController.setSelectedProject(selectedProject);

                // Set the new scene
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the update form.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Project Selected", "Please select a project to update.");
        }
    }
    @FXML
    void addProject(ActionEvent event) {
        try {
            // Load the AddProject.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProject.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the add project form.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}