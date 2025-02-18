package com.PIDev3A18.projet;

import com.google.gson.Gson;
import entity.Employee;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LayoutController {
    Employee loggedinEmployee;

    @FXML
    private Text layoutName;
    @FXML
    private Button layoutDashButton;
    @FXML
    private Button layoutDepartmentButton;
    @FXML
    private Button layoutProjectsButton;
    @FXML
    private Button layoutTasksButton;
    @FXML
    private Button layoutCalendarButton;
    @FXML
    private Button layoutMoneyButton;
    @FXML
    private Button layoutLeaveButton;
    @FXML
    private Button layoutDisconnectButton;
    @FXML
    private ImageView layoutProfilePicture;
    @FXML
    private BorderPane layoutBorderPane;
    @FXML
    private VBox layoutVbox;

    @FXML
    void initialize() {
        //Dashboard Icon
        InputStream input = getClass().getResourceAsStream("icons/dash.png");
        Image image = new Image(input, 16, 16, true, true);
        ImageView imageView = new ImageView(image);
        layoutDashButton.setGraphic(imageView);
        //Department Icon
        input = getClass().getResourceAsStream("icons/dash.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutDepartmentButton.setGraphic(imageView);
        //Projects Icon
        input = getClass().getResourceAsStream("icons/projects.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutProjectsButton.setGraphic(imageView);
        //Task Icon
        input = getClass().getResourceAsStream("icons/tasks.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutTasksButton.setGraphic(imageView);
        //Calendar Icon
        input = getClass().getResourceAsStream("icons/calendar.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutCalendarButton.setGraphic(imageView);
        //Money Icon
        input = getClass().getResourceAsStream("icons/money.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutMoneyButton.setGraphic(imageView);
        //Conge Icon
        input = getClass().getResourceAsStream("icons/conge.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutLeaveButton.setGraphic(imageView);
        //Logout Icon
        input = getClass().getResourceAsStream("icons/logout.png");
        image = new Image(input, 16, 16, true, true);
        imageView = new ImageView(image);
        layoutDisconnectButton.setGraphic(imageView);
    }

    public void layoutGoToDashboard(ActionEvent actionEvent) {
        setSelected(layoutDashButton);
        loadFXML(getClass().getResource("dashboard.fxml"));
    }
    public void layoutGoToDepartment(ActionEvent actionEvent) {
        setSelected(layoutDepartmentButton);
        loadFXML(getClass().getResource("ViewDepartment.fxml"));
    }

    public void layoutGoToProjects(ActionEvent actionEvent) {
        setSelected(layoutProjectsButton);
        loadFXML(getClass().getResource("ViewProject.fxml"));
    }

    public void layoutGoToTasks(ActionEvent actionEvent) {
        setSelected(layoutTasksButton);
        loadFXML(getClass().getResource("tasks.fxml"));
    }

    public void layoutGoToCalendar(ActionEvent actionEvent) {
        setSelected(layoutCalendarButton);
        loadFXML(getClass().getResource("calendar.fxml"));
    }

    public void layoutGoToMoney(ActionEvent actionEvent) {
        setSelected(layoutMoneyButton);
        loadFXML(getClass().getResource("money.fxml"));
    }

    public void layoutGoToConge(ActionEvent actionEvent) {
        setSelected(layoutLeaveButton);
        System.out.println(loggedinEmployee.getType());
        if (loggedinEmployee.getType().equals("responsable")) {
            loadFXML(getClass().getResource("conge_responsable.fxml"));
        }
        else {
            loadFXML(getClass().getResource("conge.fxml"));
        }
    }

    public void layoutDisconnect(ActionEvent actionEvent) throws IOException {
        Gson gson = new Gson();
        FileWriter writer = new FileWriter("saved-employee.json");
        gson.toJson(null,writer);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        layoutCalendarButton.getScene().setRoot(fxmlLoader.load());
    }

    public void setLoggedinEmployee(Employee loggedinEmployee) {
        this.loggedinEmployee = loggedinEmployee;
        layoutName.setText(loggedinEmployee.getFirstName() + " " + loggedinEmployee.getLastName());

        Image image = new Image(loggedinEmployee.getImageUrl());
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        double minSize = Math.min(imageWidth, imageHeight);
        layoutProfilePicture.setViewport(new Rectangle2D(
                (imageWidth - minSize) / 2, // Center X
                (imageHeight - minSize) / 2, // Center Y
                minSize, minSize // Crop size (square)
        ));
        layoutProfilePicture.setImage(image);
        Circle clip = new Circle(layoutProfilePicture.getFitHeight() / 2);
        clip.setCenterX(layoutProfilePicture.getFitHeight() / 2);
        clip.setCenterY(layoutProfilePicture.getFitHeight() / 2);
        layoutProfilePicture.setClip(clip);
    }

    public Employee getLoggedinEmployee() {
        return loggedinEmployee;
    }

    private void loadFXML(URL url) {
        try {
            FXMLLoader loader = new FXMLLoader(url);
            layoutBorderPane.setCenter(loader.load());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelected(Button selectedButton){
       ObservableList<Node> list = layoutVbox.getChildren();
       for (Node node : list) {
           if (node instanceof Button button) {
               if (button.getText().equals(selectedButton.getText())) {
                   button.getStyleClass().add("layout-button-selected");
               }
               else {
                   button.getStyleClass().remove("layout-button-selected");
               }
           }
       }
    }
}
