package service;

import model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String LOGIN_FILE = "logins.txt";
    private static final String ORDER_FILE = "orders.txt";
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String INVOICE_FILE = "invoices.txt";
    private static final String REPORT_FILE = "preparation_reports.txt"; 
    private static final String CAPACITY_FILE = "production_capacity.txt";
    private static final String STOCK_FILE = "stock.txt"; 
    private static final String HEAD_BAKERS_FILE = "head_bakers.txt";
    
    
    private static final String MANAGER_FILE = "manager.txt";
    private static final String BAKER_FILE = "baker.txt";
    
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static void saveLines(File file, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    public static String readFullFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return "";
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            return "";
        }
        return content.toString();
    }

    // This is the method that fixes your ID bug. 
    // Always call this to get the next ID instead of counting lines.
    public static int getNextId(String fileName) {
        int maxId = 0;
        String content = readFullFile(fileName);
        if (content.isEmpty()) return 1;
        
        for (String line : content.split("\n")) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length > 0) {
                try {
                    String idStr = parts[0].trim();
                    if (idStr.contains("-")) {
                        idStr = idStr.split("-")[1];
                    }
                    int currentId = Integer.parseInt(idStr);
                    if (currentId > maxId) maxId = currentId;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    // Skip malformed lines
                }
            }
        }
        return maxId + 1;
    }

    
    public static boolean isManagerAlreadyRegistered() {
        File f = new File(MANAGER_FILE);
        return f.exists() && f.length() > 0;
    }

    
    public static boolean updateUser(String username, String password, String role, String specialization) {
        String fileName = role.equalsIgnoreCase("Manager") ? MANAGER_FILE : BAKER_FILE;
        File file = new File(fileName);
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                if (parts.length >= 2 && parts[1].trim().equalsIgnoreCase(username)) {
                    
                    lines.add(parts[0] + "," + username + "," + password + "," + role + "," + specialization + "," + parts[5] + "," + parts[6] + "," + parts[7] + "," + parts[8] + "," + parts[9] + "," + parts[10]);
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }

        if (updated) saveLines(file, lines);
        return updated;
    }

    public static void deleteUser(String username, String role) {
        String fileName = role.equalsIgnoreCase("Manager") ? MANAGER_FILE : BAKER_FILE;
        File file = new File(fileName);
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1 && !parts[1].trim().equalsIgnoreCase(username.trim())) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file for deletion: " + e.getMessage());
        }

        saveLines(file, lines);
    }

    public static void deleteCustomer(String customerId) {
        File file = new File(CUSTOMER_FILE);
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && !parts[0].trim().equalsIgnoreCase(customerId.trim())) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading customer file: " + e.getMessage());
        }

        saveLines(file, lines);
    }

    public static boolean updateCustomer(String customerId, String fName, String lName, String email, String phone, String address, String nic) {
        File file = new File(CUSTOMER_FILE);
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[0].trim().equalsIgnoreCase(customerId)) {
                    lines.add(customerId + "," + fName + "," + lName + "," + email + "," + phone + "," + address + "," + nic);
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }

        if (updated) saveLines(file, lines);
        return updated;
    }

    public static void saveHeadBakers(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HEAD_BAKERS_FILE, false))) {
            writer.write(data);
        } catch (IOException e) { System.err.println("Error saving head bakers: " + e.getMessage()); }
    }

    public static String readHeadBakers() { return readFullFile(HEAD_BAKERS_FILE); }

    public static void saveStock(String stockData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STOCK_FILE, false))) {
            writer.write(stockData);
        } catch (IOException e) { System.err.println("Error saving stock: " + e.getMessage()); }
    }

    public static String readStock() {
        String data = readFullFile(STOCK_FILE);
        return (data.isEmpty()) ? "No stock information available." : data;
    }

    public static void saveCapacity(String capacity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAPACITY_FILE, false))) {
            writer.write(capacity);
        } catch (IOException e) { System.err.println("Error saving capacity: " + e.getMessage()); }
    }

    
    public static void saveUser(Employee emp) {
        String fileName = emp.getRole().equalsIgnoreCase("Manager") ? MANAGER_FILE : BAKER_FILE;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(emp.toFileString());
            writer.newLine();
        } catch (IOException e) { System.err.println("Error saving user: " + e.getMessage()); }
    }

    
    public static boolean validateUser(String username, String password, String expectedRole) {
        String fileName = expectedRole.equalsIgnoreCase("Manager") ? MANAGER_FILE : BAKER_FILE;
        String content = readFullFile(fileName);
        for (String line : content.split("\n")) {
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                
                if (parts[1].trim().equals(username) && parts[2].trim().equals(password)) {
                    return true; 
                }
            }
        }
        return false;
    }

    public static List<String> getBakersBySpecialization(String specialization) {
        List<String> bakers = new ArrayList<>();
        String content = readFullFile(BAKER_FILE); 
        for (String line : content.split("\n")) {
            String[] parts = line.split(",");
            if (parts.length >= 5 && parts[4].trim().equalsIgnoreCase(specialization)) {
                bakers.add(parts[0].trim());
            }
        }
        return bakers;
    }

    public static List<String> getRegisteredBakers() {
        List<String> bakers = new ArrayList<>();
        String content = readFullFile(BAKER_FILE);
        for (String line : content.split("\n")) {
            if(!line.trim().isEmpty()) bakers.add(line.split(",")[0].trim());
        }
        return bakers;
    }

    public static List<String> getRegisteredCustomers() {
        List<String> customers = new ArrayList<>();
        String content = readFullFile(CUSTOMER_FILE);
        for (String line : content.split("\n")) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length > 0) {
                customers.add(parts[0].trim());
            }
        }
        return customers;
    }

    public static void logLogin(String username, String role) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOGIN_FILE, true))) {
            String timestamp = LocalDateTime.now().format(dtf);
            writer.write(username + " | Role: " + role + " | LoggedIn: " + timestamp);
            writer.newLine();
        } catch (IOException e) { System.err.println("Error saving login details: " + e.getMessage()); }
    }

    public static void saveOrder(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE, true))) {
            writer.write(order.toFileString());
            writer.newLine();
        } catch (IOException e) { System.err.println("Error saving order: " + e.getMessage()); }
    }

    public static String readOrdersForBaker(String bakerName) {
        StringBuilder content = new StringBuilder();
        String data = readFullFile(ORDER_FILE);
        for (String line : data.split("\n")) {
            String[] parts = line.split(",");
            if (parts.length >= 8 && parts[6].trim().startsWith(bakerName)) {
                content.append(String.format("ORDER ID: %s | Client: %s | Item: %s (Qty: %s) | Status: %s | Notes: %s\n", 
                    parts[0], parts[1], parts[2], parts[3], parts[5], parts[4]));
            }
        }
        return content.toString().isEmpty() ? "No active cooking tasks assigned to you right now!" : content.toString();
    }

    public static List<String> searchOrders(String keyword) {
        List<String> matches = new ArrayList<>();
        String content = readFullFile(ORDER_FILE);
        for (String line : content.split("\n")) {
            if (line.toLowerCase().contains(keyword.toLowerCase())) matches.add(line);
        }
        return matches;
    }

    public static void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMER_FILE, true))) {
            writer.write(customer.toFileString());
            writer.newLine();
        } catch (IOException e) { System.err.println("Error saving customer: " + e.getMessage()); }
    }

    public static void saveInvoice(Invoice invoice) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVOICE_FILE, true))) {
            writer.write(invoice.toFileString());
            writer.newLine();
        } catch (IOException e) { System.err.println("Error saving invoice: " + e.getMessage()); }
    }

    public static boolean updateInvoiceStatus(String invoiceId, String newStatus) {
        File file = new File(INVOICE_FILE);
        if (!file.exists()) return false;
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && parts[0].trim().equalsIgnoreCase(invoiceId.trim())) {
                    lines.add(parts[0].trim() + "," + parts[1].trim() + "," + parts[2].trim() + "," + 
                            parts[3].trim() + "," + parts[4].trim() + "," + newStatus);
                    updated = true;
                } else lines.add(line);
            }
        } catch (IOException e) { return false; }
        if (updated) saveLines(file, lines);
        return updated;
    }

    public static void saveReport(PerparationReport report) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REPORT_FILE, true))) {
            writer.write(report.toFileString());
            writer.newLine();
        } catch (IOException e) { System.err.println("Error saving preparation report: " + e.getMessage()); }
    }
}