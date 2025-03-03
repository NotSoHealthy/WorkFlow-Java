package com.PIDev3A18.projet;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import services.ServiceFormation;

import java.sql.SQLException;
import java.util.Map;

public class StatisticFormationController {

    @FXML
    private PieChart ChartFormation;
    private ServiceFormation sf = new ServiceFormation();

    @FXML
    public void initialize() {
        Map<Integer, Integer> formationData = null;
        try {
            formationData = sf.statisticFormation();
            for (Map.Entry<Integer, Integer> entry : formationData.entrySet()) {
                PieChart.Data slice = new PieChart.Data("Year " + entry.getKey()+" ("+entry.getValue()+")", entry.getValue());
                ChartFormation.getData().add(slice);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}
