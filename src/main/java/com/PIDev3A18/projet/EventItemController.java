package com.PIDev3A18.projet;

import entity.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class EventItemController {
    @FXML private Label date;
    @FXML private Label title;
    @FXML private Label description;
    @FXML private Text time;

    private Event event;

    public EventItemController(Event event) {
        this.event = event;
    }

    public void initialize() {
        date.setText(String.valueOf(event.getDateetheure().getDayOfMonth()));
        title.setText(event.getTitre());
        description.setText(event.getDescription());
        time.setText(event.getDateetheure().getHour() + ":" + event.getDateetheure().getMinute());
    }
}
