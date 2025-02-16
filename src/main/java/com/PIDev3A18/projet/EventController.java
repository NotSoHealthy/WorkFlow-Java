package com.PIDev3A18.projet;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EventController {

    @FXML
    private Label Description;

    @FXML
    private Label Title;
    @FXML
    private Label ID_Event;

    public void setTitle(String title) {
        Title.setText(title);
    }
    public void setDescription(String description) {
        Description.setText(description);
    }
    public void setIDEvent(String IDEvent) {
        ID_Event.setText(IDEvent);
    }
}
