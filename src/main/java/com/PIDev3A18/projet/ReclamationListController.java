package com.PIDev3A18.projet;

import entity.Employee;
import entity.Reclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import services.ServiceReclamation;
import utils.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import static com.PIDev3A18.projet.Main.scene;

public class ReclamationListController {
    @FXML
    public AnchorPane anchor;
    @FXML
    public Label title;

    @FXML
    public Button addrec;
    @FXML
    private ListView<Reclamation> listView;

    private ServiceReclamation RS;

    public ReclamationListController() {
        this.RS = new ServiceReclamation();
    }
    UserSession userSession = UserSession.getInstance();
    Employee loggedinEmployee = userSession.getLoggedInEmployee();
    @FXML
    public void initialize() {
        anchor.setPrefWidth(1080);
        anchor.setPrefHeight(720);


        InputStream input = getClass().getResourceAsStream("icons/plus.png");
        Image image = new Image(input, 32, 32, true, true);
        ImageView imageView = new ImageView(image);
        addrec.setGraphic(imageView);
            // Fetch all job offers
            try {
                List<Reclamation> rec = RS.readAll();
            // Populate the ListView with job offers
            listView.getItems().setAll(rec);
            listView.setPrefHeight(3 * listView.getFixedCellSize());





                    double width = scene.getWidth();
                    double height = scene.getHeight();

                    AnchorPane.setLeftAnchor(title,(width-177)/2 - title.getWidth()/2-50);
                    AnchorPane.setTopAnchor(title,50.0);
                    AnchorPane.setLeftAnchor(addrec,(width-177)/2 - addrec.getWidth()/2);
                    AnchorPane.setBottomAnchor(addrec,50.0);



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
                            positionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
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
                            if(!loggedinEmployee.getRole().equals("Résponsable")&&loggedinEmployee.getId() != re.getEmployee().getId()) hBox2.setVisible(false);
                            vbox.getChildren().addAll(hBox,separator,nameLabel, positionLabel,hBox2);
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
            Scene scene = new Scene(loader.load());



            // Set the new scene on the current stage.
            Stage stage = (Stage) listView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

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
            Scene scene = new Scene(loader.load());

            ReclamationFormController rf = loader.getController();
            rf.SetReclamation(r);

            // Set the new scene on the current stage.
            Stage stage = (Stage) listView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the application form.", Alert.AlertType.ERROR);
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}