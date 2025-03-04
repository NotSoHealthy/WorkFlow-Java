package utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import entity.Employee;

import java.sql.SQLException;

public class SMSApi {
    public static final String ACCOUNT_SID = "AC8b8eae99a88a4372de8f75c17922dbfc";
    public static final String AUTH_TOKEN = "8418d2de0906f7054cba8f0d0e91a49a";

    public static void sendSMS(String body) throws SQLException {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String fromNumber = "+19124557725";
        String toNumber = "+21653267314";
        Message message = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(fromNumber), body).create();
        System.out.println(message);
    }

    public static void sendCodeSMS(Employee employee, String code) throws SQLException {
        String body = "Bonjour %s,  \n" +
                "Votre code de réinitialisation de mot de passe est : **%s**  \n" +
                "- L’équipe WorkFlow  \n";

        body = String.format(body,employee.getLastName()+" "+employee.getFirstName(), code);
        sendSMS(body);
    }
}
