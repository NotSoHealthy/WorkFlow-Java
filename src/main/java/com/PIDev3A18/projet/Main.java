package com.PIDev3A18.projet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.Constants.AppConstants;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), AppConstants.WIDTH, AppConstants.HEIGHT);
        stage.setTitle(AppConstants.TITLE);
        InputStream input = getClass().getResourceAsStream("icons/logo.png");
        Image image = new Image(input);
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.setMinHeight(AppConstants.HEIGHT);
        stage.setMinWidth(AppConstants.WIDTH);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}