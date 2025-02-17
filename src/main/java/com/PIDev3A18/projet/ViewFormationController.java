package com.PIDev3A18.projet;

import entity.Employee;
import entity.Formation;
import entity.Inscription;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import services.ServiceFormation;
import services.ServiceInscription;
import utils.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewFormationController implements Initializable {

    @FXML
    private TextField SearchFormation;

    @FXML
    private TableColumn<Formation, LocalDate> date_begin;

    @FXML
    private TableColumn<Formation, LocalDate> date_end;

    @FXML
    private TableColumn<Formation, String> description;

    @FXML
    private TableColumn<Formation, Integer> participants_max;

    @FXML
    private TableView<Formation> tableFormation;

    @FXML
    private TableColumn<Formation, String> title;

    @FXML
    private Button AddFormationButton;

    @FXML
    private Button RefreshButton;

    @FXML
    private Button RegisterButton;
    @FXML
    private HBox Hbox;

    ObservableList<Formation> formations= FXCollections.observableArrayList();
    UserSession userSession = UserSession.getInstance();
    Employee loggedinEmployee = userSession.getLoggedInEmployee();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ServiceFormation sf=new ServiceFormation();
        ServiceInscription si=new ServiceInscription();
        InputStream input = getClass().getResourceAsStream("icons/Add.png");
        Image image = new Image(input, 25, 25, true, true);
        ImageView imageView = new ImageView(image);
        AddFormationButton.setGraphic(imageView);
        if(loggedinEmployee.getType().equals("employee") ){
            Hbox.getChildren().remove(AddFormationButton);

        }
        input = getClass().getResourceAsStream("icons/refresh.png");
        image = new Image(input, 25, 25, true, true);
        imageView = new ImageView(image);
        RefreshButton.setGraphic(imageView);

        input = getClass().getResourceAsStream("icons/registration.png");
        image = new Image(input, 25, 25, true, true);
        imageView = new ImageView(image);
        RegisterButton.setGraphic(imageView);
        try {
            formations= FXCollections.observableArrayList(sf.readAll());
            tableFormation.setItems(formations);
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            date_begin.setCellValueFactory(new PropertyValueFactory<>("dateBegin"));
            date_end.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
            participants_max.setCellValueFactory(new PropertyValueFactory<>("participants_Max"));
            Callback<TableColumn<Formation, String>, TableCell<Formation, String>> cellFactory = (TableColumn<Formation, String> param) -> new TableCell<>() {
                final Button editButton = new Button("✎");
                final Button deleteButton = new Button("🗑");

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Formation formation = getTableView().getItems().get(getIndex());


                        editButton.setStyle("-fx-background-color: #00E676; -fx-text-fill: white; -fx-font-size: 14px;");
                        deleteButton.setStyle("-fx-background-color: #ff1744; -fx-text-fill: white; -fx-font-size: 14px;");
                        if(Objects.equals(loggedinEmployee.getType(), "responsable"))
                        {
                            editButton.setDisable(false);
                            deleteButton.setDisable(false);
                        }
                        else {
                            editButton.setDisable(true);
                            deleteButton.setDisable(true);
                        }
                        editButton.setOnMouseClicked((MouseEvent event) -> {
                            Formation selectedFormation = tableFormation.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("EditFormation.fxml"));
                            Parent parent = null;
                            try {
                                parent = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            EditFormationController controller = loader.getController();
                            controller.setFormationData(selectedFormation);

                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                        });


                        deleteButton.setOnAction((ActionEvent event) -> {
                            try {
                                if(sf.readById(formation.getFormation_ID())!=null)
                                {
                                    sf.delete(formation);
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "Formation supprimer avec succés!");
                                }
                                else  {
                                    showAlert(Alert.AlertType.ERROR, "Error", "Réinitialiser la page !");
                                }

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });


                        HBox manageBtn = new HBox(editButton, deleteButton);
                        manageBtn.setSpacing(10);
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };

            TableColumn<Formation, String> actionCol = new TableColumn<>("Actions");
            actionCol.setCellFactory(cellFactory);
            tableFormation.getColumns().add(actionCol);
            actionCol.setVisible(loggedinEmployee.getType().equals("responsable"));
            Callback<TableColumn<Formation, String>, TableCell<Formation, String>> inscriptionCellFactory = (TableColumn<Formation, String> param) -> new TableCell<>() {
                final Button registerButton = new Button("S'inscrire");

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Formation formation = getTableView().getItems().get(getIndex());

                        registerButton.setStyle("-fx-background-color: #39D2C0; -fx-text-fill: white; -fx-font-size: 14px;");
                        try {
                            registerButton.setDisable(si.isRegistered(formation, loggedinEmployee));

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        registerButton.setOnMouseClicked((MouseEvent event)-> {
                            try {
                                LocalDate date = LocalDate.now();
                                Inscription i= new Inscription(date,"en attente",formation,loggedinEmployee);
                                if(!si.isRegistered(formation,loggedinEmployee))
                                {
                                    si.add(i);
                                    showAlert(Alert.AlertType.INFORMATION, "Success", "Vous êtes maintenant inscrit, en attente de validation!");
                                }
                                else {
                                    showAlert(Alert.AlertType.ERROR, "Error", "Réinitialiser la page !");
                                }

                            } catch (SQLException e) {
                                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while registering!");
                            }
                        });

                        HBox manageBtn = new HBox(registerButton);
                        manageBtn.setStyle("-fx-alignment: center;");
                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };


            TableColumn<Formation, String> inscription = new TableColumn<>("S'inscrire");
            inscription.setCellFactory(inscriptionCellFactory);
            tableFormation.getColumns().add(inscription);
            inscription.setVisible(loggedinEmployee.getType().equals("employee"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void getAddView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddFormation.fxml"));
            Parent parent =loader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ViewFormationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    void Refresh(ActionEvent event) {
        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), RefreshButton);
        rotate.setByAngle(360);
        rotate.setInterpolator(Interpolator.EASE_BOTH);
        rotate.play();
        ServiceFormation sf=new ServiceFormation();
        formations.clear();
        try {
            formations= FXCollections.observableArrayList(sf.readAll());
            tableFormation.setItems(formations);
            title.setCellValueFactory(new PropertyValueFactory<>("title"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            date_begin.setCellValueFactory(new PropertyValueFactory<>("dateBegin"));
            date_end.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
            participants_max.setCellValueFactory(new PropertyValueFactory<>("participants_Max"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void Register(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewInscription.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
