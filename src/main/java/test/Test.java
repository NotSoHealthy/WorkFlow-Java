package test;

import entity.Employee;
import entity.Message;
import entity.Reclamation;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import services.ServiceEmployee;
import services.ServiceMessage;
import services.ServiceReclamation;

import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Test  {


    public static void main(String[] args) throws IOException {
        URL url = new URL("https://i.ibb.co/d29vTTc/1734578951342000.jpg");
        InputStream in = url.openStream();
        Image image = new Image(url.toString());
        System.out.println(image.getUrl());
        System.out.println(image.getHeight());
    }

}
