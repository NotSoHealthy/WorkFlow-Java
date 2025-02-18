package com.PIDev3A18.projet;

import entity.Reclamation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.stage.FileChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ReclamationFocusController {


    @FXML
    private Label titre;

    @FXML
    private Label description;

    @FXML
    private Label etat;  // New field for email

    @FXML
    private MenuButton dropdown;


    @FXML
    private Button send;


    private Reclamation selectedR;


    public void SetReclamation(Reclamation r) {
        this.selectedR = r;
        if (titre != null) {  // Check if labels are initialized before setting text
            initialize();
        }
    }
    @FXML
    public void initialize()
    {
        MenuItem option1 = new MenuItem("PENDING");
        MenuItem option2 = new MenuItem("IN PROGRESS");
        MenuItem option3 = new MenuItem("ON HOLD");
        MenuItem option4 = new MenuItem("CLOSED");
        MenuItem option5 = new MenuItem("REJECTED");

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
