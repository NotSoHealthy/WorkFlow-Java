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
import java.time.Instant;
import java.time.ZoneId;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Comparator;

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

    // New controls for search, sort, and filter:
    @FXML
    private TextField searchTitleTextField;
    @FXML
    private ComboBox<String> salarySortComboBox;
    @FXML
    private ComboBox<String> contractFilterComboBox;

    private final JobOfferService jobOfferService = new JobOfferService();

    private JobOffer selectedJobOffer; // Store the selected job offer

    // Store all job offers for filtering and sorting
    private List<JobOffer> allJobOffers;

    @FXML
    void initialize() {
        SubmitBtn.setDisable(true);
        setupTable();
        loadJobOffers();

        // Set up listeners for dynamic search, sort, and filter
        searchTitleTextField.textProperty().addListener((obs, oldVal, newVal) -> updateFilteredList());
        salarySortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateFilteredList());
        contractFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateFilteredList());

        // Set up sort options for salary
        salarySortComboBox.getItems().clear();
        salarySortComboBox.getItems().addAll("Ascending", "Descending");
        salarySortComboBox.setValue("Ascending");

        // Populate contract filter options based on available contract types
        updateContractFilterOptions();

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
            allJobOffers = jobOfferService.readAll();
            ObservableList<JobOffer> observableList = FXCollections.observableArrayList(allJobOffers);
            ShowJobOffer.setItems(observableList);
            // Update contract filter options in case new types are present
            updateContractFilterOptions();
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
            // Force a refresh of the table so the updated data is visible immediately
            ShowJobOffer.refresh();
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
        if(jobOffer.getExpirationDate() != null) {
            ExpirationDate.setValue(
                    Instant.ofEpochMilli(jobOffer.getExpirationDate().getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
            );
        }
    }

    private void clearFields() {
        title.clear();
        Description.clear();
        ContractType.clear();
        Salary.clear();
        ExpirationDate.setValue(null);
    }

    /**
     * Updates the contract filter ComboBox with unique contract types from the list of job offers.
     */
    private void updateContractFilterOptions() {
        Set<String> contractTypes = new HashSet<>();
        if(allJobOffers != null) {
            for (JobOffer offer : allJobOffers) {
                contractTypes.add(offer.getContractType());
            }
        }
        ObservableList<String> contractOptions = FXCollections.observableArrayList();
        contractOptions.add("All");
        contractOptions.addAll(contractTypes);
        contractFilterComboBox.setItems(contractOptions);
        contractFilterComboBox.setValue("All");
    }

    /**
     * Filters job offers based on title search, sorts by salary, and filters by contract type.
     */
    private void updateFilteredList() {
        String searchText = searchTitleTextField.getText() != null ? searchTitleTextField.getText().toLowerCase().trim() : "";
        String sortOption = salarySortComboBox.getValue();
        String contractFilter = contractFilterComboBox.getValue();
        if (contractFilter == null) {
            contractFilter = "All";
        }

        String finalContractFilter = contractFilter;
        List<JobOffer> filtered = allJobOffers.stream()
                .filter(offer -> searchText.isEmpty() || offer.getTitle().toLowerCase().contains(searchText))
                .filter(offer -> finalContractFilter.equals("All") || offer.getContractType().equalsIgnoreCase(finalContractFilter))
                .collect(Collectors.toList());

        // Sort by salary
        if (sortOption != null) {
            if (sortOption.equals("Ascending")) {
                filtered.sort(Comparator.comparing(JobOffer::getSalary));
            } else if (sortOption.equals("Descending")) {
                filtered.sort(Comparator.comparing(JobOffer::getSalary).reversed());
            }
        }

        ShowJobOffer.setItems(FXCollections.observableArrayList(filtered));
    }
}
