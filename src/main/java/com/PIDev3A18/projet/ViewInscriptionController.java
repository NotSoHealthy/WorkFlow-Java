package com.PIDev3A18.projet;

import entity.Employee;
import entity.Inscription;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ServiceInscription;
import utils.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ViewInscriptionController {
    private ServiceInscription si=new ServiceInscription();
    UserSession userSession = UserSession.getInstance();
    Employee loggedinEmployee = userSession.getLoggedInEmployee();
    @FXML
    private VBox Vbox;
    @FXML
    public void initialize() throws SQLException, IOException {
        if(Objects.equals(loggedinEmployee.getType(), "responsable")){
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
            System.out.println(li);
            for(Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                Vbox.getChildren().add(fxmlLoader.load());
            }
        }



    }
}
