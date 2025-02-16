package com.PIDev3A18.projet;

import entity.Employee;
import entity.Inscription;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import services.ServiceInscription;
import utils.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ViewInscriptionController {
    private ServiceInscription si=new ServiceInscription();
    private UserSession userSession;
    private Employee loggedEmployee=userSession.getLoggedInEmployee();
    @FXML
    private HBox Hbox;
    @FXML
    public void initialize() throws SQLException, IOException {
        if(Objects.equals(loggedEmployee.getType(), "responsable")){
            List<Inscription> li= si.readAll();
            for(Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                Hbox.getChildren().add(fxmlLoader.load());
            }
        }
        else {
            List<Inscription> li= si.SortByEmployee(loggedEmployee.getId());
            for(Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                Hbox.getChildren().add(fxmlLoader.load());
            }
        }



    }
}
