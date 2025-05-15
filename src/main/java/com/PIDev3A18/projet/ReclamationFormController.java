package com.PIDev3A18.projet;

import entity.Employee;
import entity.Reclamation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceReclamation;
import utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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

    @FXML
    private ComboBox<String> categoryC;
    @FXML
    private ComboBox<String> typeC;

    private File Attachment;

    private Boolean Editing = false;

    private Reclamation selectedR;

    private Scene PreviousScene;

    public void SetReclamation(Reclamation r) {
        this.selectedR = r;
        if (titre != null) {
            initialize();
        }
    }
    public void getScene(Scene s)
    {
        PreviousScene = s;
        if (titre != null) {
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

        if(categoryC.getItems().isEmpty()){
            categoryC.getItems().addAll("Technical", "Facturation", "Service","Autre");
            categoryC.setValue("Selectionnez category");}
        if(Editing) categoryC.setValue(selectedR.getCategory());
        if(typeC.getItems().isEmpty()){
            typeC.getItems().addAll("Plainte", "Demande", "Feedback");
            typeC.setValue("Selectionnez type");
        }
        if(Editing) typeC.setValue(selectedR.getType());


    }

    @FXML
    private void UploadAttachment()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        Attachment = fileChooser.showOpenDialog(null);
        if (Attachment != null) {

            AttachmentLabel.setText(Attachment.getName());
        }


    }
    private void CopyAttachment(String destinationPath) {
        if (Attachment == null) {
            System.out.println("No file selected.");
            return;
        }

        File destinationFile = new File(destinationPath + File.separator + Attachment.getName());

        try {
            Files.copy(Attachment.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully to: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error copying file: " + e.getMessage());
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
    } else if ("Selectionnez category".equals(categoryC.getValue()) || "Selectionnez type".equals(typeC.getValue()) ) {
            showAlert(Alert.AlertType.ERROR, "Erreur !", "Veuillez vous assurer que toutes les zones de liste déroulante sont sélectionnées.");
            
        } else {
            try {

                // Upload files to designated directories
                LocalDate localDate = LocalDate.now();
                Date sqlDate = Date.valueOf(localDate);
                LocalTime localTime = LocalTime.now();
                Time sqlTime = Time.valueOf(localTime);


                UserSession userSession = UserSession.getInstance();
                Employee loggedinEmployee = userSession.getLoggedInEmployee();
                String url = null;
                if(Attachment != null)url ="C:\\Users\\Med Amin\\Documents\\PIDev\\WorkFlow-Java\\reclamation\\"+Attachment.getName();
                Reclamation reclamation = new Reclamation(titre.getText(), description.getText(),categoryC.getValue(),typeC.getValue(),url, sqlDate, sqlTime, "PENDING", null, null, loggedinEmployee);

                // Save the application using ApplicationService.
                ServiceReclamation sr = new ServiceReclamation();
                if(Editing)
                {
                    CopyAttachment("C:\\Users\\Med Amin\\Documents\\PIDev\\WorkFlow-Java\\reclamation");
                    reclamation = new Reclamation(selectedR.getReclamation_ID(),titre.getText(), description.getText(),categoryC.getValue(),typeC.getValue(),url,selectedR.getDate(),selectedR.getHeure(),selectedR.getEtat(),selectedR.getDate_resolution(),selectedR.getResponsable(),selectedR.getEmployee());
                    sr.update(reclamation);
                }else
                {
                    CopyAttachment("C:\\Users\\Med Amin\\Documents\\PIDev\\WorkFlow-Java\\reclamation");
                    sr.add(reclamation);
                }


                if(Editing)showAlert(Alert.AlertType.INFORMATION, "Success", "La Reclamation a été modifiée avec succès !");
               else showAlert(Alert.AlertType.INFORMATION, "Success", "Reclamation soumise avec succès !");









                // Clear form fields after submission.
                titre.clear();
                description.clear();
                texterror.setText("");
                descriptionerror.setText("");
                AttachmentLabel.setText("No file chosen");


                Stage stage = (Stage) sp.getScene().getWindow();
                stage.close();



                BorderPane retrievedPane = (BorderPane) PreviousScene.getRoot();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("reclamation.fxml"));
                retrievedPane.setCenter(loader.load());












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
