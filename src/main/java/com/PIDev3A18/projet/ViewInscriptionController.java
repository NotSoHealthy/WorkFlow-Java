package com.PIDev3A18.projet;

import entity.Employee;
import entity.Inscription;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ServiceInscription;
import utils.UserSession;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class ViewInscriptionController {
    private ServiceInscription si=new ServiceInscription();
    UserSession userSession = UserSession.getInstance();
    Employee loggedinEmployee = userSession.getLoggedInEmployee();
    @FXML
    private VBox Vbox;
    @FXML
    private HBox Hbox;
    @FXML
    private Label TxtEmploye;
    @FXML
    public void initialize() throws SQLException, IOException {
        if(loggedinEmployee.getRole().equals("Résponsable")){
            List<Inscription> li= si.readAll();
            for(Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                Vbox.getChildren().add(fxmlLoader.load());
            }
        }
        else {

            List<Inscription> li= si.SortByEmployee(loggedinEmployee.getId());
            for(Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                Vbox.getChildren().add(fxmlLoader.load());
            }
            Hbox.getChildren().remove(TxtEmploye);

        }



    }
}
