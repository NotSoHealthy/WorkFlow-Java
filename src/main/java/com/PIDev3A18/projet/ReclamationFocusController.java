package com.PIDev3A18.projet;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.gmail.Gmail;
import entity.Employee;
import entity.Message;
import entity.Reclamation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import services.ServiceGmail;
import services.ServiceGoogleDrive;
import services.ServiceMessage;
import services.ServiceReclamation;
import utils.UserSession;

import java.awt.*;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.PIDev3A18.projet.Main.scene;


public class ReclamationFocusController {

    @FXML
    private Label recid;
    @FXML
    private ListView listView;
    @FXML
    private Label titre;

    @FXML
    private Label description;

    @FXML
    private VBox hboxedit;
    @FXML
    private Label submiterror;

    @FXML
    private Button close;
    @FXML
    private Button uploadtodrive;
    @FXML
    private Button respo;
    @FXML
    private Label etat;  // New field for email

    @FXML
    private ComboBox<String> dropdown;

    @FXML
    private TextArea message;


    @FXML
    private Label closetext;
    @FXML
    private Label datereso;
    @FXML
    private Label filename;


    private Reclamation selectedR;

    UserSession userSession = UserSession.getInstance();
    ServiceGoogleDrive sgd = new ServiceGoogleDrive();
    ServiceGmail sg = new ServiceGmail();
    Employee loggedinEmployee = userSession.getLoggedInEmployee();

    private ServiceReclamation sr = new ServiceReclamation();
    private ServiceMessage sm = new ServiceMessage();

    public void SetReclamation(Reclamation r) {
        this.selectedR = r;
        if (titre != null) {  // Check if labels are initialized before setting text
            initialize();
        }
    }

