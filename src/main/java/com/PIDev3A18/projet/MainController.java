package com.PIDev3A18.projet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Region; // Use a more general type
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML
    private Button ApplyBtn;

    @FXML
    private Button SignIn;

    @FXML
    public void ApplyBtn(ActionEvent event) {
        // Load the JobOfferList.fxml scene when the Apply button is clicked
        loadFXML(getClass().getResource("JobOfferList.fxml"));
    }

    @FXML
    public void SignIn(ActionEvent event) {
        // Load the login.fxml scene when the Sign In button is clicked
        loadFXML(getClass().getResource("login.fxml"));
    }

    private void loadFXML(URL url) {
        try {
            // Load the FXML content
            FXMLLoader loader = new FXMLLoader(url);
            Region root = loader.load(); // Use a more general type like Region

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) ApplyBtn.getScene().getWindow(); // Or you can use SignIn.getScene().getWindow()
            Scene scene = new Scene(root);
            stage.setScene(scene); // Change the scene to the new one
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log error if FXML loading fails
        }
    }
}
