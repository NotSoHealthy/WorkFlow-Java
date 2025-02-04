module com.example.projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.PIDev3A18.projet to javafx.fxml;
    exports com.PIDev3A18.projet;
}