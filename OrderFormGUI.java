package gui;

import main.Main;
import model.Order;
import service.FileHandler;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderFormGUI extends JPanel {
    private Main app;
    private JComboBox<String> customerDropdown;
    private JTextField orderIdField, itemNameField, qtyField, notesField;
    private JComboBox<String> statusDropdown, bakerDropdown, specDropdown;
    private JTextArea customerDetailsArea;
    private JTextField dateTimeField;

    public OrderFormGUI(Main app) {
        this.app = app;
        
        setBackground(new Color(245, 242, 235));
        setLayout(new GridBagLayout()); 

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        
        Border lineBorder = BorderFactory.createLineBorder(new Color(220, 215, 205), 1);
        Border innerPadding = BorderFactory.createEmptyBorder(25, 35, 25, 35);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, innerPadding));

        JLabel brandTitle = new JLabel("GREEN LEAF BAKERY");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandTitle.setForeground(new Color(34, 112, 63)); 
        brandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        dateTimeField = new JTextField();
        dateTimeField.setEditable(false); 
        dateTimeField.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateTimeField.setForeground(new Color(80, 80, 80));
        dateTimeField.setBackground(new Color(240, 240, 240));
        dateTimeField.setHorizontalAlignment(JTextField.CENTER);
        dateTimeField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        dateTimeField.setMaximumSize(new Dimension(300, 25));
        dateTimeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        
        Timer timer = new Timer(1000, e -> updateDisplayTime());
        timer.start();
        updateDisplayTime(); 
        

        JLabel brandSubtitle = new JLabel("Manager Central Order Production Input Desk");
        brandSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        brandSubtitle.setForeground(new Color(130, 130, 130));
        brandSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formFieldsPanel = new JPanel();
        formFieldsPanel.setBackground(Color.WHITE);
        formFieldsPanel.setLayout(new GridLayout(9, 2, 8, 8)); 
        formFieldsPanel.setMaximumSize(new Dimension(380, 320));

        Border fieldBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        );

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Color labelColor = new Color(70, 75, 70);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel lblId = new JLabel("Order ID:"); lblId.setFont(labelFont); lblId.setForeground(labelColor);
        orderIdField = new JTextField(); orderIdField.setBorder(fieldBorder); orderIdField.setFont(inputFont);

        JLabel lblCust = new JLabel("Customer:"); lblCust.setFont(labelFont); lblCust.setForeground(labelColor);
        customerDropdown = new JComboBox<>(); customerDropdown.setBackground(Color.WHITE); customerDropdown.setFont(inputFont);
        
        customerDetailsArea = new JTextArea(3, 20);
        customerDetailsArea.setEditable(false);
        customerDetailsArea.setLineWrap(true);
        customerDetailsArea.setWrapStyleWord(true);
        customerDetailsArea.setBackground(new Color(248, 248, 248));
        customerDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane detailsScrollPane = new JScrollPane(customerDetailsArea);

        JLabel lblItem = new JLabel("Item Name:"); lblItem.setFont(labelFont); lblItem.setForeground(labelColor);
        itemNameField = new JTextField(); itemNameField.setBorder(fieldBorder); itemNameField.setFont(inputFont);

        JLabel lblQty = new JLabel("Quantity:"); lblQty.setFont(labelFont); lblQty.setForeground(labelColor);
        qtyField = new JTextField(); qtyField.setBorder(fieldBorder); qtyField.setFont(inputFont);

        JLabel lblNotes = new JLabel("Notes:"); lblNotes.setFont(labelFont); lblNotes.setForeground(labelColor);
        notesField = new JTextField(); notesField.setBorder(fieldBorder); notesField.setFont(inputFont);

        JLabel lblStatus = new JLabel("Payment Status:"); lblStatus.setFont(labelFont); lblStatus.setForeground(labelColor);
        statusDropdown = new JComboBox<>(new String[]{"Pending", "Paid"}); statusDropdown.setBackground(Color.WHITE); statusDropdown.setFont(inputFont);

        JLabel lblSpec = new JLabel("Specialization:"); lblSpec.setFont(labelFont); lblSpec.setForeground(labelColor);
        specDropdown = new JComboBox<>(new String[]{"Cakes", "Breads", "Pastries"});
        specDropdown.setBackground(Color.WHITE); specDropdown.setFont(inputFont);

        JLabel lblBaker = new JLabel("Assign Baker:"); lblBaker.setFont(labelFont); lblBaker.setForeground(labelColor);
        bakerDropdown = new JComboBox<>(); bakerDropdown.setBackground(Color.WHITE); bakerDropdown.setFont(inputFont);

        formFieldsPanel.add(lblId);     formFieldsPanel.add(orderIdField);
        formFieldsPanel.add(lblCust);   formFieldsPanel.add(customerDropdown);
        formFieldsPanel.add(new JLabel("Details:")); formFieldsPanel.add(detailsScrollPane);
        formFieldsPanel.add(lblItem);   formFieldsPanel.add(itemNameField);
        formFieldsPanel.add(lblQty);    formFieldsPanel.add(qtyField);
        formFieldsPanel.add(lblNotes);  formFieldsPanel.add(notesField);
        formFieldsPanel.add(lblStatus); formFieldsPanel.add(statusDropdown);
        formFieldsPanel.add(lblSpec);   formFieldsPanel.add(specDropdown);
        formFieldsPanel.add(lblBaker);  formFieldsPanel.add(bakerDropdown);

        JButton submitBtn = new JButton("DISPATCH NEW PRODUCTION ORDER");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setBackground(new Color(34, 112, 63)); 
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.setMaximumSize(new Dimension(380, 38));
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = new JButton("↩ Return to Dashboard");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        backBtn.setForeground(new Color(110, 110, 110)); 
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(brandTitle);
        cardPanel.add(Box.createVerticalStrut(4));
        cardPanel.add(dateTimeField);
        cardPanel.add(brandSubtitle);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(formFieldsPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(submitBtn);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(backBtn);

        add(cardPanel, new GridBagConstraints());

        backBtn.addActionListener(e -> app.showScreen("ManagerDashboard"));
        specDropdown.addActionListener(e -> refreshBakerDropdown());
        customerDropdown.addActionListener(e -> updateCustomerDetails());

        submitBtn.addActionListener(e -> {
            
            updateDisplayTime(); 
            String orderId = orderIdField.getText().trim();
            String selected = (String) customerDropdown.getSelectedItem();
            if (selected == null) return;
            String customerName = selected.contains(" - ") ? selected.split(" - ")[1] : selected;
            
            String item = itemNameField.getText().trim();
            String qty = qtyField.getText().trim();
            String notes = notesField.getText().trim();
            String status = (String) statusDropdown.getSelectedItem();
            String assignedBaker = (String) bakerDropdown.getSelectedItem();

            if (orderId.isEmpty() || item.isEmpty() || qty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fulfill mandatory form fields.", "Input Mismatch", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (assignedBaker == null || assignedBaker.contains("No")) {
                JOptionPane.showMessageDialog(this, "Cannot assign order. No matching bakers found.", "Assignment Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Order newOrder = new Order(orderId, customerName, item, qty, (notes.isEmpty() ? "None" : notes), status, assignedBaker);
            FileHandler.saveOrder(newOrder);
            
            JOptionPane.showMessageDialog(this, "Order " + orderId + " successfully dispatched at " + newOrder.getTimestamp() + "!");
            itemNameField.setText(""); qtyField.setText(""); notesField.setText("");
            updateOrderId(); 
        });

        reloadCustomerDropdown();
        refreshBakerDropdown();
        updateOrderId();
        
        
        Main.applyTheme(this);
    }

    private void updateDisplayTime() {
        dateTimeField.setText("Dispatch Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    private void updateOrderId() {
        int nextId = FileHandler.getNextId("orders.txt");
        orderIdField.setText(String.format("ORD-%04d", nextId));
    }

    public void reloadCustomerDropdown() {
        customerDropdown.removeAllItems();
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("---") || line.trim().isEmpty()) continue;
                String[] cData = line.split(",");
                if (cData.length >= 2) customerDropdown.addItem(cData[0].trim() + " - " + cData[1].trim());
            }
        } catch (Exception e) { System.err.println("Error loading customers: " + e.getMessage()); }
    }

    private void updateCustomerDetails() {
        String selected = (String) customerDropdown.getSelectedItem();
        if (selected == null) return;
        
        String idToFind = selected.split(" - ")[0];
        try (BufferedReader br = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] cData = line.split(",");
                if (cData.length >= 6 && cData[0].trim().equals(idToFind)) {
                    customerDetailsArea.setText("Email: " + cData[3] + "\nPhone: " + cData[4] + "\nAddress: " + cData[5]);
                    customerDetailsArea.setCaretPosition(0); 
                    return;
                }
            }
        } catch (Exception e) { customerDetailsArea.setText("Error loading details"); }
    }

    public void refreshBakerDropdown() {
        bakerDropdown.removeAllItems();
        String selectedSpec = (String) specDropdown.getSelectedItem();
        List<String> bakers = FileHandler.getBakersBySpecialization(selectedSpec);
        if (bakers.isEmpty()) {
            bakerDropdown.addItem("No " + selectedSpec + " Bakers Available");
        } else {
            for (String b : bakers) bakerDropdown.addItem(b);
        }
    }
}