package gui;

import main.Main;
import model.PerparationReport; 
import service.FileHandler;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BakerDashboard extends JPanel {
    private Main app;
    private JTextArea orderFeedArea;
    private JTextArea instructionsArea;
    private JPanel titleTextPanel; 
    private JTextField reportIdField, flourField, sugarField, eggsField, prepTimeField, orderIdField; 
    private JComboBox<String> stateDropdown, qualityDropdown; 
    private JTextArea notesInputField;
    private String currentBaker; 

    public BakerDashboard(Main app) {
        this.app = app;
        if (app != null && app.getLoggedInUser() != null) {
            this.currentBaker = app.getLoggedInUser().trim();
        } else {
            this.currentBaker = "Baker";
        }
        
        setBackground(new Color(245, 242, 235));
        setLayout(new BorderLayout(15, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        Border lineBorder = BorderFactory.createLineBorder(new Color(215, 210, 200), 1);

        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false); 
        
        titleTextPanel = new JPanel();
        titleTextPanel.setOpaque(false);
        titleTextPanel.setLayout(new BoxLayout(titleTextPanel, BoxLayout.Y_AXIS));
        
        updateHeader();

        
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setOpaque(false);

        JButton refreshBtn = new JButton("🔄 Refresh Station");
        refreshBtn.setBackground(new Color(34, 112, 63));
        refreshBtn.setForeground(Color.WHITE);

        
        JToggleButton themeToggle = new JToggleButton(Main.isDarkMode ? "☀️ Light Mode" : "🌙 Dark Mode");
        themeToggle.addActionListener(e -> {
            app.toggleTheme(); 
            themeToggle.setText(Main.isDarkMode ? "☀️ Light Mode" : "🌙 Dark Mode");
        });

        buttonContainer.add(themeToggle);
        buttonContainer.add(refreshBtn);

        headerPanel.add(titleTextPanel, BorderLayout.WEST);
        headerPanel.add(buttonContainer, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        
        JPanel centerContainer = new JPanel(new BorderLayout(15, 15));
        centerContainer.setOpaque(false);

        instructionsArea = new JTextArea(4, 20);
        instructionsArea.setEditable(false);
        JScrollPane instrScroll = new JScrollPane(instructionsArea);
        instrScroll.setBorder(BorderFactory.createTitledBorder(lineBorder, " Manager's Daily Instructions "));
        centerContainer.add(instrScroll, BorderLayout.NORTH);

        orderFeedArea = new JTextArea();
        orderFeedArea.setEditable(false);
        JScrollPane orderScroll = new JScrollPane(orderFeedArea);
        orderScroll.setBorder(BorderFactory.createTitledBorder(lineBorder, " Your Active Orders "));
        centerContainer.add(orderScroll, BorderLayout.CENTER);

        
        JPanel reportFormPanel = new JPanel(new GridBagLayout());
        reportFormPanel.setBackground(Color.WHITE);
        JScrollPane formScrollPane = new JScrollPane(reportFormPanel);
        formScrollPane.setPreferredSize(new Dimension(380, 0)); 
        formScrollPane.setBorder(BorderFactory.createTitledBorder(lineBorder, " Baking Station Report "));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        reportIdField = new JTextField(); reportIdField.setEditable(false); generateRandomReportId();
        orderIdField = new JTextField(); 
        
        stateDropdown = new JComboBox<>(new String[]{"Baking Process Started", "Ready to Go / Packing", "Finished & Completed"});
        flourField = new JTextField(); sugarField = new JTextField(); eggsField = new JTextField(); prepTimeField = new JTextField();
        qualityDropdown = new JComboBox<>(new String[]{"Good", "Excellent", "Poor"});
        notesInputField = new JTextArea(3, 15);

        addFormRow(reportFormPanel, gbc, "Report ID:", reportIdField, 0);
        addFormRow(reportFormPanel, gbc, "Order ID:", orderIdField, 1);
        addFormRow(reportFormPanel, gbc, "State:", stateDropdown, 2);
        addFormRow(reportFormPanel, gbc, "Flour (g):", flourField, 3);
        addFormRow(reportFormPanel, gbc, "Sugar (g):", sugarField, 4);
        addFormRow(reportFormPanel, gbc, "Eggs (qty):", eggsField, 5);
        addFormRow(reportFormPanel, gbc, "Time (min):", prepTimeField, 6);
        addFormRow(reportFormPanel, gbc, "Quality:", qualityDropdown, 7);
        
        gbc.gridx = 0; gbc.gridy = 8; reportFormPanel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1; reportFormPanel.add(new JScrollPane(notesInputField), gbc);

        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 10)); 
        JButton sendReportBtn = new JButton("📤 Submit Progress Report");
        JButton capacityBtn = new JButton("Update Capacity Limit");
        JButton viewStockBtn = new JButton("📦 View Ingredient Stock");
        
        buttonPanel.add(sendReportBtn); buttonPanel.add(capacityBtn); buttonPanel.add(viewStockBtn);
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        reportFormPanel.add(buttonPanel, gbc);
        
        centerContainer.add(formScrollPane, BorderLayout.EAST);
        add(centerContainer, BorderLayout.CENTER);

        
        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to log out?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION && app != null) {
                app.showScreen("LoginScreen");
            }
        });
        add(logoutBtn, BorderLayout.SOUTH);

        
        sendReportBtn.addActionListener(e -> {
            String typedOrderId = orderIdField.getText().trim();
            if (typedOrderId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an Order ID.");
                return;
            }
            
            PerparationReport report = new PerparationReport(
                reportIdField.getText(), 
                typedOrderId, 
                currentBaker, 
                (String) stateDropdown.getSelectedItem(), 
                flourField.getText(), 
                sugarField.getText(), 
                eggsField.getText(), 
                notesInputField.getText(), 
                prepTimeField.getText(), 
                (String) qualityDropdown.getSelectedItem()
            );
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("preparation_reports.txt", true))) {
                writer.write(report.toFileString()); writer.newLine();
                JOptionPane.showMessageDialog(this, "Submitted!");
                generateRandomReportId();
                orderIdField.setText(""); 
                refreshData(); 
            } catch (IOException ex) { 
                JOptionPane.showMessageDialog(this, "Error saving: " + ex.getMessage()); 
            }
        });

        capacityBtn.addActionListener(e -> openCapacityWindow());
        viewStockBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, FileHandler.readStock()));
        refreshBtn.addActionListener(e -> refreshData());
        refreshData();

        
        Main.applyTheme(this);
    }

    private void openCapacityWindow() {
        String[] categories = {"Cakes", "Breads", "Pastries"};
        Map<String, String> currentData = new HashMap<>();
        String fileContent = FileHandler.readFullFile("production_capacity.txt");
        if (fileContent != null) {
            for (String line : fileContent.split("\n")) {
                if (line.contains(":")) {
                    String[] parts = line.split(":");
                    currentData.put(parts[0].trim(), parts.length > 1 ? parts[1].trim() : "0");
                }
            }
        }

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        Map<String, JTextField> fields = new HashMap<>();

        for (String cat : categories) {
            JTextField field = new JTextField(currentData.getOrDefault(cat, "0"));
            formPanel.add(new JLabel(cat + " limit:"));
            formPanel.add(field);
            fields.put(cat, field);
        }

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Update Category Capacities", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (FileWriter fw = new FileWriter("production_capacity.txt", false)) {
                for (String cat : categories) fw.write(cat + ": " + fields.get(cat).getText() + "\n");
                JOptionPane.showMessageDialog(this, "Capacities updated!");
            } catch (IOException ex) { JOptionPane.showMessageDialog(this, "Error saving."); }
        }
    }

    private void updateHeader() {
        titleTextPanel.removeAll();
        JLabel titleLabel = new JLabel("GREEN LEAF BAKERY");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(34, 112, 63));
        titleTextPanel.add(titleLabel);
        
        titleTextPanel.revalidate();
        titleTextPanel.repaint();
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, String label, JComponent comp, int y) {
        gbc.gridx = 0; gbc.gridy = y; p.add(new JLabel(label), gbc);
        gbc.gridx = 1; p.add(comp, gbc);
    }

    private void generateRandomReportId() {
        reportIdField.setText("REP-" + ((int)(Math.random() * 9000) + 1000));
    }

    private Set<String> getCompletedOrderIds() {
        Set<String> completedIds = new HashSet<>();
        String reports = FileHandler.readFullFile("preparation_reports.txt");
        if (reports != null && !reports.isEmpty()) {
            for (String line : reports.split("\n")) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    completedIds.add(parts[1].trim());
                }
            }
        }
        return completedIds;
    }

    private void refreshData() {
        Set<String> completedIds = getCompletedOrderIds();
        String allOrders = FileHandler.readFullFile("orders.txt");
        StringBuilder activeOrders = new StringBuilder();

        if (allOrders != null && !allOrders.isEmpty()) {
            for (String line : allOrders.split("\n")) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length > 0 && !completedIds.contains(parts[0].trim())) {
                    activeOrders.append(line).append("\n");
                }
            }
        }

        orderFeedArea.setText(activeOrders.length() > 0 ? activeOrders.toString() : "All orders completed!");
        
        String inst = FileHandler.readFullFile("instructions.txt");
        instructionsArea.setText((inst != null && !inst.isEmpty()) ? inst : "No instructions.");
        
        updateHeader();
    }
}