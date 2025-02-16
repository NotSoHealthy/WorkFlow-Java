package com.PIDev3A18.projet;

import entity.Department;
import entity.Employee;
import entity.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.ServiceDepartment;
import services.ServiceEmployee;
import services.ServiceProject;

import java.sql.Date;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.List;

public class ProjectController {
    private Employee loggedInEmployee;

    @FXML
    private Button DeleteBtn, SubmitBtn, UpdateBtn;

    @FXML
    private ListView<Project> ShowProject; // Changed from TableView to ListView

    @FXML
    private TextField Name, Description, Budget;

    @FXML
    private DatePicker StartDate, EndDate;

    @FXML
    private ComboBox<Employee> ProjectManagerComboBox;

    @FXML
    private ComboBox<Department> DepartmentComboBox;

    private final ServiceProject serviceProject = new ServiceProject();

    private Project selectedProject; // Store the selected project

    @FXML
    void initialize() {
        SubmitBtn.setDisable(true);
        setupListView(); // Updated to setup ListView
        loadProjects();
        loadComboBoxes();

        // Handle item selection in the ListView
        ShowProject.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithSelectedProject(newSelection);
                SubmitBtn.setVisible(false); // Hide Submit when selecting a project
            }
        });
    }

    @FXML
    void SubmitBtn(ActionEvent event) {
        if (loggedInEmployee == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No logged-in employee set.");
            return;
        }

        try {
            String name = Name.getText().trim();
            String description = Description.getText().trim();
            String budgetText = Budget.getText().trim();
            LocalDate startDate = StartDate.getValue();
            LocalDate endDate = EndDate.getValue();
            Employee projectManager = ProjectManagerComboBox.getValue();
            Department department = DepartmentComboBox.getValue();

            // Input Validation
            if (!isValidText(name) || !isValidText(description)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name and Description should only contain letters and spaces.");
                return;
            }

            double budget;
            try {
                budget = Double.parseDouble(budgetText);
                if (budget <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Budget", "Budget must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Budget", "Budget must be a numeric value.");
                return;
            }

            if (startDate == null || endDate == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Date", "Please select both start and end dates.");
                return;
            }

            if (endDate.isBefore(startDate)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Date", "End date must be after the start date.");
                return;
            }

            if (projectManager == null || department == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Selection", "Please select a project manager and department.");
                return;
            }

            Project project = new Project(name, description, Date.valueOf(startDate), Date.valueOf(endDate), (float) budget, projectManager, department);

            serviceProject.add(project);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project added successfully!");

            clearFields();
            loadProjects();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the project.");
        }
    }

    public void setLoggedInEmployee(Employee loggedInEmployee) {
        this.loggedInEmployee = loggedInEmployee;
        SubmitBtn.setDisable(false);
        System.out.println("Logged-in Employee set: " + loggedInEmployee.getId());
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

    private void setupListView() {
        // Set up a custom cell factory to display projects in the ListView
        ShowProject.setCellFactory(param -> new ListCell<Project>() {
            @Override
            protected void updateItem(Project project, boolean empty) {
                super.updateItem(project, empty);
                if (empty || project == null) {
                    setText(null);
                } else {
                    // Customize how each project is displayed in the ListView
                    setText("Project: " + project.getName() +
                            "\nDescription: " + project.getDescription() +
                            "\nBudget: " + project.getBudget() +
                            "\nStart Date: " + project.getStart_Date() +
                            "\nEnd Date: " + project.getEnd_Date() +
                            "\nManager: " + project.getProject_Manager().getLastName() +
                            "\nDepartment: " + project.getDepartment_id().getName());
                }
            }
        });
    }

    private void loadProjects() {
        try {
            List<Project> projects = serviceProject.readAll();
            ObservableList<Project> observableList = FXCollections.observableArrayList(projects);
            ShowProject.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load projects.");
        }
    }

    private void loadComboBoxes() {
        try {
            // Load Project Managers
            ServiceEmployee serviceEmployee = new ServiceEmployee();
            List<Employee> employees = serviceEmployee.readAll();
            ProjectManagerComboBox.setItems(FXCollections.observableArrayList(employees));

            // Load Departments
            ServiceDepartment serviceDepartment = new ServiceDepartment();
            List<Department> departments = serviceDepartment.readAll();
            DepartmentComboBox.setItems(FXCollections.observableArrayList(departments));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load combo box data.");
        }
    }

    @FXML
    void DeleteBtn(ActionEvent event) {
        Project selectedProject = ShowProject.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No project selected.");
            return;
        }

        try {
            serviceProject.delete(selectedProject);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project deleted successfully!");
            loadProjects();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the project.");
        }
    }

    @FXML
    void UpdateBtn(ActionEvent event) {
        if (selectedProject == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No project selected for update.");
            return;
        }

        try {
            String name = Name.getText().trim();
            String description = Description.getText().trim();
            String budgetText = Budget.getText().trim();
            LocalDate startDate = StartDate.getValue();
            LocalDate endDate = EndDate.getValue();
            Employee projectManager = ProjectManagerComboBox.getValue();
            Department department = DepartmentComboBox.getValue();

            // Input Validation
            if (!isValidText(name) || !isValidText(description)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name and Description should only contain letters and spaces.");
                return;
            }

            double budget;
            try {
                budget = Double.parseDouble(budgetText);
                if (budget <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Budget", "Budget must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Budget", "Budget must be a numeric value.");
                return;
            }

            if (startDate == null || endDate == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Date", "Please select both start and end dates.");
                return;
            }

            if (endDate.isBefore(startDate)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Date", "End date must be after the start date.");
                return;
            }

            if (projectManager == null || department == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Selection", "Please select a project manager and department.");
                return;
            }

            selectedProject.setName(name);
            selectedProject.setDescription(description);
            selectedProject.setStart_Date(Date.valueOf(startDate));
            selectedProject.setEnd_Date(Date.valueOf(endDate));
            selectedProject.setBudget((float) budget);
            selectedProject.setProject_Manager(projectManager);
            selectedProject.setDepartment_id(department);

            serviceProject.update(selectedProject);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Project updated successfully!");

            clearFields();
            loadProjects();
            SubmitBtn.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the project.");
        }
    }

    private void fillFormWithSelectedProject(Project project) {
        selectedProject = project;
    }

    private void clearFields() {
        Name.clear();
        Description.clear();
        Budget.clear();
        StartDate.setValue(null);
        EndDate.setValue(null);
        ProjectManagerComboBox.setValue(null);
        DepartmentComboBox.setValue(null);
    }
}