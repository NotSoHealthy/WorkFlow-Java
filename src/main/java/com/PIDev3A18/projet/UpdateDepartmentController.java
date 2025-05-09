package com.PIDev3A18.projet;

import entity.Department;
import entity.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceDepartment;
import services.ServiceEmployee;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UpdateDepartmentController {

    @FXML private TextField Name, YearBudget;
    @FXML private ComboBox<Employee> ManagerComboBox;

    private final ServiceDepartment serviceDepartment = new ServiceDepartment();
    private final ServiceEmployee serviceEmployee = new ServiceEmployee();

    private Department selectedDepartment; // The department to be updated

    @FXML
    void initialize() throws SQLException {
        loadManagers();
        // Set how Employee names are displayed in the ComboBox
        ManagerComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                setText((employee == null || empty) ? "" : employee.getFirstName() + " " + employee.getLastName());
            }
        });
        ManagerComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                setText((employee == null || empty) ? "" : employee.getFirstName() + " " + employee.getLastName());
            }
        });
    }

    private void loadManagers() throws SQLException {
        List<Employee> employees = serviceEmployee.readAll();
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
        ManagerComboBox.setItems(employeeList);
    }

    public void setSelectedDepartment(Department department) {
        this.selectedDepartment = department;
        populateFields();
    }

    private void populateFields() {
        if (selectedDepartment != null) {
            Name.setText(selectedDepartment.getName());
            YearBudget.setText(String.valueOf(selectedDepartment.getYear_Budget()));
            ManagerComboBox.setValue(selectedDepartment.getDepartment_Manager());
        }
    }

    @FXML
    void updateDepartment(ActionEvent event) {
        try {
            String name = Name.getText().trim();
            String yearBudgetText = YearBudget.getText().trim();
            Employee manager = ManagerComboBox.getValue();

            // Validate inputs
            if (!isValidText(name)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name must contain only letters and spaces.");
                return;
            }

            float yearBudget;
            try {
                yearBudget = Float.parseFloat(yearBudgetText);
                if (yearBudget <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Budget", "Year Budget must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Budget", "Year Budget must be a valid number.");
                return;
            }

            if (manager == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Selection", "Please select a manager for the department.");
                return;
            }

            // Update the Department object
            selectedDepartment.setName(name);
            selectedDepartment.setYear_Budget(yearBudget);
            selectedDepartment.setDepartment_Manager(manager);

            // Update department in the database
            serviceDepartment.update(selectedDepartment);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "Department updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the department.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }

    @FXML
    void returnToViewDepartment(ActionEvent event) {
        try {
            // Load the ViewDepartment.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();

            // Set the new scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the View Department page.");
        }
    }

    private boolean isValidText(String text) {
        return text.matches("^[a-zA-Z ]+$");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}