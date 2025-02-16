package com.PIDev3A18.projet;

import com.google.gson.Gson;
import entity.Employee;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.Constants.AppConstants;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {
    Scene scene;
    Parent root;
    @Override
    public void start(Stage stage) throws IOException {
        Gson gson = new Gson();
        Employee loggedInUser;
        try (FileReader reader = new FileReader("saved-employee.json")) {
            loggedInUser = gson.fromJson(reader, Employee.class);
        }catch (IOException e){
            loggedInUser = null;
        }

        if (loggedInUser == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
            root = fxmlLoader.load();

        }
        else{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("layout.fxml"));
            root = fxmlLoader.load();
            LayoutController controller= fxmlLoader.getController();
            controller.setLoggedinEmployee(loggedInUser);
            controller.layoutGoToDashboard(null);
        }
        scene = new Scene(root, AppConstants.WIDTH, AppConstants.HEIGHT);
        stage.setTitle(AppConstants.TITLE);
        InputStream input = getClass().getResourceAsStream("icons/logo.png");
        Image image = new Image(input);
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.setMinHeight(AppConstants.HEIGHT);
        stage.setMinWidth(AppConstants.WIDTH);
        stage.show();
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {


        launch();
    }
}