package com.PIDev3A18.projet;

import entity.Conge;
import entity.Employee;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import services.ServiceConge;
import utils.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CongeController {

    @FXML private VBox vbox;
    @FXML private HBox addBox;
    @FXML private DatePicker endDateField;
    @FXML private TextField nameField;
    @FXML private TextField raisonField;
    @FXML private DatePicker requestDateField;
    @FXML private DatePicker startDateField;
    @FXML private Button addButton;
    @FXML private Button cancelButton;

    private ServiceConge serviceConge = new ServiceConge();
    private UserSession userSession;
    private Employee loggedInEmployee;
    private List<Conge> congeList;
    private ValidationSupport validationSupport = new ValidationSupport();
    private Image confirmImage;
    private Image cancelImage;

    @FXML
    public void initialize() throws SQLException, IOException {
        InputStream inputStream = getClass().getResourceAsStream("icons/plus.png");
        confirmImage = new Image(inputStream, 16,16,true,true);
        addButton.setGraphic(new ImageView(confirmImage));
        inputStream = getClass().getResourceAsStream("icons/x.png");
        cancelImage = new Image(inputStream, 16,16,true,true);
        cancelButton.setGraphic(new ImageView(cancelImage));

        ObservableList<Node> nodeList = vbox.getChildren();
        List<Node> nodesToRemove = new ArrayList<>();

        for (Node node : nodeList) {
            if (node instanceof HBox hbox) {
                if (hbox.getId() != null && hbox.getId().equals("congeItem")) {
                    nodesToRemove.add(hbox);
                }
            }
        }
        vbox.getChildren().removeAll(nodesToRemove);

        addBox.setManaged(false);
        addBox.setVisible(false);

        userSession = UserSession.getInstance();
        loggedInEmployee = userSession.getLoggedInEmployee();
        if (loggedInEmployee.getRole().equals("employee")) {
            congeList = serviceConge.readAll().stream().filter(conge -> conge.getEmployee().getId() == loggedInEmployee.getId()).collect(Collectors.toList());
        }
        else {
            congeList = serviceConge.readAll();
        }

        congeList.stream().sorted(Comparator.comparing((Conge c)->!c.getStatus().equals("Pending")).thenComparing(Conge::getRequest_date)).collect(Collectors.toList());

        for (Conge conge : congeList) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("conge_item.fxml"));
            CongeItemController congeItemController = new CongeItemController(conge);
            fxmlLoader.setController(congeItemController);
            vbox.getChildren().add(fxmlLoader.load());
        }

        requestDateField.setValue(LocalDate.now());
        nameField.setText(loggedInEmployee.getFirstName() + " " + loggedInEmployee.getLastName());
    }


    @FXML
    void addConge(ActionEvent event) {
        addBox.setVisible(!addBox.isVisible());
        addBox.setManaged(!addBox.isManaged());
    }

    @FXML
    void cancelConge(ActionEvent event) {
        addBox.setVisible(false);
        addBox.setManaged(false);
        nameField.clear();
        raisonField.clear();
        endDateField.setValue(null);
        startDateField.setValue(null);
        startDateField.getStyleClass().remove("error");
        endDateField.getStyleClass().remove("error");
        raisonField.getStyleClass().remove("error");
    }

    @FXML
    void confirmConge(ActionEvent event) throws SQLException, IOException {
        Boolean verif=true;
        startDateField.getStyleClass().remove("error");
        endDateField.getStyleClass().remove("error");
        raisonField.getStyleClass().remove("error");
        if(startDateField.getValue() == null || startDateField.getValue().isBefore(LocalDate.now())) {
            startDateField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("La date doit etre au minimum aujourd'hui");
            startDateField.setTooltip(tooltip);
            verif=false;
        }
        if(startDateField.getValue()==null || endDateField.getValue() == null || endDateField.getValue().isBefore(startDateField.getValue())) {
            endDateField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("La date doit etre apres la date de debut");
            endDateField.setTooltip(tooltip);
            verif=false;
        }
        if(raisonField.getText().isBlank()){
            raisonField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Raison invalide");
            raisonField.setTooltip(tooltip);
            verif=false;
        }

        if (verif) {
            Conge conge = new Conge(loggedInEmployee,LocalDate.now(),startDateField.getValue(),endDateField.getValue(),raisonField.getText(),"pending");
            serviceConge.add(conge);
            this.initialize();
        }
    }

    public void changeDebut(ActionEvent event) {
        startDateField.getStyleClass().remove("error");
        if(startDateField.getValue() == null || startDateField.getValue().isBefore(LocalDate.now())) {
            startDateField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("La date doit etre au minimum aujourd'hui");
            startDateField.setTooltip(tooltip);
        }
    }

    public void changeFin(ActionEvent event) {
        endDateField.getStyleClass().remove("error");
        if(startDateField.getValue()==null || endDateField.getValue() == null || endDateField.getValue().isBefore(startDateField.getValue())) {
            endDateField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("La date doit etre apres la date de debut");
            endDateField.setTooltip(tooltip);
        }
    }

    public void changeRaison() {
        raisonField.getStyleClass().remove("error");
        if(raisonField.getText().isBlank()){
            raisonField.getStyleClass().add("error");
            Tooltip tooltip = new Tooltip("Raison invalide");
            raisonField.setTooltip(tooltip);
        }
    }
}