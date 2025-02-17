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
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class EvenementsController {

    private Event ToUpdateEvent;
    @FXML
    private VBox eventHolder = null;
    @FXML
    private Button creer;
    @FXML
    private AnchorPane AddEvent;
    @FXML
    private AnchorPane EventDisplay;
    @FXML
    private AnchorPane UpdateEvent;
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
    private TextField TitreUpdate;
    @FXML
    private TextArea DescriptionUpdate;
    @FXML
    private DatePicker DateUpdate;
    @FXML
    private TextField HeureUpdate;
    @FXML
    private TextField MinuteUpdate;
    @FXML
    private TextField AdresseUpdate;
    @FXML
    private ComboBox<String> TypeListUpdate;
    @FXML
    private TextField NbplaceUpdate;
    @FXML
    private ImageView backUpdate;
    @FXML
    private Label TitreErrorUpdate;
    @FXML
    private Label DescriptionErrorUpdate;
    @FXML
    private Label DateErrorUpdate;
    @FXML
    private Label HeureErrorUpdate;
    @FXML
    private Label MinuteErrorUpdate;
    @FXML
    private Label AdresseErrorUpdate;
    @FXML
    private Label TypeErrorUpdate;
    @FXML
    private Label NbplaceErrorUpdate;
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale.FRENCH);
                String formattedDate = event.getDateetheure().format(formatter);
                controller.setDateHeure(formattedDate);
                controller.setAdresse("Adresse: "+event.getLieu());
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
    public void setUpdateEvent(Event e) {
        this.ToUpdateEvent = e;
    }
    public void setTitreUpdate(String titre) {
        TitreUpdate.setText(titre);
    }
    public void setDescriptionUpdate(String description) {
        DescriptionUpdate.setText(description);
    }
    public void setDateUpdate(LocalDate date) {
        DateUpdate.setValue(date);
    }
    public void setHeureUpdate(String heure) {
        HeureUpdate.setText(heure);
    }
    public void setMinuteUpdate(String minute) {
        MinuteUpdate.setText(minute);
    }
    public void setAdresseUpdate(String adresse) {
        AdresseUpdate.setText(adresse);
    }
    public void setTypeListUpdate(String type) {
        TypeListUpdate.setValue(type);
    }
    public void setNbplaceUpdate(String nbplace) {
        NbplaceUpdate.setText(nbplace);
    }
    public void layoutGoToAddEvenements(ActionEvent actionEvent) {
        AddEvent.setVisible(true);
        EventDisplay.setVisible(false);
        back.setVisible(true);
    }
    public void layoutGoToUpdateEvenement() {
        EventDisplay.setVisible(false);
        backUpdate.setVisible(true);
        UpdateEvent.setVisible(true);
    }
    public void layoutGoBack(javafx.scene.input.MouseEvent mouseEvent) {
        AddEvent.setVisible(false);
        UpdateEvent.setVisible(false);
        EventDisplay.setVisible(true);
        back.setVisible(false);
        backUpdate.setVisible(false);
        Titre.setText("");
        Description.setText("");
        Date.setValue(null);
        Heure.setText("");
        Minute.setText("");
        Adresse.setText("");
        TypeList.setValue(null);
        Nbplace.setText("");
        TitreUpdate.setText("");
        DescriptionUpdate.setText("");
        DateUpdate.setValue(null);
        HeureUpdate.setText("");
        MinuteUpdate.setText("");
        AdresseUpdate.setText("");
        TypeListUpdate.setValue(null);
        NbplaceUpdate.setText("");
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
    public void UpdateEvenements(ActionEvent event) {
        boolean test=true;
        ServiceEvent se = new ServiceEvent();
        LocalDate selectedDate=DateUpdate.getValue();
        TitreErrorUpdate.setText("");
        DescriptionErrorUpdate.setText("");
        DateErrorUpdate.setText("");
        HeureErrorUpdate.setText("");
        MinuteErrorUpdate.setText("");
        AdresseErrorUpdate.setText("");
        TypeErrorUpdate.setText("");
        NbplaceErrorUpdate.setText("");
        if(TitreUpdate.getText().isBlank()){
            TitreErrorUpdate.setText("le titre ne peut pas être vide");
            test=false;
        }
        if(DescriptionUpdate.getText().isBlank()){
            DescriptionErrorUpdate.setText("la description ne peut pas être vide");
            test=false;
        }
        if(selectedDate == null || selectedDate.isBefore(LocalDate.now())){
            DateErrorUpdate.setText("Date Invalide");
            test=false;
        }
        int hour=0;
        int minute=0;
        if(HeureUpdate.getText().isBlank()){
            HeureErrorUpdate.setText("l'Heure ne peut pas étre vide");
            test=false;
        }
        else{
            hour=Integer.parseInt(HeureUpdate.getText());
            if(hour>23||hour<0){
                HeureErrorUpdate.setText("Heure invalide");
                test=false;
            }
        }
        if(MinuteUpdate.getText().isBlank()){
            MinuteErrorUpdate.setText("la Minute ne peut pas être vide");
            test=false;
        }
        else{
            minute=Integer.parseInt(MinuteUpdate.getText());
            if(minute>59||minute<0){
                MinuteErrorUpdate.setText("Minute invalide");
            }
        }
        if(AdresseUpdate.getText().isBlank()){
            AdresseErrorUpdate.setText("l'Adresse ne peut pas être vide");
            test=false;
        }
        if(TypeListUpdate.getValue()==null){
            TypeErrorUpdate.setText("sélectionner un type");
            test=false;
        }
        if(NbplaceUpdate.getText().isBlank()){
            NbplaceErrorUpdate.setText("donner le nombre de places");
            test=false;
        }
        if(test==true) {
            LocalDateTime DateAndTime = LocalDateTime.of(selectedDate, LocalTime.of(hour, minute));
            Event e = new Event(ToUpdateEvent.getId(),TitreUpdate.getText(), DescriptionUpdate.getText(), DateAndTime, AdresseUpdate.getText(), TypeListUpdate.getValue(), Integer.parseInt(NbplaceUpdate.getText()), ToUpdateEvent.getEmployee());
            se.update(e);
            UpdateEvent.setVisible(false);
            EventDisplay.setVisible(true);
            backUpdate.setVisible(false);
            initialize();
            TitreUpdate.setText("");
            DescriptionUpdate.setText("");
            DateUpdate.setValue(null);
            HeureUpdate.setText("");
            MinuteUpdate.setText("");
            AdresseUpdate.setText("");
            TypeListUpdate.setValue(null);
            NbplaceUpdate.setText("");
        }
    }

}
