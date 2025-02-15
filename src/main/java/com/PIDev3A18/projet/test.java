package com.PIDev3A18.projet;

import com.google.gson.Gson;
import entity.Employee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.Constants.AppConstants;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class test extends Application {
    Scene scene;
    Parent root;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Evenements.fxml"));
        root = fxmlLoader.load();
        scene = new Scene(root, AppConstants.WIDTH, AppConstants.HEIGHT);
        stage.setTitle(AppConstants.TITLE);
        stage.setScene(scene);
        stage.setMinHeight(AppConstants.HEIGHT);
        stage.setMinWidth(AppConstants.WIDTH);
        stage.show();
    }

    public static void main(String[] args) {


        launch();
    }
}