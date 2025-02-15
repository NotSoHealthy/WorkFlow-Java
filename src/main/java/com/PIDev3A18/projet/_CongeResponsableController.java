package com.PIDev3A18.projet;

import entity.Conge;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import services.ServiceConge;

import java.sql.SQLException;

public class _CongeResponsableController {
    private ObservableList<Conge> congeList;
    @FXML
    private ListView<Conge> listView;

    @FXML
    public void initialize() throws SQLException {
        ServiceConge serviceConge = new ServiceConge();
        congeList = FXCollections.observableList(serviceConge.readAll());
        listView.setItems(congeList);

        // Set the custom cell factory
        listView.setCellFactory(param -> new Cell());
    }


    static class Cell extends ListCell<Conge> {
        private HBox hbox = new HBox();
        private Text id = new Text();
        private Text employeeName = new Text();
        private Text requestDate = new Text();
        private Text startDate = new Text();
        private Text endDate = new Text();
        private Text reason = new Text();
        private Button button = new Button("Approve");

        public Cell() {
            super();
            hbox.getChildren().addAll(id, employeeName, requestDate, startDate, endDate, reason, button);
            hbox.setSpacing(10);
        }

        @Override
        protected void updateItem(Conge conge, boolean empty) {
            super.updateItem(conge, empty);

            if (empty || conge == null) {
                setGraphic(null);
            } else {
                id.setText(String.valueOf(conge.getId()));
                employeeName.setText(conge.getEmployee().getFirstName() + " " + conge.getEmployee().getLastName());
                requestDate.setText(conge.getRequest_date().toString());
                startDate.setText(conge.getStart_date().toString());
                endDate.setText(conge.getEnd_date().toString());
                reason.setText(conge.getReason());

                setGraphic(hbox);
            }
        }
    }
}
