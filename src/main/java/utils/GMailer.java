package utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import entity.Employee;
import org.apache.commons.codec.binary.Base64;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static javax.mail.Message.RecipientType.TO;

public class GMailer {

    private static final String TEST_EMAIL = "aminbenhamouda16@gmail.com";
    private final Gmail service;

    public GMailer() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("WorkFlow")
                .build();
    }

    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(GMailer.class.getResourceAsStream("/credentials.json")));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void sendMail(String recipientEmail,String subject, String message) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(TEST_EMAIL));
        email.addRecipient(TO, new InternetAddress(recipientEmail));
        email.setSubject(subject);
        email.setText(message);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            msg = service.users().messages().send("me", msg).execute();
            System.out.println("Message id: " + msg.getId());
            System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }

    public void sendSignUpMail(Employee employee) throws Exception {
        String subject = "Création de votre compte – WorkFlow";
        String body = "Bonjour %s,\n" +
                "Merci de vous être inscrit sur WorkFlow !\n" +
                "Votre compte a bien été créé et est actuellement en attente de validation par notre équipe. Nous vous informerons dès qu’il sera approuvé.\n" +
                "Cette vérification est nécessaire pour garantir la sécurité et la qualité de notre service.\n" +
                "Si vous avez des questions, n’hésitez pas à nous contacter.\n" +
                "Merci de votre patience et à très bientôt !\n\n" +
                "L’équipe WorkFlow";
        body = String.format(body, employee.getLastName()+" "+employee.getFirstName());
        sendMail(employee.getEmail(),subject,body);
    }

    public void sendConfirmationMail(Employee employee) throws Exception {
        String subject = "Votre compte a été validé – Bienvenue sur WorkFlow !";
        String body = "Bonjour %s,\n" +
                "Bonne nouvelle ! Votre compte sur WorkFlow a été validé par notre équipe.\n" +
                "Bienvenue parmi nous et à très bientôt !\n\n" +
                "L’équipe WorkFlow";
        body = String.format(body, employee.getLastName()+" "+employee.getFirstName());
        sendMail(employee.getEmail(),subject,body);
    }

    public void sendDenialMail(Employee employee) throws Exception {
        String subject = "Statut de votre inscription sur WorkFlow !";
        String body = "Bonjour %s,\n" +
                "Après examen de votre inscription sur WorkFlow, nous regrettons de vous informer que votre demande n’a pas été approuvée.\n" +
                "Si vous pensez qu'il s'agit d'une erreur ou que vous souhaitez plus d’informations, n’hésitez pas à nous contacter.\n\n" +
                "Cordialement,\n" +
                "L’équipe WorkFlow";
        body = String.format(body, employee.getLastName()+" "+employee.getFirstName());
        sendMail(employee.getEmail(),subject,body);
    }

    public void sendCodeMail(Employee employee, String code) throws Exception {
        String subject = "Réinitialisation de votre mot de passe - WorkFlow !";
        String body = "Bonjour %s,\n" +
                "Vous avez demandé à réinitialiser votre mot de passe. Utilisez le code de vérification ci-dessous pour procéder à la modification :\n" +
                "%s\n" +
                "Si vous n’êtes pas à l’origine de cette demande, vous pouvez ignorer ce message en toute sécurité.\n" +
                "Si vous avez besoin d’aide, n’hésitez pas à nous contacter.\n\n" +
                "L’équipe WorkFlow";
        body = String.format(body, employee.getLastName()+" "+employee.getFirstName(), code);
        sendMail(employee.getEmail(),subject,body);
    }

    public static void main(String[] args) throws Exception {
//        String subject = "Création de votre compte – WorkFlow";
//        String body = "Bonjour %s,\n" +
//                "Merci de vous être inscrit sur WorkFlow !\n" +
//                "Votre compte a bien été créé et est actuellement en attente de validation par notre équipe. Nous vous informerons dès qu’il sera approuvé.\n" +
//                "Cette vérification est nécessaire pour garantir la sécurité et la qualité de notre service.\n" +
//                "Si vous avez des questions, n’hésitez pas à nous contacter.\n" +
//                "Merci de votre patience et à très bientôt !\n\n" +
//                "L’équipe WorkFlow";
//        body = String.format(body, "nom prenom");
//        new GMailer().sendMail("aminbenhamouda16@gmail.com",subject,body);
    }
}