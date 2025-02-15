package com.PIDev3A18.projet;

import entity.Formation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ServiceFormation;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ViewFormationController implements Initializable {

    @FXML
    private TextField SearchFormation;

    @FXML
    private TableColumn<Formation, LocalDate> date_begin;

    @FXML
    private TableColumn<Formation, LocalDate> date_end;

    @FXML
    private TableColumn<Formation, String> description;

    @FXML
    private TableColumn<Formation, Integer> participants_max;

    @FXML
    private TableView<Formation> tableFormation;

    @FXML
    private TableColumn<Formation, String> title;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ServiceFormation sf=new ServiceFormation();
        try {
            ObservableList<Formation> formations= FXCollections.observableArrayList(sf.readAll());
            System.out.println(formations);
            tableFormation.setItems(formations);
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            date_begin.setCellValueFactory(new PropertyValueFactory<>("dateBegin"));
            date_end.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
            participants_max.setCellValueFactory(new PropertyValueFactory<>("participants_Max"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
