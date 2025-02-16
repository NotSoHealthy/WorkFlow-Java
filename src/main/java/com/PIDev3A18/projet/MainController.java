package com.PIDev3A18.projet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML
    private Button ApplyBtn;

    @FXML
    private Button SignIn;

    @FXML
    public void ApplyBtn(ActionEvent event) {
        loadFXML(getClass().getResource("Application.fxml"));
    }

    @FXML
    public void SignIn(ActionEvent event) {
        loadFXML(getClass().getResource("Login.fxml"));


    }


    private void loadFXML(URL url) {
        FXMLLoader loader = new FXMLLoader(url);
    }




}
