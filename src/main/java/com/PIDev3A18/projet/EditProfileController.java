package com.PIDev3A18.projet;

import entity.Employee;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import services.ServiceEmployee;
import utils.ImgApi;
import utils.UserSession;

import javax.imageio.ImageIO;
import javax.tools.Tool;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class EditProfileController{

    @FXML private ImageView profilePicture;
    @FXML private StackPane confirmStackPane;
    @FXML private Button confirmationBtn;
    @FXML private PasswordField confirmationField;
    @FXML private TextField confirmationText;
    @FXML private TextField emailField;
    @FXML private TextField nomField;
    @FXML private TextField numField;
    @FXML private Button passwordBtn;
    @FXML private PasswordField passwordField;
    @FXML private StackPane passwordStackPane;
    @FXML private TextField passwordText;
    @FXML private TextField prenomField;

    Image eye1Image;
    Image eye2Image;
    UserSession userSession;
    Employee loggedInEmployee;
    ServiceEmployee serviceEmployee;
    LayoutController layoutController;

    public EditProfileController(LayoutController layoutController){
        this.layoutController = layoutController;
        serviceEmployee = new ServiceEmployee();
        userSession = UserSession.getInstance();
        loggedInEmployee = userSession.getLoggedInEmployee();
        InputStream stream = getClass().getResourceAsStream("icons/eye1.png");
        eye1Image = new Image(stream,20,20,true,true);
        stream = getClass().getResourceAsStream("icons/eye2.png");
        eye2Image = new Image(stream,20,20,true,true);
    }

    @FXML
    public void initialize(){
        passwordBtn.setGraphic(new ImageView(eye1Image));
        confirmationBtn.setGraphic(new ImageView(eye1Image));

        Image image = new Image(loggedInEmployee.getImageUrl());
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double minSize = Math.min(imageWidth, imageHeight);
        profilePicture.setViewport(new Rectangle2D(
                (imageWidth - minSize) / 2, // Center X
                (imageHeight - minSize) / 2, // Center Y
                minSize, minSize // Crop size (square)
        ));
        profilePicture.setImage(image);
        Circle clip = new Circle(profilePicture.getFitHeight() / 2);
        clip.setCenterX(profilePicture.getFitHeight() / 2);
        clip.setCenterY(profilePicture.getFitHeight() / 2);
        profilePicture.setClip(clip);

        nomField.setText(loggedInEmployee.getLastName());
        prenomField.setText(loggedInEmployee.getFirstName());
        emailField.setText(loggedInEmployee.getEmail());
        numField.setText(loggedInEmployee.getPhone());
        passwordField.setText(loggedInEmployee.getPassword());
        passwordText.setText(loggedInEmployee.getPassword());
        confirmationField.setText(loggedInEmployee.getPassword());
        confirmationText.setText(loggedInEmployee.getPassword());
    }

    public void passwordClick(){
        if (passwordField.isVisible()){
            passwordBtn.setGraphic(new ImageView(eye2Image));
        }
        else{
            passwordBtn.setGraphic(new ImageView(eye1Image));
        }
        passwordField.setVisible(!passwordField.isVisible());
        passwordText.setVisible(!passwordText.isVisible());
    }

    public void confirmationClick(){
        if (confirmationField.isVisible()){
        confirmationBtn.setGraphic(new ImageView(eye2Image));
    }
    else{
        confirmationBtn.setGraphic(new ImageView(eye1Image));
    }
        confirmationField.setVisible(!confirmationField.isVisible());
        confirmationText.setVisible(!confirmationText.isVisible());
    }

    public void passwordFieldChange(){
        passwordText.setText(passwordField.getText());
    }

    public void passwordTextChange(){
        passwordField.setText(passwordText.getText());
    }

    public void confirmationFieldChange(){
        confirmationText.setText(confirmationField.getText());
    }

    public void confirmationTextChange(){
        confirmationField.setText(confirmationText.getText());
    }

    public void updateAccount() throws SQLException {
        if(verif()){
            boolean v = true;
            emailField.getStyleClass().remove("error");
            if (serviceEmployee.verifEmail(emailField.getText()) && !emailField.getText().equals(loggedInEmployee.getEmail())){
                emailField.getStyleClass().add("error");
                Tooltip tooltip = new Tooltip("Email déja utilisé");
                emailField.setTooltip(tooltip);
                v = false;
            }
            numField.getStyleClass().remove("error");
            if (serviceEmployee.verifPhone(numField.getText()) && !numField.getText().equals(loggedInEmployee.getPhone())){
                numField.getStyleClass().add("error");
                Tooltip tooltip = new Tooltip("Numero déja utilisé");
                numField.setTooltip(tooltip);
                v = false;
            }
            if(v){
                loggedInEmployee.setLastName(nomField.getText());
                loggedInEmployee.setFirstName(prenomField.getText());
                loggedInEmployee.setEmail(emailField.getText());
                loggedInEmployee.setPhone(numField.getText());
                loggedInEmployee.setPassword(passwordField.getText());
                serviceEmployee.update(loggedInEmployee);
                layoutController.refreshUser();
            }
        }
    }

    public Boolean verif(){
        boolean v = true;
        nomField.getStyleClass().remove("error");
        prenomField.getStyleClass().remove("error");
        emailField.getStyleClass().remove("error");
        numField.getStyleClass().remove("error");
        passwordField.getStyleClass().remove("error");
        passwordText.getStyleClass().remove("error");
        confirmationField.getStyleClass().remove("error");
        confirmationText.getStyleClass().remove("error");

        if(nomField.getText().isBlank()){
            nomField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Nom invalide");
            nomField.setTooltip(tooltip);
            v = false;
        }
        if(prenomField.getText().isBlank()){
            prenomField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Prenom invalide");
            prenomField.setTooltip(tooltip);
            v = false;
        }
        if(!Pattern.matches("^[0-9]{8}$",numField.getText())){
            numField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Numero invalide");
            numField.setTooltip(tooltip);
            v = false;
        }
        if(!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",emailField.getText())){
            emailField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Email invalide");
            emailField.setTooltip(tooltip);
            v = false;
        }
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

        return v;
    }

    public void uploadImage(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterpng);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file!=null) {
            new Thread(() -> {
                try {
                    String imageUrl = ImgApi.uploadImage(file);
                    Platform.runLater(() -> {
                        loggedInEmployee.setImageUrl(imageUrl);
                        try {
                            serviceEmployee.update(loggedInEmployee);
                            Image image = new Image(imageUrl);
                            profilePicture.setImage(null);
                            double imageWidth = image.getWidth();
                            double imageHeight = image.getHeight();
                            double minSize = Math.min(imageWidth, imageHeight);
                            profilePicture.setViewport(new Rectangle2D(
                                    (imageWidth - minSize) / 2,
                                    (imageHeight - minSize) / 2,
                                    minSize, minSize
                            ));
                            profilePicture.setImage(image);
                            layoutController.refreshUser();

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    System.out.println(e.getMessage()+" upload failed");
                }
            }).start();
        }
    }

}
