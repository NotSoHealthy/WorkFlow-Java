package com.PIDev3A18.projet;

import entity.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.ServiceReservation;

public class ReservationController {

    Reservation reservation;
    EvenementsController e;
    @FXML
    private Label DateHeureMyReservation;

    @FXML
    private Button Del;

    @FXML
    private Label NbplacesMyReservation;

    @FXML
    private Label TitleMyReservations;

    @FXML
    private Label TotaleMyReservation;

    @FXML
    private Label TypeMyReservation;

    @FXML
    void Delete(ActionEvent event) {
        ServiceReservation sr = new ServiceReservation();
        sr.delete(reservation);
        e.populateReservations(event);
    }
    public void setTitleMyReservations(String title) {
        TitleMyReservations.setText(title);
    }
    public void setDateHeureMyReservation(String date) {
        DateHeureMyReservation.setText(date);
    }
    public void setNbplacesMyReservation(String nbplaces) {
        NbplacesMyReservation.setText(nbplaces);
    }
    public void setTotalMyReservation(String total) {
        TotaleMyReservation.setText(total);
    }
    public void setTypeMyReservation(String type) {
        TypeMyReservation.setText(type);
    }
    public void setController(EvenementsController e) {
        this.e = e;
    }
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

}
