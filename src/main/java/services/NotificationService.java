package services;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.net.URL;

public class NotificationService {
    public void sendnotification(String message) {
        if (SystemTray.isSupported()) {
            displayTray("Workflow Notification", message);
        } else {
            System.out.println("System tray not supported!");
        }
    }

    public static void displayTray(String title, String message) {
        try {
            // Get the system tray instance
            SystemTray tray = SystemTray.getSystemTray();

            // Load the custom image (Logo.png)
            URL imageUrl = NotificationService.class.getResource("/com/PIDev3A18/projet/icons/Logo.png");
            if (imageUrl == null) {
                System.err.println("Logo.png not found in the classpath!");
                return;
            }
            Image image = Toolkit.getDefaultToolkit().getImage(imageUrl);


            TrayIcon trayIcon = new TrayIcon(image, "Java Notification");
            trayIcon.setImageAutoSize(true); // Auto-size the image to fit the system tray
            trayIcon.setToolTip("Notification Example");

            // Add the TrayIcon to the system tray
            tray.add(trayIcon);

            // Display the notification
            trayIcon.displayMessage(title, message, MessageType.INFO); // Use NONE to hide the default icon

        } catch (Exception e) {
            e.printStackTrace();
        }
}}