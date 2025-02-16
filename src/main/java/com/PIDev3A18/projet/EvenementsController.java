package com.PIDev3A18.projet;

import entity.Employee;
import entity.Event;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import services.ServiceEvent;
import utils.UserSession;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class EvenementsController {

    @FXML
    private VBox eventHolder = null;
    @FXML
    private Button creer;
    @FXML
    private AnchorPane AddEvent;
    @FXML
    private AnchorPane EventDisplay;
    @FXML
    private TextField Titre;
    @FXML
    private TextArea Description;
    @FXML
    private DatePicker Date;
    @FXML
    private TextField Heure;
    @FXML
    private TextField Minute;
    @FXML
    private TextField Adresse;
    @FXML
    private ComboBox<String> TypeList;
    @FXML
    private TextField Nbplace;
    @FXML
    private ImageView back;
    @FXML
    private Label TitreError;
    @FXML
    private Label DescriptionError;
    @FXML
    private Label DateError;
    @FXML
    private Label HeureError;
    @FXML
    private Label MinuteError;
    @FXML
    private Label AdresseError;
    @FXML
    private Label TypeError;
    @FXML
    private Label NbplaceError;
    @FXML
    public void initialize() {
        eventHolder.getChildren().clear();
        TypeList.setItems(FXCollections.observableArrayList("Workshop", "Commerce", "Conference" , "Webinaire" , "Networking" , "Reunion"));
        UserSession userSession;
        userSession = UserSession.getInstance();
        Employee loggedinEmployee = userSession.getLoggedInEmployee();
        if(loggedinEmployee.getType().equals("responsable")){
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
                controller.setEvent(event);
                controller.setController(this);
                if(!loggedinEmployee.getType().equals("responsable")){
                    controller.setDeleteInvisible();
                }
                nodes[i]= node;
                eventHolder.getChildren().add(nodes[i]);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            i++;
        }
    }
    public void layoutGoToAddEvenements(ActionEvent actionEvent) {
        AddEvent.setVisible(true);
        EventDisplay.setVisible(false);
        back.setVisible(true);
    }
    public void layoutGoBack(javafx.scene.input.MouseEvent mouseEvent) {
        AddEvent.setVisible(false);
        EventDisplay.setVisible(true);
        back.setVisible(false);
        Titre.setText("");
        Description.setText("");
        Date.setValue(null);
        Heure.setText("");
        Minute.setText("");
        Adresse.setText("");
        TypeList.setValue(null);
        Nbplace.setText("");
    }
    public void AddEvenements(ActionEvent event) {
        boolean test=true;
        UserSession userSession;
        userSession = UserSession.getInstance();
        Employee loggedinEmployee = userSession.getLoggedInEmployee();
        ServiceEvent se = new ServiceEvent();
        LocalDate selectedDate=Date.getValue();
        TitreError.setText("");
        DescriptionError.setText("");
        DateError.setText("");
        HeureError.setText("");
        MinuteError.setText("");
        AdresseError.setText("");
        TypeError.setText("");
        NbplaceError.setText("");
        if(Titre.getText().isBlank()){
            TitreError.setText("le titre ne peut pas être vide");
            test=false;
        }
        if(Description.getText().isBlank()){
            DescriptionError.setText("la description ne peut pas être vide");
            test=false;
        }
        if(selectedDate == null || selectedDate.isBefore(LocalDate.now())){
            DateError.setText("Date Invalide");
            test=false;
        }
        int hour=0;
        int minute=0;
        if(Heure.getText().isBlank()){
            HeureError.setText("l'Heure ne peut pas étre vide");
            test=false;
        }
        else{
            hour=Integer.parseInt(Heure.getText());
            if(hour>23||hour<0){
                HeureError.setText("Heure invalide");
                test=false;
            }
        }
        if(Minute.getText().isBlank()){
            MinuteError.setText("la Minute ne peut pas être vide");
            test=false;
        }
        else{
            minute=Integer.parseInt(Minute.getText());
            if(minute>59||minute<0){
                MinuteError.setText("Minute invalide");
            }
        }
        if(Adresse.getText().isBlank()){
            AdresseError.setText("l'Adresse ne peut pas être vide");
            test=false;
        }
        if(TypeList.getValue()==null){
            TypeError.setText("sélectionner un type");
            test=false;
        }
        if(Nbplace.getText().isBlank()){
            NbplaceError.setText("donner le nombre de places");
            test=false;
        }
        if(test==true) {
            LocalDateTime DateAndTime = LocalDateTime.of(selectedDate, LocalTime.of(hour, minute));
            Event e = new Event(Titre.getText(), Description.getText(), DateAndTime, Adresse.getText(), TypeList.getValue(), Integer.parseInt(Nbplace.getText()), loggedinEmployee);
            se.add(e);
            AddEvent.setVisible(false);
            EventDisplay.setVisible(true);
            back.setVisible(false);
            initialize();
            Titre.setText("");
            Description.setText("");
            Date.setValue(null);
            Heure.setText("");
            Minute.setText("");
            Adresse.setText("");
            TypeList.setValue(null);
            Nbplace.setText("");
        }
    }

}
