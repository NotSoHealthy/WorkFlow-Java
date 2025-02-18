package com.PIDev3A18.projet;

import entity.Employee;
import entity.Reclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceReclamation;
import utils.UserSession;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;


public class ReclamationFormController {

    @FXML
    public StackPane sp;
    @FXML
    public TextArea description;
    @FXML
    public TextField titre;
    @FXML
    public Label texterror;
    @FXML
    public Label descriptionerror;
    @FXML
    private Label AttachmentLabel;

    private File Attachment;

    private Boolean Editing = false;

    private Reclamation selectedR;


    public void SetReclamation(Reclamation r) {
        this.selectedR = r;
        if (titre != null) {  // Check if labels are initialized before setting text
            initialize();
        }
    }

    @FXML
    public void initialize()
    {
        description.setPrefRowCount(100);
        description.setPrefHeight(100);
        if(selectedR != null)
        {
            Editing = true;
        }
        if(Editing)
        {
            titre.setText(selectedR.getTitle());
            description.setText(selectedR.getDescription());
        }
        System.out.println(Editing);



    }

    @FXML
    private void UploadAttachment()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        Attachment = fileChooser.showOpenDialog(null);
        if (Attachment != null) {
            AttachmentLabel.setText(Attachment.getName());
        }


    }
    @FXML
    private void Submit()
    {

        if(titre.getText().isEmpty()){
            texterror.setText("Champ vide !");
   }

         else if(description.getText().isEmpty()){
             descriptionerror.setText("Champ vide !");
    }

else {
            try {
                // Upload files to designated directories
                LocalDate localDate = LocalDate.now();
                Date sqlDate = Date.valueOf(localDate);
                LocalTime localTime = LocalTime.now();
                Time sqlTime = Time.valueOf(localTime);


                UserSession userSession = UserSession.getInstance();
                Employee loggedinEmployee = userSession.getLoggedInEmployee();
                Reclamation reclamation = new Reclamation(titre.getText(), description.getText(), sqlDate, sqlTime, "PENDING", null, null, loggedinEmployee);

                // Save the application using ApplicationService.
                ServiceReclamation sr = new ServiceReclamation();
                if(Editing)
                {

                    reclamation = new Reclamation(selectedR.getReclamation_ID(),titre.getText(), description.getText(),selectedR.getDate(),selectedR.getHeure(),selectedR.getEtat(),selectedR.getDate_resolution(),selectedR.getResponsable(),selectedR.getEmployee());
                    sr.update(reclamation);
                }else
                {
                    sr.add(reclamation);
                }


                showAlert(Alert.AlertType.INFORMATION, "Success", "Application submitted successfully!");

                // Clear form fields after submission.
                titre.clear();
                description.clear();
                texterror.setText("");
                descriptionerror.setText("");
                AttachmentLabel.setText("No file chosen");



                FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));

                Scene scene = new Scene(loader.load());
                BorderPane retrievedPane = (BorderPane) scene.getRoot();
                FXMLLoader loader2 = new FXMLLoader(getClass().getResource("reclamation.fxml"));
                retrievedPane.setCenter(loader2.load());
                Stage stage = (Stage) sp.getScene().getWindow();
                stage.setScene(scene);


                stage.show();


            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while submitting the application.");
            }
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
