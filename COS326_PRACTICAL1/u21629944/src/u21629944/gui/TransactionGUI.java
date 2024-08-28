package u21629944.gui;

/**
 *
 * @author Dominique Da Silva
 * u21629944
 */

import javax.swing.*; //using JAVA SWING for GUI
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import u21629944.model.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TransactionGUI extends JFrame {
    private JTextField idField, dateField, amountField, senderField, receiverField, searchIdField, updateIdField;
    private JComboBox<String> typeComboBox;
    private JButton saveButton, searchButton, updateButton, deleteButton, calculateTotalButton;
    private JTable transactionTable;
    private JTextArea resultArea;
    
    private EntityManagerFactory emf;
    private EntityManager em;
    
    private JTabbedPane tabbedPane;

    //___________________________________________________________________________________________________________________
    public TransactionGUI() {
        
        // Initialize EntityManager
        emf = Persistence.createEntityManagerFactory("objectdb:$objectdb/db/transactions.odb");
        em = emf.createEntityManager(); // connection with the database
        
        
        /*
          * Starting GUI outline
        */
        setTitle("COS326 PRACTICAL 1 TRANSACTION MANAGEMENT SYSTEM");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        /*
          *Initialise fields
        */
        idField = new JTextField(15);
        dateField = new JTextField(15);
        amountField = new JTextField(15);
        senderField = new JTextField(15);
        receiverField = new JTextField(15);
        searchIdField = new JTextField(15);
        updateIdField = new JTextField(15);
        typeComboBox = new JComboBox<>(new String[]{"Deposit", "Withdrawal", "Transfer"});
        
        
        /*
          *Create tabs
        */
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Search/Delete", createSearchDeletePanel());
        tabbedPane.addTab("Save", createSavePanel());
        tabbedPane.addTab("Update", createUpdatePanel());
        
        tabbedPane.addTab("Calculate Total", createCalculateTotalPanel());
        
        /*
          * Result Area to see what actions have been performed
          * Does have sperate messages for each operations
        */
        resultArea = new JTextArea(10, 10);
        resultArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
  
        /*
          * For displaying the table
        */
        String[] columnNames = {"ID", "Date", "Amount", "Sender", "Receiver", "Type"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(model);
        JScrollPane tableScrollPane = new JScrollPane(transactionTable);

        /*
          * Adding all of the components to the frame
          * Adding the main panel to the GUI frame
        */
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.NORTH);
        mainPanel.add(resultScrollPane, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);
        add(mainPanel);
        
        
        refreshTable();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    //___________________________________________________________________________________________________________________
    private void addLabelAndField(JPanel panel, String labelText, JTextField field, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    // Create/ Store Transaction
    //___________________________________________________________________________________________________________________
    private void saveTransaction() {
        try {
            // Check if any field is empty
            if (dateField.getText().isEmpty() || amountField.getText().isEmpty() ||
                senderField.getText().isEmpty() || receiverField.getText().isEmpty()) {
                showMessage("All fields must be filled!");
                return;
            }

            // Validate date format and check if it's not in the future
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // Ensure strict date parsing
            Date date;
            try {
                date = sdf.parse(dateField.getText());
                if (date.after(new Date())) {
                    showMessage("The date cannot be in the future.");
                    return;
                }
            } catch (ParseException e) {
                showMessage("Invalid date format! Please use yyyy-MM-dd.");
                return;
            }

            // Validate amount
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    showMessage("The amount must be greater than zero.");
                    return;
                }
            } catch (NumberFormatException e) {
                showMessage("Invalid amount format! Please enter a valid real number.");
                return;
            }

            // Validate sender and receiver account numbers
            String sender = senderField.getText();
            String receiver = receiverField.getText();
            if (!sender.matches("\\d+") || !receiver.matches("\\d+")) {
                showMessage("Account numbers must contain only integers.");
                return;
            }

            // Get transaction type
            String type = (String) typeComboBox.getSelectedItem();

            // Create new Transaction object
            Transaction transaction = new Transaction(date, amount, sender, receiver, type);

            // Storing new Transaction Objects
            em.getTransaction().begin();
            em.persist(transaction);
            em.getTransaction().commit();

            showMessage("Transaction saved successfully!");
            clearFields();
            refreshTable();
        } catch (Exception ex) {
            showMessage("Error saving transaction: " + ex.getMessage());
        }
    }
   

    // Read Transaction Details
    //___________________________________________________________________________________________________________________
    private void searchTransaction() {
        String idText = searchIdField.getText();
        if (idText.isEmpty()) {
            showMessage("Please enter a Transaction ID to search!");
            return;
        }

        try {
            Long id = Long.parseLong(idText);
            Transaction transaction = em.find(Transaction.class, id);

            if (transaction != null) {
                showMessage("Transaction found:\n" + transactionToString(transaction));
                populateFields(transaction);
            } else {
                showMessage("No transaction found with ID: " + id);
            }
            clearFields();
        } catch (NumberFormatException ex) {
            showMessage("Invalid Transaction ID format!");
        }
    }

    // Update Transaction Details
    //___________________________________________________________________________________________________________________
    private void updateTransaction() {
            String idText = updateIdField.getText();
            if (idText.isEmpty()) {
                showMessage("Please enter a Transaction ID to update!");
                return;
            }

            try {
                Long id = Long.parseLong(idText);
                Transaction transaction = em.find(Transaction.class, id);

                if (transaction == null) {
                    showMessage("Transaction not found!");
                    return;
                }

                // Validate date format and check if it's not in the future
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                sdf.setLenient(false); // Ensure strict date parsing
                Date date;
                try {
                    date = sdf.parse(dateField.getText());
                    if (date.after(new Date())) {
                        showMessage("The date cannot be in the future.");
                        return;
                    }
                } catch (ParseException e) {
                    showMessage("Invalid date format! Please use yyyy-MM-dd.");
                    return;
                }

                // Validate amount
                double amount;
                try {
                    amount = Double.parseDouble(amountField.getText());
                    if (amount <= 0) {
                        showMessage("The amount must be greater than zero.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showMessage("Invalid amount format! Please enter a valid real number.");
                    return;
                }

                // Validate sender and receiver account numbers
                String sender = senderField.getText();
                String receiver = receiverField.getText();
                if (!sender.matches("\\d+") || !receiver.matches("\\d+")) {
                    showMessage("Account numbers must contain only integers.");
                    return;
                }

                // Get transaction type
                String type = (String) typeComboBox.getSelectedItem();

                // Update Transaction object
                em.getTransaction().begin();
                transaction.setTransactionDate(date);
                transaction.setAmount(amount);
                transaction.setSenderAccountNumber(sender);
                transaction.setReceiverAccountNumber(receiver);
                transaction.setTransactionType(type);
                em.getTransaction().commit();

                showMessage("Transaction updated successfully!");
                clearFields();
                refreshTable();
            } catch (NumberFormatException e) {
                showMessage("Invalid ID format! Please enter a valid integer.");
            }
    }

    // Delete Transaction Details
    //___________________________________________________________________________________________________________________
    private void deleteTransaction() {
        String idText = searchIdField.getText();
        if (idText.isEmpty()) {
            showMessage("Please enter a Transaction ID to delete!");
            return;
        }

        try {
            Long id = Long.parseLong(idText);
            Transaction transaction = em.find(Transaction.class, id);

            if (transaction != null) {
                em.getTransaction().begin();
                em.remove(transaction);
                em.getTransaction().commit();
                showMessage("Transaction deleted successfully!");
                clearFields();
                refreshTable();
            } else {
                showMessage("No transaction found with ID: " + id);
            }
            clearFields();
            refreshTable();
        } catch (NumberFormatException ex) {
            showMessage("Invalid Transaction ID format!");
        }
    }

    // Calculate Transaction Details
    //___________________________________________________________________________________________________________________
    private void calculateTotal() {
        TypedQuery<Double> query = em.createQuery("SELECT SUM(t.amount) FROM Transaction t", Double.class);
        Double total = query.getSingleResult();

        if (total != null) {
            showMessage("Total amount of all transactions: " + String.format("%.2f", total));
        } else {
            showMessage("No transactions found in the database.");
        }
    }

    //___________________________________________________________________________________________________________________
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Operation Result", JOptionPane.INFORMATION_MESSAGE);
        resultArea.append(message + "\n");
    }

    //___________________________________________________________________________________________________________________
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransactionGUI());
    }
    
    //___________________________________________________________________________________________________________________
    private void clearFields() {
        idField.setText("");
        dateField.setText("");
        amountField.setText("");
        senderField.setText("");
        receiverField.setText("");
        typeComboBox.setSelectedIndex(0);
        searchIdField.setText("");
        updateIdField.setText("");
    }
    
    //___________________________________________________________________________________________________________________
    private void populateFields(Transaction t) {
        idField.setText(String.valueOf(t.getId()));
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(t.getTransactionDate()));
        amountField.setText(String.valueOf(t.getAmount()));
        senderField.setText(t.getSenderAccountNumber());
        receiverField.setText(t.getReceiverAccountNumber());
        typeComboBox.setSelectedItem(t.getTransactionType());
    }
    
    //___________________________________________________________________________________________________________________
    private String transactionToString(Transaction t) {
        return "ID: " + t.getId() +
               "\nDate: " + new SimpleDateFormat("yyyy-MM-dd").format(t.getTransactionDate()) +
               "\nAmount: " + t.getAmount() +
               "\nSender: " + t.getSenderAccountNumber() +
               "\nReceiver: " + t.getReceiverAccountNumber() +
               "\nType: " + t.getTransactionType();
    }
    
    //___________________________________________________________________________________________________________________
    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0);

        TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t", Transaction.class);
        List<Transaction> transactions = query.getResultList();

        for (Transaction t : transactions) {
            model.addRow(new Object[]{
                t.getId(),
                new SimpleDateFormat("yyyy-MM-dd").format(t.getTransactionDate()),
                t.getAmount(),
                t.getSenderAccountNumber(),
                t.getReceiverAccountNumber(),
                t.getTransactionType()
            });
        }
    }
    
    //___________________________________________________________________________________________________________________
    private JPanel createSavePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        addLabelAndComponent(panel, "Date (YYYY-MM-DD):", dateField, gbc);
        addLabelAndComponent(panel, "Amount:", amountField, gbc);
        addLabelAndComponent(panel, "Sender Account:", senderField, gbc);
        addLabelAndComponent(panel, "Receiver Account:", receiverField, gbc);
        addLabelAndComponent(panel, "Transaction Type:", typeComboBox, gbc);

        saveButton = new JButton("Save");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        saveButton.addActionListener(e -> saveTransaction());

        return panel;
    }
    
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        addLabelAndComponent(panel, "Transaction ID:", updateIdField, gbc);
        addLabelAndComponent(panel, "Date (YYYY-MM-DD):", dateField, gbc);
        addLabelAndComponent(panel, "Amount:", amountField, gbc);
        addLabelAndComponent(panel, "Sender Account:", senderField, gbc);
        addLabelAndComponent(panel, "Receiver Account:", receiverField, gbc);
        addLabelAndComponent(panel, "Transaction Type:", typeComboBox, gbc);

        updateButton = new JButton("Update");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        updateButton.addActionListener(e -> updateTransaction());

        return panel;
    }

    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private JPanel createSearchDeletePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        addLabelAndField(panel, "Transaction ID:", searchIdField, gbc);

        searchButton = new JButton("Search");
        deleteButton = new JButton("Delete");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        searchButton.addActionListener(e -> searchTransaction());
        deleteButton.addActionListener(e -> deleteTransaction());

        return panel;
    }

    private JPanel createCalculateTotalPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        calculateTotalButton = new JButton("Calculate Total");
        panel.add(calculateTotalButton);

        calculateTotalButton.addActionListener(e -> calculateTotal());

        return panel;
    }
    
    //___________________________________________________________________________________________________________________
}
