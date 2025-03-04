package com.PIDev3A18.projet;

import entity.Reclamation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import services.ServiceReclamation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReclamationGraphController {


    @FXML
    public PieChart pie;
    @FXML
    public Label titre;
    @FXML
    public Button suiv;
    @FXML
    public PieChart typepie;
    @FXML
    public PieChart categorypie;
    public ServiceReclamation RS = new ServiceReclamation();

    public Scene PreviousScene;
    public int pienum = 0;
    int cooldownDuration = 1;

    public void ChangePie()
    {



        suiv.setDisable(true);

        // Create a timeline for the cooldown
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    cooldownDuration--;
                    if (cooldownDuration > 0) {
                        // Update the button text with the remaining time

                    } else {
                        // Cooldown is over, re-enable the button and reset the text
                        suiv.setDisable(false);

                        cooldownDuration = 1; // Reset cooldown duration
                    }
                })
        );
        timeline.setCycleCount(cooldownDuration); // Run for the cooldown duration
        timeline.play();






      pienum++;
      if(pienum == 3)pienum=0;

      FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), pie);

        typepie.setOpacity(0);
        categorypie.setOpacity(0);
        titre.setText("Les Etats :");
      if(pienum == 1){
          titre.setText("Les Categories :");
          fadeIn = new FadeTransition(Duration.seconds(1), categorypie);
          pie.setOpacity(0);
          categorypie.setOpacity(0);}
      if(pienum == 2) {
          titre.setText("Les Types :");
          fadeIn = new FadeTransition(Duration.seconds(1), typepie);
          pie.setOpacity(0);
          typepie.setOpacity(0);
      }

      fadeIn.setFromValue(0);
      fadeIn.setToValue(1);

      fadeIn.play();

    }
    @FXML
    public void initialize() {
        List<Reclamation> reclamations = null;
        try {
            reclamations = RS.readAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Map<String, Integer> etatCount = new HashMap<>();
        Map<String, Integer> typeCount = new HashMap<>();
        Map<String, Integer> categoryCount = new HashMap<>();

        for (Reclamation rec : reclamations) {
            String etat = rec.getEtat();
            String type = rec.getType();
            String category = rec.getCategory();
            etatCount.put(etat, etatCount.getOrDefault(etat, 0) + 1);
            typeCount.put(type, typeCount.getOrDefault(type, 0) + 1);
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
        }

        titre.setText("Les Etats :");
        typepie.setOpacity(0);
        categorypie.setOpacity(0);




        String[] pastelColors = {"#FFB6C1", "#FFD700", "#87CEFA", "#98FB98", "#FFA07A"};
        String[] darkColors = {"#2C3E50", "#34495E", "#7F8C8D", "#95A5A6", "#BDC3C7"};

        for (Map.Entry<String, Integer> entry : etatCount.entrySet()) {
            pie.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));

        }
        for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
            typepie.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            categorypie.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }


        int i = 0;

        for (PieChart.Data data : typepie.getData()) {
            // Set pie slice color
            data.getNode().setStyle("-fx-pie-color: " + pastelColors[i % pastelColors.length] + ";");


            i++;
        }
        i=0;
        for (PieChart.Data data : categorypie.getData()) {
            data.getNode().setStyle("-fx-pie-color: " + darkColors[i % darkColors.length] + ";");

            i++;
        }

        Platform.runLater(() -> {
            int j = 0;
            for (PieChart.Data data : typepie.getData()) {
                Node legendItem = typepie.lookup(".chart-legend-item-symbol.data" + j);
                if (legendItem != null) {
                    legendItem.setStyle("-fx-background-color: " + pastelColors[j % pastelColors.length] + ";");
                }
                j++;
            }
            j = 0;
            for (PieChart.Data data : categorypie.getData()) {
                Node legendItem = categorypie.lookup(".chart-legend-item-symbol.data" + j);
                if (legendItem != null) {
                    legendItem.setStyle("-fx-background-color: " + darkColors[j % darkColors.length] + ";");
                }
                j++;
            }
        });
        // Create PieCh


    }
    }
