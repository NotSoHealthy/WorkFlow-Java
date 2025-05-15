package com.PIDev3A18.projet;

import entity.Employee;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import services.ServiceEmployee;
import utils.ImgApi;
import utils.PasswordHasher;
import utils.UserSession;

import java.io.*;
import java.sql.SQLException;
import java.util.regex.Pattern;

import static com.j256.twofactorauth.TimeBasedOneTimePasswordUtil.*;

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
    @FXML private Label uploadLabel;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private ComboBox<String> comboBox;
    @FXML private TextField adresseField;
    @FXML private HBox notifHbox;
    @FXML private Text notifText;
    @FXML private ImageView qrImage;
    @FXML private VBox qrVBox;
    @FXML private Button qrButton;

    Image eye1Image;
    Image eye2Image;
    UserSession userSession;
    Employee loggedInEmployee;
    ServiceEmployee serviceEmployee;
    LayoutController layoutController;
    String newImageUrl;
    String two_factor_secret;

    public EditProfileController(LayoutController layoutController){
        this.layoutController = layoutController;
        serviceEmployee = new ServiceEmployee();
        userSession = UserSession.getInstance();
        loggedInEmployee = userSession.getLoggedInEmployee();
        newImageUrl = loggedInEmployee.getImageUrl();
        InputStream stream = getClass().getResourceAsStream("icons/eye1.png");
        eye1Image = new Image(stream,20,20,true,true);
        stream = getClass().getResourceAsStream("icons/eye2.png");
        eye2Image = new Image(stream,20,20,true,true);
    }

    @FXML
    public void initialize() throws SQLException {
        comboBox.setItems(FXCollections.observableArrayList("Ariana Ville", "Bab El Bhar", "Bab Souika", "Ben Arous", "Bou Mhel el-Bassatine",
                "Carthage", "Cité El Khadra", "Djebel Jelloud", "El Kabaria", "El Menzah",
                "El Mourouj", "El Omrane", "El Omrane supérieur", "El Ouardia", "Ettadhamen",
                "Ettahrir", "Ezzahra", "Ezzouhour", "Fouchana", "Hammam Chott",
                "Hammam Lif", "Hraïria", "Kalâat el-Andalous", "La Goulette", "La Marsa",
                "La Médina", "La Soukra", "Le Bardo", "Le Kram", "Medina Jedida",
                "Mégrine", "Mnihla", "Mohamedia", "Mornag", "Radès", "Raoued",
                "Séjoumi", "Sidi El Béchir", "Sidi Hassine", "Sidi Thabet"));

        passwordBtn.setGraphic(new ImageView(eye1Image));
        confirmationBtn.setGraphic(new ImageView(eye1Image));

        qrButton.setText((loggedInEmployee.getTwo_factor_secret() == null) ? "Activer" : "Désactiver");
        qrVBox.setVisible(loggedInEmployee.getTwo_factor_secret() != null);
        if (loggedInEmployee.getTwo_factor_secret() != null){
            qrButton.setOnAction(event -> disable2FA());
            String qrCode = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data="+generateOtpAuthUrl("WorkFlow: "+loggedInEmployee.getEmail(),two_factor_secret);
            Image qrCodeImage = new Image(qrCode,true);
            qrImage.setImage(qrCodeImage);
        }

        Image image = new Image(loggedInEmployee.getImageUrl(), true);
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
        adresseField.setText(loggedInEmployee.getAdresse());
        comboBox.getSelectionModel().select(loggedInEmployee.getGouvernorat());
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
                loggedInEmployee.setPassword(PasswordHasher.hashPassword(passwordField.getText()));
                loggedInEmployee.setAdresse(adresseField.getText());
                loggedInEmployee.setGouvernorat(comboBox.getSelectionModel().getSelectedItem());

                if (!loggedInEmployee.getImageUrl().equals(newImageUrl)) {
                    loggedInEmployee.setImageUrl(newImageUrl);
                    serviceEmployee.update(loggedInEmployee);
                    Image image = new Image(newImageUrl);
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
                }
                else{
                    serviceEmployee.update(loggedInEmployee);
                }
                showNotification("Compte mis à jour avec succès",2,true);
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
        adresseField.getStyleClass().remove("error");
        comboBox.getStyleClass().remove("error");

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
        if (adresseField.getText() == null || adresseField.getText().isBlank()) {
            adresseField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Adresse invalide");
            adresseField.setTooltip(tooltip);
            v = false;
        }
        if (comboBox.getValue() == null) {
            comboBox.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Choisissez un gouvernorat");
            comboBox.setTooltip(tooltip);
            v = false;
        }

        return v;
    }

    public void uploadImage() throws IOException {
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
                System.out.println("Upload Started");
                uploadLabel.setVisible(true);
                cancelBtn.setDisable(true);
                confirmBtn.setDisable(true);
                try {
                    String imageUrl = ImgApi.uploadImage(file);
                    Platform.runLater(() -> {
                        System.out.println("Upload Finished");
                        uploadLabel.setVisible(false);
                        cancelBtn.setDisable(false);
                        confirmBtn.setDisable(false);
                        newImageUrl = imageUrl;
                        Image image = new Image(newImageUrl);
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
                    });
                } catch (IOException e) {
                    System.out.println(e.getMessage()+" upload failed");
                }
            }).start();
        }
    }

    public void annuler(ActionEvent event) {
        if (!loggedInEmployee.getImageUrl().equals(newImageUrl)) {
            newImageUrl = loggedInEmployee.getImageUrl();
            Image image = new Image(newImageUrl);
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
        }

        nomField.setText(loggedInEmployee.getLastName());
        prenomField.setText(loggedInEmployee.getFirstName());
        emailField.setText(loggedInEmployee.getEmail());
        numField.setText(loggedInEmployee.getPhone());
        passwordField.setText(loggedInEmployee.getPassword());
        passwordText.setText(loggedInEmployee.getPassword());
        confirmationField.setText(loggedInEmployee.getPassword());
        confirmationText.setText(loggedInEmployee.getPassword());
        if (loggedInEmployee.getAdresse() != null) {
            adresseField.setText(loggedInEmployee.getAdresse());
        }
        if (loggedInEmployee.getGouvernorat() != null) {
            comboBox.getSelectionModel().select(loggedInEmployee.getGouvernorat());
        }
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

    public void enable2FA() {
        two_factor_secret = generateBase32Secret();
        loggedInEmployee.setTwo_factor_secret(two_factor_secret);
        try {
            serviceEmployee.set2FASecret(loggedInEmployee);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String qrCode = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&data="+generateOtpAuthUrl("WorkFlow: "+loggedInEmployee.getEmail(),two_factor_secret);
        Image qrCodeImage = new Image(qrCode,true);
        qrImage.setImage(qrCodeImage);
        qrVBox.setVisible(true);
        qrButton.setText("Désactiver");
        qrButton.setOnAction(event -> disable2FA());
    }

    public void disable2FA() {
        loggedInEmployee.setTwo_factor_secret(null);
        try {
            serviceEmployee.remove2FASecret(loggedInEmployee);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        qrImage.setImage(null);
        qrVBox.setVisible(false);
        qrButton.setText("Activer");
        qrButton.setOnAction(event -> enable2FA());
    }

}
