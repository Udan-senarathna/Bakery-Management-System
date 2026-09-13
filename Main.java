package main;

import gui.*;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    
    public static boolean isDarkMode = false;

    
    public static void applyTheme(Container container) {
        Color bgColor = isDarkMode ? new Color(40, 40, 40) : new Color(245, 242, 235);
        Color textColor = isDarkMode ? Color.WHITE : Color.BLACK;
        Color btnColor = isDarkMode ? new Color(70, 70, 70) : Color.WHITE;
        Color btnText = isDarkMode ? Color.WHITE : new Color(34, 112, 63);

        container.setBackground(bgColor);

        for (Component c : container.getComponents()) {
            if (c instanceof JPanel || c instanceof JScrollPane || c instanceof JViewport) {
                applyTheme((Container) c); 
            } else if (c instanceof JLabel) {
                c.setForeground(textColor);
            } else if (c instanceof JButton) {
                c.setBackground(btnColor);
                c.setForeground(btnText);
            } else if (c instanceof JTextField || c instanceof JTextArea || c instanceof JComboBox) {
                c.setBackground(isDarkMode ? new Color(60, 60, 60) : Color.WHITE);
                c.setForeground(textColor);
            }
        }
    }

    
    public void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme(mainContainer);
        mainContainer.revalidate();
        mainContainer.repaint();
    }

    private CardLayout cardLayout;
    private JPanel mainContainer;
    private String loggedInUser; 
    
    private OrderFormGUI orderFormPanel;
    private SignupGUI signupPanel;

    public Main() {
        setTitle("Green Leaf Bakery System");
        setSize(850, 650); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        
        orderFormPanel = new OrderFormGUI(this); 
        signupPanel = new SignupGUI(this); 

        mainContainer.add(new LoginGUI(this), "LoginScreen");
        mainContainer.add(signupPanel, "SignupScreen");
        mainContainer.add(new ManagerDashboard(this), "ManagerDashboard");
        mainContainer.add(orderFormPanel, "OrderFormScreen"); 
        mainContainer.add(new BakerDashboard(this), "BakerDashboard");
        mainContainer.add(new CustomerInvoiceGUI(this), "CustomerInvoiceScreen");

        add(mainContainer);
        showScreen("LoginScreen");
    }

    public void setLoggedInUser(String username) { this.loggedInUser = username; }
    public String getLoggedInUser() { return this.loggedInUser; }

    public void showScreen(String screenName) {
        if (screenName.equalsIgnoreCase("OrderFormScreen")) {
            if (orderFormPanel != null) {
                orderFormPanel.reloadCustomerDropdown(); 
                orderFormPanel.refreshBakerDropdown();   
            }
        }
        
        if (screenName.equalsIgnoreCase("SignupScreen")) {
            if (signupPanel != null) {
                signupPanel.refreshRoleOptions();
            }
        }
        
        cardLayout.show(mainContainer, screenName);
        
        
        applyTheme(mainContainer);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}