package com.PIDev3A18.projet;

import entity.Employee;
import entity.Event;
import entity.Reservation;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import services.ServiceEvent;
import services.ServiceReservation;
import utils.CalendarServiceBuilder;
import utils.ImgApi;
import utils.UserSession;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
public class EvenementsController {

    private Event ToReserveEvent;
    private Event ToUpdateEvent;
    Map<String, Integer> ReserveTypeMap = new HashMap<>();
    @FXML
    private VBox eventHolder = null;
    @FXML
    private VBox ReservationHolder = null;
    @FXML
    private Button creer;
    @FXML
    private AnchorPane AddEvent;
    @FXML
    private AnchorPane EventDisplay;
    @FXML
    private AnchorPane UpdateEvent;
    @FXML
    private AnchorPane ReservationDisplay;
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
    private AnchorPane ReserverPage;
    @FXML
    private ImageView backReserver;
    @FXML
    private TextField NomEvent;
    @FXML
    private TextField PriceReserver;
    @FXML
    private ComboBox<String> TypeListReserver;
    @FXML
    private TextField NbplaceReserver;
    @FXML
    private Label TypeListReserverError;
    @FXML
    private Label NbplaceReserverError;
    @FXML
    private TextField SearchTitle;
    @FXML
    private CheckBox SortDate;
    @FXML
    private ComboBox<String> TypeListSort;
    @FXML
    private Label empty;
    @FXML
    private ScrollPane SC;
    @FXML
    private BarChart barChart;
    @FXML
    private AnchorPane DisplayStatistics;
    @FXML
    public void initialize() {
        eventHolder.getChildren().clear();
        TypeList.setItems(FXCollections.observableArrayList("Workshop", "Commerce", "Conference" , "Webinaire" , "Networking" , "Reunion","Concert"));
        TypeListSort.setItems(FXCollections.observableArrayList("All","Workshop", "Commerce", "Conference" , "Webinaire" , "Networking" , "Reunion","Concert"));
        UserSession userSession;
        userSession = UserSession.getInstance();
        Employee loggedinEmployee = userSession.getLoggedInEmployee();
        if(loggedinEmployee.getRole().equals("Résponsable")){
            creer.setVisible(true);
        }
        else{
            creer.setVisible(false);
        }
        ServiceEvent se=new ServiceEvent();
        populateEventHolder(se.readAll());
    }
    public void populateReservations(ActionEvent event){
        EventDisplay.setVisible(false);
        ReservationDisplay.setVisible(true);
        ReservationHolder.getChildren().clear();
        UserSession userSession;
        userSession = UserSession.getInstance();
        Employee loggedinEmployee = userSession.getLoggedInEmployee();
        ServiceReservation sr=new ServiceReservation();
        for (Reservation reservation : sr.readAllById(loggedinEmployee.getId())) {
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Reservation.fxml"));
                Node node = loader.load(); // Load FXML
                ReservationController controller = loader.getController();
                controller.setTitleMyReservations(reservation.getEvent().getTitre());
                controller.setReservation(reservation);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale.FRENCH);
                String formattedDate = reservation.getEvent().getDateetheure().format(formatter);
                controller.setDateHeureMyReservation(formattedDate);
                controller.setTypeMyReservation(reservation.getEvent().getType());
                controller.setNbplacesMyReservation("Nombre de places réservées: "+reservation.getNombreDePlaces());
                Image image=new Image(reservation.getQr_url());
                controller.setQr_code(image);
                controller.setTotalMyReservation("Totale: "+reservation.getPrice() +"TND");
                controller.setController(this);
                ReservationHolder.getChildren().add(node);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void setUpdateEvent(Event e) {
        this.ToUpdateEvent = e;
    }
    public void setReserveEvent(Event e){
        this.ToReserveEvent=e;
    }
    public void layoutGoToAddEvenements(ActionEvent actionEvent) {
        AddEvent.setVisible(true);
        EventDisplay.setVisible(false);
        back.setVisible(true);
    }
    public void layoutGoToUpdateEvenement() {
        TypeListUpdate.setItems(FXCollections.observableArrayList("Workshop", "Commerce", "Conference" , "Webinaire" , "Networking" , "Reunion","Concert"));
        TitreUpdate.setText(ToUpdateEvent.getTitre());
        DescriptionUpdate.setText(ToUpdateEvent.getDescription());
        LocalDate dateOnly=ToUpdateEvent.getDateetheure().toLocalDate();
        DateUpdate.setValue(dateOnly);
        String hour = Integer.toString(ToUpdateEvent.getDateetheure().getHour());
        String minute = Integer.toString(ToUpdateEvent.getDateetheure().getMinute());
        HeureUpdate.setText(hour);
        MinuteUpdate.setText(minute);
        AdresseUpdate.setText(ToUpdateEvent.getLieu());
        TypeListUpdate.setValue(ToUpdateEvent.getType());
        NbplaceUpdate.setText(Integer.toString(ToUpdateEvent.getNombredeplace()));
        EventDisplay.setVisible(false);
        backUpdate.setVisible(true);
        UpdateEvent.setVisible(true);
    }
    public void layoutGoToReserveEvenement() {
        TypeListReserver.getItems().clear();
        ReserveTypeMap.put("VIP",90);
        ReserveTypeMap.put("Semi-VIP",50);
        ReserveTypeMap.put("Accès-Normal",40);
        TypeListReserver.getItems().addAll(ReserveTypeMap.keySet());
        NbplaceReserver.setText("");
        NomEvent.setText(ToReserveEvent.getTitre());
        ReserverPage.setVisible(true);
        EventDisplay.setVisible(false);
        backReserver.setVisible(true);
    }
    public void layoutGoBack(javafx.scene.input.MouseEvent mouseEvent) {
        AddEvent.setVisible(false);
        ReservationDisplay.setVisible(false);
        UpdateEvent.setVisible(false);
        EventDisplay.setVisible(true);
        ReserverPage.setVisible(false);
        back.setVisible(false);
        backUpdate.setVisible(false);
        backReserver.setVisible(false);
        DisplayStatistics.setVisible(false);
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
        TypeListReserver.setValue(null);
        NbplaceReserver.setText("");
        PriceReserver.setText("");
        barChart.getData().clear();
        initialize();
    }
    public void layoutGoToStatistics(javafx.scene.input.MouseEvent mouseEvent){
        ServiceEvent se = new ServiceEvent();
        Map<String, Integer> eventData=se.getStatistics();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre d'evenements par type");
        List<String> color=Arrays.asList("red","blue","green","yellow");
        int i=0;
        for (Map.Entry<String, Integer> entry : eventData.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            series.getData().add(data);
            int finalI = i;
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {// Or define a color based on the entry
                    newNode.setStyle("-fx-bar-fill: " + color.get(finalI) + ";");
                }
            });
            i++;
        }
        barChart.getData().clear();
        barChart.getData().add(series);
        EventDisplay.setVisible(false);
        DisplayStatistics.setVisible(true);
    }
    public void AddEvenements(ActionEvent event) {
        boolean test=true;
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
        if(Nbplace.getText().isBlank()||NbplaceReserver.getText().matches(".*[a-zA-Z].*")){
            NbplaceError.setText("donner le nombre de places");
            test=false;
        }
        if(test==true) {
            UserSession userSession;
            userSession = UserSession.getInstance();
            Employee loggedinEmployee = userSession.getLoggedInEmployee();
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
        if(NbplaceUpdate.getText().isBlank()||NbplaceReserver.getText().matches(".*[a-zA-Z].*")){
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
    public void CalculateType(ActionEvent event) {
        if(TypeListReserver.getValue()!=null&&!NbplaceReserver.getText().isBlank()&&!NbplaceReserver.getText().matches(".*[a-zA-Z].*")){
            double PriceU=ReserveTypeMap.get(TypeListReserver.getValue());
            int nb=Integer.parseInt(NbplaceReserver.getText());
            PriceReserver.setText(String.valueOf(PriceU*nb)+"TND");
        }
    }
    public void CalculateNP(KeyEvent event) {
        if(TypeListReserver.getValue()!=null&&!NbplaceReserver.getText().isBlank()&&!NbplaceReserver.getText().matches(".*[a-zA-Z].*")){
            double PriceU=ReserveTypeMap.get(TypeListReserver.getValue());
            int nb=Integer.parseInt(NbplaceReserver.getText());
            PriceReserver.setText(String.valueOf(PriceU*nb)+"TND");
        }
    }
    public void AddReservation(ActionEvent event){
        boolean test=true;
        TypeListReserverError.setText("");
        NbplaceReserverError.setText("");
        if(NbplaceReserver.getText().isBlank()||NbplaceReserver.getText().matches(".*[a-zA-Z].*")){
            NbplaceReserverError.setText("Nombre de place invalide");
            test=false;
        }
        if(TypeListReserver.getValue()==null){
            TypeListReserverError.setText("Selectionner un type");
            test=false;
        }
        if(test==true) {
            UserSession userSession;
            userSession = UserSession.getInstance();
            Employee loggedinEmployee = userSession.getLoggedInEmployee();
            ServiceReservation sr=new ServiceReservation();
            Reservation reservation=new Reservation(Double.parseDouble(PriceReserver.getText().substring(0, PriceReserver.getText().length() - 3)),TypeListReserver.getValue(),Integer.parseInt(NbplaceReserver.getText()),loggedinEmployee,ToReserveEvent);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale.FRENCH);
            String formattedDate = reservation.getEvent().getDateetheure().format(formatter);
            String text = "Evenement: " + reservation.getEvent().getTitre() + "\nType: " + reservation.getType() + "\nNombre de places: " + reservation.getNombreDePlaces() +"\nTotale: " + reservation.getPrice() +"TND\nDate: " + formattedDate;  // The text to encode
            File qrFile = new File("C:/Users/elite/IdeaProjects/WorkFlow-Java/src/main/resources/com/PIDev3A18/projet/images/qrcode.png");
            try {
                Writer writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 300, 300);
                BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

                // Save the QR code as an image
                ImageIO.write(qrImage, "PNG", qrFile);
                Alert uploadAlert = new Alert(Alert.AlertType.INFORMATION);
                uploadAlert.setTitle("Création d'une Réservation");
                uploadAlert.setHeaderText(null);
                uploadAlert.setContentText("Création d'une Réservation , attendez...");
                uploadAlert.show();
                String qr_url= ImgApi.uploadImage(qrFile);
                uploadAlert.close();
                reservation.setQr_url(qr_url);
                System.out.println("QR Code generated");
                qrFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            com.google.api.services.calendar.model.Event calendarEvent = new com.google.api.services.calendar.model.Event()
                    .setSummary(reservation.getEvent().getTitre())
                    .setLocation(reservation.getEvent().getLieu())
                    .setDescription(reservation.getEvent().getDescription());
            LocalDateTime endDateTime = reservation.getEvent().getDateetheure().plusHours(3);
            ZoneId zoneId = ZoneId.of("Africa/Tunis"); // Change this to the correct time zone
            DateTime googleStart = new DateTime(reservation.getEvent().getDateetheure().atZone(zoneId).toInstant().toEpochMilli());
            DateTime googleEnd = new DateTime(endDateTime.atZone(zoneId).toInstant().toEpochMilli());
            EventDateTime start = new EventDateTime()
                    .setDateTime(googleStart)
                    .setTimeZone("Africa/Tunis");
            EventDateTime end = new EventDateTime()
                    .setDateTime(googleEnd)
                    .setTimeZone("Africa/Tunis");
            calendarEvent.setStart(start);
            calendarEvent.setEnd(end);
            String calendarId = "primary";
            try {
                calendarEvent = CalendarServiceBuilder.getCalendarService().events().insert(calendarId, calendarEvent).execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.printf("Event created: %s\n", calendarEvent.getHtmlLink());
            sr.add(reservation);
            ReserverPage.setVisible(false);
            EventDisplay.setVisible(true);
            backReserver.setVisible(false);
            initialize();
            TypeListReserver.setValue(null);
            NbplaceReserver.setText("");
        }
    }
    public void SearchByTitle(KeyEvent event) {
        ServiceEvent se=new ServiceEvent();
        if(SearchTitle.getText().isBlank()){
            initialize();
        }
        else{
            List<Event> le=se.SearchByTitle(SearchTitle.getText());
            populateEventHolder(le);
        }
    }
    public void SortByDate(ActionEvent event) {
        if(SortDate.isSelected()) {
            ServiceEvent se = new ServiceEvent();
            List<Event> le = se.SortByDate();
            populateEventHolder(le);
        }
        else{
            initialize();
        }
    }
    public void SortByType(ActionEvent event){
        ServiceEvent se = new ServiceEvent();
        if(TypeListSort.getValue()!=null) {
            if (TypeListSort.getValue().equals("All")) {
                populateEventHolder(se.readAll());
            } else {
                populateEventHolder(se.SortByType(TypeListSort.getValue()));
            }
        }
    }
    public void populateEventHolder(List<Event> le){
        eventHolder.getChildren().clear();
        if(le.isEmpty()){
            SC.setVisible(false);
            empty.setVisible(true);
        }
        else {
            SC.setVisible(true);
            empty.setVisible(false);
            UserSession userSession;
            userSession = UserSession.getInstance();
            Employee loggedinEmployee = userSession.getLoggedInEmployee();
            for (Event evente : le) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Event.fxml"));
                    Node node = loader.load(); // Load FXML
                    EventController controller = loader.getController();
                    controller.setTitle(evente.getTitre());
                    controller.setDescription(evente.getDescription());
                    controller.setEvent(evente);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", Locale.FRENCH);
                    String formattedDate = evente.getDateetheure().format(formatter);
                    controller.setDateHeure(formattedDate);
                    controller.setAdresse("Adresse: " + evente.getLieu());
                    controller.setType(evente.getType());
                    controller.setNbdispo("Places disponibles: " + evente.getNombredeplace());
                    controller.setController(this);
                    if (!loggedinEmployee.getRole().equals("Résponsable")) {
                        controller.setDeleteInvisible();
                        controller.setUpInvisible();
                    }
                    eventHolder.getChildren().add(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
