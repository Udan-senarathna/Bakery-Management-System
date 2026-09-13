package model;

import service.FileHandler;

public class OrderManager extends Employee {
    
    public OrderManager(String username, String password) {
        super(username, password, "Manager");
    }

    
    public static int getCategoryLimit(String category) {
        String content = FileHandler.readFullFile("production_capacity.txt");
        if (content == null || content.isEmpty()) return 0;

        for (String line : content.split("\n")) {
            if (line.startsWith(category + ":")) {
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    try {
                        return Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            }
        }
        return 0; 
    }
    
    public static OrderManager fromFileString(String fileLine) {
        String[] parts = fileLine.split(",");
        if (parts.length == 3) {
            String user = parts[0].trim();
            String pass = parts[1].trim();
            String role = parts[2].trim();
            
            if (role.equalsIgnoreCase("Manager")) {
                return new OrderManager(user, pass);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Order Manager: " + getUsername();
    }
}