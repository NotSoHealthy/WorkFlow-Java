package com.PIDev3A18.projet;

import entity.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;
import services.*;
import utils.UserSession;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private Region day1Region;
    @FXML private Text day1Text;
    @FXML private Region day2Region;
    @FXML private Text day2Text;
    @FXML private Region day3Region;
    @FXML private Text day3Text;
    @FXML private Region day4Region;
    @FXML private Text day4Text;
    @FXML private Region day5Region;
    @FXML private Text day5Text;
    @FXML private Region day6Region;
    @FXML private Text day6Text;
    @FXML private Region day7Region;
    @FXML private Text day7Text;
    @FXML private VBox taskVBox;
    @FXML private Text titleText;
    @FXML private VBox calendar1VBox;
    @FXML private VBox calendar2VBox;
    @FXML private VBox calendar3VBox;
    @FXML private VBox calendar4VBox;
    @FXML private VBox calendar5VBox;
    @FXML private VBox calendar6VBox;
    @FXML private VBox calendar7VBox;
    @FXML private Label congeNumber;
    @FXML private Button congeButton;
    @FXML private VBox eventVBox;
    @FXML private Label reclamationNumber;
    @FXML private Label formationNumber;
    @FXML private Label eventNumber;

    private Employee loggedInEmployee;
    private ServiceEmployee serviceEmployee;
    private ServiceAttendance serviceAttendance;
    private ServiceConge serviceConge;
    private List<Text> textList;
    private List<Region> regionList;
    private List<VBox> calendarVBoxList;
    private ServiceEvent serviceEvent;
    private ServiceTask serviceTask;
    private ServiceReclamation serviceReclamation;
    private LayoutController layoutController;
    private ServiceInscription serviceInscription;
    private ServiceReservation serviceReservation;

    public DashboardController(LayoutController layoutController) {
        this.layoutController = layoutController;
        loggedInEmployee = UserSession.getInstance().getLoggedInEmployee();
        serviceAttendance = new ServiceAttendance();
        serviceEmployee = new ServiceEmployee();
        serviceConge = new ServiceConge();
        serviceEvent = new ServiceEvent();
        serviceTask = new ServiceTask();
        serviceReclamation = new ServiceReclamation();
        serviceInscription = new ServiceInscription();
        serviceReservation = new ServiceReservation();
    }

    public void initialize() throws Exception {
        textList = Arrays.asList(day1Text, day2Text, day3Text, day4Text, day5Text, day6Text, day7Text);
        regionList = Arrays.asList(day1Region, day2Region, day3Region, day4Region, day5Region, day6Region, day7Region);
        calendarVBoxList = Arrays.asList(calendar1VBox, calendar2VBox, calendar3VBox, calendar4VBox, calendar5VBox, calendar6VBox, calendar7VBox);

        InputStream inputStream = getClass().getResourceAsStream("icons/conge.png");
        Image img = new Image(inputStream,16,16,true,true);
        congeNumber.setGraphic(new ImageView(img));

        inputStream = getClass().getResourceAsStream("icons/pen.png");
        img = new Image(inputStream,14,14,true,true);
        reclamationNumber.setGraphic(new ImageView(img));

        inputStream = getClass().getResourceAsStream("icons/event.png");
        img = new Image(inputStream,14,14,true,true);
        eventNumber.setGraphic(new ImageView(img));

        inputStream = getClass().getResourceAsStream("icons/Formation.png");
        img = new Image(inputStream,14,14,true,true);
        formationNumber.setGraphic(new ImageView(img));

        titleText.setText("Bonjour, "+loggedInEmployee.getFirstName());
        loadHours();
        loadTasks();
        loadConge();
        loadReclamation();
        loadFormation();
        loadCalendar();
        loadEvents();
    }

    public void loadHours() throws SQLException {
        List<Attendance> attendanceList = serviceAttendance.readAllByEmployee(loggedInEmployee);
        attendanceList = attendanceList.stream().limit(7).toList().reversed();
        int i = 0;
        long fullMinutes = 8 * 60;
        int height = 30;
        int gap = 10;
        double margin = 0;
//        String baseStyle = "-fx-background-color: linear-gradient(to top, #007CFF %s%%, transparent %s%%);";
        LocalDate lastDate = LocalDate.now();

        for (Attendance attendance : attendanceList) {
            textList.get(i).setText(StringUtils.capitalize(attendance.getDate().format(DateTimeFormatter.ofPattern("E d", Locale.FRENCH))));

            double workedMinutes = Duration.between(attendance.getEntry_time(), attendance.getExit_time()).toMinutes();
            double percentageValue = (workedMinutes / fullMinutes) * 100;
            percentageValue = Math.max(0, Math.min(100, percentageValue));
//            double remainingPercentage = 100 - percentageValue;

//            String percentage = String.format("%.0f", percentageValue);
//            String remaining = String.format("%.0f", remainingPercentage);

//            String finalStyle = String.format(baseStyle, percentage, remaining);
//            regionList.get(i).setStyle(finalStyle);
            margin = (100-percentageValue) / 20 * height;
            margin += Math.floor((100-percentageValue) / 20) * gap;
            GridPane.setMargin(regionList.get(i),new Insets(margin,0,0,0) );

            lastDate = attendance.getDate();
            i++;
        }

        while (i<7){
            assert lastDate != null;
            lastDate = lastDate.plusDays(1);
            while(lastDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || lastDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                lastDate = lastDate.plusDays(1);
            }
            textList.get(i).setText(StringUtils.capitalize(lastDate.format(DateTimeFormatter.ofPattern("E d", Locale.FRENCH))));
            regionList.get(i).setStyle("-fx-background-color:#C6E1FF;");
            i++;
        }

    }

    public void loadTasks() throws SQLException, IOException {
        List<Task> taskList = serviceTask.getTaskByEmployee(loggedInEmployee);
        taskList = taskList.stream().sorted(Comparator.comparing(Task::getDueDate)).limit(5).toList();
        for(Task task : taskList){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("task_item.fxml"));
            TaskItemController controller = new TaskItemController(task);
            fxmlLoader.setController(controller);
            taskVBox.getChildren().add(fxmlLoader.load());
        }
    }

    public void goToTasks(){
        layoutController.layoutGoToProjects(null);
    }

    public void loadConge() throws SQLException {
        int conge = 30-serviceConge.getCongeThisYear(loggedInEmployee);
        congeNumber.setText(String.valueOf(conge));
        congeButton.setDisable(conge<=0);
    }

    public void goToConge(){
        layoutController.layoutGoToConge(null);
    }

    public void loadReclamation() throws SQLException {
        List<Reclamation> reclamationList = serviceReclamation.readAll().stream().filter(r -> r.getEmployee().getId()==loggedInEmployee.getId() && r.getEtat().equals("IN PROGRESS")).toList();
        reclamationNumber.setText(String.valueOf(reclamationList.size()));
    }

    public void goToReclamation(){
        layoutController.layoutGoToReclamation(null);
    }

    public void loadFormation() throws SQLException {
        List<Inscription> inscriptionList = serviceInscription.readAll().stream().filter(i-> i.getEmployee().getId()==loggedInEmployee.getId() && i.getFormation().getDateEnd().isAfter(LocalDate.now())).toList();
        formationNumber.setText(String.valueOf(inscriptionList.size()));
    }

    public void goToFormation(){
        layoutController.layoutGoToFormation(null);
    }

    public void loadCalendar() throws SQLException {
        LocalDate day = LocalDate.now();
        while (!day.getDayOfWeek().equals(DayOfWeek.MONDAY)){
            day = day.minusDays(1);
        }

        for (VBox calendarVBox : calendarVBoxList) {
            Text date = (Text) calendarVBox.getChildren().getLast();
            date.setText(String.valueOf(day.getDayOfMonth()));

            if (day.getDayOfWeek().equals(LocalDate.now().getDayOfWeek())) {
                calendarVBox.getStyleClass().add("calendar-current-day");
            }

            day = day.plusDays(1);
        }
    }

    public void loadEvents() throws SQLException, IOException {
        List<Event> eventList = serviceEvent.readAll().stream().filter(e -> !e.getDateetheure().isBefore(LocalDateTime.now())).sorted(Comparator.comparing(Event::getDateetheure)).limit(4).collect(Collectors.toList());
        for (Event event : eventList) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event_item.fxml"));
            EventItemController controller = new EventItemController(event);
            fxmlLoader.setController(controller);
            eventVBox.getChildren().add(fxmlLoader.load());
        }

        List<Reservation> reservationList = serviceReservation.readAll().stream().filter(r -> r.getEmployee().getId()==loggedInEmployee.getId() && r.getEvent().getDateetheure().isAfter(LocalDateTime.now())).toList();
        eventNumber.setText(String.valueOf(reservationList.stream().mapToInt(Reservation::getNombreDePlaces).sum()));
    }

    public void goToEvent() throws SQLException {
        layoutController.layoutGoToEvenements(null);
    }

}
