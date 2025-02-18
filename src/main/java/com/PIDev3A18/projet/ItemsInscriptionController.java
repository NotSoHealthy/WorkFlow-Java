package com.PIDev3A18.projet;

import entity.Employee;
import entity.Inscription;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.ServiceInscription;
import utils.UserSession;

import java.sql.SQLException;

public class ItemsInscriptionController {
    private Inscription inscription;
    @FXML
    private Label TxtDateInscrit;

    @FXML
    private Label TxtFormation;
    @FXML
    private Label TxtEmploye;
    @FXML
    private HBox Hbox;

    @FXML
    private ComboBox<String> comboBox;

    private Employee loggedInEmployee;
    private UserSession userSession;


    @FXML
    public void initialize() {
        userSession = UserSession.getInstance();
        loggedInEmployee = userSession.getLoggedInEmployee();
        comboBox.setDisable(!loggedInEmployee.getRole().equals("Résponsable"));
        comboBox.setItems(FXCollections.observableArrayList("Approver", "Refuser"));
        String status = inscription.getStatus();
        comboBox.setValue(status);
        comboBox.getStyleClass().add(inscription.getStatus());
        TxtDateInscrit.setText(inscription.getDateRegistration().toString());
        TxtFormation.setText(inscription.getFormation().getTitle());
        TxtEmploye.setText("Employé");
        if(loggedInEmployee.getRole().equals("Résponsable")) {
            TxtEmploye.setText(inscription.getEmployee().getFirstName() + " " + inscription.getEmployee().getLastName());
        }
        else{
            Hbox.getChildren().remove(TxtEmploye);
        }
    }
    @FXML
    public void comboBoxChange(ActionEvent event) throws SQLException {
        ServiceInscription si = new ServiceInscription();
        inscription.setStatus(comboBox.getValue().toLowerCase());
        si.update(inscription);
        if(comboBox.getValue().equals("Refuser")) {
            si.delete(inscription);
            Hbox.getChildren().clear(); // Remove all elements
            initialize();
        }

    }
    public ItemsInscriptionController(Inscription inscription) {
        this.inscription = inscription;
    }


}
