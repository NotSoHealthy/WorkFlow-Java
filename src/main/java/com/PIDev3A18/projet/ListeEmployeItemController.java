package com.PIDev3A18.projet;

import entity.Department;
import entity.Employee;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.ServiceDepartment;
import services.ServiceEmployee;
import utils.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class ListeEmployeItemController {

    @FXML private Button acceptButton;
    @FXML private HBox hBox;
    @FXML private Button denyButton;
    @FXML private ComboBox<Department> departementBox;
    @FXML private Button editButton;
    @FXML private Label emailField;
    @FXML private Label nameField;
    @FXML private ComboBox<String> roleBox;
    @FXML private HBox pendingBox;
    @FXML private HBox editBox;
    @FXML private Label roleLabel;
    @FXML private Label depLabel;

    private Employee employee;
    private ServiceEmployee serviceEmployee;
    private ServiceDepartment serviceDepartment;
    private ListeEmployeController listeEmployeController;
    private UserSession userSession;
    private Employee loggedInEmployee;

    public ListeEmployeItemController(Employee employee, ListeEmployeController listeEmployeController) {
        this.employee = employee;
        serviceEmployee = new ServiceEmployee();
        serviceDepartment = new ServiceDepartment();
        this.listeEmployeController = listeEmployeController;
        userSession = UserSession.getInstance();
        loggedInEmployee = userSession.getLoggedInEmployee();
    }

    public void initialize() throws SQLException {
        hBox.getStyleClass().add(employee.getStatus());
        departementBox.setItems(FXCollections.observableArrayList(serviceDepartment.readAll()));
        roleBox.setItems(FXCollections.observableArrayList("Employé","Résponsable"));
        nameField.setText(employee.getFirstName()+" "+employee.getLastName());
        emailField.setText(employee.getEmail());
        roleBox.getSelectionModel().select(employee.getRole());
        if (employee.getDepartment() != null) {
            departementBox.getSelectionModel().select(employee.getDepartment());
        }

        if (employee.getStatus().equals("pending")){
            editBox.setVisible(false);
            editBox.setManaged(false);
        }
        else{
            pendingBox.setVisible(false);
            pendingBox.setManaged(false);
            departementBox.setVisible(false);
            depLabel.setVisible(true);
            depLabel.setText(employee.getDepartment().getName());
            roleBox.setVisible(false);
            roleLabel.setVisible(true);
            roleLabel.setText(employee.getRole());

            if(employee.getId() == loggedInEmployee.getId()){
                editButton.setDisable(true);
            }
        }

        InputStream stream = getClass().getResourceAsStream("icons/edit.png");
        Image image = new Image(stream,16,16,true,true);
        editButton.setGraphic(new ImageView(image));
        stream = getClass().getResourceAsStream("icons/check.png");
        image = new Image(stream,16,16,true,true);
        acceptButton.setGraphic(new ImageView(image));
        stream = getClass().getResourceAsStream("icons/x.png");
        image = new Image(stream,16,16,true,true);
        denyButton.setGraphic(new ImageView(image));
    }

    @FXML
    void accept() throws SQLException, IOException {
        departementBox.getStyleClass().remove("error");
        if(departementBox.getSelectionModel().getSelectedItem() == null){
            Tooltip tooltip = new Tooltip("Selectionner um département");
            departementBox.setTooltip(tooltip);
            departementBox.getStyleClass().add("error");
            return;
        }
        employee.setStatus("approved");
        employee.setDepartment(departementBox.getSelectionModel().getSelectedItem());
        employee.setRole(roleBox.getSelectionModel().getSelectedItem());
        serviceEmployee.update(employee);
        this.listeEmployeController.update();
    }

    @FXML
    void deny() throws SQLException, IOException {
        employee.setStatus("denied");
        serviceEmployee.update(employee);
        this.listeEmployeController.update();
    }

    @FXML
    void edit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view_profile.fxml"));
        ViewProfileController viewProfileController = new ViewProfileController(employee);
        fxmlLoader.setController(viewProfileController);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Modifier Profile "+employee.getFirstName()+" "+employee.getLastName());
        stage.setWidth(800);
        stage.setHeight(720);
        stage.show();
    }

}
