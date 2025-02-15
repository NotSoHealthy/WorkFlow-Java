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

public class Test {
    public static void main(String[] args) throws SQLException {
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);
        LocalTime localTime = LocalTime.now();
        Time sqlTime = Time.valueOf(localTime);

       /* Employee employee = new Employee("Amine","Kerfai","aminekerfai75","12457852","employee");
        ServiceEmployee serviceEmployee = new ServiceEmployee();
        //serviceEmployee.add(employee);
        ServiceReclamation sr = new ServiceReclamation();

        //sr.add(new Reclamation("Reclamation 2","Une probleme avec les moniteurs",sqlDate,sqlTime,"REJECTED",sqlDate,serviceEmployee.readById(2),serviceEmployee.readById(3)));
        Date new_date=Date.valueOf("2025-11-15");
        //sr.update(new Reclamation(12,"Reclamation 3","Imprimante cassée",new_date,sqlTime,"pending",new_date,serviceEmployee.readById(1),serviceEmployee.readById(3)));
        Reclamation r = new Reclamation(12,"Reclamation 2","Une probleme avec les moniteurs",sqlDate,sqlTime,"REJECTED",sqlDate,serviceEmployee.readById(2),serviceEmployee.readById(3));
        sr.delete(r);

        //System.out.println(sr.readById(12));
        //System.out.println(sr.search("Reclamation 3"));
        //System.out.println(sr.sortTitre());
        //System.out.println(sr.searchByDate(sqlDate));
        //System.out.println(sr.searchByState("pending"));

        //System.out.println(serviceEmployee.readAll());
        //System.out.println(sr.readAll().toString());
        System.out.println(sqlDate);
        System.out.println(sqlTime);


        ServiceMessage sm =  new ServiceMessage();
        Message m = new Message("bonjour, nous aurons besoin de fichiers pour confirmer le paiement",sqlDate,sqlTime,sr.readById(11),serviceEmployee.readById(3));
        //sm.add(m);
        //sm.update(new Message(2,"bonjour, ",sqlDate,sqlTime,sr.readById(11),serviceEmployee.readById(3)));
        //sm.delete(new Message(2,"bonjour, ",sqlDate,sqlTime,sr.readById(11),serviceEmployee.readById(3)));
        System.out.println(sm.readAll());
        System.out.println("//////////////////////////////////////////////////////////////////////////////////////////////////////");
        System.out.println(sm.readById(3));


*/




    public static void main(String[] args) throws IOException {
        URL url = new URL("https://i.ibb.co/d29vTTc/1734578951342000.jpg");
        InputStream in = url.openStream();
        Image image = new Image(url.toString());
        System.out.println(image.getUrl());
        System.out.println(image.getHeight());
    }

}
