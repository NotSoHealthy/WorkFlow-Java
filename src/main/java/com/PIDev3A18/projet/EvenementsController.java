package com.PIDev3A18.projet;

import entity.Employee;
import entity.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import services.ServiceEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EvenementsController{

    @FXML
    private VBox eventHolder = null;
    @FXML
    private Button creer;

    public void populateEventHolder(Employee loggedInUser){
        if(loggedInUser.getType().equals("responsable")){
            creer.setVisible(true);
        }
        else{
            creer.setVisible(false);
        }
        ServiceEvent se=new ServiceEvent();
        int length= se.readAll().size();
        Node[] nodes= new Node[length];
        int i=0;
        for (Event event : se.readAll()) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Event.fxml"));
                Node node = loader.load(); // Load FXML
                EventController controller = loader.getController();
                controller.setTitle(event.getTitre());
                controller.setDescription(event.getDescription());
                controller.setIDEvent(String.valueOf(event.getId()));
                nodes[i]= node;
                eventHolder.getChildren().add(nodes[i]);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            i++;
        }
    }
}
