package model;

public class Baker extends Employee {
    
    
    public Baker(String username, String password) {
        super(username, password, "Baker");
    }

    
    public static Baker fromFileString(String fileLine) {
        String[] parts = fileLine.split(",");
        if (parts.length == 3) {
            String user = parts[0].trim();
            String pass = parts[1].trim();
            String role = parts[2].trim();
            
            if (role.equalsIgnoreCase("Baker")) {
                return new Baker(user, pass);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Baker: " + getUsername();
    }
}