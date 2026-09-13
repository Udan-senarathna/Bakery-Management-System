package gui;

import main.Main;
import service.FileHandler;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LoginGUI extends JPanel {
    private Main app;
    private JTextField userField;
    private JPasswordField passField;
    private JComboBox<String> roleBox;
    private JCheckBox showPasswordCheck; 

    public LoginGUI(Main app) {
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

        JLabel brandSubtitle = new JLabel("Secure Employee Access Gateway");
        brandSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        brandSubtitle.setForeground(new Color(130, 130, 130));
        brandSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formFieldsPanel = new JPanel();
        formFieldsPanel.setBackground(Color.WHITE);
        formFieldsPanel.setLayout(new GridLayout(7, 1, 0, 4));
        formFieldsPanel.setMaximumSize(new Dimension(280, 240)); 

        Border fieldBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        );

        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Color labelColor = new Color(70, 75, 70);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(labelFont);
        userField = new JTextField();
        userField.setBorder(fieldBorder);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JLabel passLabel = new JLabel("Security Password:");
        passLabel.setFont(labelFont);
        passField = new JPasswordField();
        passField.setBorder(fieldBorder);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        showPasswordCheck = new JCheckBox("Show Password");
        showPasswordCheck.setBackground(Color.WHITE);
        showPasswordCheck.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        showPasswordCheck.setForeground(labelColor);
        showPasswordCheck.setFocusPainted(false);

        JLabel roleLabel = new JLabel("Select Account Access Profile Type:");
        roleLabel.setFont(labelFont);
        roleBox = new JComboBox<>(new String[]{"Manager", "Baker"});
        roleBox.setBackground(Color.WHITE);
        roleBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        formFieldsPanel.add(userLabel);     
        formFieldsPanel.add(userField);
        formFieldsPanel.add(passLabel);     
        formFieldsPanel.add(passField);
        formFieldsPanel.add(showPasswordCheck); 
        formFieldsPanel.add(roleLabel);     
        formFieldsPanel.add(roleBox);

        JButton loginBtn = new JButton("SECURE SYSTEM LOGIN");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(new Color(34, 112, 63));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setMaximumSize(new Dimension(280, 38));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton gotoSignupBtn = new JButton("Don't have an account? Go to Signup");
        gotoSignupBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        gotoSignupBtn.setForeground(new Color(110, 110, 110)); 
        gotoSignupBtn.setContentAreaFilled(false);
        gotoSignupBtn.setBorderPainted(false);
        gotoSignupBtn.setFocusPainted(false);
        gotoSignupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gotoSignupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(brandTitle);
        cardPanel.add(Box.createVerticalStrut(4));
        cardPanel.add(brandSubtitle);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(formFieldsPanel);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(loginBtn);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(gotoSignupBtn);

        add(cardPanel, new GridBagConstraints());

        showPasswordCheck.addActionListener(e -> {
            if (showPasswordCheck.isSelected()) {
                passField.setEchoChar((char) 0); 
            } else {
                passField.setEchoChar('•'); 
            }
        });

        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            String selectedRole = (String) roleBox.getSelectedItem(); 

            
            boolean isValidUser = FileHandler.validateUser(user, pass, selectedRole);

            if (isValidUser) {
                app.setLoggedInUser(user); 
                FileHandler.logLogin(user, selectedRole);
                
                JOptionPane.showMessageDialog(this, "Access Granted! Welcome back " + user);
                userField.setText("");
                passField.setText("");
                showPasswordCheck.setSelected(false);
                passField.setEchoChar('•');

                if (selectedRole.equalsIgnoreCase("Manager")) {
                    app.showScreen("ManagerDashboard");
                } else if (selectedRole.equalsIgnoreCase("Baker")) {
                    app.showScreen("BakerDashboard");
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Authentication Failed! Invalid details matching a " + selectedRole + " profile.", 
                    "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        });

        gotoSignupBtn.addActionListener(e -> app.showScreen("SignupScreen"));

        
        Main.applyTheme(this);
    }
}