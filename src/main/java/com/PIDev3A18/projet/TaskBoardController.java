package com.PIDev3A18.projet;

import entity.Project;
import entity.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.ServiceTask;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TaskBoardController {
    @FXML private VBox todoTasks, inProgressTasks, completedTasks, blockedTasks;
    @FXML private Button deleteButton;

    private final ServiceTask serviceTask = new ServiceTask();
    private Project currentProject;

    @FXML
    public void initialize() {
        if (todoTasks == null || inProgressTasks == null || completedTasks == null || blockedTasks == null ||
                deleteButton == null) {
            showError("FXML elements not initialized. Check TaskBoard.fxml.");
            return;
        }
        setupDragAndDrop();
        setupDeleteButton();
        System.out.println("TaskBoard initialized. currentProject: " + (currentProject != null ? currentProject.getName() : "null"));
    }

    private void setupDeleteButton() {
        deleteButton.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof Label) {
                event.acceptTransferModes(TransferMode.MOVE);
                System.out.println("Drag over delete button, currentProject: " + (currentProject != null ? currentProject.getName() : "null"));
                deleteButton.setStyle("-fx-background-color: #ff4444;");
            }
            event.consume();
        });

        deleteButton.setOnDragExited(event -> {
            deleteButton.setStyle("-fx-background-color: #ff4444;");
            event.consume();
        });

        deleteButton.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                try {
                    int taskId = Integer.parseInt(db.getString());
                    System.out.println("Dropping task with ID: " + taskId + " to delete, currentProject: " + (currentProject != null ? currentProject.getName() : "null"));
                    deleteTask(taskId);
                } catch (NumberFormatException e) {
                    showError("Invalid task ID: " + e.getMessage());
                }
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private void deleteTask(int taskId) {
        if (currentProject == null) {
            showError("No project selected. Cannot delete tasks.");
            System.out.println("Cannot delete task " + taskId + " - no project selected");
            return;
        }

        try {
            serviceTask.delete(new Task(taskId, null, null, null, null, null, null, null, null, null, null, null));
            showAlert("Success", "Task deleted successfully!", Alert.AlertType.INFORMATION);

            // Reload tasks after deletion
            loadTasksForProject(currentProject.getProject_id());
            System.out.println("Task " + taskId + " deleted from database and UI, project: " + currentProject.getName());
        } catch (SQLException e) {
            showError("Error deleting task: " + e.getMessage());
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    private void setupDragAndDrop() {
        setupColumn(todoTasks, "To Do");
        setupColumn(inProgressTasks, "In Progress");
        setupColumn(completedTasks, "Completed");
        setupColumn(blockedTasks, "Blocked");
    }

    private void setupColumn(VBox column, String newStatus) {
        column.setOnDragOver(event -> {
            if (event.getGestureSource() instanceof Label) {
                event.acceptTransferModes(TransferMode.MOVE);
                System.out.println("Drag over column for status: " + newStatus + ", children count: " + column.getChildren().size() + ", currentProject: " + (currentProject != null ? currentProject.getName() : "null"));
            }
            event.consume();
        });

        column.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                try {
                    int taskId = Integer.parseInt(db.getString());
                    System.out.println("Dropping task with ID: " + taskId + " to status: " + newStatus + ", children count: " + column.getChildren().size() + ", currentProject: " + (currentProject != null ? currentProject.getName() : "null"));
                    moveTask(taskId, newStatus, column);
                } catch (NumberFormatException e) {
                    showError("Invalid task ID: " + e.getMessage());
                }
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private void moveTask(int taskId, String newStatus, VBox newColumn) {
        if (currentProject == null) {
            showError("No project selected. Cannot move tasks.");
            System.out.println("Cannot move task " + taskId + " - no project selected");
            return;
        }

        try {
            serviceTask.updateStatus(taskId, newStatus);
            loadTasksForProject(currentProject.getProject_id());
            System.out.println("Task " + taskId + " moved to " + newStatus + ", project: " + currentProject.getName());
        } catch (SQLException e) {
            showError("Error updating task status: " + e.getMessage());
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    private Label createTaskLabel(Task task) {
        Label taskLabel = new Label(task.getTitle() != null && !task.getTitle().isEmpty() ? task.getTitle() : "No Title");
        taskLabel.getStyleClass().add("task-label");
        taskLabel.setWrapText(true);
        taskLabel.setUserData(task);

        taskLabel.setOnDragDetected(event -> {
            if (task != null && task.getTaskId() > 0) { // Ensure task and ID are valid
                Dragboard db = taskLabel.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(task.getTaskId()));
                db.setContent(content);
                taskLabel.setOpacity(0.5);
                System.out.println("Drag detected for task ID: " + task.getTaskId() + ", currentProject: " + (currentProject != null ? currentProject.getName() : "null"));
                event.consume();
            } else {
                System.out.println("Invalid task or task ID for dragging, currentProject: " + (currentProject != null ? currentProject.getName() : "null"));
                event.consume();
            }
        });

        taskLabel.setOnDragDone(event -> {
            taskLabel.setOpacity(1.0); // Reset opacity
            System.out.println("Drag done for task ID: " + (task != null ? task.getTaskId() : "null") + ", currentProject: " + (currentProject != null ? currentProject.getName() : "null"));
            event.consume();
        });

        return taskLabel;
    }

    private void addToColumn(Task task) {
        Label taskLabel = createTaskLabel(task);
        System.out.println("Adding task: " + task.getTitle() + " with status: " + task.getStatus() + ", currentProject: " + (currentProject != null ? currentProject.getName() : "null"));

        // Add to the correct column
        switch (task.getStatus()) {
            case "To Do": todoTasks.getChildren().add(taskLabel); break;
            case "In Progress": inProgressTasks.getChildren().add(taskLabel); break;
            case "Completed": completedTasks.getChildren().add(taskLabel); break;
            case "Blocked": blockedTasks.getChildren().add(taskLabel); break;
            default:
                System.out.println("Unknown status for task " + task.getTaskId() + ": " + task.getStatus());
                todoTasks.getChildren().add(taskLabel); // Default to To Do if status is unknown
        }
    }

    private String getColumnColor(String status) {
        switch (status) {
            case "To Do": return "#ffeeee"; // Light red
            case "In Progress": return "#fff2e6"; // Light yellow
            case "Completed": return "#e6ffe6"; // Light green
            case "Blocked": return "#e6f2ff"; // Light blue
            default: return "#ffffff"; // Default white
        }
    }

    public void setProject(Project project) {
        this.currentProject = project;
        System.out.println("Project set: " + (project != null ? project.getName() : "null"));
        if (project != null) {
            loadTasksForProject(project.getProject_id());
        } else {
            showError("No project selected. Cannot load tasks.");
            clearColumns();
        }
    }

    public void loadTasksForProject(int projectId) {
        if (currentProject == null) {
            showError("No project selected. Cannot load tasks.");
            System.out.println("Cannot load tasks - no project selected for project ID: " + projectId);
            return;
        }

        try {
            List<Task> tasks = serviceTask.getTasksByProjectId(projectId);
            System.out.println("Loading tasks for project ID: " + projectId + ", Found " + tasks.size() + " tasks, project: " + currentProject.getName());

            clearColumns(); // Clears old tasks before loading new ones
            tasks.forEach(this::addToColumn);
        } catch (SQLException e) {
            showError("Error loading tasks: " + e.getMessage());
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    private void clearColumns() {
        todoTasks.getChildren().clear();
        inProgressTasks.getChildren().clear();
        completedTasks.getChildren().clear();
        blockedTasks.getChildren().clear();
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

    @FXML
    public void openAddTaskForm() {
        if (currentProject == null) {
            showError("No project selected for this task!");
            return;
        }

        try {
            // Ensure the FXML path is correct and the file exists
            System.out.println("Loading AddTaskForm.fxml from: /com/PIDev3A18/projet/AddTaskForm.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/PIDev3A18/projet/AddTaskForm.fxml"));
            Parent root = loader.load();

            // Pass project data to AddTaskFormController
            AddTaskFormController controller = loader.getController();
            if (controller == null) {
                throw new IOException("AddTaskFormController could not be loaded.");
            }
            controller.setProject(currentProject);
            controller.setTaskBoardController(this); // Pass reference to this controller for refresh

            // Create and configure the stage
            Stage stage = new Stage();
            stage.setTitle("Add New Task");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Block other windows until closed

            // Pass the stage to the controller before showing
            controller.setStage(stage);
            System.out.println("Stage set in AddTaskFormController: " + stage);

            stage.showAndWait();

            // Refresh tasks after adding, handling potential SQLException
            loadTasksForProject(currentProject.getProject_id());
        } catch (IOException e) {
            showError("Error loading AddTaskForm.fxml: " + e.getMessage());
            System.out.println("IOException loading AddTaskForm.fxml: " + e.getMessage());
        }
    }
}