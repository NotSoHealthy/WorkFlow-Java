package com.PIDev3A18.projet;

import entity.Conge;
import entity.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import services.ServiceEmployee;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListeEmployeController {

    @FXML private VBox listVbox;
    @FXML private Label depLabel;
    @FXML private Label emailLabel;
    @FXML private Label etatLabel;;
    @FXML private Label nameLabel;
    @FXML private Label roleLabel;
    @FXML private TextField searchBar;

    private ServiceEmployee serviceEmployee;
    private int count = 3;
    private int startDelete = 3;
    private String sortBy = "";
    private String sortDirection = "";
    private ImageView sortArrow;

    public ListeEmployeController(){
        serviceEmployee = new ServiceEmployee();
        InputStream inputStream = getClass().getResourceAsStream("icons/sort.png");
        sortArrow = new ImageView(new Image(inputStream, 12,12,true,true));
    }

    public void initialize() throws Exception {
        sortByEtat();
    }

    public void update() throws Exception {
        sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        switch (sortBy){
            case "Name":
                sortByName();
                break;
            case "Email":
                sortByEmail();
                break;
            case "Dep":
                sortByDep();
                break;
            case "Role":
                sortByRole();
                break;
            case "Etat":
                sortByEtat();
                break;
        }

    }

    public void showList(List<Employee> employeeList) throws IOException {
        clearList();
        count = startDelete;
        if (!searchBar.getText().isBlank()){
            employeeList = employeeList.stream().filter(e -> (e.getFirstName()+" "+e.getLastName()).toLowerCase().contains(searchBar.getText().toLowerCase())).collect(Collectors.toList());
        }
        for (Employee employee : employeeList) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("liste_employe_item.fxml"));
            ListeEmployeItemController listeEmployeItemController = new ListeEmployeItemController(employee, this);
            fxmlLoader.setController(listeEmployeItemController);
            if(employee.getStatus().equals("pending")){
                listVbox.getChildren().add(startDelete,fxmlLoader.load());
                count++;
            }
            else {
                listVbox.getChildren().add(count+1,fxmlLoader.load());
            }
            listVbox.layout();
        }
    }

    public void clearList(){
        ObservableList<Node> list = listVbox.getChildren();
        ObservableList<Node> toRemove = FXCollections.observableArrayList();
        for (int i = 0; i < list.size(); i++) {
            if(i>=startDelete && i!=count){
                toRemove.add(list.get(i));
            }
        }
        listVbox.getChildren().removeAll(toRemove);
    }

    public void clearSortArrow(){
        nameLabel.setGraphic(null);
        emailLabel.setGraphic(null);
        depLabel.setGraphic(null);
        roleLabel.setGraphic(null);
        etatLabel.setGraphic(null);
    }

    public void sortByName() throws Exception {
        List<Employee> employeeList;
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
        employeeList = serviceEmployee.readAll().stream().sorted(Comparator.comparing(e -> e.getFirstName()+e.getLastName())).collect(Collectors.toList());
        if (sortDirection.equals("asc")) {
            employeeList = employeeList.reversed();
        }
        showList(employeeList);
    }

    public void sortByEmail() throws Exception {
        List<Employee> employeeList;
        if (!sortBy.equals("Email")) {
            sortBy = "Email";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        emailLabel.setGraphic(sortArrow);
        employeeList = serviceEmployee.readAll().stream().sorted(Comparator.comparing(Employee::getEmail)).collect(Collectors.toList());
        if (sortDirection.equals("asc")) {
            employeeList = employeeList.reversed();
        }
        showList(employeeList);
    }

    public void sortByDep() throws Exception {
        List<Employee> employeeList;
        if (!sortBy.equals("Dep")) {
            sortBy = "Dep";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        depLabel.setGraphic(sortArrow);
        employeeList = serviceEmployee.readAll().stream().sorted(Comparator.comparing(e -> e.getDepartment().getName())).collect(Collectors.toList());
        if (sortDirection.equals("asc")) {
            employeeList = employeeList.reversed();
        }
        showList(employeeList);
    }

    public void sortByRole() throws Exception {
        List<Employee> employeeList;
        if (!sortBy.equals("Role")) {
            sortBy = "Role";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        roleLabel.setGraphic(sortArrow);
        employeeList = serviceEmployee.readAll().stream().sorted(Comparator.comparing(Employee::getRole)).collect(Collectors.toList());
        if (sortDirection.equals("asc")) {
            employeeList = employeeList.reversed();
        }
        showList(employeeList);
    }

    public void sortByEtat() throws Exception {
        List<Employee> employeeList;
        if (!sortBy.equals("Etat")) {
            sortBy = "Etat";
            sortDirection = "desc";
        }
        else{
            sortDirection = sortDirection.equals("desc") ? "asc" : "desc";
        }
        sortArrow.setRotate(sortDirection.equals("desc") ? 0 : 180);
        clearSortArrow();
        etatLabel.setGraphic(sortArrow);
        employeeList = serviceEmployee.readAll().stream().sorted(Comparator.comparing(Employee::getStatus)).collect(Collectors.toList());
        if (sortDirection.equals("asc")) {
            employeeList = employeeList.reversed();
        }
        showList(employeeList);
    }
}
