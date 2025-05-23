package com.PIDev3A18.projet;

import entity.Employee;
import entity.Formation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.*;

import javafx.stage.Stage;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import services.ServiceFormation;
import utils.UserSession;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.regex.Pattern;


public class AddFormationController {
    UserSession userSession = UserSession.getInstance();
    Employee loggedEmployee = userSession.getLoggedInEmployee();
    @FXML
    private Button AddFormationButton;
    @FXML
    private Button ClearFormationButton;

    @FXML
    private TextField TxtParticipants;

    @FXML
    private TextField TxtTitle;

    @FXML
    private DatePicker dateFin;
    @FXML
    private DatePicker dateDebut;

    @FXML
    private TextField TxtDescription;

    ValidationSupport validationSupport;

    public AddFormationController() {
        validationSupport = new ValidationSupport();
    }
    @FXML
    void AddFormation(ActionEvent event)  {
        String title = TxtTitle.getText();
        String description = TxtDescription.getText();
        int participants = Integer.parseInt(TxtParticipants.getText());
        LocalDate debut = dateDebut.getValue();
        LocalDate fin = dateFin.getValue();
        Formation f= new Formation(title,description,debut,fin,participants,loggedEmployee);
        ServiceFormation sf=new ServiceFormation();
        try {
            sf.add(f);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Formation ajouté avec succès!");
            ((Stage) TxtTitle.getScene().getWindow()).close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the formation!");
        }

    }
    @FXML
    void ClearInfo(ActionEvent event) {
        TxtParticipants.setText(null);
        TxtTitle.setText(null);
        dateDebut.setValue(null);
        dateFin.setValue(null);
        TxtDescription.setText(null);
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void initialize(){
        dateDebut.setDayCellFactory(dayCell -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffcccc;");
                }
            }
        });
        dateFin.setDayCellFactory(dayCell -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate oneWeekFromToday = LocalDate.now().plusWeeks(1);
                if (date.isBefore(oneWeekFromToday)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffcccc;");
                }
            }
        });
        dateDebut.setValue(LocalDate.now());
        validationSupport.registerValidator(TxtTitle, Validator.createEmptyValidator("Titre ne peut pas être vide"));
        validationSupport.registerValidator(TxtDescription, Validator.createEmptyValidator("Description ne peut pas être vide"));
        validationSupport.registerValidator(TxtParticipants,Validator.createRegexValidator("Le nombre de participants doit être un nombre entier positif",Pattern.compile("^[1-9][0-9]*$"), Severity.ERROR));
        validationSupport.registerValidator(dateDebut, Validator.createEmptyValidator("Date debut doit être selectionner"));
        LocalDate startDate = dateDebut.getValue();
        LocalDate endDate = startDate.plusWeeks(1);
        dateFin.setValue(endDate);
        validationSupport.registerValidator(dateFin, Validator.createEmptyValidator("Date fin doit être selectionner"));
        validationSupport.registerValidator(dateDebut, Validator.createPredicateValidator(
                val -> val != null && ((LocalDate) val).isBefore(dateFin.getValue()), "Date debut doit être avant date fin"));
    }


}
