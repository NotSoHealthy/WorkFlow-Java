package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(13)).replaceFirst("\\$2a\\$", "\\$2y\\$");
    }

    public static boolean isPasswordValid(String plainPassword, String hashedPassword) {
        // Convert $2y$ to $2a$ for JBCrypt compatibility
        String javaHash = hashedPassword.replaceFirst("\\$2y\\$", "\\$2a\\$");
        return BCrypt.checkpw(plainPassword, javaHash);
    }

    public static void main(String[] args) {
        System.out.println(hashPassword("password.1"));
    }
}
