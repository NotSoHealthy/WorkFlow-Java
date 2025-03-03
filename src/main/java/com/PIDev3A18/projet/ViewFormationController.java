package com.PIDev3A18.projet;

import entity.Employee;
import entity.Formation;
import entity.Inscription;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
    private CheckBox SortTitle;

    @FXML
    private CheckBox SortId;

    @FXML
    private DatePicker DateBegin;

    @FXML
    private DatePicker DateEnd;

    @FXML
    private ComboBox<String> ComboBoxOrder;

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
    private TableColumn<Formation, String> ActionsColumn;

    @FXML
    private Button AddFormationButton;

    @FXML
    private Button RefreshButton;

    @FXML
    private Button SearchButton;

    @FXML
    private Button RegisterButton;

    @FXML
    private Button StatButton;

    @FXML
    private HBox Hbox;

    private ObservableList<Formation> formations= FXCollections.observableArrayList();
    private ServiceFormation sf=new ServiceFormation();
    private ServiceInscription si=new ServiceInscription();

    UserSession userSession = UserSession.getInstance();
    Employee loggedinEmployee = userSession.getLoggedInEmployee();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(loggedinEmployee.getRole().equals("Employé"))
        {
            Platform.runLater(() -> {
                if (loggedinEmployee == null) {
                    loggedinEmployee = UserSession.getInstance().getLoggedInEmployee();
                }
                else{
                    try {
                        si.sendSMS(loggedinEmployee);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        DateBegin.setValue(LocalDate.now());
        LocalDate startDate = DateBegin.getValue();
        LocalDate endDate = startDate.plusWeeks(1);
        DateEnd.setValue(endDate);

        ComboBoxOrder.getItems().addAll("ASC", "DESC");
        ComboBoxOrder.setValue("ASC");
        InputStream input = getClass().getResourceAsStream("icons/Add.png");
        Image image = new Image(input, 25, 25, true, true);
        ImageView imageView = new ImageView(image);
        AddFormationButton.setGraphic(imageView);

        input = getClass().getResourceAsStream("icons/statistic.png");
        image = new Image(input, 25, 25, true, true);
        imageView = new ImageView(image);
        StatButton.setGraphic(imageView);
        if(loggedinEmployee.getRole().equals("Employé") ){
            Hbox.getChildren().remove(AddFormationButton);
            Hbox.getChildren().remove(StatButton);

        }

        input = getClass().getResourceAsStream("icons/refresh.png");
        image = new Image(input, 25, 25, true, true);
        imageView = new ImageView(image);
        RefreshButton.setGraphic(imageView);



        input = getClass().getResourceAsStream("icons/search.png");
        image = new Image(input, 25, 25, true, true);
        imageView = new ImageView(image);
        SearchButton.setGraphic(imageView);

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
            ActionsColumn.setCellFactory(param -> new TableCell<Formation, String>() {
                final Button editButton = new Button("✎");
                final Button deleteButton = new Button("🗑");
                final Button registerButton = new Button("S'inscrire");

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
                        registerButton.setStyle("-fx-background-color: #39D2C0; -fx-text-fill: white; -fx-font-size: 14px;");

                        editButton.setVisible(loggedinEmployee.getRole().equals("Résponsable"));
                        deleteButton.setVisible(loggedinEmployee.getRole().equals("Résponsable"));
                        editButton.setOnMouseClicked(event -> openEditFormationWindow(formation));
                        deleteButton.setOnAction(event -> deleteFormation(formation));

                        registerButton.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                LocalDate date = LocalDate.now();
                                Inscription i = new Inscription(date, "en attente", formation, loggedinEmployee);
                                si.add(i);
                                Refresh();
                            } catch (SQLException e) {
                                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'inscription !");
                            }
                        });

                        if(loggedinEmployee.getRole().equals("Résponsable"))
                        {
                            HBox manageBtn = new HBox(editButton, deleteButton);
                            manageBtn.setAlignment(Pos.CENTER);
                            manageBtn.setSpacing(10);
                            setGraphic(manageBtn);
                            setText(null);
                        }
                        else
                        {
                            HBox manageBtn = new HBox(registerButton);
                            manageBtn.setAlignment(Pos.CENTER);
                            setGraphic(manageBtn);
                            setText(null);
                            LocalDate date = LocalDate.now();
                            LocalDate dateBegin = date_begin.getCellData(formation);
                            try {
                                registerButton.setDisable(dateBegin.isBefore(date));
                                if (si.isRegistered(formation, loggedinEmployee)) {
                                    registerButton.setDisable(true);
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }


                    }
                }
                private void openEditFormationWindow(Formation formation) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditFormation.fxml"));
                        Parent parent = loader.load();

                        EditFormationController controller = loader.getController();
                        controller.setFormationData(formation);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.initStyle(StageStyle.UTILITY);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                private void deleteFormation(Formation formation) {
                    try {
                        sf.delete(formation);
                        Refresh();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
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
    void Refresh() {

        RotateTransition rotate = new RotateTransition(Duration.seconds(0.5), RefreshButton);
        rotate.setByAngle(360);
        rotate.setInterpolator(Interpolator.EASE_BOTH);
        rotate.play();
        sf=new ServiceFormation();
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
        DateBegin.setValue(LocalDate.now());
        LocalDate startDate = DateBegin.getValue();
        LocalDate endDate = startDate.plusWeeks(1);
        DateEnd.setValue(endDate);
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
            stage.setWidth(1100);
            stage.setHeight(720);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void SearchFormation(KeyEvent event) {
        String searchText=SearchFormation.getText().toLowerCase().trim();
        if(searchText.isEmpty())
        {
            Refresh();
        }
        else
        {
            try {
                formations= FXCollections.observableArrayList(sf.search(searchText));
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

    }
    @FXML
    void SortByTitle(ActionEvent event) {
        if (SortTitle.isSelected())
        {
            SortId.setDisable(true);
            try {
                ComboBoxOrder.setDisable(true);
                formations=FXCollections.observableArrayList(sf.sortTitle(ComboBoxOrder.getValue()));
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
        else
        {
            Refresh();
            ComboBoxOrder.setValue("ASC");
            SortId.setDisable(false);
            ComboBoxOrder.setDisable(false);
        }


    }
    @FXML
    void SortById(ActionEvent event) {
        if (SortId.isSelected())
        {
            SortTitle.setDisable(true);
            try {
                ComboBoxOrder.setDisable(true);
                formations=FXCollections.observableArrayList(sf.sortId(ComboBoxOrder.getValue()));
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
        else
        {
            Refresh();
            ComboBoxOrder.setValue("ASC");
            SortTitle.setDisable(false);
            ComboBoxOrder.setDisable(false);
        }
    }
    @FXML
    void SearchByDate(ActionEvent event)
    {
        LocalDate debut = DateBegin.getValue();
        LocalDate fin = DateEnd.getValue();
        try {
            formations=FXCollections.observableArrayList(sf.searchByDate(debut,fin));
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
    @FXML
    void getStatView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatisticFormation.fxml"));
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
}
