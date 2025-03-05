package com.PIDev3A18.projet;

import entity.Employee;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.UserSession;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ChatbotController {

    @FXML private ScrollPane chatScrollPane;
    @FXML private VBox chatContainer;
    @FXML private TextField userInputField;
    @FXML private Button sendButton;
    @FXML private ImageView botIcon;
    @FXML private ImageView userIcon;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiKey = "AIzaSyBWjh26eCEQGl1M8DxaFY5xtIa1ZtgtlKA"; // Fetch from environment variable
    private final String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro-002:generateContent?key=" + apiKey;    private List<Message> conversationHistory = new ArrayList<>();
    private boolean isUserScrolled = false;  // Track if the user has manually scrolled

    // Inner class to hold message role and content
    private static class Message {
        String role;
        String content;
        Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    @FXML
    private void initialize() {
        sendButton.setOnAction(event -> handleSendButton());

        // Load bot icon with multiple path attempts for debugging
        try {
            String[] possiblePaths = {
                    "icons/bot.png"
            };

            boolean iconLoaded = false;
            for (String path : possiblePaths) {
                URL resource = getClass().getResource(path);
                if (resource != null) {
                    botIcon.setImage(new Image(resource.toExternalForm(), 30, 30, true, true));
                    System.out.println("Successfully loaded bot icon from: " + path);
                    iconLoaded = true;
                    break;
                } else {
                    System.err.println("Bot icon not found at: " + path);
                }
            }

            if (!iconLoaded) {
                System.err.println("Bot icon not found. Using default or no icon.");
            }
        } catch (Exception e) {
            System.err.println("Error loading bot icon: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            UserSession userSession = UserSession.getInstance();
            Employee loggedInEmployee = userSession.getLoggedInEmployee();
            if (loggedInEmployee != null) {
                Image image = new Image(loggedInEmployee.getImageUrl());
                double imageWidth = image.getWidth();
                double imageHeight = image.getHeight();
                double minSize = Math.min(imageWidth, imageHeight);

                userIcon.setImage(image);
                userIcon.setViewport(new Rectangle2D(
                        (imageWidth - minSize) / 2,
                        (imageHeight - minSize) / 2,
                        minSize, minSize
                ));
                userIcon.setFitWidth(30);
                userIcon.setFitHeight(30);

                Circle clip = new Circle(15);
                clip.setCenterX(15);
                clip.setCenterY(15);
                userIcon.setClip(clip);
            } else {
                System.err.println("Logged-in employee not found in UserSession.");
            }
        } catch (Exception e) {
            System.err.println("Error loading user icon: " + e.getMessage());
            e.printStackTrace();
        }

        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        chatScrollPane.vvalueProperty().addListener((obs, oldValue, newValue) -> {
            isUserScrolled = newValue.doubleValue() < 1.0;
        });
    }

    private void handleSendButton() {
        String userInput = userInputField.getText().trim();
        if (userInput.isEmpty()) return;

        addMessage("You", userInput, true);
        conversationHistory.add(new Message("user", userInput));

        if (apiKey == null || apiKey.isEmpty()) {
            addMessage("Bot", "API key is missing. Please set GEMINI_API_KEY.", false);
            return;
        }

        String requestBody = buildRequestBody();

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                return response.body();
            }
        };

        task.setOnSucceeded(e -> {
            String botResponse = task.getValue();
            String reply = extractReplyFromResponse(botResponse);
            addMessage("Bot", reply, false);  // false for left alignment
            conversationHistory.add(new Message("assistant", reply));
        });

        task.setOnFailed(e -> {
            addMessage("Bot", "Sorry, I couldn’t process that. Check your connection or API key.", false);
        });

        new Thread(task).start();
        userInputField.clear();
    }

    private void addMessage(String sender, String content, boolean isUser) {

        Text messageText = new Text(content + "\n");
        messageText.setFont(new javafx.scene.text.Font("Segoe UI", 16));
        TextFlow textFlow = new TextFlow(messageText);


        textFlow.getStyleClass().add(isUser ? "user-bubble" : "bot-bubble");

        HBox messageBox = new HBox(15);  // Increased spacing for better spacing
        if (isUser) {
            messageBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            messageBox.getChildren().addAll(textFlow, userIcon);
        } else {
            messageBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            messageBox.getChildren().addAll(botIcon, textFlow);
        }

        chatContainer.getChildren().add(messageBox);

        if (!isUserScrolled) {
            chatScrollPane.setVvalue(1.0);
        }
    }

    private String buildRequestBody() {
        StringBuilder contentsJson = new StringBuilder();
        for (Message msg : conversationHistory) {
            contentsJson.append("{\"role\": \"").append(msg.role).append("\", \"parts\": [{\"text\": \"").append(msg.content).append("\"}]},");
        }
        if (contentsJson.length() > 0) {
            contentsJson.deleteCharAt(contentsJson.length() - 1);
        }
        return "{\"contents\": [" + contentsJson.toString() + "]}";
    }

    private String extractReplyFromResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            if (json.has("error")) {
                return "API Error: " + json.getJSONObject("error").getString("message");
            }
            JSONArray candidates = json.getJSONArray("candidates");
            if (candidates.length() > 0) {
                JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (parts.length() > 0) {
                    return parts.getJSONObject(0).getString("text");
                }
            }
            return "No reply from API.";
        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }
}