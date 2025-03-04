package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordChecker {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
    private static final String GEMINI_API_KEY = "AIzaSyBbeQks7sz0LgHOG9cQ1ErLCzuXZ5PESw0";
    private static final String ENZOIC_API_URL = "https://api.enzoic.com/v1/passwords";
    private static final String ENZOIC_API_KEY = "ae1d9151ee8d49189f608d229a159441";
    private static final String ENZOIC_API_SECRET = "P*yF5T+&J-!8m2SWJjR2ZKV3stHqhM+h";

    public static int checkPasswordLevel(String password) throws IOException {
        URL url = new URL(GEMINI_API_URL + GEMINI_API_KEY);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // JSON Request Body
        String jsonInputString = "{"
                + "\"contents\": [{"
                + "\"parts\": [{\"text\": \"I am going to give you a password. "
                + "Give this password a score between 1 and 10 based on how secure it is:\\n"
                + password
                + "\\nRemember, give a score between 1 and 10 as your only output.\"}]"
                + "}],"
                + "\"generationConfig\": {"
                + "\"response_mime_type\": \"application/json\","
                + "\"response_schema\": {"
                + "\"type\": \"object\","
                + "\"properties\": {"
                + "\"number\": {\"type\": \"integer\"}"
                + "},"
                + "\"required\": [\"number\"]"
                + "}"
                + "}"
                + "}";

        // Send Request
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        // Read API Response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = bufferedReader.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Print Raw API Response for Debugging
                System.out.println("Raw API Response: " + response.toString());

                // Parse JSON Response
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();

                // Extract "text" field inside "parts"
                JsonObject firstCandidate = jsonResponse.getAsJsonArray("candidates").get(0).getAsJsonObject();
                JsonObject content = firstCandidate.getAsJsonObject("content");
                JsonObject firstPart = content.getAsJsonArray("parts").get(0).getAsJsonObject();

                // Extract text containing JSON string
                String jsonText = firstPart.get("text").getAsString().trim();

                // Parse inner JSON string to get "number"
                JsonObject parsedNumberObject = JsonParser.parseString(jsonText).getAsJsonObject();
                int number = parsedNumberObject.get("number").getAsInt();

                System.out.println("Password Score: " + number);
                return number;

            } catch (Exception e) {
                System.err.println("Error processing JSON response: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("HTTP request failed with response code: " + responseCode);
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), "UTF-8"))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println(line);
                }
            }
        }

        connection.disconnect();
        return 0;  // Return 0 in case of failure
    }

    public static boolean isPasswordCompromised(String password) throws IOException {
        String partialSHA256 = encodeToSHA256(password).substring(0, 10);
        URL url = new URL(ENZOIC_API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Authorization", getBasicAuth());
        connection.setDoOutput(true);

        String jsonInputString = "{ \"partialSHA256\": \"" + partialSHA256 + "\" }";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        connection.disconnect();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Password is COMPROMISED.");
            return true;
        } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            System.out.println("Password is SAFE.");
            return false;
        } else {
            System.err.println("Error: Received unexpected response code: " + responseCode);
            return false;
        }
    }

    public static String encodeToSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 algorithm not found!", e);
        }
    }

    private static String getBasicAuth() {
        String auth = ENZOIC_API_KEY + ":" + ENZOIC_API_SECRET;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }
}


