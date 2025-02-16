package com.PIDev3A18.projet;

import entity.Conge;
import entity.Employee;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import services.ServiceConge;
import services.ServiceEmployee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CongeResponsableController {
    @FXML
    private ListView<List<Node>> listView;

    private Text id = new Text();
    private Text employeeName = new Text();
    private Text requestDate = new Text();
    private Text startDate = new Text();
    private Text endDate = new Text();
    private Text reason = new Text();

    @FXML
    public void initialize() throws SQLException {
        List<Node> listItems = new ArrayList<>();
        ServiceConge serviceConge = new ServiceConge();
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        List<Conge> congeList = serviceConge.readAll();
        System.out.println(congeList);
        for (Conge conge : congeList) {
            System.out.println(conge);
            id.setText(""+conge.getId());
            employeeName.setText(conge.getEmployee().getFirstName() + " " + conge.getEmployee().getLastName());
            requestDate.setText(conge.getRequest_date().toString());
            startDate.setText(conge.getStart_date().toString());
            endDate.setText(conge.getEnd_date().toString());
            reason.setText(conge.getReason());

            listView.getItems().add(listItems);
        }

    }
}
