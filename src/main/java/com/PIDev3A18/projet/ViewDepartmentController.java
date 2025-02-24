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
import javafx.stage.Stage;
import services.ServiceDepartment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewDepartmentController {

    @FXML private ListView<Department> DepartmentList;
    @FXML private Button DeleteBtn;
    @FXML private Button UpdateBtn;
    @FXML private Button AddBtn;
    @FXML private TextField searchField; // Field for search
    @FXML private ComboBox<String> searchSortCombo; // Updated field for sort (matches FXML fx:id)

    private final ServiceDepartment serviceDepartment = new ServiceDepartment();
    private ObservableList<Department> departmentList; // Store departments for manipulation

    @FXML
    void initialize() throws SQLException {
        loadDepartments();

        DeleteBtn.setDisable(true);
        UpdateBtn.setDisable(true);
        DepartmentList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            DeleteBtn.setDisable(newValue == null);
            UpdateBtn.setDisable(newValue == null);
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
        String selectedSort = searchSortCombo.getSelectionModel().getSelectedItem(); // Use searchSortCombo
        if (selectedSort != null) {
            switch (selectedSort) {
                case "Trier par nom":
                    departmentList.setAll(serviceDepartment.sortName()); // Use sortName method
                    break;
                case "Trier par budget":
                    departmentList.setAll(serviceDepartment.sortBudget());
                    break;
                default:
                    loadDepartments(); // Default to unsorted list
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