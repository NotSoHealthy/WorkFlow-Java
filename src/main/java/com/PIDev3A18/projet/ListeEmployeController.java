package com.PIDev3A18.projet;

import entity.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import services.ServiceEmployee;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListeEmployeController {

    @FXML private VBox listVbox;

    private ServiceEmployee serviceEmployee;
    private int count;

    public ListeEmployeController(){
        serviceEmployee = new ServiceEmployee();
    }

    public void initialize() throws SQLException, IOException {
        count = 2;
        showList(serviceEmployee.readAll());
    }

    public void update() throws SQLException, IOException {
        showList(serviceEmployee.readAll());
    }

    public void showList(List<Employee> employeeList) throws IOException {
        clearList();
        count = 2;
        for (Employee employee : employeeList) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("liste_employe_item.fxml"));
            ListeEmployeItemController listeEmployeItemController = new ListeEmployeItemController(employee, this);
            fxmlLoader.setController(listeEmployeItemController);
            if(employee.getStatus().equals("pending")){
                listVbox.getChildren().add(2,fxmlLoader.load());
                count++;
            }
            else {
                listVbox.getChildren().add(count+1,fxmlLoader.load());
            }
            listVbox.layout();
        }
    }

    public void clearList(){
        ObservableList<Node> list = listVbox.getChildren();
        ObservableList<Node> toRemove = FXCollections.observableArrayList();
        for (int i = 0; i < list.size(); i++) {
            if(i>1 && i!=count){
                toRemove.add(list.get(i));
            }
        }
        listVbox.getChildren().removeAll(toRemove);
    }
}
