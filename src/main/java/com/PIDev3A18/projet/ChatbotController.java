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

    @FXML private ScrollPane chatScrollPane;  // Reference to ScrollPane
    @FXML private VBox chatContainer;  // Changed from TextArea to VBox
    @FXML private TextField userInputField;
    @FXML private Button sendButton;
    @FXML private ImageView botIcon;   // Bot icon
    @FXML private ImageView userIcon;  // User icon

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
                    "/icons/bot.png",    // Absolute path from resources root
                    "icons/bot.png",     // Relative path
                    "/images/bot.png"    // Alternative directory
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

        // Load user icon (profile picture) from UserSession
        try {
            UserSession userSession = UserSession.getInstance();
            Employee loggedInEmployee = userSession.getLoggedInEmployee();
            if (loggedInEmployee != null) {
                Image image = new Image(loggedInEmployee.getImageUrl());
                double imageWidth = image.getWidth();
                double imageHeight = image.getHeight();
                double minSize = Math.min(imageWidth, imageHeight);

                // Crop to a square and set as user icon
                userIcon.setImage(image);
                userIcon.setViewport(new Rectangle2D(
                        (imageWidth - minSize) / 2, // Center X
                        (imageHeight - minSize) / 2, // Center Y
                        minSize, minSize // Crop size (square)
                ));
                userIcon.setFitWidth(30);
                userIcon.setFitHeight(30);

                // Apply circular clip to make it a circular icon
                Circle clip = new Circle(15); // Radius of 15 for a 30x30 circle
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

        // Configure ScrollPane for manual scrolling
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Listen for scroll events to detect manual scrolling
        chatScrollPane.vvalueProperty().addListener((obs, oldValue, newValue) -> {
            // If the user scrolls up (value < 1.0), mark they’ve scrolled manually
            isUserScrolled = newValue.doubleValue() < 1.0;
        });
    }

    private void handleSendButton() {
        String userInput = userInputField.getText().trim();
        if (userInput.isEmpty()) return;

        // Add user message (right-aligned bubble with user icon)
        addMessage("You", userInput, true);  // true for right alignment
        conversationHistory.add(new Message("user", userInput));

        // Check for API key
        if (apiKey == null || apiKey.isEmpty()) {
            addMessage("Bot", "API key is missing. Please set GEMINI_API_KEY.", false);  // false for left alignment
            return;
        }

        // Prepare the API request with full history
        String requestBody = buildRequestBody();

        // Call the API asynchronously
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
        // Create a TextFlow for the message content
        Text messageText = new Text(content + "\n");
        messageText.setFont(new javafx.scene.text.Font("Segoe UI", 16));
        TextFlow textFlow = new TextFlow(messageText);

        // Apply CSS class for bubble styling
        textFlow.getStyleClass().add(isUser ? "user-bubble" : "bot-bubble");

        // Create an HBox for positioning
        HBox messageBox = new HBox(15);  // Increased spacing for better spacing
        if (isUser) {
            messageBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            // Add user icon before the bubble (right-aligned)
            messageBox.getChildren().addAll(textFlow, userIcon);
        } else {
            messageBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            // Add bot icon before the bubble (left-aligned)
            messageBox.getChildren().addAll(botIcon, textFlow);
        }

        // Add to chat container
        chatContainer.getChildren().add(messageBox);

        // Scroll behavior: Only auto-scroll to bottom if user hasn’t manually scrolled
        if (!isUserScrolled) {
            chatScrollPane.setVvalue(1.0);  // Scroll to the bottom
        }
    }

    private String buildRequestBody() {
        // Build JSON in Gemini API format (contents array)
        StringBuilder contentsJson = new StringBuilder();
        for (Message msg : conversationHistory) {
            contentsJson.append("{\"role\": \"").append(msg.role).append("\", \"parts\": [{\"text\": \"").append(msg.content).append("\"}]},");
        }
        if (contentsJson.length() > 0) {
            contentsJson.deleteCharAt(contentsJson.length() - 1); // Remove trailing comma
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