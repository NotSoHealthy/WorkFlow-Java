package com.PIDev3A18.projet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.Constants.AppConstants;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), AppConstants.WIDTH, AppConstants.HEIGHT);
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