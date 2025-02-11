package com.PIDev3A18.projet;

import com.google.gson.Gson;
import entity.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import services.ServiceEmployee;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.FileReader;
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

    ValidationSupport validationSupport = new ValidationSupport();

    public void login(ActionEvent event) throws SQLException, IOException {
        Gson gson = new Gson();

        if (validationSupport.isInvalid()){
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
            else{
                try (FileWriter writer = new FileWriter("saved-employee.json")) {
                    gson.toJson(null, writer);
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void initialize() {
        validationSupport.registerValidator(emailField, Validator.createRegexValidator("Email invalide",Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE), Severity.ERROR));
        validationSupport.registerValidator(passwordField, Validator.createRegexValidator("Mot de passe invalide",Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[.@$!%*#?&])[A-Za-z\\d.@$!%*#?&]{8,}$"),Severity.ERROR));
    }
}
