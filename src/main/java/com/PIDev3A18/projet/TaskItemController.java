package com.PIDev3A18.projet;

import entity.Employee;
import entity.Project;
import entity.Task;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import services.ServiceTask;


public class TaskItemController {
    @FXML private Text date;
    @FXML private Region dot;
    @FXML private ImageView image;
    @FXML private Label name;
    @FXML private Label title;

    private ServiceTask serviceTask;
    private Task task;

    public TaskItemController(Task task) {
        this.task = task;
        serviceTask = new ServiceTask();
    }

    public void initialize() {
        Project project = task.getProject();

        Image img = new Image(project.getProject_Manager().getImageUrl(), true);
        double imageWidth = img.getWidth();
        double imageHeight = img.getHeight();
        double minSize = Math.min(imageWidth, imageHeight);
        image.setViewport(new Rectangle2D(
                (imageWidth - minSize) / 2, // Center X
                (imageHeight - minSize) / 2, // Center Y
                minSize, minSize // Crop size (square)
        ));
        image.setImage(img);
        Circle clip = new Circle(image.getFitHeight() / 2);
        clip.setCenterX(image.getFitHeight() / 2);
        clip.setCenterY(image.getFitHeight() / 2);
        image.setClip(clip);

        name.setText(project.getProject_Manager().getFirstName());
        title.setText(task.getTitle());
        date.setText(String.valueOf(task.getDueDate()));
        switch (task.getStatus()){
            case "To Do":
                dot.setStyle("-fx-background-color: #F9CF58");
                break;
            case "In Progress":
                dot.setStyle("-fx-background-color: #EE8B60");
                break;
            case "Completed":
                dot.setStyle("-fx-background-color: #39D2C0");
                break;
        }

    }
}
