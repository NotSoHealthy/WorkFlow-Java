package com.PIDev3A18.projet;

import entity.Employee;
import entity.Formation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import services.ServiceFormation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class EditFormationController {
    Employee loggedEmployee;
    @FXML
    private Button AddFormationButton;

    @FXML
    private Button ClearFormationButton;

    @FXML
    private TextField TxtDescription;

    @FXML
    private TextField TxtParticipants;

    @FXML
    private TextField TxtTitle;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;
    private boolean update;
    private Formation currentFormation;
    ValidationSupport validationSupport;

    @FXML
    void EditFormation(ActionEvent event) {
        currentFormation.setTitle(TxtTitle.getText());
        currentFormation.setDescription(TxtDescription.getText());
        currentFormation.setParticipants_Max(Integer.parseInt(TxtParticipants.getText()));
        currentFormation.setDateBegin(dateDebut.getValue());
        currentFormation.setDateEnd(dateFin.getValue());
        ServiceFormation sf = new ServiceFormation();
        try {
            sf.update(currentFormation);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Formation enregistrer avec succès!");
            ((Stage) TxtTitle.getScene().getWindow()).close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the formation.");
        }

    }
    public EditFormationController()
    {
        validationSupport = new ValidationSupport();
    }
    @FXML
    public void initialize(){

        validationSupport.registerValidator(TxtTitle, Validator.createEmptyValidator("Titre ne peut pas être vide"));
        validationSupport.registerValidator(TxtDescription, Validator.createEmptyValidator("Description ne peut pas être vide"));
        validationSupport.registerValidator(TxtParticipants,Validator.createRegexValidator("Le nombre de participants doit être un nombre entier positif", Pattern.compile("^[1-9][0-9]*$"), Severity.ERROR));
        validationSupport.registerValidator(dateDebut, Validator.createEmptyValidator("Date debut doit être selectionner"));
        validationSupport.registerValidator(dateFin, Validator.createEmptyValidator("Date fin doit être selectionner"));
        validationSupport.registerValidator(dateDebut, Validator.createPredicateValidator(
                val -> val != null && ((LocalDate) val).isBefore(dateFin.getValue()), "Date debut doit être avant date fin "));
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
    public void setLoggedinEmployee(Employee loggedinEmployee) {
        this.loggedEmployee = loggedinEmployee;
    }
    public void setFormationData(Formation formation) {
        this.currentFormation = formation;

        TxtTitle.setText(formation.getTitle());
        TxtDescription.setText(formation.getDescription());
        TxtParticipants.setText(String.valueOf(formation.getParticipants_Max()));
        dateDebut.setValue(formation.getDateBegin());
        dateFin.setValue(formation.getDateEnd());
    }
}
