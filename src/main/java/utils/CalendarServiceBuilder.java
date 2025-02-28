package utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;

public class CalendarServiceBuilder {

    public static Calendar getCalendarService() throws Exception {
        Credential credential = GoogleCalendarAuth.authorize();
        return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("WorkFlow")
                .build();
    }
}
