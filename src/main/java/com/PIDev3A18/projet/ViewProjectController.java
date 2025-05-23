package com.PIDev3A18.projet;

import entity.Project;
import entity.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import services.ServiceProject;
import services.ServiceTask;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ViewProjectController {

    @FXML private ListView<Project> ShowProject;
    @FXML private Button DeleteBtn;
    @FXML private Button UpdateBtn;
    @FXML private Button AddBtn;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortCombo;
    @FXML private Button chatbotBtn;

    private final ServiceProject serviceProject = new ServiceProject();
    private final ServiceTask serviceTask = new ServiceTask();
    private ObservableList<Project> projectList;

    @FXML
    void initialize() throws SQLException {
        loadProjects();

        DeleteBtn.setDisable(true);
        UpdateBtn.setDisable(true);
        chatbotBtn.setDisable(false);
        AddBtn.setDisable(false);
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
                                    "State: " + project.getState());
                        }
                    }
                };
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchProjects(null);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Search Error", "Failed to search projects.");
                e.printStackTrace();
            }
        });
    }
    private void checkEndDateNotifications(List<Project> projects) {
        LocalDate today = LocalDate.now(); // Current date (March 04, 2025)
        for (Project project : projects) {
            LocalDate endDate = project.getLocalEndDate();
            System.out.println("Project: " + project.getName() + ", End Date: " + endDate + ", Today: " + today);
            if (endDate != null) {
                long daysUntilEnd = ChronoUnit.DAYS.between(today, endDate);
                System.out.println("Days until end for " + project.getName() + ": " + daysUntilEnd);
                if (daysUntilEnd <= 7 && daysUntilEnd >= 0) { // Less than 7 days (one week) and not past due
                    showWindowsNotification(
                            "Project Deadline Approaching",
                            "Project '" + project.getName() + "' ends on " + endDate + " (in " + daysUntilEnd + " days). Please review!"
                    );
                } else if (daysUntilEnd < 0) { // Past due
                    showWindowsNotification(
                            "Project Overdue",
                            "Project '" + project.getName() + "' ended on " + endDate + " and is overdue by " + Math.abs(daysUntilEnd) + " days!"
                    );
                }
            } else {
                System.out.println("End Date is null for project: " + project.getName());
            }
        }
    }

    private void showWindowsNotification(String title, String message) {
        if (SystemTray.isSupported()) {
            try {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage("icons/Logo.png");
                TrayIcon trayIcon = new TrayIcon(image, "Project Manager Notification");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("Click for more info");
                tray.add(trayIcon);
                trayIcon.displayMessage(title, message, TrayIcon.MessageType.WARNING);

                new Thread(() -> {
                    try {
                        Thread.sleep(5000); // Wait 5 seconds
                        tray.remove(trayIcon);
                    } catch (InterruptedException e) {
                        System.err.println("Error removing tray icon: " + e.getMessage());
                    }
                }).start();
            } catch (AWTException e) {
                System.err.println("Error displaying Windows notification: " + e.getMessage());

                showNotification(title, message);
            }
        } else {
            System.err.println("System tray not supported on this platform!");

            showNotification(title, message);
        }
    }

    private void showNotification(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadProjects() throws SQLException {
        List<Project> projects = serviceProject.readAll();
        projectList = FXCollections.observableArrayList(projects);
        ShowProject.setItems(projectList);
        checkEndDateNotifications(projects);
    }

    @FXML
    void deleteProject(ActionEvent event) {
        Project selectedProject = ShowProject.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            try {
                serviceProject.delete(selectedProject);
                loadProjects();
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
            loadProjects();
        } else {
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
                    loadProjects(); 
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

    @FXML
    private void openChatbot() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/PIDev3A18/projet/Chatbot.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Project Chat    ");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewProjectTask() {
        Project selectedProject = ShowProject.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskBoard.fxml"));
                Parent root = loader.load();
                TaskBoardController controller = loader.getController();
                controller.setProject(selectedProject); // Set the project before loading tasks
                System.out.println("Opening TaskBoard for project: " + selectedProject.getName());
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Tâches du Projet: " + selectedProject.getName());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to load TaskBoard.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un projet avant d'afficher les tâches.");
        }
    }
}