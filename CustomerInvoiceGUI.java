package gui;

import main.Main;
import service.FileHandler;
import model.Invoice;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.print.PrinterException;
import java.time.LocalDate;

public class CustomerInvoiceGUI extends JPanel {
    private Main app;
    private JList<String> recordsList;
    private DefaultListModel<String> listModel;

    private JTextField iIdField, iAmountField, iCustomerNameField, updateIdField, searchField;
    private JComboBox<String> iOrderDropdown;

    public CustomerInvoiceGUI(Main app) {
        this.app = app;
        setBackground(new Color(245, 242, 235));
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Border lineBorder = BorderFactory.createLineBorder(new Color(215, 210, 200), 1);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 6, 5, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(245, 242, 235));
        leftPanel.setPreferredSize(new Dimension(360, 0));

        JPanel invPanel = new JPanel(new GridBagLayout());
        invPanel.setBackground(Color.WHITE);
        invPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, " Generate Order Invoices "));
        invPanel.setMaximumSize(new Dimension(360, 240)); 
        
        iIdField = new JTextField(12); iIdField.setEditable(false); 
        iAmountField = new JTextField(12); iCustomerNameField = new JTextField(12);
        
        iOrderDropdown = new JComboBox<>();
        populateOrderIds();

        iOrderDropdown.addActionListener(e -> {
            String selected = (String) iOrderDropdown.getSelectedItem();
            if (selected != null && !selected.equals("No Orders Found")) {
                iCustomerNameField.setText(findCustomerNameByOrderId(selected));
            } else {
                iCustomerNameField.setText("");
            }
        });

        JComboBox<String> iStatusBox = new JComboBox<>(new String[]{"PENDING", "PAID", "DISPATCHED"});
        JButton saveInvBtn = new JButton("Issue Bakery Invoice");
        saveInvBtn.setBackground(new Color(34, 112, 63)); saveInvBtn.setForeground(Color.WHITE);
        
        gbc.gridwidth=1; gbc.gridx=0; gbc.gridy=0; invPanel.add(new JLabel("Inv ID:"), gbc); gbc.gridx=1; invPanel.add(iIdField, gbc);
        gbc.gridx=0; gbc.gridy=1; invPanel.add(new JLabel("Order ID:"), gbc); gbc.gridx=1; invPanel.add(iOrderDropdown, gbc);
        gbc.gridx=0; gbc.gridy=2; invPanel.add(new JLabel("Customer:"), gbc); gbc.gridx=1; invPanel.add(iCustomerNameField, gbc);
        gbc.gridx=0; gbc.gridy=3; invPanel.add(new JLabel("Amount:"), gbc); gbc.gridx=1; invPanel.add(iAmountField, gbc);
        gbc.gridx=0; gbc.gridy=4; invPanel.add(new JLabel("Status:"), gbc); gbc.gridx=1; invPanel.add(iStatusBox, gbc);
        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=2; invPanel.add(saveInvBtn, gbc);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, " Quick Modifications "));
        actionPanel.setPreferredSize(new Dimension(360, 100)); 
        actionPanel.setMaximumSize(new Dimension(360, 100)); 
        
        updateIdField = new JTextField(10);
        JButton markPaidBtn = new JButton("Update Status");
        markPaidBtn.setBackground(new Color(34, 112, 63)); markPaidBtn.setForeground(Color.WHITE);
        
        actionPanel.add(new JLabel("ID: "));
        actionPanel.add(updateIdField);
        actionPanel.add(markPaidBtn);

        leftPanel.add(invPanel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(actionPanel);
        add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new BorderLayout(6, 6));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, " System Records "));
        
        JPanel topLogPanel = new JPanel(new BorderLayout(10, 10));
        topLogPanel.setBackground(Color.WHITE);
        
        JPanel logControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logControls.setBackground(Color.WHITE);
        JButton viewInvsBtn = new JButton("All");
        JButton viewDispatchedBtn = new JButton("Dispatched");
        JButton printBtn = new JButton("🖨️ Print Selected");
        
        logControls.add(new JLabel("View:"));
        logControls.add(viewInvsBtn);
        logControls.add(viewDispatchedBtn);
        logControls.add(printBtn);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBar.setBackground(Color.WHITE);
        searchField = new JTextField(10);
        JButton filterBtn = new JButton("🔍 Filter");
        searchBar.add(new JLabel("Search:")); searchBar.add(searchField); searchBar.add(filterBtn);
        
        topLogPanel.add(logControls, BorderLayout.WEST);
        topLogPanel.add(searchBar, BorderLayout.EAST);
        
        listModel = new DefaultListModel<>();
        recordsList = new JList<>(listModel);
        recordsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        rightPanel.add(topLogPanel, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(recordsList), BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setBackground(new Color(245, 242, 235));
        JButton backBtn = new JButton("↩ Back to Dashboard");
        bottomBar.add(backBtn);
        add(bottomBar, BorderLayout.SOUTH);

        refreshIds();
        
        saveInvBtn.addActionListener(e -> {
            try {
                String date = LocalDate.now().toString();
                String selectedOrder = (String) iOrderDropdown.getSelectedItem();
                
                if (selectedOrder == null || selectedOrder.equals("No Orders Found")) {
                    JOptionPane.showMessageDialog(this, "Please select a valid Order ID.");
                    return;
                }

                FileHandler.saveInvoice(new Invoice(iIdField.getText(), selectedOrder, iCustomerNameField.getText(), date, Double.parseDouble(iAmountField.getText()), (String)iStatusBox.getSelectedItem()));
                JOptionPane.showMessageDialog(this, "Invoice Generated!"); 
                showAllInvoices(); refreshIds();
                iCustomerNameField.setText(""); iAmountField.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        markPaidBtn.addActionListener(e -> {
            String id = updateIdField.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter an Invoice ID!"); return; }
            
            String[] options = {"PAID", "DISPATCHED"};
            String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Status", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            
            if (newStatus != null && FileHandler.updateInvoiceStatus(id, newStatus)) {
                JOptionPane.showMessageDialog(this, "Invoice " + id + " updated to " + newStatus + "!");
                showAllInvoices(); updateIdField.setText("");
            } else { JOptionPane.showMessageDialog(this, "Update failed or ID not found."); }
        });

        viewInvsBtn.addActionListener(e -> showAllInvoices());
        viewDispatchedBtn.addActionListener(e -> showDispatchedInvoices());
        
        printBtn.addActionListener(e -> {
            String selected = recordsList.getSelectedValue();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select an invoice from the list to print.");
                return;
            }
            try {
                JTextArea printArea = new JTextArea(formatInvoiceForPrinting(selected));
                printArea.print();
            } catch (PrinterException pe) {
                JOptionPane.showMessageDialog(this, "Print error: " + pe.getMessage());
            }
        });

        filterBtn.addActionListener(e -> {
            String kw = searchField.getText().toLowerCase();
            listModel.clear();
            String data = FileHandler.readFullFile("invoices.txt");
            if (data != null) {
                for (String line : data.split("\n")) {
                    if (!line.trim().isEmpty() && line.toLowerCase().contains(kw)) listModel.addElement(line);
                }
            }
        });
        backBtn.addActionListener(e -> app.showScreen("ManagerDashboard"));
        
        Main.applyTheme(this);
    }

    // This ensures the list updates every time the panel is shown
    @Override
    public void addNotify() {
        super.addNotify();
        populateOrderIds();
    }

    private String formatInvoiceForPrinting(String rawLine) {
        String[] p = rawLine.split(",");
        if (p.length < 5) return rawLine;
        return "==================================\n" +
               "      GREEN LEAF BAKERY           \n" +
               "==================================\n" +
               "Invoice ID : " + p[0] + "\n" +
               "Order ID   : " + p[1] + "\n" +
               "Customer   : " + p[2] + "\n" +
               "Date       : " + p[3] + "\n" +
               "Amount     : $" + p[4] + "\n" +
               "Status     : " + p[5] + "\n" +
               "==================================";
    }

    private String findCustomerNameByOrderId(String orderId) {
        String[] files = {"orders.txt", "dispatched_orders.txt"};
        for (String file : files) {
            String data = FileHandler.readFullFile(file);
            if (data != null && !data.trim().isEmpty() && !data.contains("No records")) {
                for (String line : data.split("\n")) {
                    if (line.startsWith(orderId + ",")) {
                        String[] parts = line.split(",");
                        if (parts.length > 1) return parts[1].trim();
                    }
                }
            }
        }
        return "";
    }

    private void populateOrderIds() {
        iOrderDropdown.removeAllItems();
        addOrdersFromFile("orders.txt");
        addOrdersFromFile("dispatched_orders.txt");
        if (iOrderDropdown.getItemCount() == 0) iOrderDropdown.addItem("No Orders Found");
    }

    private void addOrdersFromFile(String filename) {
        String data = FileHandler.readFullFile(filename);
        if (data != null && !data.trim().isEmpty() && !data.contains("No records")) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(",");
                    iOrderDropdown.addItem(parts[0].trim());
                }
            }
        }
    }

    private void refreshIds() {
        int nextInv = FileHandler.getNextId("invoices.txt");
        iIdField.setText(String.format("INV-%04d", nextInv));
    }

    private void showAllInvoices() { 
        listModel.clear();
        String data = FileHandler.readFullFile("invoices.txt");
        if (data != null) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty()) listModel.addElement(line);
            }
        }
    }

    private void showDispatchedInvoices() {
        listModel.clear();
        String data = FileHandler.readFullFile("invoices.txt");
        if (data != null) {
            for (String line : data.split("\n")) {
                if (!line.trim().isEmpty() && line.contains(",DISPATCHED")) {
                    listModel.addElement(line);
                }
            }
        }
    }
}