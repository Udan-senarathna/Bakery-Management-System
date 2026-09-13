package gui;

import main.Main;
import service.FileHandler;
import java.io.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SignupGUI extends JPanel {
    private Main app;
    private JTextField nicField, firstNameField, lastNameField, emailField, addressField, usernameField;
    private JPasswordField passwordField;
    private JTextArea qualificationsArea; 
    private JComboBox<String> roleBox;
    private JComboBox<String> specializationBox;
    private JLabel specLabel;
    private JCheckBox showPasswordCheck;

    public SignupGUI(Main app) {
        this.app = app;
        
        setBackground(new Color(245, 242, 235));
        setLayout(new GridBagLayout());

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        Border lineBorder = BorderFactory.createLineBorder(new Color(220, 215, 205), 1);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, BorderFactory.createEmptyBorder(25, 35, 25, 35)));

        JLabel brandTitle = new JLabel("GREEN LEAF BAKERY");
        brandTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandTitle.setForeground(new Color(34, 112, 63));
        brandTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        
        JPanel formFieldsPanel = new JPanel(new GridBagLayout());
        formFieldsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        usernameField = new JTextField(15);
        nicField = new JTextField(15);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        emailField = new JTextField(15);
        addressField = new JTextField(15);
        passwordField = new JPasswordField(15);
        
        qualificationsArea = new JTextArea(3, 15);
        qualificationsArea.setLineWrap(true);
        JScrollPane qualScroll = new JScrollPane(qualificationsArea);
        
        showPasswordCheck = new JCheckBox("Show Password");
        showPasswordCheck.setBackground(Color.WHITE);
        
        roleBox = new JComboBox<>();
        specLabel = new JLabel("Baker Specialization:");
        specializationBox = new JComboBox<>(new String[]{"Cakes", "Breads", "Pastries"});

        refreshRoleOptions();

        // Helper to add rows to GridBagLayout
        int row = 0;
        addFormRow(formFieldsPanel, "Choose Username:", usernameField, gbc, row++);
        addFormRow(formFieldsPanel, "NIC Number:", nicField, gbc, row++);
        addFormRow(formFieldsPanel, "First Name:", firstNameField, gbc, row++);
        addFormRow(formFieldsPanel, "Last Name:", lastNameField, gbc, row++);
        addFormRow(formFieldsPanel, "Email:", emailField, gbc, row++);
        addFormRow(formFieldsPanel, "Address:", addressField, gbc, row++);
        addFormRow(formFieldsPanel, "Qualifications:", qualScroll, gbc, row++);
        addFormRow(formFieldsPanel, "Assign Password:", passwordField, gbc, row++);
        
        
        gbc.gridx = 0; gbc.gridy = row++;
        formFieldsPanel.add(showPasswordCheck, gbc);
        
        gbc.gridx = 0; gbc.gridy = row++;
        formFieldsPanel.add(new JLabel("Select Workplace Access Group:"), gbc);
        gbc.gridx = 1; 
        formFieldsPanel.add(roleBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = row++;
        formFieldsPanel.add(specLabel, gbc);
        gbc.gridx = 1;
        formFieldsPanel.add(specializationBox, gbc);

        
        JButton registerBtn = new JButton("REGISTER SYSTEM ACCOUNT");
        registerBtn.setBackground(new Color(34, 112, 63));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        registerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton backBtn = new JButton("BACK TO LOGIN");
        backBtn.setBackground(new Color(120, 120, 120)); 
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cardPanel.add(brandTitle);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(formFieldsPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(registerBtn);
        cardPanel.add(Box.createVerticalStrut(10)); 
        cardPanel.add(backBtn);

        add(cardPanel, new GridBagConstraints());

        
        showPasswordCheck.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheck.isSelected() ? (char) 0 : '•');
        });

        roleBox.addActionListener(e -> updateSpecializationVisibility());

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleBox.getSelectedItem();
            String nic = nicField.getText().trim();
            String fName = firstNameField.getText().trim();
            String lName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String addr = addressField.getText().trim();
            String quals = qualificationsArea.getText().trim().replace("\n", " ").replace(",", ";");
            
            String fileName = role.equals("Manager") ? "manager.txt" : "baker.txt";
            String idPrefix = role.equals("Manager") ? "MGR-" : "BAR-";
            
            if ("Manager".equals(role) && FileHandler.isManagerAlreadyRegistered()) {
                JOptionPane.showMessageDialog(this, "A Manager account already exists.");
                return;
            }

            String spec = (role != null && role.equals("Baker")) ? (String) specializationBox.getSelectedItem() : "None";

            if (username.isEmpty() || password.isEmpty() || nic.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
                return;
            }
            
            int nextIdNum = FileHandler.getNextId(fileName);
            String assignedId = idPrefix + String.format("%04d", nextIdNum);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
                writer.write(assignedId + "," + username + "," + password + "," + role + "," + spec + "," + nic + "," + fName + "," + lName + "," + email + "," + addr + "," + quals);
                writer.newLine();
                JOptionPane.showMessageDialog(this, "Account created! ID: " + assignedId);
                app.showScreen("LoginScreen");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> app.showScreen("LoginScreen"));
        Main.applyTheme(this);
    }

    
    private void addFormRow(JPanel panel, String labelText, Component field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    public void refreshRoleOptions() {
        roleBox.removeAllItems();
        roleBox.addItem("Baker");
        if (!FileHandler.isManagerAlreadyRegistered()) {
            roleBox.insertItemAt("Manager", 0);
        }
        updateSpecializationVisibility();
    }

    private void updateSpecializationVisibility() {
        boolean isBaker = roleBox.getSelectedItem() != null && roleBox.getSelectedItem().equals("Baker");
        specLabel.setVisible(isBaker);
        specializationBox.setVisible(isBaker);
        revalidate();
        repaint();
    }
}