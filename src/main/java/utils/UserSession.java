package utils;

import entity.Employee;

public class UserSession {
    private static UserSession instance; // Singleton instance
    private Employee loggedInEmployee;   // Store the logged-in employee

    // Private constructor to prevent direct instantiation
    private UserSession() {}

    // Method to get the single instance (Singleton Pattern)
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Method to log in an employee
    public void login(Employee employee) {
        this.loggedInEmployee = employee;
    }

    // Method to log out the employee
    public void logout() {
        this.loggedInEmployee = null;
    }

    // Check if an employee is logged in
    public boolean isLoggedIn() {
        return loggedInEmployee != null;
    }

    // Get the logged-in employee
    public Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }
}
