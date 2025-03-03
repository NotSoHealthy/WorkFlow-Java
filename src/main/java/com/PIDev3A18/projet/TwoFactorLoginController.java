package com.PIDev3A18.projet;

import entity.Employee;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import utils.UserSession;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.j256.twofactorauth.TimeBasedOneTimePasswordUtil.generateCurrentNumberString;

public class TwoFactorLoginController {
    @FXML private TextField codeField;
    @FXML private HBox notifHbox;
    @FXML private Text notifText;

    private Employee employee;
    private UserSession userSession;

    public TwoFactorLoginController(Employee employee) {
        this.employee = employee;
        userSession = UserSession.getInstance();
    }

    public void login() throws IOException, GeneralSecurityException {
        String code = codeField.getText();
        String toVerify = generateCurrentNumberString(employee.getTwo_factor_secret());
        if (code.equals(toVerify)) {
            userSession.login(employee);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            Parent root = loader.load();
            LayoutController layoutController = loader.getController();
            layoutController.layoutGoToDashboard(null);
            codeField.getScene().setRoot(root);
        }
        else{
            showNotification("Code invalide",2,false);
        }
    }

    public void goToLogIn() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        codeField.getScene().setRoot(loader.load());
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
}