    @FXML
    public void UpdateAdmin()
    {
        selectedR.setResponsable(this.loggedinEmployee);
        try {
            sr.update(selectedR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        initialize();
    }
    @FXML
    public void Upload() throws Exception {
       sgd.UploadToDrive(selectedR.getAttachedfile());
    }
    @FXML
    public void initialize()
    {
        if(selectedR !=null ) {
            uploadtodrive.setVisible(false);
            filename.setVisible(false);
            if(selectedR.getResponsable() != null||!"Résponsable".equals(loggedinEmployee.getRole()))
                respo.setVisible(false);

            if(selectedR.getAttachedfile() != null){
                uploadtodrive.setVisible(true);
                filename.setVisible(true);
                filename.setText( selectedR.getAttachedfile().substring(selectedR.getAttachedfile().lastIndexOf("/") + 1));
            }

            InputStream input = getClass().getResourceAsStream("icons/drive.png");
            Image image1 = new Image(input, 24, 24, true, true);
            ImageView imageView1 = new ImageView(image1);
            uploadtodrive.setGraphic(imageView1);



            if(selectedR.getDate_resolution()!=null){
                datereso.setText("résolu : "+selectedR.getDate_resolution().toString());
                closetext.setText("Fermé");
                dropdown.setVisible(false);
                close.setVisible(false);
            }

            if("Résponsable".equals(loggedinEmployee.getRole()) && selectedR.getDate_resolution()==null && selectedR.getResponsable() != null && loggedinEmployee.getId() == selectedR.getResponsable().getId())
            {
                dropdown.setVisible(true);
                close.setVisible(true);
            }
            else
            {
                dropdown.setVisible(false);
                close.setVisible(false);
            }
            if(dropdown.getItems().isEmpty()){
        dropdown.getItems().addAll("Pending", "In Progress", "On Hold","Closed","Rejected");
        dropdown.setValue(selectedR.getEtat());
                if ("Pending".equals(dropdown.getValue())) hboxedit.getStyleClass().add("orange");

                 else if ("In Progress".equals(dropdown.getValue())) hboxedit.getStyleClass().add("purple");


                else if ("On Hold".equals(dropdown.getValue())) hboxedit.getStyleClass().add("yellow");

                else if ("Closed".equals(dropdown.getValue())) hboxedit.getStyleClass().add("green");

                else hboxedit.getStyleClass().add("red");


            }






        dropdown.setOnAction(event -> {
            String selectedValue = dropdown.getValue();
            FadeTransition fade = new FadeTransition(Duration.seconds(1), hboxedit);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();


            if ("Pending".equals(selectedValue)) {
                hboxedit.getStyleClass().clear();
                hboxedit.getStyleClass().add("orange");
                selectedR.setEtat("Pending");
                etat.setText(selectedR.getEtat());

            } else if ("In Progress".equals(selectedValue)) {
                hboxedit.getStyleClass().clear();
                hboxedit.getStyleClass().add("purple");
                selectedR.setEtat("In Progress");
                etat.setText(selectedR.getEtat());

            }else if ("On Hold".equals(selectedValue)) {
                hboxedit.getStyleClass().clear();
                hboxedit.getStyleClass().add("yellow");
                selectedR.setEtat("On Hold");
                etat.setText(selectedR.getEtat());

            }
            else if ("Closed".equals(selectedValue)) {
                hboxedit.getStyleClass().clear();
                hboxedit.getStyleClass().add("green");
                selectedR.setEtat("Closed");
                etat.setText(selectedR.getEtat());

            }
            else
            {
                hboxedit.getStyleClass().clear();
                hboxedit.getStyleClass().add("red");
                selectedR.setEtat("Rejected");
                etat.setText(selectedR.getEtat());

            }
            try {
                sr.update(selectedR);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


     String padded = String.format("%04d",selectedR.getReclamation_ID());

     recid.setText(padded);
     titre.setText(selectedR.getTitle());
     description.setText(selectedR.getDescription());
     etat.setText(selectedR.getEtat());


        try {
            List<Message> mes = null;

            mes = sm.readAll();
            // Populate the ListView with job offers
            listView.getItems().setAll(mes);
           // listView.setPrefHeight(3 * listView.getFixedCellSize());
            listView.setCellFactory(param -> new javafx.scene.control.ListCell<Message>() {
                @Override
                protected void updateItem(Message me, boolean empty) {
                    super.updateItem(me, empty);
                    if (empty || me == null) {

                        setText(null);
                        setGraphic(null);
                        getStyleClass().clear();

                    }
                    else if(me != null && selectedR.getReclamation_ID() != me.getReclamation().getReclamation_ID()){
                        setText(null);
                        setGraphic(null);
                        getStyleClass().clear();}

                    else {

                        setStyle("-fx-background-color: transparent; -fx-padding: 5 10 5 10;");
                        ImageView imageView = new ImageView(new javafx.scene.image.Image(me.getUser().getImageUrl()));
                        imageView.setFitWidth(50); // Set width
                        imageView.setFitHeight(50); // Set height
                        Circle clip = new Circle(20, 20, 20); // CenterX, CenterY, Radius
                        imageView.setClip(clip);
                        Label fn = new Label(me.getUser().getFirstName());
                        Label ln = new Label(me.getUser().getLastName());
                        ln.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;-fx-padding: 10 5 0 0;");
                        fn.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;-fx-padding: 10 5 0 0;");
                        Label timedate = new Label("Créer : " + me.getDate()+ "  à "+ me.getHeure().toString().substring(0, me.getHeure().toString().length() - 3));
                        timedate.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;-fx-padding: 11 0 0 20;");
                        HBox hBox = new HBox();
                        hBox.getChildren().addAll(imageView,fn,ln,timedate);
                        Separator separator = new Separator();
                        separator.setStyle("-fx-padding: 0 0 10 0;");
                        VBox vbox = new VBox();
                        vbox.getStyleClass().add("vbox-custom");



                        Label positionLabel = new Label(me.getContenu());
                        positionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                        positionLabel.setWrapText(true);
                        InputStream input = getClass().getResourceAsStream("icons/delete.png");
                        javafx.scene.image.Image image3 = new Image(input, 16, 16, true, true);
                        ImageView imageView3 = new ImageView(image3);
                        HBox hbox2 = new HBox();

                        input = getClass().getResourceAsStream("icons/edit.png");
                        Image image2 = new Image(input, 16, 16, true, true);
                        ImageView imageView2 = new ImageView(image2);
                        Button edit = new Button();
                        edit.getStyleClass().add("edit");
                        edit.setText("Modifier");
                        edit.setGraphic(imageView2);
                        edit.setOnAction(event -> {


                        });

                        Button delete = new Button();
                        delete.getStyleClass().add("delete");
                        delete.setGraphic(imageView3);
                        delete.setOnAction(event -> {
                            try {
                                sm.delete(me);
                                List<Message> rec = sm.readAll();

                                listView.getItems().setAll(rec);

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        HBox.setMargin(delete, new javafx.geometry.Insets(10, 10, 0, 500));
                        HBox.setMargin(edit,new javafx.geometry.Insets(10, 10, 0, 180));

                        if(!loggedinEmployee.getRole().equals("Résponsable"))delete.setVisible(false);
                        hbox2.getChildren().addAll(delete);
                        vbox.getChildren().addAll(hBox,separator, positionLabel, hbox2);
                        setGraphic(vbox);
                    }
                }
            });
    }catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load job offers from the database.", Alert.AlertType.ERROR);
        }
    }
    }


    @FXML
    private void sendmessage()
    {
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);
        LocalTime localTime = LocalTime.now();
        Time sqlTime = Time.valueOf(localTime);

        if(loggedinEmployee.getId() != selectedR.getEmployee().getId() && loggedinEmployee.getId() != selectedR.getResponsable().getId())
            submiterror.setText("Vous n'avez pas accès à cette discussion !");
        else if(selectedR.getDate_resolution()!=null)
        {submiterror.setText("Reclamation Fermé !");

        }
        else if(message.getText().isEmpty())
        {
            submiterror.setText("Champ Vide !");
        }
        else
        {
            Message m = new Message(message.getText(),sqlDate,sqlTime,selectedR,loggedinEmployee);
            message.setText("");
            try {

                sm.add(m);
                initialize();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    private void closeState()
    {

     if("Closed".equals(selectedR.getEtat()))
     {

         try {
             sg.sendGmail(selectedR.getEmployee().getEmail(),"Mise à jour de votre réclamation","Madame, Monsieur "+selectedR.getEmployee().getFirstName()+" "+selectedR.getEmployee().getLastName()+"\n" +
                     "\n" +
                     "Nous avons le plaisir de vous informer que votre réclamation a été examinée et approuvée. Après une analyse approfondie, nous avons constaté que votre demande répond aux critères requis, et les mesures appropriées ont été prises pour résoudre le problème.\n" +
                     "\n" +
                     "Si vous avez des questions supplémentaires ou besoin d'une assistance complémentaire, n'hésitez pas à nous contacter. Nous accordons une grande importance à vos commentaires et nous nous engageons à garantir votre satisfaction.\n" +
                     "\n" +
                     "Nous vous remercions d'avoir porté cette question à notre attention.\nCordialement,\n" +
                     selectedR.getResponsable().getFirstName()+" "+selectedR.getResponsable().getLastName()+"\n" +

                     "Workflow\n" +
                     selectedR.getResponsable().getPhone()+"\n" +
                     "\n");
         etat.setText(selectedR.getEtat());
         LocalDate localDate = LocalDate.now();
         Date sqlDate = Date.valueOf(localDate);
         selectedR.setDate_resolution(sqlDate);

         sr.update(selectedR);


    } catch (Exception e) {
             throw new RuntimeException(e);
         }

     }
     else
     {
         try {
             sg.sendGmail(selectedR.getEmployee().getEmail(),"Mise à jour de votre réclamation","Madame, Monsieur "+selectedR.getEmployee().getFirstName()+" "+selectedR.getEmployee().getLastName()+",\n" +
                     "\n" +
                     "Après un examen approfondi de votre réclamation, nous regrettons de vous informer que votre demande a été refusée. Sur la base des informations fournies et de nos politiques internes, nous n'avons pas pu approuver votre réclamation à ce stade.\n" +
                     "\n" +
                     "Nous comprenons que cette décision peut être décevante, et nous vous encourageons à nous contacter si vous disposez d'informations ou de documents supplémentaires qui pourraient soutenir votre dossier. Notre équipe reste à votre disposition pour vous assister et vous fournir des éclaircissements concernant cette décision.\n" +
                     "\n" +
                     "Nous vous remercions de votre compréhension et vous remercions de nous avoir donné l'opportunité de traiter votre demande.\n" +
                     "\n" +
                     "Cordialement,\n" +
                     selectedR.getResponsable().getFirstName()+" "+selectedR.getResponsable().getLastName()+ "\n" +

                     "Workflow\n" +
                     selectedR.getResponsable().getPhone());
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
         selectedR.setEtat("Rejected");
         try {


             LocalDate localDate = LocalDate.now();
             Date sqlDate = Date.valueOf(localDate);

             selectedR.setDate_resolution(sqlDate);

             sr.update(selectedR);
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
         etat.setText(selectedR.getEtat());

     }
        initialize();
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
