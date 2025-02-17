package com.PIDev3A18.projet;

import entity.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    public void Delete(ActionEvent event) {
        ServiceEvent se = new ServiceEvent();
        se.delete(this.e);
        c.initialize();
    }
    public void Update(ActionEvent event) {
        c.setUpdateEvent(e);
        c.setTitreUpdate(e.getTitre());
        c.setDescriptionUpdate(e.getDescription());
        LocalDate dateOnly=e.getDateetheure().toLocalDate();
        c.setDateUpdate(dateOnly);
        String hour = Integer.toString(e.getDateetheure().getHour());
        String minute = Integer.toString(e.getDateetheure().getMinute());
        c.setHeureUpdate(hour);
        c.setMinuteUpdate(minute);
        c.setAdresseUpdate(e.getLieu());
        c.setTypeListUpdate(e.getType());
        c.setNbplaceUpdate(Integer.toString(e.getNombredeplace()));
        c.layoutGoToUpdateEvenement();
    }
    public void setController(EvenementsController controller) {
        this.c = controller;
    }
    public void setDeleteInvisible(){
        DeleteButton.setVisible(false);
    }
}
