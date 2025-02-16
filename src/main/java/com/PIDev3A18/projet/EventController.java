package com.PIDev3A18.projet;

import entity.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.ServiceEvent;

public class EventController {
    Event e;
    @FXML
    private Label Description;

    @FXML
    private Label Title;
    @FXML
    private Button DeleteButton;

    EvenementsController c;
    public void setTitle(String title) {
        Title.setText(title);
    }
    public void setDescription(String description) {
        Description.setText(description);
    }
    public void setEvent(Event event) {
        this.e = event;
    }
    public void Delete(ActionEvent event) {
        ServiceEvent se = new ServiceEvent();
        se.delete(this.e);
        c.initialize();
    }
    public void setController(EvenementsController controller) {
        this.c = controller;
    }
    public void setDeleteInvisible(){
        DeleteButton.setVisible(false);
    }
}
