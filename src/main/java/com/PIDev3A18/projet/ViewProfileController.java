package com.PIDev3A18.projet;

import entity.Department;
import entity.Employee;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import services.ServiceDepartment;
import services.ServiceEmployee;

import java.sql.SQLException;
import java.util.List;

public class ViewProfileController {

    @FXML private TextField adresseField;
    @FXML private ComboBox<String> gouvernoratBox;
    @FXML private ComboBox<Department> departementBox;
    @FXML private TextField emailField;
    @FXML private Text nomField;
    @FXML private TextField numField;
    @FXML private ImageView profilePicture;
    @FXML private ComboBox<String> roleBox;

    private Employee employee;
    private ServiceEmployee serviceEmployee;
    private ServiceDepartment serviceDepartment;

    public ViewProfileController(Employee employee) {
        this.employee = employee;
        serviceEmployee = new ServiceEmployee();
        serviceDepartment = new ServiceDepartment();
    }

    public void initialize() throws SQLException {
        Image image = new Image(employee.getImageUrl());
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double minSize = Math.min(imageWidth, imageHeight);
        profilePicture.setViewport(new Rectangle2D(
                (imageWidth - minSize) / 2, // Center X
                (imageHeight - minSize) / 2, // Center Y
                minSize, minSize // Crop size (square)
        ));
        profilePicture.setImage(image);
        Circle clip = new Circle(profilePicture.getFitHeight() / 2);
        clip.setCenterX(profilePicture.getFitHeight() / 2);
        clip.setCenterY(profilePicture.getFitHeight() / 2);
        profilePicture.setClip(clip);

        gouvernoratBox.setItems(FXCollections.observableArrayList("Ariana Ville", "Bab El Bhar", "Bab Souika", "Ben Arous", "Bou Mhel el-Bassatine",
                "Carthage", "Cité El Khadra", "Djebel Jelloud", "El Kabaria", "El Menzah",
                "El Mourouj", "El Omrane", "El Omrane supérieur", "El Ouardia", "Ettadhamen",
                "Ettahrir", "Ezzahra", "Ezzouhour", "Fouchana", "Hammam Chott",
                "Hammam Lif", "Hraïria", "Kalâat el-Andalous", "La Goulette", "La Marsa",
                "La Médina", "La Soukra", "Le Bardo", "Le Kram", "Medina Jedida",
                "Mégrine", "Mnihla", "Mohamedia", "Mornag", "Radès", "Raoued",
                "Séjoumi", "Sidi El Béchir", "Sidi Hassine", "Sidi Thabet"));

        roleBox.setItems(FXCollections.observableArrayList("Employé","Résponsable"));

        List<Department> departmentList = serviceDepartment.readAll();
        departementBox.setItems(FXCollections.observableList(departmentList));

        nomField.setText(employee.getFirstName()+" "+employee.getLastName());
        emailField.setText(employee.getEmail());
        numField.setText(employee.getPhone());
        adresseField.setText(employee.getAdresse());
        gouvernoratBox.getSelectionModel().select(employee.getGouvernorat());
        departementBox.getSelectionModel().select(employee.getDepartment());
        roleBox.getSelectionModel().select(employee.getRole());
    }

    public void confirmer() throws SQLException {
        employee.setRole(roleBox.getSelectionModel().getSelectedItem());
        employee.setDepartment(departementBox.getSelectionModel().getSelectedItem());
        serviceEmployee.update(employee);
        departementBox.getScene().getWindow().hide();
    }

    public void annuler(){
        nomField.setText(employee.getFirstName()+" "+employee.getLastName());
        emailField.setText(employee.getEmail());
        numField.setText(employee.getPhone());
        adresseField.setText(employee.getAdresse());
        gouvernoratBox.getSelectionModel().select(employee.getGouvernorat());
        departementBox.getSelectionModel().select(employee.getDepartment());
        roleBox.getSelectionModel().select(employee.getRole());
        departementBox.getScene().getWindow().hide();
    }

}
