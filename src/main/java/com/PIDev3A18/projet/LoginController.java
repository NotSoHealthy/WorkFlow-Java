package com.PIDev3A18.projet;

import com.google.gson.Gson;
import entity.Employee;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.ServiceEmployee;
import utils.UserSession;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberCheckbox;
    @FXML private HBox notifHbox;
    @FXML private Text notifText;
    @FXML private Button passwordBtn;
    @FXML private TextField passwordText;

    Image eye1Image;
    Image eye2Image;
    UserSession userSession;
    ServiceEmployee serviceEmployee;
    Gson gson;

    public LoginController() {
        gson = new Gson();
        userSession = UserSession.getInstance();
        serviceEmployee = new ServiceEmployee();
        InputStream stream = getClass().getResourceAsStream("icons/eye1.png");
        eye1Image = new Image(stream,20,20,true,true);
        stream = getClass().getResourceAsStream("icons/eye2.png");
        eye2Image = new Image(stream,20,20,true,true);
    }

    public void initialize(){
        passwordBtn.setGraphic(new ImageView(eye1Image));
    }

    public void passwordShow(){
        if (passwordField.isVisible()) {
            passwordBtn.setGraphic(new ImageView(eye2Image));
        }
        else {
            passwordBtn.setGraphic(new ImageView(eye1Image));
        }
        passwordField.setVisible(!passwordField.isVisible());
        passwordText.setVisible(!passwordText.isVisible());
    }

    public void login(ActionEvent event) throws SQLException, IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (serviceEmployee.verifPassword(email, password)){
            System.out.println("Password verified");
            Employee employee = serviceEmployee.readByEmailAndPassword(email, password);
            if (employee.getStatus().equals("pending")){
                showNotification("Votre compte est toujours en cours de vérification",2, false);
                return;
            }
            if (employee.getStatus().equals("denied")){
                showNotification("Votre demande de compte a été refusée",2, false);
                return;
            }
            if (rememberCheckbox.isSelected()) {
                try (FileWriter writer = new FileWriter("saved-employee.json")) {
                    gson.toJson(employee, writer);
                    System.out.println("Account saved successfully");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            userSession.login(employee);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();
            LayoutController layoutController = loader.getController();
            layoutController.layoutGoToDashboard(null);

            rememberCheckbox.getScene().setRoot(root);
        }
        else{
            if (serviceEmployee.verifEmail(email)){
                showNotification("Mot de passe invalide",1, false);
            }
            else {
                showNotification("Compte inexistant", 1, false);
            }        }
    }

    public void showNotification(String text, int duration, boolean success) {
        if (success) {
            notifHbox.getStyleClass().add("notif-success");
        }
        notifText.setText(text);
        notifHbox.setOpacity(0); // Start fully transparent
        notifHbox.setVisible(true);

        // Create fade-in transition
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), notifHbox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(.8);

        // Create fade-out transition
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), notifHbox);
        fadeOut.setFromValue(.8);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(duration)); // Wait 3 seconds before fading out

        // After fade-out, hide the HBox to free up space
        if (success) {
            fadeOut.setOnFinished(event -> {
                notifHbox.setVisible(false);
                notifHbox.getStyleClass().remove("notif-success");
            });
        }
        else{
            fadeOut.setOnFinished(event -> notifHbox.setVisible(false));
        }

        // Play fade-in first, then fade-out after delay
        fadeIn.setOnFinished(event -> fadeOut.play());
        fadeIn.play();
    }

    public void goToSignup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
        Parent root = loader.load();
        emailField.getScene().setRoot(root);
    }

    public void passwordTextChanged(){
        passwordField.setText(passwordText.getText());
    }

    public void passwordFieldChanged(){
        passwordText.setText(passwordField.getText());
    }

    public void goToJobOffer() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("JobOfferList.fxml"));
        Parent root = loader.load();
        emailField.getScene().setRoot(root);
    }
}