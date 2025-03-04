package com.PIDev3A18.projet;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import services.ServiceInscription;

import java.sql.SQLException;
import java.util.Map;

public class StatisticInscriptionController {

    @FXML
    private PieChart ChartInscription;
    private ServiceInscription si=new ServiceInscription();

    @FXML
    public void initialize() {
        Map<String, Integer> formationData = null;
        try {
            formationData = si.statisticInscription();
            for (Map.Entry<String, Integer> entry : formationData.entrySet()) {
                PieChart.Data slice = new PieChart.Data("Title : " + entry.getKey()+" ("+entry.getValue()+")", entry.getValue());
                ChartInscription.getData().add(slice);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
