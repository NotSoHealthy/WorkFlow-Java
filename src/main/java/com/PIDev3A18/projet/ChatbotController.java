package com.PIDev3A18.projet;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ChatbotController {

    @FXML private TextArea chatHistory;
    @FXML private TextField userInputField;
    @FXML private Button sendButton;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiKey = System.getenv("GEMINI_API_KEY"); // Fetch from environment variable
    private final String apiUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro-002:generateContent?key=" + apiKey;
    private List<Message> conversationHistory = new ArrayList<>();

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
    }

    private void handleSendButton() {
        String userInput = userInputField.getText().trim();
        if (userInput.isEmpty()) return;

        // Add user message to UI and history
        conversationHistory.add(new Message("user", userInput));
        chatHistory.appendText("You: " + userInput + "\n");

        // Check for API key
        if (apiKey == null || apiKey.isEmpty()) {
            chatHistory.appendText("Bot: API key is missing. Please set GEMINI_API_KEY.\n");
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
            conversationHistory.add(new Message("assistant", reply));
            chatHistory.appendText("Bot: " + reply + "\n");
        });

        task.setOnFailed(e -> {
            chatHistory.appendText("Bot: Sorry, I couldn’t process that. Check your connection or API key.\n");
        });

        new Thread(task).start();
        userInputField.clear();
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