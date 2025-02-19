package com.PIDev3A18.projet;

import entity.Employee;
import entity.JobOffer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.JobOfferService;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.SQLException;
import java.util.List;

public class JobOfferController {
    private Employee loggedinEmployee;

    @FXML
    private Button DeleteBtn, SubmitBtn, UpdtateBtn;

    @FXML
    private TableView<JobOffer> ShowJobOffer;

    @FXML
    private TableColumn<JobOffer, Integer> colJobID;

    @FXML
    private TableColumn<JobOffer, String> colTitle, colDescription, colContractType, colEmployee;

    @FXML
    private TableColumn<JobOffer, Double> colSalary;

    @FXML
    private TextField ContractType, Description, Salary, title;

    @FXML
    private DatePicker ExpirationDate;

    private final JobOfferService jobOfferService = new JobOfferService();

    private JobOffer selectedJobOffer; // Store the selected job offer

    @FXML
    void initialize() {
        SubmitBtn.setDisable(true);
        setupTable();
        loadJobOffers();

        // Handle row selection in the table
        ShowJobOffer.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithSelectedJobOffer(newSelection);
                SubmitBtn.setVisible(false);
            }
        });
    }

    @FXML
    void SubmitBtn(ActionEvent event) {
        if (loggedinEmployee == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No logged-in employee set.");
            return;
        }

        try {
            String jobTitle = title.getText().trim();
            String description = Description.getText().trim();
            String contractType = ContractType.getText().trim();
            String salaryText = Salary.getText().trim();
            LocalDate expirationLocalDate = ExpirationDate.getValue();

            if (!isValidText(jobTitle) || !isValidText(description) || !isValidText(contractType)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Title, Description, and Contract Type should only contain letters and spaces.");
                return;
            }

            double salary;
            try {
                salary = Double.parseDouble(salaryText);
                if (salary <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Salary", "Salary must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Salary", "Salary must be a numeric value.");
                return;
            }

            Date publicationDate = new Date(System.currentTimeMillis());
            Date expirationDate = (expirationLocalDate != null) ? Date.valueOf(expirationLocalDate) : null;

            JobOffer jobOffer = new JobOffer(jobTitle, description, publicationDate, expirationDate, contractType, salary, loggedinEmployee);

            jobOfferService.add(jobOffer);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Job offer added successfully!");

            clearFields();
            loadJobOffers();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the job offer.");
        }
    }

    public void setLoggedinEmployee(Employee loggedinEmployee) {
        this.loggedinEmployee = loggedinEmployee;
        SubmitBtn.setDisable(false);
        System.out.println("Logged-in Employee set: " + loggedinEmployee.getId());
    }

    private boolean isValidText(String text) {
        return text.matches("^[a-zA-Z ]+$");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setupTable() {
        colJobID.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getJobId()).asObject());
        colTitle.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        colDescription.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        colContractType.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getContractType()));
        colSalary.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getSalary()));
        colEmployee.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmployeeId().getLastName()));
    }

    private void loadJobOffers() {
        try {
            List<JobOffer> jobOffers = jobOfferService.readAll();
            ObservableList<JobOffer> observableList = FXCollections.observableArrayList(jobOffers);
            ShowJobOffer.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load job offers.");
        }
    }

    @FXML
    void DeleteBtn(ActionEvent event) {
        JobOffer selectedOffer = ShowJobOffer.getSelectionModel().getSelectedItem();
        if (selectedOffer == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No job offer selected.");
            return;
        }

        try {
            jobOfferService.delete(selectedOffer);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Job offer deleted successfully!");
            loadJobOffers();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the job offer.");
        }
    }

    @FXML
    
    void UpdtateBtn(ActionEvent event) {
        if (selectedJobOffer == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No job offer selected for update.");
            return;
        }

        try {
            String jobTitle = title.getText().trim();
            String description = Description.getText().trim();
            String contractType = ContractType.getText().trim();
            String salaryText = Salary.getText().trim();
            LocalDate expirationLocalDate = ExpirationDate.getValue();

            // Input Validation
            if (!isValidText(jobTitle)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Title should only contain letters and spaces.");
                return;
            }
            if (!isValidText(description)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Description should only contain letters and spaces.");
                return;
            }
            if (!isValidText(contractType)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Contract Type should only contain letters and spaces.");
                return;
            }

            double salary;
            try {
                salary = Double.parseDouble(salaryText);
                if (salary <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Salary", "Salary must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Salary", "Salary must be a numeric value.");
                return;
            }

            // Check expiration date
            if (expirationLocalDate == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid Date", "Please select an expiration date.");
                return;
            }
            if (expirationLocalDate.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Invalid Date", "Expiration date must be in the future.");
                return;
            }

            selectedJobOffer.setTitle(jobTitle);
            selectedJobOffer.setDescription(description);
            selectedJobOffer.setContractType(contractType);
            selectedJobOffer.setSalary(salary);
            selectedJobOffer.setExpirationDate(Date.valueOf(expirationLocalDate));

            jobOfferService.update(selectedJobOffer);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Job offer updated successfully!");

            clearFields();
            loadJobOffers();
            SubmitBtn.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the job offer.");
        }
    }

    private void fillFormWithSelectedJobOffer(JobOffer jobOffer) {
        selectedJobOffer = jobOffer;

        title.setText(jobOffer.getTitle());
        Description.setText(jobOffer.getDescription());
        ContractType.setText(jobOffer.getContractType());
        Salary.setText(String.valueOf(jobOffer.getSalary()));


    }

    private void clearFields() {
        title.clear();
        Description.clear();
        ContractType.clear();
        Salary.clear();
        ExpirationDate.setValue(null);
    }
}
