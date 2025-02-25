module com.example.projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.compiler;
    requires okhttp3;
    requires com.google.zxing.javase;
    requires com.google.zxing;


    opens com.PIDev3A18.projet to javafx.fxml;
    exports com.PIDev3A18.projet;
    opens entity to com.google.gson, javafx.base;
}