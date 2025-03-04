package services;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.awt.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import com.google.api.services.drive.model.File;

public class ServiceGoogleDrive {
    public static final String APPLICATION_NAME = "Workflow";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens_mehdi_drive";
    public static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    public static final String CREDENTIALS_FILE_PATH = "C:/Users/Mega-Pc/Documents/GitHub/WorkFlow-Java/credentials/googledrive.json";

    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        // Load client secrets
        java.io.File credentialsFile = new java.io.File(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(credentialsFile)));

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        flow.getCredentialDataStore().delete("user");
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void UploadToDrive(String path)  throws Exception {
        // Build a new authorized API client service
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Use the Drive service to interact with Google Drive
        System.out.println("Connected to Google Drive!");
        String filePath = path; // Replace with your file path
        String folderId = "root";
        uploadFileToDrive(service, filePath, folderId);
        openGoogleDriveInBrowser();


    }

    public static void openGoogleDriveInBrowser() {
        try {
            // Create a URI for Google Drive
            URI googleDriveUri = new URI("https://drive.google.com");

            // Check if the Desktop API is supported
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                // Open the URI in the default browser
                Desktop.getDesktop().browse(googleDriveUri);
                System.out.println("Google Drive opened in your browser.");
            } else {
                System.out.println("Desktop browsing is not supported on this platform.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void uploadFileToDrive(Drive service, String filePath, String folderId) throws IOException {
        // Create a File object to represent the file metadata
        File fileMetadata = new File();
        fileMetadata.setName(new java.io.File(filePath).getName()); // Set the file name
        fileMetadata.setParents(Collections.singletonList(folderId)); // Set the parent folder ID

        // Create a FileContent object to represent the file content
        java.io.File localFile = new java.io.File(filePath);
        FileContent mediaContent = new FileContent("text/plain", localFile); // Replace "text/plain" with the appropriate MIME type

        // Upload the file to Google Drive
        File uploadedFile = service.files().create(fileMetadata, mediaContent)
                .setFields("id, name, webViewLink") // Specify the fields you want in the response
                .execute();



    }
}