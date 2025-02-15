package com.PIDev3A18.projet;

import com.google.gson.Gson;
import entity.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class LayoutController {
    Employee loggedinEmployee;

    @FXML
    private Text layoutName;
    @FXML
    private Button layoutDashButton;
    @FXML
    private Button layoutProjectsButton;
    @FXML
    private Button layoutTasksButton;
    @FXML
    private Button layoutCalendarButton;
    @FXML
    private Button layoutMoneyButton;
    @FXML
    private Button layoutLeaveButton;
    @FXML
    private Button layoutDisconnectButton;
    @FXML
    private ImageView layoutProfilePicture;

    @FXML
    void initialize() {
        //Dashboard Icon
        InputStream input = getClass().getResourceAsStream("icons/dash.png");
        Image image = new Image(input, 16, 16, true, true);
        ImageView imageView = new ImageView(image);
        layoutDashButton.setGraphic(imageView);
        //Projects Icon
        input = getClass().getResourceAsStream("icons/projects.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutProjectsButton.setGraphic(imageView);
        //Task Icon
        input = getClass().getResourceAsStream("icons/tasks.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutTasksButton.setGraphic(imageView);
        //Calendar Icon
        input = getClass().getResourceAsStream("icons/calendar.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutCalendarButton.setGraphic(imageView);
        //Money Icon
        input = getClass().getResourceAsStream("icons/money.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutMoneyButton.setGraphic(imageView);
        //Conge Icon
        input = getClass().getResourceAsStream("icons/conge.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutLeaveButton.setGraphic(imageView);
        //Logout Icon
        input = getClass().getResourceAsStream("icons/logout.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutDisconnectButton.setGraphic(imageView);
    }

    public void layoutGoToDashboard(ActionEvent actionEvent) {
    }

    public void layoutGoToProjects(ActionEvent actionEvent) {
    }

    public void layoutGoToTasks(ActionEvent actionEvent) {
    }

    public void layoutGoToCalendar(ActionEvent actionEvent) {
    }

    public void layoutGoToMoney(ActionEvent actionEvent) {
    }

    public void layoutGoToConge(ActionEvent actionEvent) {
    }

    public void layoutDisconnect(ActionEvent actionEvent) throws IOException {
        Gson gson = new Gson();
        FileWriter writer = new FileWriter("saved-employee.json");
        gson.toJson(null,writer);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        layoutCalendarButton.getScene().setRoot(fxmlLoader.load());
    }

    public void setLoggedinEmployee(Employee loggedinEmployee) {
        this.loggedinEmployee = loggedinEmployee;
        layoutName.setText(loggedinEmployee.getFirstName() + " " + loggedinEmployee.getLastName());

        Image image = new Image(loggedinEmployee.getImageUrl());
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double minSize = Math.min(imageWidth, imageHeight);
        layoutProfilePicture.setViewport(new Rectangle2D(
                (imageWidth - minSize) / 2, // Center X
                (imageHeight - minSize) / 2, // Center Y
                minSize, minSize // Crop size (square)
        ));
        layoutProfilePicture.setImage(image);
        Circle clip = new Circle(layoutProfilePicture.getFitHeight() / 2);
        clip.setCenterX(layoutProfilePicture.getFitHeight() / 2);
        clip.setCenterY(layoutProfilePicture.getFitHeight() / 2);
        layoutProfilePicture.setClip(clip);
    }

    public Employee getLoggedinEmployee() {
        return loggedinEmployee;
    }
}
