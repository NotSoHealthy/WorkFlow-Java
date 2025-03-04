package com.PIDev3A18.projet;

import entity.Employee;
import entity.Inscription;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import services.ServiceInscription;
import utils.UserSession;


import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    private TextField SearchInscription;

    @FXML
    private HBox HBoxSearch;

    @FXML
    private VBox VboxItems;

    @FXML
    private ComboBox<String> ComboBoxStatus;

    @FXML
    private Button Refresh;

    @FXML
    private Button StatInscription;

    private List<Inscription> li;

    {
        try {
            li = si.readAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() throws SQLException, IOException {
        InputStream input = getClass().getResourceAsStream("icons/refresh.png");
        Image image = new Image(input, 25, 25, true, true);
        ImageView imageView = new ImageView(image);
        Refresh.setGraphic(imageView);
        input = getClass().getResourceAsStream("icons/statistic.png");
        image = new Image(input, 25, 25, true, true);
        imageView = new ImageView(image);
        StatInscription.setGraphic(imageView);
        ComboBoxStatus.getItems().addAll("en attente", "approuver");
        ComboBoxStatus.setValue("en attente");
        if(loggedinEmployee.getRole().equals("Résponsable")){

            for(Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                VboxItems.getChildren().add(fxmlLoader.load());
            }
        }
        else {

            li= si.SortByEmployee(loggedinEmployee.getId());
            for(Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                VboxItems.getChildren().add(fxmlLoader.load());
            }
            Hbox.getChildren().remove(TxtEmploye);
            Vbox.getChildren().remove(HBoxSearch);

        }



    }
    @FXML
    void SearchInscription(KeyEvent event) throws SQLException, IOException {
        String searchText=SearchInscription.getText().toLowerCase().trim();
        VboxItems.getChildren().clear();
        if(searchText.isEmpty())
        {
            Refresh();
        }
        else
        {
            li = si.search(searchText);
            for (Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                VboxItems.getChildren().add(fxmlLoader.load());
            }

        }
    }
    @FXML
    void Filter(ActionEvent event) throws SQLException, IOException {
        VboxItems.getChildren().clear();
        if (ComboBoxStatus.getValue().equals("en attente"))
        {
            li=si.sortStatus("en attente");
            for (Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                VboxItems.getChildren().add(fxmlLoader.load());
            }
        }
        else
        {
            li=si.sortStatus("approuver");
            for (Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                VboxItems.getChildren().add(fxmlLoader.load());
            }
        }
    }
    @FXML
    void Refresh() throws SQLException, IOException {
        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), Refresh);
        rotate.setByAngle(360);
        rotate.setInterpolator(Interpolator.EASE_BOTH);
        rotate.play();
        VboxItems.getChildren().clear();
        ComboBoxStatus.setValue("en attente");
        if(loggedinEmployee.getRole().equals("Résponsable")){
            li = si.readAll();

            for(Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                VboxItems.getChildren().add(fxmlLoader.load());
            }
        }
        else {

            li= si.SortByEmployee(loggedinEmployee.getId());
            for(Inscription i : li)
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ItemsInscription.fxml"));
                ItemsInscriptionController ItemController = new ItemsInscriptionController(i);
                fxmlLoader.setController(ItemController);
                VboxItems.getChildren().add(fxmlLoader.load());
            }
            Hbox.getChildren().remove(TxtEmploye);
            Vbox.getChildren().remove(HBoxSearch);

        }
    }
    @FXML
    void getStatView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatisticInscription.fxml"));
            Parent parent =loader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ViewFormationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
