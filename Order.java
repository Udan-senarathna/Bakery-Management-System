package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private String id;
    private String customerName;
    private String items;
    private String quantity;
    private String notes;
    private String status;
    private String assignedBaker;
    private String timestamp; 

    public Order(String id, String customerName, String items, String quantity, String notes, String status, String assignedBaker) {
        this.id = id;
        this.customerName = customerName;
        this.items = items;
        this.quantity = quantity;
        this.notes = notes;
        this.status = status;
        this.assignedBaker = assignedBaker;
        
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    
    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getItems() { return items; }
    public String getQuantity() { return quantity; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public String getAssignedBaker() { return assignedBaker; }
    public String getTimestamp() { return timestamp; } 

    
    public String toFileString() {
        return id + "," + customerName + "," + items + "," + quantity + "," + notes + "," + status + "," + assignedBaker + "," + timestamp;
    }
}