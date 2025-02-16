package utils;

import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;

public class ImgApi {
    private static final String UPLOAD_URL = "https://api.imgbb.com/1/upload";
    private static final String API_KEY = "73a46ef3fc1b634993a6addc9b377f0e";

    public static String uploadImage(File imageFile, ProgressBar progressBar) throws IOException {
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add("key", API_KEY)
                .add("image", base64Image)
                .build();

        Request request = new Request.Builder()
                .url(UPLOAD_URL)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                return extractImageUrl(responseBody);  // Extract URL from JSON response
            } else {
                throw new IOException("Failed to upload image: " + response.message());
            }
        }
    }

    private static String extractImageUrl(String jsonResponse) {
        // Simple parsing to extract the image URL (better to use a JSON parser like Gson or Jackson)
        int start = jsonResponse.indexOf("\"url\":\"") + 7;
        int end = jsonResponse.indexOf("\"", start);
        return jsonResponse.substring(start, end).replace("\\/", "/"); // Fix escaped slashes
    }
}
