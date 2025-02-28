package com.PIDev3A18.projet;

import entity.Conge;
import entity.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    @FXML private Label requestDateLabel;
    @FXML private Label nameLabel;
    @FXML private Label startDateLabel;
    @FXML private Label endDateLabel;
    @FXML private Label statusLabel;
    @FXML private Label reasonLabel;

    private ServiceConge serviceConge = new ServiceConge();
    private UserSession userSession;
    private Employee loggedInEmployee;
    private Image confirmImage;
    private Image cancelImage;
    private String sortBy = "";
    private String sortDirection = "";
    private ImageView sortArrow;

    public CongeController() {
        InputStream inputStream = getClass().getResourceAsStream("icons/plus.png");
        confirmImage = new Image(inputStream, 16,16,true,true);
        inputStream = getClass().getResourceAsStream("icons/x.png");
        cancelImage = new Image(inputStream, 16,16,true,true);
        inputStream = getClass().getResourceAsStream("icons/sort.png");
        sortArrow = new ImageView(new Image(inputStream, 12,12,true,true));

        userSession = UserSession.getInstance();
        loggedInEmployee = userSession.getLoggedInEmployee();
    }

    @FXML
    public void initialize() throws SQLException, IOException {
        addButton.setGraphic(new ImageView(confirmImage));
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

        List<Conge> congeList;
        if (loggedInEmployee.getRole().equals("Employé")) {
            congeList = serviceConge.readAll().stream().filter(conge -> conge.getEmployee().getId() == loggedInEmployee.getId()).collect(Collectors.toList());
        }
        else {
            congeList = serviceConge.readAll();
        }

        congeList.stream().sorted(Comparator.comparing((Conge c)->!c.getStatus().equals("Pending")).thenComparing(Conge::getRequest_date)).collect(Collectors.toList());

        sortByRequestDate();

        requestDateField.setValue(LocalDate.now());
        nameField.setText(loggedInEmployee.getFirstName() + " " + loggedInEmployee.getLastName());
    }

    public void showList(List<Conge> congeList) throws IOException {
        clearList();
        for (Conge conge : congeList) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("conge_item.fxml"));
            CongeItemController congeItemController = new CongeItemController(conge);
            fxmlLoader.setController(congeItemController);
            vbox.getChildren().add(fxmlLoader.load());
        }
    }

    public void clearList(){
        ObservableList<Node> nodeList = vbox.getChildren();
        ObservableList<Node> toRemove = FXCollections.observableArrayList();

        for (int i = 3; i < nodeList.size(); i++) {
            toRemove.add(nodeList.get(i));
        }

        vbox.getChildren().removeAll(toRemove);
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

    public void sortByName() throws SQLException, IOException {
        List<Conge> congeList;
        if (!sortBy.equals("Name")) {
            sortBy = "Name";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        nameLabel.setGraphic(sortArrow);
        if (sortDirection.equals("desc")) {
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(c -> c.getEmployee().getFirstName()+c.getEmployee().getLastName())).collect(Collectors.toList());
        }
        else{
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(c -> c.getEmployee().getFirstName()+c.getEmployee().getLastName())).collect(Collectors.toList());
            congeList = congeList.reversed();
        }
        showList(congeList);
    }

    public void sortByRequestDate() throws SQLException, IOException {
        List<Conge> congeList;
        if (!sortBy.equals("RequestDate")) {
            sortBy = "RequestDate";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        requestDateLabel.setGraphic(sortArrow);
        if (sortDirection.equals("asc")) {
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getRequest_date)).collect(Collectors.toList());
        }
        else{
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getRequest_date).reversed()).collect(Collectors.toList());
        }
        showList(congeList);
    }

    public void sortByStartDate() throws SQLException, IOException {
        List<Conge> congeList;
        if (!sortBy.equals("StartDate")) {
            sortBy = "StartDate";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        startDateLabel.setGraphic(sortArrow);
        if (sortDirection.equals("asc")) {
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getStart_date)).collect(Collectors.toList());
        }
        else{
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getStart_date).reversed()).collect(Collectors.toList());
        }
        showList(congeList);
    }

    public void sortByEndDate() throws SQLException, IOException {
        List<Conge> congeList;
        if (!sortBy.equals("EndDate")) {
            sortBy = "EndDate";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        endDateLabel.setGraphic(sortArrow);
        if (sortDirection.equals("asc")) {
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getEnd_date)).collect(Collectors.toList());
        }
        else{
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getEnd_date).reversed()).collect(Collectors.toList());
        }
        showList(congeList);
    }

    public void sortByStatus() throws SQLException, IOException {
        List<Conge> congeList;
        if (!sortBy.equals("Status")) {
            sortBy = "Status";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        statusLabel.setGraphic(sortArrow);
        if (sortDirection.equals("desc")) {
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getStatus)).collect(Collectors.toList());
        }
        else{
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getStatus).reversed()).collect(Collectors.toList());
        }
        showList(congeList);
    }

    public void sortByReason() throws SQLException, IOException {
        List<Conge> congeList;
        if (!sortBy.equals("Reason")) {
            sortBy = "Reason";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        reasonLabel.setGraphic(sortArrow);
        if (sortDirection.equals("desc")) {
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getReason)).collect(Collectors.toList());
        }
        else{
            congeList = serviceConge.readAll().stream().sorted(Comparator.comparing(Conge::getReason).reversed()).collect(Collectors.toList());
        }
        showList(congeList);
    }

    public void clearSortArrow(){
        nameLabel.setGraphic(null);
        requestDateLabel.setGraphic(null);
        endDateLabel.setGraphic(null);
        startDateLabel.setGraphic(null);
        statusLabel.setGraphic(null);
        reasonLabel.setGraphic(null);
    }
}