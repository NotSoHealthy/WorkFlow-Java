package com.PIDev3A18.projet;

import entity.Conge;
import entity.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import services.ServiceConge;

import java.sql.SQLException;
import java.time.LocalDate;

public class CongeResponsableController {

    @FXML private TableView<Conge> tableView;
    @FXML private TableColumn<Conge, Integer> idColumn;
    @FXML private TableColumn<Conge, String> employeeColumn;
    @FXML private TableColumn<Conge, String> requestDateColumn;
    @FXML private TableColumn<Conge, String> startDateColumn;
    @FXML private TableColumn<Conge, String> endDateColumn;
    @FXML private TableColumn<Conge, String> reasonColumn;
    @FXML private TableColumn<Conge, Void> statusColumn; // Uses Void because it contains UI elements

    private ServiceConge serviceConge = new ServiceConge();

    @FXML
    public void initialize() throws SQLException {
        ObservableList<Conge> congeList = FXCollections.observableList(serviceConge.readAll());

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmployee().getFirstName() + " " +
                        cellData.getValue().getEmployee().getLastName()));
        requestDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRequest_date().toString()));
        startDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart_date().toString()));
        endDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd_date().toString()));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));

        // Setup the status column with ChoiceBox
        statusColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Conge, Void> call(TableColumn<Conge, Void> param) {
                return new TableCell<>() {
                    private final ChoiceBox<String> choiceBox = new ChoiceBox<>(
                            FXCollections.observableArrayList("Pending", "Approved", "Denied")
                    );

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || getTableRow() == null) {
                            setGraphic(null);
                        } else {
                            Conge conge = getTableView().getItems().get(getIndex());
                            choiceBox.setValue(conge.getStatus());

                            choiceBox.setOnAction(event -> {
                                String newStatus = choiceBox.getValue();
                                updateCongeStatus(conge, newStatus);
                            });

                            setGraphic(choiceBox);
                        }
                    }
                };
            }
        });

        tableView.setItems(congeList);
    }

    private void updateCongeStatus(Conge conge, String newStatus) {
//        try {
//            serviceConge.updateStatus(conge.getId(), newStatus);
            conge.setStatus(newStatus);
            tableView.refresh(); // Refresh UI after update
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}