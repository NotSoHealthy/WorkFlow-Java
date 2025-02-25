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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceDepartment;
import services.ServiceEmployee;
import services.ServiceProject;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.List;

public class UpdateProjectController {

    @FXML private TextField Name, Budget;
    @FXML private TextArea Description;
    @FXML private DatePicker StartDate, EndDate;
    @FXML private ComboBox<Employee> ProjectManagerComboBox;
    @FXML private ComboBox<Department> DepartmentComboBox;

    private final ServiceProject serviceProject = new ServiceProject();
    private final ServiceEmployee serviceEmployee = new ServiceEmployee();
    private final ServiceDepartment serviceDepartment = new ServiceDepartment();

    private Project selectedProject; // The project to be updated

    @FXML
    void initialize() throws SQLException {
        loadProjectManagers();
        loadDepartments();
        populateFields();

        // Customize how employee names are displayed in the ComboBox
        ProjectManagerComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (employee == null || empty) {
                    setText(null);
                } else {
                    setText(employee.getFirstName() + " " + employee.getLastName()); // Display full name
                }
            }
        });

        // Customize how the selected employee name is displayed in the ComboBox
        ProjectManagerComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (employee == null || empty) {
                    setText(null);
                } else {
                    setText(employee.getFirstName() + " " + employee.getLastName()); // Display full name
                }
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

    private void populateFields() {
        if (selectedProject != null) {
            // Set Name and Description
            Name.setText(selectedProject.getName());
            Description.setText(selectedProject.getDescription());

            // Set Budget (no null check needed for primitive float)
            Budget.setText(String.valueOf(selectedProject.getBudget()));

            // Set Project Manager (with null check)
            if (selectedProject.getProject_Manager() != null) {
                ProjectManagerComboBox.setValue(selectedProject.getProject_Manager());
            } else {
                ProjectManagerComboBox.setValue(null); // Clear the ComboBox if Project_Manager is null
            }

            // Set Department (with null check)
            if (selectedProject.getDepartment_id() != null) {
                DepartmentComboBox.setValue(selectedProject.getDepartment_id());
            } else {
                DepartmentComboBox.setValue(null); // Clear the ComboBox if Department_id is null
            }
        }
    }

    public void setSelectedProject(Project project) {
        this.selectedProject = project;
        populateFields();
    }

    @FXML
    void updateProject(ActionEvent event) {
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
            float budget = Float.parseFloat(budgetText);

            if (budget <= 0) {
                showAlert(Alert.AlertType.ERROR, "Budget Invalide", "Budget doit etre un réel positif .");
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

            Project updatedProject = new Project(name, description, Date.valueOf(startDate), Date.valueOf(endDate), budget, projectManager, department);
            updatedProject.setProject_id(selectedProject.getProject_id());
            serviceProject.update(updatedProject);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Projet mis à jour avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la mise à jour.");
        }
    }

    private boolean isValidText(String text) {
        return text != null && text.matches("[A-Za-z\\s]+");
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