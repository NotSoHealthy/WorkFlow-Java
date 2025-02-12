package com.PIDev3A18.projet;

import com.google.gson.Gson;
import entity.Employee;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import services.ServiceEmployee;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class loginController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private CheckBox rememberCheckbox;
    @FXML
    private HBox notifHbox;
    @FXML
    private Text notifText;

    ValidationSupport validationSupport = new ValidationSupport();

    public void login(ActionEvent event) throws SQLException, IOException {
        Gson gson = new Gson();

        if (validationSupport.isInvalid()){
            showNotification("Compte inexistant");
            return;
        }

        ServiceEmployee serviceEmployee = new ServiceEmployee();
        String email = emailField.getText();
        String password = passwordField.getText();
        if (serviceEmployee.verifPassword(email, password)){
            System.out.println("Password verified");
            Employee employee = serviceEmployee.readByEmailAndPassword(email, password);
            if (rememberCheckbox.isSelected()) {
                try (FileWriter writer = new FileWriter("saved-employee.json")) {
                    gson.toJson(employee, writer);
                    System.out.println("Account saved successfully");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();
            LayoutController layoutController = loader.getController();
            layoutController.setLoggedEmployee(employee);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            notifHbox.setVisible(true);
        }
    }

    @FXML
    public void initialize() {
        validationSupport.registerValidator(emailField, Validator.createRegexValidator("Email invalide",Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE), Severity.ERROR));
        validationSupport.registerValidator(passwordField, Validator.createRegexValidator("Mot de passe invalide",Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[.@$!%*#?&])[A-Za-z\\d.@$!%*#?&]{8,}$"),Severity.ERROR));
    }

    public void showNotification(String text) {
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
        fadeOut.setDelay(Duration.seconds(3)); // Wait 3 seconds before fading out

        // After fade-out, hide the HBox to free up space
        fadeOut.setOnFinished(event -> notifHbox.setVisible(false));

        // Play fade-in first, then fade-out after delay
        fadeIn.setOnFinished(event -> fadeOut.play());
        fadeIn.play();
    }
}
