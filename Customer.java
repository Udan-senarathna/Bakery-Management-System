package model;

public class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String nic;

    public Customer(String customerId, String firstName, String lastName, String email, String phone, String address, String nic) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.nic = nic;
    }

    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    
    public String toFileString() {
        return customerId + "," + firstName + "," + lastName + "," + email + "," + phone + "," + address + "," + nic;
    }

    public static Customer fromFileString(String fileLine) {
        String[] parts = fileLine.split(",");
        
        
        if (parts.length >= 7) {
            return new Customer(
                parts[0].trim(), // ID
                parts[1].trim(), // First Name
                parts[2].trim(), // Last Name
                parts[3].trim(), // Email
                parts[4].trim(), // Phone
                parts[5].trim(), // Address
                parts[6].trim()  // NIC
            );
        } 
        return null;
    }

    @Override
    public String toString() {
        return "ID: " + customerId + " | Name: " + firstName + " " + lastName + 
               " | Phone: " + phone + " | Email: " + email + " | Address: " + address + " | NIC: " + nic;
    }
}