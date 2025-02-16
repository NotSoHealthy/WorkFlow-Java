package com.PIDev3A18.projet;

import entity.Formation;
import entity.Inscription;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import services.ServiceInscription;

import java.sql.SQLException;

public class ItemsInscriptionController {
    private Inscription inscription;
    @FXML
    private Label TxtDateInscrit;

    @FXML
    private Label TxtFormation;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    public void initialize() {
        comboBox.setItems(FXCollections.observableArrayList("Approver", "Refuser"));
        String status = inscription.getStatus();
        comboBox.setValue(status);
        comboBox.getStyleClass().add(inscription.getStatus());
        TxtDateInscrit.setText(inscription.getDateRegistration().toString());
        TxtFormation.setText(inscription.getFormation().getTitle());
    }
    @FXML
    public void comboBoxChange(ActionEvent event) throws SQLException {
        ServiceInscription si = new ServiceInscription();
        inscription.setStatus(comboBox.getValue().toLowerCase());
        si.update(inscription);
        if(comboBox.getValue().equals("Refuser")) {
            si.delete(inscription);
            ((Stage) TxtDateInscrit.getScene().getWindow()).close();
        }

    }
    public ItemsInscriptionController(Inscription inscription) {
        this.inscription = inscription;
    }


}
