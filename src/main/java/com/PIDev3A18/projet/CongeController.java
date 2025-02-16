package com.PIDev3A18.projet;

import entity.Conge;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import services.ServiceConge;
import utils.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CongeController {

    @FXML private VBox vbox;

    private ServiceConge serviceConge = new ServiceConge();
    private UserSession userSession;



    @FXML
    public void initialize() throws SQLException, IOException {
        List<Conge> congeList = serviceConge.readAll();
        if ()

        for (Conge conge : congeList) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("congeItem.fxml"));
            CongeItemController congeItemController = new CongeItemController(conge);
            fxmlLoader.setController(congeItemController);
            vbox.getChildren().add(fxmlLoader.load());
        }
    }

}