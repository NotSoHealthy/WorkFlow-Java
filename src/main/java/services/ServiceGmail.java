package services;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ServiceGmail {
    private static final String APPLICATION_NAME = "Workflow";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens_mehdi";
    private static final List<String> SCOPES = List.of(GmailScopes.GMAIL_SEND, GmailScopes.GMAIL_COMPOSE);
    private static final String CREDENTIALS_FILE_PATH = "/credentials_mahdi_gmail.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws Exception If an error occurs while authorizing.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        // Load client secrets
        InputStream credentialsStream = GmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(credentialsStream));

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

    public void sendGmail(String gmail,String Header,String Message) throws Exception {
        // Build a new authorized API client service
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Send an email
        String to = gmail;
        String from = "workflow.ithr@gmail.com";

       String  subject = "Mise à jour de votre réclamation";
       String bodyText = Message;

        sendEmail(service, to, from, subject, bodyText);
    }

    /**
     * Sends an email using the Gmail API.
     *
     * @param service   The Gmail service instance.
     * @param to        The recipient's email address.
     * @param from      The sender's email address.
     * @param subject   The subject of the email.
     * @param bodyText  The body of the email.
     * @throws MessagingException If an error occurs while creating the email.
     * @throws IOException        If an error occurs while sending the email.
     */
    private static void sendEmail(Gmail service, String to, String from, String subject, String bodyText)
            throws MessagingException, IOException {
        // Create a MimeMessage object
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);

        // Encode the MimeMessage as a base64url string
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);

        // Create a Message object
        Message message = new Message();
        message.setRaw(encodedEmail);

        // Send the email
        message = service.users().messages().send("me", message).execute();
        System.out.println("Email sent! Message ID: " + message.getId());
    }
}