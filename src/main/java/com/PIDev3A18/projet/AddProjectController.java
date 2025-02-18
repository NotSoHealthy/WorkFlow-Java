package com.PIDev3A18.projet;

import entity.Department;
import entity.Employee;
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
import services.ServiceDepartment;
import services.ServiceEmployee;
import services.ServiceProject;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.List;

public class AddProjectController {
    @FXML private Button SubmitBtn;
    @FXML private ListView<Project> ShowProject;
    @FXML private TextField Name, Budget;
    @FXML private TextArea Description;
    @FXML private DatePicker StartDate;
    @FXML private DatePicker EndDate;
    @FXML private ComboBox<Employee> ProjectManagerComboBox;
    @FXML private ComboBox<Department> DepartmentComboBox;

    private final ServiceProject serviceProject = new ServiceProject();
    private final ServiceEmployee serviceEmployee = new ServiceEmployee();
    private final ServiceDepartment serviceDepartment = new ServiceDepartment();

    @FXML
    void initialize() throws SQLException {
        loadProjectManagers();
        loadDepartments();
        ProjectManagerComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                setText((employee == null || empty) ? "" : employee.getFirstName() + " " + employee.getLastName());
            }
        });
        ProjectManagerComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                setText((employee == null || empty) ? "" : employee.getFirstName() + " " + employee.getLastName());
            }
        });

        // Set how Department names are displayed
        DepartmentComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText((department == null || empty) ? "" : department.getName());
            }
        });
        DepartmentComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText((department == null || empty) ? "" : department.getName());
            }
        });
    }

    private void loadProjectManagers() throws SQLException {
        List<Employee> employees = serviceEmployee.readAll();
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(employees);
        ProjectManagerComboBox.setItems(employeeList);
    }

    private void loadDepartments() throws SQLException {
        List<Department> departments = serviceDepartment.readAll();
        ObservableList<Department> departmentList = FXCollections.observableArrayList(departments);
        DepartmentComboBox.setItems(departmentList);
    }

    @FXML
    void SubmitBtn(ActionEvent event) {
        try {
            String name = Name.getText().trim();
            String description = Description.getText().trim();
            String budgetText = Budget.getText().trim();
            LocalDate startDate = StartDate.getValue();
            LocalDate endDate = EndDate.getValue();
            Employee projectManager = ProjectManagerComboBox.getValue();
            Department department = DepartmentComboBox.getValue();
            if (!isValidText(name) || !isValidText(description)) {
                showAlert(Alert.AlertType.ERROR, "Entrée Invalide", "Nom et Description doivent contenir des Lettres et des Espaces Seulement .");
                return;
            }
            float budget;
            try {
                budget = Float.parseFloat(budgetText);
                if (budget <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Budget Invalide", "Budget doit etre un réel positif .");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Budget Invalide", "Budget doit etre un réel.");
                return;
            }
            if (startDate == null || endDate == null) {
                showAlert(Alert.AlertType.ERROR, "Selection Invalide", "Selectionner une date debut et une date de fin .");
                return;
            }
            if (endDate.isBefore(startDate)) {
                showAlert(Alert.AlertType.ERROR, "Selection Invalide", "datte de fin doit etre aprés la date debut.");
                return;
            }
            if (projectManager == null || department == null) {
                showAlert(Alert.AlertType.ERROR, "Selection Invalide", "Selectionner un Chef De Projet et Un Department.");
                return;
            }
            // Create the Project object
            Project project = new Project(name, description, Date.valueOf(startDate), Date.valueOf(endDate), budget, projectManager, department);
            // Add project to database
            serviceProject.add(project);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Projet a été ajoutter avec Succée !");
            Name.clear();
            Description.clear();
            Budget.clear();
            StartDate.setValue(null);
            EndDate.setValue(null);
            ProjectManagerComboBox.setValue(null);
            DepartmentComboBox.setValue(null);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the project.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred.");
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
}
