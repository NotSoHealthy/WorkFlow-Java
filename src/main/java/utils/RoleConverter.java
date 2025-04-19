package utils;

import java.sql.SQLException;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Employee;
import services.ServiceEmployee;

public class RoleConverter {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String convertToSymfony(String role){
        List<String> list = List.of();
        try {
            if (role.equals("Employé")){
                return mapper.writeValueAsString(list);
            }
            else{
                list = List.of("ROLE_RESPONSABLE");
                return mapper.writeValueAsString(list);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertToJava(String role){
        try {
            List<String> roles = mapper.readValue(role, new TypeReference<List<String>>() {});
            if (roles.contains("ROLE_RESPONSABLE")){
                return "Résponsable";
            }
            else{
                return "Employé";
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
