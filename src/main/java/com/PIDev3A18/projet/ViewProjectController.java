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
import javafx.scene.control.*;
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
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortCombo;

    private final ServiceProject serviceProject = new ServiceProject();
    private ObservableList<Project> projectList;

    @FXML
    void initialize() throws SQLException {
        loadProjects();

        DeleteBtn.setDisable(true);
        UpdateBtn.setDisable(true);
        ShowProject.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            DeleteBtn.setDisable(newValue == null);
            UpdateBtn.setDisable(newValue == null);
        });

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
                                    "Manager: " + project.getProject_Manager().getFirstName() + " " + project.getProject_Manager().getLastName() + "\n" +
                                    "State: " + project.getState()); // Added State
                        }
                    }
                };
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchProjects(null); // Trigger search on text change
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Search Error", "Failed to search projects.");
                e.printStackTrace();
            }
        });
    }

    private void loadProjects() throws SQLException {
        List<Project> projects = serviceProject.readAll();
        projectList = FXCollections.observableArrayList(projects);
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateProject.fxml"));
                Parent root = loader.load();
                UpdateProjectController updateController = loader.getController();
                updateController.setSelectedProject(selectedProject);
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

    @FXML
    void searchProjects(ActionEvent event) throws SQLException {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadProjects(); // Show all projects if search is empty
        } else {
            // Use the searchByName method from ServiceProject
            List<Project> searchedProjects = serviceProject.searchByName(searchText);
            projectList.setAll(searchedProjects);
        }
    }

    @FXML
    void sortProjects(ActionEvent event) throws SQLException {
        String selectedSort = sortCombo.getSelectionModel().getSelectedItem();
        if (selectedSort != null) {
            switch (selectedSort) {
                case "Trier par statut":
                    projectList.setAll(serviceProject.sortState());
                    break;
                case "Trier par date":
                    projectList.setAll(serviceProject.sortDate());
                    break;
                case "Trier par budget":
                    projectList.setAll(serviceProject.sortBudget());
                    break;
                default:
                    loadProjects(); // Default to unsorted list
                    break;
            }
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