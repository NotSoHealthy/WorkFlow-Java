package com.PIDev3A18.projet;

import entity.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.Validator;
import services.ServiceEmployee;
import utils.GMailer;
import utils.SMSApi;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Pattern;

public class PasswordResetController {

    @FXML private TextField codeField;
    @FXML private VBox codeVBox;
    @FXML private Button confirmationButton;
    @FXML private PasswordField confirmationField;
    @FXML private TextField confirmationText;
    @FXML private Button emailButton;
    @FXML private TextField emailField;
    @FXML private VBox emailVBox;
    @FXML private VBox inputVBox;
    @FXML private Button numButton;
    @FXML private TextField numberField;
    @FXML private VBox numberVBox;
    @FXML private Button passwordButton;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordText;
    @FXML private VBox passwordVBox;
    @FXML private Text triesText;

    private ServiceEmployee serviceEmployee;
    private GMailer gmailer;
    private int code;
    private Random random;
    private int tries = 3;
    Image eye1Image;
    Image eye2Image;
    private Boolean email = true;
    private Employee employee;

    public PasswordResetController() {
        serviceEmployee = new ServiceEmployee();
        random = new Random();
        InputStream stream = getClass().getResourceAsStream("icons/eye1.png");
        eye1Image = new Image(stream,20,20,true,true);
        stream = getClass().getResourceAsStream("icons/eye2.png");
        eye2Image = new Image(stream,20,20,true,true);
    }

    public void initialize() {
        passwordButton.setGraphic(new ImageView(eye1Image));
        confirmationButton.setGraphic(new ImageView(eye1Image));
    }

    @FXML
    void send(ActionEvent event) throws Exception {
        code = 100000 + random.nextInt(900000);

        emailField.getStyleClass().remove("error");
        numberField.getStyleClass().remove("error");
        if (emailVBox.isVisible()){
            if(!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",emailField.getText())){
                emailField.getStyleClass().add("error");
                Tooltip tooltip = new Tooltip("Email invalide");
                emailField.setTooltip(tooltip);
            }
            else if (!serviceEmployee.verifEmail(emailField.getText())){
                emailField.getStyleClass().add("error");
                Tooltip tooltip = new Tooltip("Email inéxistant");
                emailField.setTooltip(tooltip);
            }
            else{
                employee = serviceEmployee.readByEmail(emailField.getText());
                new GMailer().sendCodeMail(employee,Integer.toString(code));
                email = true;
                inputVBox.setVisible(false);
                codeVBox.setVisible(true);
            }
        }
        else{
            if(!Pattern.matches("^[0-9]{8}$",numberField.getText())){
                numberField.getStyleClass().add("error");
                Tooltip tooltip = new Tooltip("Numero invalide");
                numberField.setTooltip(tooltip);
            }
            else if (!serviceEmployee.verifPhone(numberField.getText())){
                numberField.getStyleClass().add("error");
                Tooltip tooltip = new Tooltip("Numero inéxistant");
                numberField.setTooltip(tooltip);
            }
            else{
                employee = serviceEmployee.readByNumber(numberField.getText());
                SMSApi.sendCodeSMS(employee,Integer.toString(code));
                email = false;
                inputVBox.setVisible(false);
                codeVBox.setVisible(true);
            }
        }
    }

    @FXML
    void swapToPhone(ActionEvent event) {
        emailVBox.setVisible(false);
        numberVBox.setVisible(true);
        emailButton.getStyleClass().removeAll("selected-btn");
        numButton.getStyleClass().add("selected-btn");
    }

    @FXML
    void swapToEmail(ActionEvent event) {
        emailVBox.setVisible(true);
        numberVBox.setVisible(false);
        emailButton.getStyleClass().add("selected-btn");
        numButton.getStyleClass().removeAll("selected-btn");
    }

    @FXML
    void goToSignIn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        emailButton.getScene().setRoot(root);
    }

    @FXML
    void checkCode() throws IOException {
        tries--;
        if (codeField.getText().equals(Integer.toString(code))){
            codeVBox.setVisible(false);
            passwordVBox.setVisible(true);
        }
        else if (tries == 0){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = fxmlLoader.load();
            LoginController loginController = fxmlLoader.getController();
            loginController.showNotification("Vous avez échoué toutes vos tentatives de saisie du code",3,false);
            emailButton.getScene().setRoot(root);
        }
        else{
            triesText.setText(tries + " tentatives restantes");
        }
    }

    public void passwordView(){
        if (passwordField.isVisible()){
            passwordButton.setGraphic(new ImageView(eye2Image));
        }
        else{
            passwordButton.setGraphic(new ImageView(eye1Image));
        }
        passwordField.setVisible(!passwordField.isVisible());
        passwordText.setVisible(!passwordText.isVisible());
    }

    public void confirmationView(){
        if (confirmationField.isVisible()){
            confirmationButton.setGraphic(new ImageView(eye2Image));
        }
        else{
            confirmationButton.setGraphic(new ImageView(eye1Image));
        }
        confirmationField.setVisible(!confirmationField.isVisible());
        confirmationText.setVisible(!confirmationText.isVisible());
    }

    public void passwordChange(){
        passwordText.setText(passwordField.getText());
    }

    public void passwordTextChange(){
        passwordField.setText(passwordText.getText());
    }

    public void confirmationChange(){
        confirmationText.setText(confirmationField.getText());
    }

    public void confirmationTextChange(){
        confirmationField.setText(confirmationText.getText());
    }

    public void changePassword() throws Exception {
        boolean v = true;
        passwordField.getStyleClass().remove("error");
        passwordText.getStyleClass().remove("error");
        confirmationField.getStyleClass().remove("error");
        confirmationText.getStyleClass().remove("error");

        if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[.@$!%*#?&])[A-Za-z\\d.@$!%*#?&]{8,}$",passwordField.getText())){
            passwordField.getStyleClass().add("error");
            passwordText.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Le mot de passe doit contenir:\n" +
                    "Un minimum de 8 caractères\n" +
                    "Un nombre\n" +
                    "Et un caractères spéciale");
            passwordField.setTooltip(tooltip);
            passwordText.setTooltip(tooltip);
            v = false;
        }
        if(!confirmationField.getText().equals(passwordField.getText())){
            confirmationField.getStyleClass().add("error");
            confirmationText.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Confirmation invalide");
            confirmationField.setTooltip(tooltip);
            confirmationText.setTooltip(tooltip);
            v = false;
        }

        if (v){
            employee.setPassword(passwordField.getText());
            serviceEmployee.update(employee);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = fxmlLoader.load();
            LoginController loginController = fxmlLoader.getController();
            loginController.showNotification("Mot de passe réinitialisé avec succès",2,true);
            emailButton.getScene().setRoot(root);
        }
    }
}
