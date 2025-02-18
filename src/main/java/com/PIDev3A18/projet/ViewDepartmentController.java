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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import services.ServiceDepartment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ViewDepartmentController {

    @FXML private ListView<Department> DepartmentList;
    @FXML private Button DeleteBtn;
    @FXML private Button UpdateBtn;

    private final ServiceDepartment serviceDepartment = new ServiceDepartment();

    @FXML
    void initialize() throws SQLException {
        loadDepartments();
        DepartmentList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Enable buttons only if a department is selected
                DeleteBtn.setDisable(false);
                UpdateBtn.setDisable(false);
            }
        });
    }

    private void loadDepartments() throws SQLException {
        List<Department> departments = serviceDepartment.readAll();
        ObservableList<Department> departmentList = FXCollections.observableArrayList(departments);
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}