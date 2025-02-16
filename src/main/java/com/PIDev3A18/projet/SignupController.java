package com.PIDev3A18.projet;

import entity.Employee;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import services.ServiceEmployee;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.regex.Pattern;

public class SignupController {
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordText;
    @FXML
    private PasswordField confirmationField;
    @FXML
    private TextField confirmationText;
    @FXML
    private Button passwordButton;
    @FXML
    private Button confirmationButton;
    @FXML
    private HBox notifHbox;
    @FXML
    private Text notifText;

    ValidationSupport validationSupport;
    Image eye1Image;
    Image eye2Image;

    public SignupController() {
        validationSupport = new ValidationSupport();
        InputStream stream = getClass().getResourceAsStream("icons/eye1.png");
        eye1Image = new Image(stream,20,20,true,true);
        stream = getClass().getResourceAsStream("icons/eye2.png");
        eye2Image = new Image(stream,20,20,true,true);
    }

    public void signup(ActionEvent event) throws SQLException, IOException {
        if (validationSupport.isInvalid()){
            return;
        }
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String number = numberField.getText();
        String password = passwordField.getText();

        if (serviceEmployee.verifEmail(email)) {
            showNotification("Email déja utilisé",1, false);
            return;
        }
        if (serviceEmployee.verifPhone(number)) {
            showNotification("Numéro déja utilisé",1, false);
            return;
        }

        Employee employee = new Employee(nom, prenom, email, number, password, "employee");
        serviceEmployee.add(employee);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.showNotification("Votre compte a été créé\nUn e-mail vous sera envoyé dès qu'il aura été examiné par un responsable",4,true);
        nomField.getScene().setRoot(root);
    }

    @FXML
    public void initialize() throws InterruptedException {
        validationSupport.registerValidator(nomField,Validator.createEmptyValidator("Nom invalide"));
        validationSupport.registerValidator(prenomField,Validator.createEmptyValidator("Prenom invalide"));
        validationSupport.registerValidator(numberField,Validator.createRegexValidator("Numéro invalide",Pattern.compile("^[0-9]{8}$"),Severity.ERROR));
        validationSupport.registerValidator(emailField, Validator.createRegexValidator("Email invalide",Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", Pattern.CASE_INSENSITIVE), Severity.ERROR));
        validationSupport.registerValidator(passwordField, Validator.createRegexValidator("Le mot de passe doit contenir:\nUn minimum de 8 caractères\nUn nombre\nEt un caractères spéciale",Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[.@$!%*#?&])[A-Za-z\\d.@$!%*#?&]{8,}$"),Severity.ERROR));
        validationSupport.registerValidator(confirmationField, Validator.createEqualsValidator("Confirmation invalide", Collections.singletonList(passwordField.getText())));

        passwordButton.setGraphic(new ImageView(eye1Image));
        confirmationButton.setGraphic(new ImageView(eye1Image));
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

    public void goToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        emailField.getScene().setRoot(loader.load());
    }

    public void passwordChange() {
        passwordText.setText(passwordField.getText());
        validationSupport.registerValidator(confirmationField, Validator.createEqualsValidator("Confirmation invalide", Collections.singletonList(passwordField.getText())));
        validationSupport.registerValidator(confirmationText, Validator.createEqualsValidator("Confirmation invalide", Collections.singletonList(passwordField.getText())));
    }

    public void passwordTextChange(){
        passwordField.setText(passwordText.getText());
        validationSupport.registerValidator(confirmationField, Validator.createEqualsValidator("Confirmation invalide", Collections.singletonList(passwordField.getText())));
        validationSupport.registerValidator(confirmationText, Validator.createEqualsValidator("Confirmation invalide", Collections.singletonList(passwordField.getText())));
    }

    public void confirmationChange(){
        confirmationText.setText(confirmationField.getText());
    }

    public void confirmationTextChange(){
        confirmationField.setText(confirmationText.getText());
    }

    public void passwordView(ActionEvent event) {
        if (passwordField.isVisible()){
            passwordText.setVisible(true);
            validationSupport.registerValidator(passwordText, Validator.createRegexValidator("Le mot de passe doit contenir:\nUn minimum de 8 caractères\nUn nombre\nEt un caractères spéciale",Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[.@$!%*#?&])[A-Za-z\\d.@$!%*#?&]{8,}$"),Severity.ERROR));
            passwordField.setVisible(false);
            passwordButton.setGraphic(new ImageView(eye2Image));
        }
        else{
            passwordText.setVisible(false);
            passwordField.setVisible(true);
            validationSupport.registerValidator(passwordField, Validator.createRegexValidator("Le mot de passe doit contenir:\nUn minimum de 8 caractères\nUn nombre\nEt un caractères spéciale",Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[.@$!%*#?&])[A-Za-z\\d.@$!%*#?&]{8,}$"),Severity.ERROR));
            passwordButton.setGraphic(new ImageView(eye1Image));
        }
    }

    public void confirmationView(ActionEvent event) {
        if (confirmationField.isVisible()){
            confirmationText.setVisible(true);
            validationSupport.registerValidator(confirmationText, Validator.createEqualsValidator("Confirmation invalide", Collections.singletonList(passwordField.getText())));
            confirmationField.setVisible(false);
            confirmationButton.setGraphic(new ImageView(eye2Image));
        }
        else{
            confirmationText.setVisible(false);
            confirmationField.setVisible(true);
            validationSupport.registerValidator(confirmationField, Validator.createEqualsValidator("Confirmation invalide", Collections.singletonList(passwordField.getText())));
            confirmationButton.setGraphic(new ImageView(eye1Image));
        }
    }
}
