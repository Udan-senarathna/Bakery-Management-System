package model;

public class Employee {
    private String username;
    private String password;
    private String role; 

   
    public Employee(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    
    public String toFileString() {
        return username + "," + password + "," + role;
    }

    
    public static Employee fromFileString(String fileLine) {
        String[] parts = fileLine.split(",");
        if (parts.length == 3) {
            return new Employee(parts[0].trim(), parts[1].trim(), parts[2].trim());
        }
        return null;
    }

    @Override
    public String toString() {
        return "User: " + username + " | Role: " + role;
    }
}