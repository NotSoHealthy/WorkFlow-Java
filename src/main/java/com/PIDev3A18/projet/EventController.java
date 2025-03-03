package com.PIDev3A18.projet;

import entity.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.ServiceEvent;

import java.time.LocalDate;

public class EventController {
    Event e;
    @FXML
    private Label Description;

    @FXML
    private Label Title;
    @FXML
    private Label DateHeure;
    @FXML
    private Label Adresse;
    @FXML
    private Button DeleteButton;
    @FXML
    private Label Type;
    @FXML
    private Label nbdispo;
    @FXML
    private Label isOnline;
    @FXML
    private Button Up;

    EvenementsController c;
    public void setTitle(String title) {
        Title.setText(title);
    }
    public void setDescription(String description) {
        Description.setText(description);
    }
    public void setDateHeure(String dateHeure) {
        DateHeure.setText(dateHeure);
    }
    public void setAdresse(String adresse) {
        Adresse.setText(adresse);
    }
    public void setEvent(Event event) {
        this.e = event;
    }
    public void setType(String type) {
        Type.setText(type);
    }
    public void setNbdispo(String nbdispo) {
        this.nbdispo.setText(nbdispo);
    }
    public void Delete(ActionEvent event) {
        ServiceEvent se = new ServiceEvent();
        se.delete(this.e);
        c.initialize();
    }
    public void Update(ActionEvent event) {
        c.setUpdateEvent(e);
        c.layoutGoToUpdateEvenement();
    }
    public void Reserver(ActionEvent event) {
        if(e.getNombredeplace()!=0) {
            c.setReserveEvent(e);
            c.layoutGoToReserveEvenement();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement!");
            alert.setHeaderText("Cet événement est complet");
            alert.showAndWait();
        }
    }
    public void setController(EvenementsController controller) {
        this.c = controller;
    }
    public void setDeleteInvisible(){
        DeleteButton.setVisible(false);
    }
    public void setUpInvisible(){
        Up.setVisible(false);
    }
    public void setisOnline() {
        isOnline.setText("En Ligne");
    }
}
