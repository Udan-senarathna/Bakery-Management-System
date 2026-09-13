package gui;

import main.Main;
import model.PerparationReport;
import service.FileHandler;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class ManagerDashboard extends JPanel {
    private JLabel capacityLabel;
    private JLabel bakerCountLabel, customerCountLabel, orderCountLabel, completedOrdersCountLabel, dispatchedCountLabel;

    public ManagerDashboard(Main app) {
        setBackground(new Color(245, 242, 235));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        
        JPanel topContainer = new JPanel(new BorderLayout(0, 10));
        topContainer.setOpaque(false);

        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JPanel titleBox = new JPanel();
        titleBox.setOpaque(false);
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("GREEN LEAF BAKERY");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(34, 112, 63));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Manager Central Administrative Command Center");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleBox.add(titleLabel);
        titleBox.add(Box.createVerticalStrut(5));
        titleBox.add(subtitleLabel);

        
        JToggleButton themeToggle = new JToggleButton(Main.isDarkMode ? "🌙 Dark Mode" : "☀️ Toggle Theme");
        themeToggle.addActionListener(e -> {
            app.toggleTheme(); 
            themeToggle.setText(Main.isDarkMode ? "🌙 Dark Mode" : "☀️ Toggle Theme");
        });

        headerPanel.add(titleBox, BorderLayout.CENTER);
        headerPanel.add(themeToggle, BorderLayout.EAST);

        
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        statsPanel.setOpaque(false);
        
        bakerCountLabel = new JLabel("0");
        customerCountLabel = new JLabel("0");
        orderCountLabel = new JLabel("0");
        completedOrdersCountLabel = new JLabel("0");
        dispatchedCountLabel = new JLabel("0");
        
        statsPanel.add(createStatTile("Total Bakers", bakerCountLabel, new Color(230, 245, 233)));
        statsPanel.add(createStatTile("Total Customers", customerCountLabel, new Color(232, 240, 254)));
        statsPanel.add(createStatTile("Total Orders", orderCountLabel, new Color(255, 243, 224)));
        statsPanel.add(createStatTile("Completed", completedOrdersCountLabel, new Color(230, 230, 250)));
        statsPanel.add(createStatTile("Dispatched", dispatchedCountLabel, new Color(224, 247, 250)));

        
        JPanel capacityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        capacityPanel.setBackground(new Color(255, 240, 240));
        capacityPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 180, 180), 1));
        
        capacityLabel = new JLabel();
        capacityLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        capacityLabel.setForeground(new Color(175, 65, 65));
        
        JButton refreshCapacityBtn = new JButton("🔄");
        refreshCapacityBtn.setToolTipText("Refresh Data");
        refreshCapacityBtn.setMargin(new Insets(2, 5, 2, 5));
        refreshCapacityBtn.addActionListener(e -> {
            refreshCapacityDisplay();
            refreshStats();
        });
        
        capacityPanel.add(capacityLabel);
        capacityPanel.add(refreshCapacityBtn);

        topContainer.add(headerPanel, BorderLayout.NORTH);
        topContainer.add(statsPanel, BorderLayout.CENTER);
        topContainer.add(capacityPanel, BorderLayout.SOUTH);
        
        add(topContainer, BorderLayout.NORTH);

        
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 15, 15)); 
        gridPanel.setOpaque(false);

        JButton openCRMBtn = createTileButton("📁", "Invoice CRM", "Manage billing");
        JButton openOrdersBtn = createTileButton("🛒", "System Order Desk", "Launch new orders");
        JButton manageStockBtn = createTileButton("📦", "Manage Stock", "Update ingredients");
        JButton viewReportsBtn = createTileButton("📊", "Production Reports", "View stats");
        JButton instructionsBtn = createTileButton("📝", "Daily Instructions", "Staff notes");
        JButton manageSystemBtn = createTileButton("⚙️", "Manage Database", "Users & Data"); 

        gridPanel.add(openCRMBtn);
        gridPanel.add(openOrdersBtn);
        gridPanel.add(manageStockBtn);
        gridPanel.add(viewReportsBtn);
        gridPanel.add(instructionsBtn);
        gridPanel.add(manageSystemBtn);
        add(gridPanel, BorderLayout.CENTER);

        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);

        JButton logoutBtn = new JButton("🚪 End Work Session");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(175, 65, 65));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(180, 35));
        footerPanel.add(logoutBtn);
        add(footerPanel, BorderLayout.SOUTH);

        
        openCRMBtn.addActionListener(e -> app.showScreen("CustomerInvoiceScreen"));
        openOrdersBtn.addActionListener(e -> app.showScreen("OrderFormScreen"));
        manageStockBtn.addActionListener(e -> showStockForm());
        instructionsBtn.addActionListener(e -> showInstructionsForm());
        manageSystemBtn.addActionListener(e -> showDatabaseOptions());

        viewReportsBtn.addActionListener(e -> {
            String rawData = FileHandler.readFullFile("preparation_reports.txt");
            if (rawData == null || rawData.trim().isEmpty() || rawData.contains("No")) {
                JOptionPane.showMessageDialog(this, "No production reports found.");
                return;
            }
            StringBuilder displayData = new StringBuilder();
            displayData.append(String.format("%-10s | %-10s | %-12s | %-15s | %-8s | %-8s\n", 
                    "Rep ID", "Order ID", "Baker", "Status", "Time", "Quality"));
            displayData.append("------------------------------------------------------------------------------------------\n");
            for (String line : rawData.split("\n")) {
                if (!line.trim().isEmpty()) {
                    PerparationReport report = PerparationReport.fromFileString(line);
                    if (report != null) {
                        displayData.append(String.format("%-10s | %-10s | %-12s | %-15s | %-8s | %-8s\n", 
                                report.getReportId(), report.getOrderId(), report.getBakerName(), 
                                report.getStatus(), report.getPrepTime(), report.getQuality()));
                    }
                }
            }
            JTextArea area = new JTextArea(15, 60);
            area.setText(displayData.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Production Reports Log", JOptionPane.PLAIN_MESSAGE);
        });

        logoutBtn.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Are you sure?") == JOptionPane.YES_OPTION) app.showScreen("LoginScreen");
        });
        
        refreshCapacityDisplay();
        refreshStats();
        
        
        Main.applyTheme(this);
    }

    private JPanel createStatTile(String title, JLabel valueLabel, Color bg) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 210, 200), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8))); 
        p.setBackground(bg);
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        p.add(titleLbl);
        p.add(Box.createVerticalStrut(2));
        p.add(valueLabel);
        return p;
    }

    private void refreshStats() {
        bakerCountLabel.setText(String.valueOf(getFileLines("baker.txt").size()));
        customerCountLabel.setText(String.valueOf(getFileLines("customers.txt").size()));
        orderCountLabel.setText(String.valueOf(getFileLines("orders.txt").size()));
        completedOrdersCountLabel.setText(String.valueOf(countPreparationReportsByStatus("Finished & Completed")));
        dispatchedCountLabel.setText(String.valueOf(countInvoicesByStatus("DISPATCHED")));
    }

    private int countPreparationReportsByStatus(String status) {
        int count = 0;
        List<String> lines = getFileLines("preparation_reports.txt");
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            PerparationReport report = PerparationReport.fromFileString(line);
            if (report != null && status.equalsIgnoreCase(report.getStatus())) {
                count++;
            }
        }
        return count;
    }

    private int countInvoicesByStatus(String status) {
        int count = 0;
        List<String> lines = getFileLines("invoices.txt");
        for (String line : lines) {
            if (!line.trim().isEmpty() && line.contains("," + status)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        refreshStats();
        refreshCapacityDisplay();
    }

    private void showDatabaseOptions() {
        String[] options = {"Manage Customers", "Manage Bakers"};
        int choice = JOptionPane.showOptionDialog(this, "Select a database to edit:", "Database Admin", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 0) openCustomerManagement();
        else if (choice == 1) openBakerManagement();
    }

    private void openCustomerManagement() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Manage Customers", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 400);
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String s : getFileLines("customers.txt")) model.addElement(s);
        JList<String> list = new JList<>(model);
        JPanel btnPanel = new JPanel();
        JButton regBtn = new JButton("Register New"), editBtn = new JButton("Edit Selected"), delBtn = new JButton("Delete");
        
        regBtn.addActionListener(e -> {
            JTextField fName = new JTextField(), lName = new JTextField(), phone = new JTextField(), email = new JTextField(), addr = new JTextField();
            Object[] msg = {"First Name:", fName, "Last Name:", lName, "Phone:", phone, "Email:", email, "Address:", addr};
            if (JOptionPane.showConfirmDialog(dialog, msg, "Register Customer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                // FIXED: Use FileHandler to get the next ID securely
                int nextIdNum = FileHandler.getNextId("customers.txt");
                String customerId = String.format("CUST-%04d", nextIdNum);
                String line = customerId + "," + fName.getText() + "," + lName.getText() + "," + phone.getText() + "," + email.getText() + "," + addr.getText();
                model.addElement(line); saveListToFile(model, "customers.txt");
            }
        });
        editBtn.addActionListener(e -> {
            if (list.getSelectedValue() == null) return;
            String[] p = list.getSelectedValue().split(",");
            JTextField fName = new JTextField(p.length > 1 ? p[1] : ""), lName = new JTextField(p.length > 2 ? p[2] : ""), phone = new JTextField(p.length > 3 ? p[3] : ""), email = new JTextField(p.length > 4 ? p[4] : ""), addr = new JTextField(p.length > 5 ? p[5] : "");
            Object[] msg = {"First Name:", fName, "Last Name:", lName, "Phone:", phone, "Email:", email, "Address:", addr};
            if (JOptionPane.showConfirmDialog(dialog, msg, "Edit Customer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                model.set(list.getSelectedIndex(), p[0] + "," + fName.getText() + "," + lName.getText() + "," + phone.getText() + "," + email.getText() + "," + addr.getText());
                saveListToFile(model, "customers.txt");
            }
        });
        delBtn.addActionListener(e -> { if(list.getSelectedIndex() != -1) { model.remove(list.getSelectedIndex()); saveListToFile(model, "customers.txt"); }});
        btnPanel.add(regBtn); btnPanel.add(editBtn); btnPanel.add(delBtn);
        dialog.add(new JScrollPane(list), BorderLayout.CENTER); dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this); dialog.setVisible(true);
    }

    private void openBakerManagement() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Manage Bakers", true);
        dialog.setLayout(new FlowLayout()); dialog.setSize(350, 150);
        JComboBox<String> bakerDropdown = new JComboBox<>();
        for (String line : getFileLines("baker.txt")) if (!line.trim().isEmpty()) bakerDropdown.addItem(line.split(",")[0].trim());
        JButton editBtn = new JButton("Edit"), delBtn = new JButton("Delete");
        
        editBtn.addActionListener(e -> {
            String selectedId = (String) bakerDropdown.getSelectedItem();
            if(selectedId == null) return;
            int indexToUpdate = -1; String[] currentData = null; List<String> allLines = getFileLines("baker.txt");
            for (int i = 0; i < allLines.size(); i++) { if (allLines.get(i).startsWith(selectedId + ",")) { indexToUpdate = i; currentData = allLines.get(i).split(","); break; } }
            if (indexToUpdate != -1) {
                JTextField fName = new JTextField(currentData.length > 6 ? currentData[6] : ""), lName = new JTextField(currentData.length > 7 ? currentData[7] : ""), email = new JTextField(currentData.length > 8 ? currentData[8] : ""), pass = new JTextField(currentData.length > 2 ? currentData[2] : "");
                Object[] message = {"First Name:", fName, "Last Name:", lName, "Email:", email, "Password:", pass};
                if (JOptionPane.showConfirmDialog(dialog, message, "Edit Baker", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    if (currentData.length > 6) currentData[6] = fName.getText(); if (currentData.length > 7) currentData[7] = lName.getText(); if (currentData.length > 8) currentData[8] = email.getText(); if (currentData.length > 2) currentData[2] = pass.getText();
                    allLines.set(indexToUpdate, String.join(",", currentData));
                    try (PrintWriter pw = new PrintWriter(new FileWriter("baker.txt"))) { for (String line : allLines) pw.println(line); JOptionPane.showMessageDialog(dialog, "Updated!"); dialog.dispose(); } catch (IOException ex) { JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage()); }
                }
            }
        });
        delBtn.addActionListener(e -> {
            String selected = (String)bakerDropdown.getSelectedItem();
            if(selected == null) return;
            if (JOptionPane.showConfirmDialog(dialog, "Delete Baker " + selected + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                List<String> allLines = getFileLines("baker.txt");
                allLines.removeIf(line -> line.startsWith(selected + ","));
                try (PrintWriter pw = new PrintWriter(new FileWriter("baker.txt"))) { for (String line : allLines) pw.println(line); JOptionPane.showMessageDialog(dialog, "Deleted."); dialog.dispose(); } catch (IOException ex) { JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage()); }
            }
        });
        dialog.add(new JLabel("Select Baker ID:")); dialog.add(bakerDropdown); dialog.add(editBtn); dialog.add(delBtn);
        dialog.setLocationRelativeTo(this); dialog.setVisible(true);
    }

    private List<String> getFileLines(String filename) {
        File f = new File(filename);
        try { if (f.exists()) return Files.readAllLines(f.toPath()); } catch (IOException e) { e.printStackTrace(); }
        return new ArrayList<>();
    }
    private void saveListToFile(DefaultListModel<String> model, String filename) { try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) { for (int i = 0; i < model.size(); i++) pw.println(model.get(i)); } catch (IOException e) { e.printStackTrace(); } }
    
    private void showStockForm() {
        String currentData = FileHandler.readFullFile("stock.txt");
        if (currentData.contains("No records") || currentData.trim().isEmpty()) currentData = "Flour: 0\nSugar: 0\nEggs: 0\nButter: 0\nMilk: 0";
        List<String> ingredientNames = new ArrayList<>(); List<JTextField> textFields = new ArrayList<>(); JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        for (String line : currentData.split("\n")) { if (line.contains(":")) { String[] parts = line.split(":", 2); formPanel.add(new JLabel(parts[0].trim() + ":")); JTextField field = new JTextField(parts[1].trim()); formPanel.add(field); ingredientNames.add(parts[0].trim()); textFields.add(field); } }
        if (JOptionPane.showConfirmDialog(this, formPanel, "Update Stock", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            StringBuilder newData = new StringBuilder(); for (int i = 0; i < ingredientNames.size(); i++) newData.append(ingredientNames.get(i)).append(": ").append(textFields.get(i).getText()).append("\n");
            FileHandler.saveStock(newData.toString()); JOptionPane.showMessageDialog(this, "Updated!");
        }
    }
    
    private void showInstructionsForm() {
        File file = new File("instructions.txt"); String content = "";
        if (file.exists()) { try (Scanner scanner = new Scanner(file)) { while (scanner.hasNextLine()) content += scanner.nextLine() + "\n"; } catch (FileNotFoundException e) { e.printStackTrace(); } }
        JTextArea textArea = new JTextArea(10, 30); textArea.setText(content);
        if (JOptionPane.showConfirmDialog(this, new JScrollPane(textArea), "Update Instructions", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try (FileWriter writer = new FileWriter("instructions.txt")) { writer.write(textArea.getText()); JOptionPane.showMessageDialog(this, "Saved!"); } catch (IOException e) { JOptionPane.showMessageDialog(this, "Error!"); }
        }
    }

    public void refreshCapacityDisplay() {
        String capacityText = FileHandler.readFullFile("production_capacity.txt");
        if (capacityText == null || capacityText.contains("No records") || capacityText.trim().isEmpty()) capacityLabel.setText("⚠️ Kitchen Production Limit: Not set");
        else capacityLabel.setText("Kitchen Limits: " + capacityText.replace("\n", " | "));
    }

    private JButton createTileButton(String icon, String title, String sub) {
        JButton btn = new JButton("<html><center><font size='5'>" + icon + "</font><br><b>" + title + 
                                            "</b><br><font size='2' color='#888888'>" + sub + "</font></center></html>");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13)); 
        btn.setBackground(Color.WHITE); 
        btn.setForeground(new Color(34, 112, 63)); 
        btn.setFocusPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 210, 200), 1), 
                BorderFactory.createEmptyBorder(15, 10, 15, 10)));
        return btn;
    }
}