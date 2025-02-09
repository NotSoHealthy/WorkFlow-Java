package com.PIDev3A18.projet;

import entity.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.InputStream;

public class LayoutController {
    Employee loggedEmployee;

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

    public void layoutDisconnect(ActionEvent actionEvent) {
    }

    public void setLoggedEmployee(Employee loggedEmployee) {
        this.loggedEmployee = loggedEmployee;
        layoutName.setText(loggedEmployee.getFirstName() + " " + loggedEmployee.getLastName());
    }

    public Employee getLoggedEmployee() {
        return loggedEmployee;
    }
}
