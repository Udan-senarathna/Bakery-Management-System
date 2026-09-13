package model;

import java.time.LocalDate;

public class Invoice {
    private String invoiceId;
    private String orderId;
    private String customerName;    
    private String dateIssued;     
    private double totalAmount;
    private String paymentStatus; 

    public Invoice(String invoiceId, String orderId, String customerName, String dateIssued, double totalAmount, String paymentStatus) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.customerName = customerName;
        this.dateIssued = dateIssued;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

    
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getDateIssued() { return dateIssued; }
    public void setDateIssued(String dateIssued) { this.dateIssued = dateIssued; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    
    public String toFileString() {
        return invoiceId + "," + orderId + "," + customerName + "," + dateIssued + "," + totalAmount + "," + paymentStatus;
    }

    
    public static Invoice fromFileString(String fileLine) {
        String[] parts = fileLine.split(",");
        if (parts.length == 6) {
            try {
                String id = parts[0].trim();
                String oId = parts[1].trim();
                String name = parts[2].trim();
                String date = parts[3].trim();
                double amount = Double.parseDouble(parts[4].trim());
                String status = parts[5].trim();
                return new Invoice(id, oId, name, date, amount, status);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing invoice amount: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Invoice ID: " + invoiceId + " | Customer: " + customerName + " | Date: " + dateIssued + " | Total: $" + totalAmount + " | Status: " + paymentStatus;
    }
}