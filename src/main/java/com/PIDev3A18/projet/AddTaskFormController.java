package com.PIDev3A18.projet;

import entity.Project;
import entity.Task;
import javafx.stage.Stage;
import services.ServiceTask;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class AddTaskFormController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private ChoiceBox<String> priorityChoice;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker dueDatePicker;

    private Project project;
    private TaskBoardController taskBoardController;
    private final ServiceTask serviceTask = new ServiceTask();
    private Stage stage;

    public void setProject(Project project) {
        this.project = project;
        initializeComboBoxes();
    }

    public void setTaskBoardController(TaskBoardController controller) {
        this.taskBoardController = controller;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void initializeComboBoxes() {
        statusChoice.getItems().addAll("To Do", "In Progress", "Completed", "Blocked");
        statusChoice.setValue("To Do");
        priorityChoice.getItems().addAll("Low", "Medium", "High");
        priorityChoice.setValue("Medium");
    }

    @FXML
    private void handleAddTask() {
        if (project == null) {
            showError("No project selected for this task!");
            return;
        }

        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String status = statusChoice.getValue();
        String priority = priorityChoice.getValue();

        if (title.isEmpty()) {
            showError("Task title cannot be empty!");
            return;
        }

        try {
            createAndAddTask(title, description, status, priority);
            showAlert("Success", "Task added successfully!", Alert.AlertType.INFORMATION);
            stage.close(); // Close the dialog
        } catch (SQLException e) {
            showError("Error adding task: " + e.getMessage());
            System.out.println("SQL Error adding task: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private void createAndAddTask(String title, String description, String status, String priority) throws SQLException {
        java.sql.Date startDate = null;
        java.sql.Date dueDate = null;

        LocalDate startLocalDate = startDatePicker.getValue();
        LocalDate dueLocalDate = dueDatePicker.getValue();

        System.out.println("Start Date (LocalDate): " + startLocalDate);
        System.out.println("Due Date (LocalDate): " + dueLocalDate);

        if (startLocalDate != null) {
            startDate = java.sql.Date.valueOf(startLocalDate);
        } else {
            System.out.println("Start date is null, setting to null in Task");
        }

        if (dueLocalDate != null) {
            dueDate = java.sql.Date.valueOf(dueLocalDate);
        } else {
            System.out.println("Due date is null, setting to null in Task");
        }

        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
        System.out.println("Created At: " + createdAt + ", Updated At: " + updatedAt);

        Task task = new Task(0, title, description, status, priority, startDate, dueDate, null, null, project, createdAt, updatedAt);
        serviceTask.add(task);

        if (taskBoardController != null) {
            taskBoardController.loadTasksForProject(project.getProject_id());
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        showAlert("Error", message, Alert.AlertType.ERROR);
    }
}