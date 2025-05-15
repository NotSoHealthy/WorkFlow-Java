package com.PIDev3A18.projet;

import entity.Department;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import services.ServiceDepartment;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class DepartmentStatsController {

    @FXML private Label statsTitle;
    @FXML private BarChart<String, Number> budgetChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;

    private Department department; // For single department stats
    private List<Department> allDepartments; // For all departments stats

    public void setDepartment(Department dept) {
        this.department = dept;
        this.allDepartments = null; // Clear all departments when setting a single department
        if (dept != null) {
            statsTitle.setText("Budget Statistics for " + dept.getName() + " (TND)");
            populateChart();
        }
    }

    public void setAllDepartments(List<Department> departments) {
        this.allDepartments = departments;
        this.department = null; // Clear single department when showing all
        if (departments != null && !departments.isEmpty()) {
            statsTitle.setText("Budget Statistics for All Departments (TND)");
            populateChart();
        }
    }

    private void populateChart() {
        // Clear existing data
        budgetChart.getData().clear();

        try {
            ServiceDepartment service = new ServiceDepartment();
            ObservableList<Department> departmentsToShow;

            // Determine which departments to show
            if (allDepartments != null) {
                departmentsToShow = FXCollections.observableArrayList(allDepartments);
            } else if (department != null) {
                departmentsToShow = FXCollections.observableArrayList(department);
            } else {
                departmentsToShow = FXCollections.observableArrayList(service.readAll()); // Default to all departments
            }

            // Create series for the chart
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Budget (TND)");

            for (Department dept : departmentsToShow) {
                series.getData().add(new XYChart.Data<>(dept.getName(), dept.getYear_Budget()));
            }

            // Add series to chart
            budgetChart.getData().add(series);

            // Apply flat 2D styling with CSS (no rotation or 3D effects)
            apply2DEffect();

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error (e.g., show an alert or log)
        }
    }

    private void apply2DEffect() {
        // Apply flat 2D styling via CSS (no rotation, no 3D effects, ensure alignment)
        budgetChart.setStyle(
                "-fx-bar-fill: linear-gradient(to bottom, #FF5722, #E64A19); " + // Orange gradient for bars (matching your image)
                        "-fx-background-color: transparent; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 5, 0, 0, 2); " + // Subtle shadow for flat 2D look
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 14px;" +
                        "-fx-alignment: center;" // Ensure alignment is centered and flat
        );

        // Explicitly clear any transforms to ensure no rotation or tilt
        budgetChart.getTransforms().clear();

        // Add CSS stylesheet with error handling
        try {
            String cssPath = "/css/Department.css"; // Absolute path from resources root
            URL cssUrl = getClass().getResource(cssPath);
            if (cssUrl == null) {
                System.err.println("CSS file not found at: " + cssPath);
                return; // Skip adding stylesheet if not found
            }
            budgetChart.getStylesheets().add(cssUrl.toExternalForm());
        } catch (Exception e) {
            System.err.println("Error loading CSS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}