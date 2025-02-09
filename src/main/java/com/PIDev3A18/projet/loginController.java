package com.PIDev3A18.projet;

import entity.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.ServiceEmployee;

import java.io.IOException;
import java.sql.SQLException;

public class loginController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;

    public void login(ActionEvent event) throws SQLException, IOException {
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        String email = emailField.getText();
        String password = passwordField.getText();
        System.out.println("Login "+email+" "+password);
        if (serviceEmployee.verifPassword(email, password)){
            System.out.println("Password verified");
            Employee employee = serviceEmployee.readByEmailAndPassword(email, password);

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
}
