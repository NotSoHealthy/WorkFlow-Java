package com.PIDev3A18.projet;

import entity.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.ServiceDepartment;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class ViewDepartmentController {

    @FXML private ListView<Department> DepartmentList;
    @FXML private Button DeleteBtn;
    @FXML private Button UpdateBtn;
    @FXML private Button AddBtn;
    @FXML private TextField searchField; // Field for search
    @FXML private ComboBox<String> searchSortCombo; // Field for sort
    @FXML private ImageView statsIcon; // New field for statistics icon

    private final ServiceDepartment serviceDepartment = new ServiceDepartment();
    private ObservableList<Department> departmentList; // Store departments for manipulation

    @FXML
    void initialize() throws SQLException {
        loadDepartments();

        DeleteBtn.setDisable(true);
        UpdateBtn.setDisable(true);
        // Ensure statsIcon is visible and functional from the start (no disable)

        DepartmentList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            DeleteBtn.setDisable(newValue == null);
            UpdateBtn.setDisable(newValue == null);
            // No need to disable statsIcon here since it’s always visible and functional
        });

        // Set a custom cell factory for the ListView, matching ViewProjectController
        DepartmentList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                if (department == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(department.getName() + "\n" +
                            "Budget: " + department.getYear_Budget() + "\n" +
                            "Manager: " + department.getDepartment_Manager().getFirstName() + " " + department.getDepartment_Manager().getLastName());
                }
            }
        });

        // Add listener for real-time search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchDepartments(null);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Search Error", "Failed to search departments.");
                e.printStackTrace();
            }
        });

        // Load the stats icon image and add click handler with detailed error handling
        try {
            InputStream input = getClass().getResourceAsStream("icons/Stat.png");
            if (input == null) {
                throw new IllegalArgumentException("Resource 'icons/Stat.png' not found");
            }
            statsIcon.setImage(new Image(input, 30, 30, true, true));
            System.out.println("Successfully loaded icon from: icons/Stat.png");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Image Error", "Could not load statistics icon: " + e.getMessage());
            e.printStackTrace();
        }

        statsIcon.setOnMouseClicked(event -> {
            // Show stats for all departments, no department selection required
            showAllDepartmentStats();
        });
    }

    private void loadDepartments() throws SQLException {
        List<Department> departments = serviceDepartment.readAll();
        departmentList = FXCollections.observableArrayList(departments);
        DepartmentList.setItems(departmentList);
    }

    @FXML
    void deleteDepartment(ActionEvent event) {
        Department selectedDepartment = DepartmentList.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            try {
                serviceDepartment.delete(selectedDepartment);
                loadDepartments(); // Reload the list of departments after deletion
                showAlert(Alert.AlertType.INFORMATION, "Success", "Department deleted successfully.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete the department.");
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Department Selected", "Please select a department to delete.");
        }
    }

    @FXML
    void updateDepartment(ActionEvent event) {
        Department selectedDepartment = DepartmentList.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            try {
                // Load the UpdateDepartment.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateDepartment.fxml"));
                Parent root = loader.load();

                // Get the controller and pass the selected department
                UpdateDepartmentController updateController = loader.getController();
                updateController.setSelectedDepartment(selectedDepartment);

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
            showAlert(Alert.AlertType.WARNING, "No Department Selected", "Please select a department to update.");
        }
    }

    @FXML
    void addDepartment(ActionEvent event) {
        try {
            // Load the AddDepartment.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddDepartment.fxml"));
            Parent root = loader.load();

            // Set the new scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the add department form.");
        }
    }

    @FXML
    void searchDepartments(ActionEvent event) throws SQLException {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadDepartments(); // Show all departments if search is empty
        } else {
            // Use the searchByName method from ServiceDepartment
            List<Department> searchedDepartments = serviceDepartment.searchByName(searchText);
            if (departmentList != null) { // Ensure departmentList is not null
                departmentList.setAll(searchedDepartments);
            } else {
                departmentList = FXCollections.observableArrayList(searchedDepartments);
                DepartmentList.setItems(departmentList);
            }
        }
    }

    @FXML
    void sortDepartments(ActionEvent event) throws SQLException {
        String selectedSort = searchSortCombo.getSelectionModel().getSelectedItem();
        if (selectedSort != null) {
            switch (selectedSort) {
                case "Trier par nom":
                    departmentList.setAll(serviceDepartment.sortName());
                    break;
                case "Trier par budget":
                    departmentList.setAll(serviceDepartment.sortBudget());
                    break;
                default:
                    loadDepartments();
                    break;
            }
        }
    }

    private void showAllDepartmentStats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DepartmentStats.fxml"));
            Parent root = loader.load();
            DepartmentStatsController statsController = loader.getController();
            // Pass all departments to show stats for the entire database
            statsController.setAllDepartments(serviceDepartment.readAll());
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Department Statistics - All Departments");
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load department statistics: " + e.getMessage());
        }
    }

    private void showDepartmentStats(Department dept) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DepartmentStats.fxml"));
            Parent root = loader.load();
            DepartmentStatsController statsController = loader.getController();
            statsController.setDepartment(dept);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Department Statistics - " + dept.getName());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load department statistics.");
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