package com.PIDev3A18.projet;

import entity.Employee;
import entity.Reclamation;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import services.ServiceReclamation;
import utils.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.PIDev3A18.projet.Main.scene;

public class ReclamationListController {
    @FXML
    public AnchorPane anchor;
    @FXML
    public Label title;
    @FXML
    public Button addrec;
    @FXML
    public ListView<Reclamation> listView;
    @FXML
    private ComboBox<String> statefilter;
    @FXML
    public Button submitsearch;
    @FXML
    public Button ASC;
    @FXML
    public Button DESC;
    @FXML
    public Button pieButton;
    @FXML
    public DatePicker datefilter;
    @FXML
    public TextField searchfilter;


    private ServiceReclamation RS;
    public ReclamationListController() {
        this.RS = new ServiceReclamation();
    }
    UserSession userSession = UserSession.getInstance();
    Employee loggedinEmployee = userSession.getLoggedInEmployee();


    public void SendToStats()
    {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("statsreclamation.fxml"));
            Parent parent =loader.load();
            Scene scene = new Scene(parent);

            parent.setOpacity(0);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), parent);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);



            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("icons/Logo.png")));
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
            fadeIn.play();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the application form.", Alert.AlertType.ERROR);
        }

    }
    @FXML
    public void initialize() {


        InputStream input = getClass().getResourceAsStream("icons/ASC.png");
        Image image1 = new Image(input, 24, 24, true, true);
        ImageView imageView1 = new ImageView(image1);
        ASC.setGraphic(imageView1);

        input = getClass().getResourceAsStream("icons/DESC.png");
        Image image2 = new Image(input, 24, 24, true, true);
        ImageView imageView2 = new ImageView(image2);
        DESC.setGraphic(imageView2);

        input = getClass().getResourceAsStream("icons/stats.png");
        Image image3 = new Image(input, 32, 32, true, true);
        ImageView imageView3 = new ImageView(image3);
        pieButton.setGraphic(imageView3);





        if(statefilter.getItems().isEmpty()) {
            statefilter.getItems().addAll("Pending", "In Progress", "On Hold", "Closed", "Rejected");
        }
        statefilter.setValue("Sélectionnez l'état");


            try {
                listView.getItems().setAll(RS.readAll());
            listView.setPrefHeight(3 * listView.getFixedCellSize());



                // Custom cell factory to display job offer details with formatting
                listView.setCellFactory(param -> new javafx.scene.control.ListCell<Reclamation>() {
                    @Override
                    protected void updateItem(Reclamation re, boolean empty) {
                        super.updateItem(re, empty);
                        if (empty || re == null) {
                            setText(null);
                            setGraphic(null);
                            getStyleClass().clear();
                        } else {
                            setStyle("-fx-background-color: transparent; -fx-padding: 5 10 5 10;");
                            // Create a layout for each list item (job offer)
                            ImageView imageView = new ImageView(new Image(re.getEmployee().getImageUrl()));

                            imageView.setFitWidth(50); // Set width
                            imageView.setFitHeight(50); // Set height

                            Circle clip = new Circle(20, 20, 20);

                            imageView.setClip(clip);

                            Label fn = new Label(re.getEmployee().getFirstName());
                            Label ln = new Label(re.getEmployee().getLastName());
                            ln.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;-fx-padding: 10 5 0 0;");
                            fn.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;-fx-padding: 10 5 0 0;");
                            Label timedate = new Label("Créer : " + re.getDate()+ "  à "+ re.getHeure().toString().substring(0, re.getHeure().toString().length() - 3));
                            timedate.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;-fx-padding: 11 0 0 20;");
                            HBox hBox = new HBox();
                            hBox.getChildren().addAll(imageView,fn,ln,timedate);
                            Separator separator = new Separator();
                            separator.setStyle("-fx-padding: 0 0 10 0;");
                            VBox vbox = new VBox();
                            vbox.getStyleClass().add("vbox-custom");

                            Label nameLabel = new Label( "Problème :  " + re.getTitle());
                            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                            Label positionLabel = new Label("Description : "+ re.getDescription());
                            positionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                            Label CategoryLabel = new Label( "Category :  " + re.getCategory());
                            CategoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                            Label TypeLabel = new Label( "Type :  " + re.getType());
                            TypeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                            Label StateLabel = new Label( "Etat :  " + re.getEtat());

                            StateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                            Label RespoLabel = new Label("");
                            if(re.getResponsable()!=null){
                            RespoLabel = new Label( "Traité par :  " + re.getResponsable().getFirstName() +" "+re.getResponsable().getLastName());
                            RespoLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                            }
                            positionLabel.setWrapText(true);

                            HBox hBox2 = new HBox();

                            InputStream input = getClass().getResourceAsStream("icons/edit.png");
                            Image image2 = new Image(input, 16, 16, true, true);
                            ImageView imageView2 = new ImageView(image2);



                            Button edit = new Button();
                            edit.getStyleClass().add("edit");
                            edit.setText("Modifier");
                            edit.setGraphic(imageView2);
                            edit.setOnAction(event -> {
                                // Call your function here
                                SendToEdit(re);
                            });
                            input = getClass().getResourceAsStream("icons/delete.png");
                            Image image3 = new Image(input, 16, 16, true, true);
                            ImageView imageView3 = new ImageView(image3);


                            Button delete = new Button();
                            delete.getStyleClass().add("delete");

                            delete.setGraphic(imageView3);
                            delete.setOnAction(event -> {

                                try {
                                    RS.delete(re);
                                    List<Reclamation> rec = RS.readAll();
                                    listView.getItems().setAll(rec);


                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            HBox.setMargin(delete, new Insets(10, 10, 0, 10));
                            HBox.setMargin(edit, new Insets(10, 0, 0,580));

                            hBox2.getChildren().addAll(edit,delete);
                            hBox2.setVisible(false);
                            if(loggedinEmployee.getRole().equals("Résponsable")||loggedinEmployee.getId() == re.getEmployee().getId()) hBox2.setVisible(true);
                            vbox.getChildren().addAll(hBox,separator,nameLabel, positionLabel,CategoryLabel,TypeLabel,StateLabel,hBox2);

                            vbox.getChildren().add(RespoLabel);

                            setGraphic(vbox);
                        }
                    }
                });


            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, re) -> {
                if (re != null) {
                    SendToFocus(re);
                }
            });






        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load job offers from the database.", Alert.AlertType.ERROR);
        }
    }

   private void SendToFocus(Reclamation selected) {
        try {
            Scene scene = title.getScene();
            BorderPane retrievedPane = (BorderPane) scene.getRoot();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("focusreclamation.fxml"));
            retrievedPane.setCenter(loader.load());

            ReclamationFocusController rf = loader.getController();
            rf.SetReclamation(selected);





        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the application form.", Alert.AlertType.ERROR);
        }
    }

   @FXML
    private void SendToForm()
    {

        try {
            // Load the form.fxml file using an absolute resource path.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("form.fxml"));
            Parent parent =loader.load();
            Scene scene = new Scene(parent);

            // Set initial opacity to 0 (fully transparent)
            parent.setOpacity(0);

            // Create a fade-in animation
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), parent);
            fadeIn.setFromValue(0); // Start fully transparent
            fadeIn.setToValue(1);   // End fully opaque
            ReclamationFormController rf = loader.getController();
            rf.getScene(title.getScene());
            // Show the stage and play the fade-in animation



            // Set the new scene on the current stage.
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("icons/Logo.png")));
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
            fadeIn.play();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the application form.", Alert.AlertType.ERROR);
        }

    }
    @FXML
    private void SendToEdit(Reclamation r)
    {
        try {


            FXMLLoader loader = new FXMLLoader(getClass().getResource("form.fxml"));
            Parent parent =loader.load();
            Scene scene = new Scene(parent);

            parent.setOpacity(0);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), parent);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);


            ReclamationFormController rf = loader.getController();
            rf.SetReclamation(r);
            rf.getScene(title.getScene());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("icons/Logo.png")));
            stage.initStyle(StageStyle.DECORATED);
            stage.show();
            fadeIn.play();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the application form.", Alert.AlertType.ERROR);
        }
    }
   @FXML
    public void SortASC()
    {

        try {
        listView.getItems().setAll( RS.sortTitre(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void SortDESC()
    {

        try {
            listView.getItems().setAll( RS.sortTitre(0));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void SubmitFilters()
    {
        try {
        List<Reclamation> union = RS.readAll();
        List<Reclamation> search = new ArrayList<>();
        List<Reclamation> state = new ArrayList<>();
        List<Reclamation> date = new ArrayList<>();

        if(!searchfilter.getText().isEmpty())
        {
         
            search = RS.search(searchfilter.getText());



        }
        if(!statefilter.getValue().equals("Sélectionnez l'état"))
        {
            state = RS.searchByState(statefilter.getValue());

        }

        if(datefilter.getValue() != null)
        {
            date = RS.searchByDate( Date.valueOf(datefilter.getValue()));

        }

            if(!search.isEmpty())
             union = EqualId(union,search);

            if(!state.isEmpty())
                union = EqualId(union,state);
            if(!date.isEmpty())
                union = EqualId(union,state);


        listView.getItems().setAll(union);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    private List<Reclamation> EqualId(List<Reclamation>l1,List<Reclamation>l2)
    {
        List<Reclamation> x = new ArrayList<>();
        for (Reclamation rec1 : l1) {
            // Compare with each element in list l2
            for (Reclamation rec2 : l2) {

                if (rec1.getReclamation_ID() == rec2.getReclamation_ID()) {

                    x.add(rec1);
                    break;
                }
            }
        }
        return x;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}