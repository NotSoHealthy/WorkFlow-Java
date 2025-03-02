module com.example.projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.compiler;
    requires okhttp3;
    requires jdk.httpserver;
    requires com.google.zxing.javase;
    requires com.google.zxing;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client.json.jackson2;
    requires com.google.api.client;
    requires com.google.api.services.calendar;
    requires java.mail;


    opens com.PIDev3A18.projet to javafx.fxml;
    exports com.PIDev3A18.projet;
    opens entity to com.google.gson, javafx.base;
}