package com.PIDev3A18.projet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.io.InputStream;

public class LayoutController {
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
        System.out.println("cock");
        InputStream input = getClass().getResourceAsStream("icons/logout.png");
        //set the size for image
        Image image = new Image(input, 16, 16, true, true);
        ImageView imageView = new ImageView(image);
        layoutDisconnectButton.setGraphic(imageView);
    }
}
