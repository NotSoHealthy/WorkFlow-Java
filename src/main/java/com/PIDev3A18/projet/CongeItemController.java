package com.PIDev3A18.projet;

import entity.Conge;
import entity.Employee;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import services.ServiceConge;
import utils.UserSession;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;

public class CongeItemController {
    private Conge conge;

    @FXML private ComboBox<String> comboBox;
    @FXML private Label endDateField;
    @FXML private Label nomText;
    @FXML private Label reasonField;
    @FXML private Label requestDateField;
    @FXML private Label startDateField;
    private Employee loggedInEmployee;
    private UserSession userSession;

    public CongeItemController(Conge conge) {
        this.conge = conge;
    }

    @FXML
    void initialize() {
        userSession = UserSession.getInstance();
        loggedInEmployee = userSession.getLoggedInEmployee();
        if (loggedInEmployee.getType().equals("responsable")){
            comboBox.setDisable(conge.getEmployee().getId() == loggedInEmployee.getId());
        }
        if (loggedInEmployee.getType().equals("employee")){
            comboBox.setDisable(true);
        }

        comboBox.setItems(FXCollections.observableArrayList("Pending", "Approved", "Denied"));
        String status = conge.getStatus();
        status = status.substring(0, 1).toUpperCase() + status.substring(1);
        comboBox.setValue(status);
        comboBox.getStyleClass().add(conge.getStatus());

        nomText.setText(conge.getEmployee().getFirstName() + " " + conge.getEmployee().getLastName());
        reasonField.setText(conge.getReason());
        requestDateField.setText(conge.getRequest_date().toString());
        startDateField.setText(conge.getStart_date().toString());
        endDateField.setText(conge.getEnd_date().toString());

        if (conge.getRequest_date().isAfter(LocalDate.now())){
            requestDateField.getStyleClass().add("date-invalid");
        }
        else {
            requestDateField.getStyleClass().add("date-valid");
        }
    }

    public void comboBoxChange(ActionEvent event) throws SQLException {
        ServiceConge serviceConge = new ServiceConge();
        conge.setStatus(comboBox.getValue().toLowerCase());
        serviceConge.update(conge);

        comboBox.getStyleClass().removeAll("pending","approved","denied");
        comboBox.getStyleClass().add(conge.getStatus());
    }

}

