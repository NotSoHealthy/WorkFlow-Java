module com.example.projet {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.controlsfx.controls;
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
    requires com.google.api.client.json.gson;
    requires com.google.api.services.gmail;
    requires twilio;
    requires org.apache.commons.codec;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires annotations;
    requires org.apache.commons.lang3;
    requires two.factor.auth;
    requires mysql.connector.j;
    requires org.twitter4j.core;
    requires java.net.http;
    requires scribejava.core;
    requires scribejava.apis;
    requires java.activation;
    requires org.json;
    requires com.google.api.services.drive;
    requires jbcrypt;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.desktop;


    opens com.PIDev3A18.projet to javafx.fxml;
    exports com.PIDev3A18.projet;
    opens entity to com.google.gson, javafx.base;
}