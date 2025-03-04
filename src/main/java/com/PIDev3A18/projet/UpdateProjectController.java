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

public class UpdateProjectController {

    @FXML private TextField Name, Budget;
    @FXML private TextArea Description;
    @FXML private DatePicker StartDate, EndDate;
    @FXML private ComboBox<Employee> ProjectManagerComboBox;
    @FXML private ComboBox<Department> DepartmentComboBox;
    @FXML private ComboBox<String> StateComboBox; // New ComboBox for State

    private final ServiceProject serviceProject = new ServiceProject();
    private final ServiceEmployee serviceEmployee = new ServiceEmployee();
    private final ServiceDepartment serviceDepartment = new ServiceDepartment();

    private Project selectedProject; // The project to be updated

    @FXML
    void initialize() throws SQLException {
        loadProjectManagers();
        loadDepartments();
        loadStates(); // Load state options
        populateFields();

        // Customize how employee names are displayed in ProjectManagerComboBox
        ProjectManagerComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                setText((employee == null || empty) ? null : employee.getFirstName() + " " + employee.getLastName());
            }
        });
        ProjectManagerComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                setText((employee == null || empty) ? null : employee.getFirstName() + " " + employee.getLastName());
            }
        });

        // Customize how department names are displayed in DepartmentComboBox
        DepartmentComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText((department == null || empty) ? null : department.getName());
            }
        });
        DepartmentComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                setText((department == null || empty) ? null : department.getName());
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

    private void loadStates() {
        ObservableList<String> states = FXCollections.observableArrayList("In Progress", "Almost Finished", "Finished");
        StateComboBox.setItems(states);
    }

    private void populateFields() {
        if (selectedProject != null) {
            Name.setText(selectedProject.getName());
            Description.setText(selectedProject.getDescription());
            Budget.setText(String.valueOf(selectedProject.getBudget()));

            // Convert java.sql.Date to LocalDate for DatePicker
            if (selectedProject.getStart_Date() != null) {
                StartDate.setValue(((Date) selectedProject.getStart_Date()).toLocalDate()); // Cast to java.sql.Date
            }
            if (selectedProject.getEnd_Date() != null) {
                EndDate.setValue(((Date) selectedProject.getEnd_Date()).toLocalDate()); // Cast to java.sql.Date
            }

            ProjectManagerComboBox.setValue(selectedProject.getProject_Manager());
            DepartmentComboBox.setValue(selectedProject.getDepartment_id());
            StateComboBox.setValue(selectedProject.getState()); // Set the current state
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
            String state = StateComboBox.getValue();

            // Validation
            if (!isValidText(name) || !isValidText(description)) {
                showAlert(Alert.AlertType.ERROR, "Entrée Invalide", "Nom et Description doivent contenir des lettres et des espaces seulement.");
                return;
            }

            float budget;
            try {
                budget = Float.parseFloat(budgetText);
                if (budget <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Budget Invalide", "Le budget doit être un réel positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Budget Invalide", "Le budget doit être un réel.");
                return;
            }

            if (startDate == null || endDate == null) {
                showAlert(Alert.AlertType.ERROR, "Sélection Invalide", "Sélectionner une date de début et une date de fin.");
                return;
            }

            if (endDate.isBefore(startDate)) {
                showAlert(Alert.AlertType.ERROR, "Sélection Invalide", "La date de fin doit être après la date de début.");
                return;
            }

            if (projectManager == null || department == null) {
                showAlert(Alert.AlertType.ERROR, "Sélection Invalide", "Sélectionner un chef de projet et un département.");
                return;
            }

            if (state == null) {
                showAlert(Alert.AlertType.ERROR, "Sélection Invalide", "Sélectionner un état pour le projet.");
                return;
            }

            // Create updated project object
            Project updatedProject = new Project(name, description, Date.valueOf(startDate), Date.valueOf(endDate),
                    budget, projectManager, department, state);
            updatedProject.setProject_id(selectedProject.getProject_id());

            // Update project in database
            serviceProject.update(updatedProject);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Projet mis à jour avec succès !");

            // Return to ViewProject
            returnToViewProject(event);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur Base de Données", "Une erreur est survenue lors de la mise à jour.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur inattendue est survenue.");
        }
    }

    private boolean isValidText(String text) {
        return text != null && !text.isEmpty() && text.matches("[A-Za-z\\s]+");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement de la page View Department.");
        }
    }

    private void returnToViewProject(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewProject.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec du chargement de la page View Project.");
        }
    }
}