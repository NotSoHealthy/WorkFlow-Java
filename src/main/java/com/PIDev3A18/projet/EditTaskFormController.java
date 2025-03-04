package com.PIDev3A18.projet;

import entity.Task;
import services.ServiceTask;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.sql.SQLException;

public class EditTaskFormController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private ChoiceBox<String> priorityChoice;
    @FXML private DatePicker dueDatePicker;

    private ServiceTask serviceTask;
    private Task task;

    public void setServiceTask(ServiceTask serviceTask) {
        this.serviceTask = serviceTask;
    }

    public void setTask(Task task) {
        this.task = task;
        titleField.setText(task.getTitle());
        descriptionField.setText(task.getDescription());
        statusChoice.getItems().addAll("To Do", "In Progress", "Completed", "Blocked");
        priorityChoice.getItems().addAll("Low", "Medium", "High", "Critical");

        statusChoice.setValue(task.getStatus());
        priorityChoice.setValue(task.getPriority());

        if (task.getDueDate() != null) {
            dueDatePicker.setValue(task.getDueDate().toLocalDate());
        }
    }

    @FXML
    public void handleSaveTask() {
        if (titleField.getText().isEmpty() || dueDatePicker.getValue() == null) {
            showAlert("Error", "Title and Due Date are required!", Alert.AlertType.ERROR);
            return;
        }

        task.setTitle(titleField.getText());
        task.setDescription(descriptionField.getText());
        task.setStatus(statusChoice.getValue());
        task.setPriority(priorityChoice.getValue());
        task.setDueDate(Date.valueOf(dueDatePicker.getValue()));

        try {
            serviceTask.update(task);
            showAlert("Success", "Task updated successfully!", Alert.AlertType.INFORMATION);
            titleField.getScene().getWindow().hide();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
