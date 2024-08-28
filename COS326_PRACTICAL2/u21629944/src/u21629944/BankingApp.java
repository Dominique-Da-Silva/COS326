package u21629944;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BankingApp extends JFrame {
    private DatabaseManager dbManager;
    private JTextField accountNumberField, accountHolderNameField;
    private JTextField transactionDateField, amountField;
    private JComboBox<String> transactionTypeField;
    private JComboBox<String> senderAccountComboBox, receiverAccountComboBox;
    private JTextField searchAccountNumberField;
    private JTable transactionTable, bankAccountTable, allTransactionsTable, searchResultTable;
    private JButton resetButton;

    public BankingApp() {
        dbManager = new DatabaseManager();
        initComponents();
        updateAccountComboBoxes();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Banking Application");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Save Bank Account Tab
        JPanel saveBankAccountPanel = createSaveBankAccountPanel();
        tabbedPane.addTab("Save Bank Account", saveBankAccountPanel);

        // Save Transaction Tab
        JPanel saveTransactionPanel = createSaveTransactionPanel();
        tabbedPane.addTab("Save Transaction", saveTransactionPanel);

        // Search/Delete Tab
        JPanel searchDeletePanel = createSearchDeletePanel();
        tabbedPane.addTab("Search/Delete", searchDeletePanel);
        
        // Bank Accounts Tab
        JPanel bankAccountsPanel = createBankAccountsPanel();
        tabbedPane.addTab("Bank Accounts", bankAccountsPanel);
        
        // All Transactions Tab
        JPanel allTransactionsPanel = createAllTransactionsPanel();
        tabbedPane.addTab("All Transactions", allTransactionsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createBankAccountsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        bankAccountTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bankAccountTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        updateBankAccountTable();
        return panel;
    }

    private JPanel createSaveBankAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        accountNumberField = new JTextField(15);
        accountHolderNameField = new JTextField(15);

        inputPanel.add(new JLabel("Account Number:"));
        inputPanel.add(accountNumberField);
        inputPanel.add(new JLabel("Account Holder Name:"));
        inputPanel.add(accountHolderNameField);

        JButton saveButton = new JButton("Save Bank Account");
        saveButton.addActionListener(e -> saveBankAccount());
        saveButton.setPreferredSize(new Dimension(200, 40));

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(saveButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSaveTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        transactionDateField = new JTextField(15);
        amountField = new JTextField(15);
        senderAccountComboBox = new JComboBox<>();
        receiverAccountComboBox = new JComboBox<>();
        transactionTypeField = new JComboBox<>(new String[]{"Deposit", "Withdrawal", "Transfer"});

        inputPanel.add(new JLabel("Transaction Date (YYYY-MM-DD):"));
        inputPanel.add(transactionDateField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Sender Account:"));
        inputPanel.add(senderAccountComboBox);
        inputPanel.add(new JLabel("Receiver Account:"));
        inputPanel.add(receiverAccountComboBox);
        inputPanel.add(new JLabel("Transaction Type:"));
        inputPanel.add(transactionTypeField);

        JButton saveButton = new JButton("Save Transaction");
        saveButton.addActionListener(e -> saveTransaction());
        saveButton.setPreferredSize(new Dimension(200, 40));

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(saveButton, BorderLayout.SOUTH);

        transactionTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAllTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        allTransactionsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(allTransactionsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> updateAllTransactionsTable());
        refreshButton.setPreferredSize(new Dimension(200, 40));
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSearchDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        searchAccountNumberField = new JTextField(15);
        topPanel.add(new JLabel("Account Number:"));
        topPanel.add(searchAccountNumberField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchData());
        searchButton.setPreferredSize(new Dimension(200, 40));
        topPanel.add(searchButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteData());
        deleteButton.setPreferredSize(new Dimension(200, 40));
        topPanel.add(deleteButton);

        panel.add(topPanel, BorderLayout.NORTH);

        searchResultTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(searchResultTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void saveBankAccount() {
        try {
            String accountNumber = accountNumberField.getText();
            String accountHolderName = accountHolderNameField.getText();

            if (accountNumber.isEmpty() || accountHolderName.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
            }

            if (!accountNumber.matches("\\d+")) {
                throw new IllegalArgumentException("Account number must contain only numbers.");
            }
            
            if (accountNumber.length() > 15) {
                throw new IllegalArgumentException("Account number cannot exceed 15 digits.");
            }

            if (!accountHolderName.matches("[a-zA-Z ]+")) {
                throw new IllegalArgumentException("Account holder name must contain only alphabetical characters and spaces.");
            }

            BankAccount account = new BankAccount(accountNumber, accountHolderName);
            dbManager.saveBankAccount(account);

            JOptionPane.showMessageDialog(this, "Bank account saved successfully!");
            updateBankAccountTable();
            updateAccountComboBoxes(); // Update the combo boxes after saving a bank account
            clearBankAccountFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving bank account: " + ex.getMessage());
        }
    }

    private void saveTransaction() {
        try {
            String transactionDateStr = transactionDateField.getText();
            double amount = Double.parseDouble(amountField.getText());
            String senderAccount = ((String) senderAccountComboBox.getSelectedItem()).split(" - ")[0];
            String receiverAccount = ((String) receiverAccountComboBox.getSelectedItem()).split(" - ")[0];
            String transactionType = (String) transactionTypeField.getSelectedItem();

            if (transactionDateStr.isEmpty() || amountField.getText().isEmpty() || 
                senderAccount.isEmpty() || receiverAccount.isEmpty()) {
                throw new IllegalArgumentException("All fields must be filled.");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            Date transactionDate = dateFormat.parse(transactionDateStr);

            if (transactionDate.after(new Date())) {
                throw new IllegalArgumentException("Transaction date cannot be in the future.");
            }

            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0.");
            }

            BankAccount senderBankAccount = dbManager.findBankAccount(senderAccount);
            BankAccount receiverBankAccount = dbManager.findBankAccount(receiverAccount);

            if (senderBankAccount == null || receiverBankAccount == null) {
                throw new IllegalArgumentException("Invalid sender or receiver account.");
            }

            Transaction transaction = new Transaction(transactionDate, amount, senderAccount, receiverAccount, transactionType);
            transaction.setBankAccount(senderBankAccount);
            dbManager.saveTransaction(transaction);

            JOptionPane.showMessageDialog(this, "Transaction saved successfully!");
            updateTransactionTable();
            updateAllTransactionsTable();
            clearTransactionFields();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Amount must be a valid number.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving transaction: " + e.getMessage());
        }
    }

    private void searchData() {
        String accountNumber = searchAccountNumberField.getText();
        if (accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Account number is required for search.");
            return;
        }

        List<Transaction> transactions = dbManager.findTransactions(accountNumber);
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found for the account number.");
        } else {
            updateSearchResultTable(transactions);
        }

        // Clear the search text field
        searchAccountNumberField.setText("");
    }

    private void deleteData() {
        String accountNumber = searchAccountNumberField.getText();
        if (accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Account number is required for deletion.");
            return;
        }

        dbManager.deleteBankAccountAndTransactions(accountNumber);
        JOptionPane.showMessageDialog(this, "Bank account and associated transactions deleted successfully!");

        // Clear the search text field
        searchAccountNumberField.setText("");

        // Refresh the bank account and all transactions tables
        updateBankAccountTable();
        updateTransactionTable();
        updateAllTransactionsTable();
    }


    private void updateBankAccountTable() {
        List<BankAccount> bankAccounts = dbManager.getAllBankAccounts();
        DefaultTableModel model = new DefaultTableModel(new String[]{"Account Number", "Account Holder Name"}, 0);
        for (BankAccount account : bankAccounts) {
            model.addRow(new Object[]{account.getAccountNumber(), account.getAccountHolderName()});
        }
        bankAccountTable.setModel(model);
    }

    private void updateTransactionTable() {
        List<Transaction> transactions = dbManager.getAllTransactions();
        DefaultTableModel model = new DefaultTableModel(new String[]{
            "ID", "Date", "Amount", "Sender Account", "Sender Name", 
            "Receiver Account", "Receiver Name", "Type"}, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Transaction transaction : transactions) {
            BankAccount senderAccount = dbManager.findBankAccount(transaction.getSenderAccountNumber());
            BankAccount receiverAccount = dbManager.findBankAccount(transaction.getReceiverAccountNumber());

            String formattedDate = dateFormat.format(transaction.getTransactionDate());

            model.addRow(new Object[]{
                transaction.getTransactionId(),
                formattedDate,
                transaction.getAmount(),
                transaction.getSenderAccountNumber(),
                senderAccount != null ? senderAccount.getAccountHolderName() : "Unknown",
                transaction.getReceiverAccountNumber(),
                receiverAccount != null ? receiverAccount.getAccountHolderName() : "Unknown",
                transaction.getTransactionType()
            });
        }
        transactionTable.setModel(model);
    }


    private void updateAllTransactionsTable() {
        List<Transaction> transactions = dbManager.getAllTransactions();
        DefaultTableModel model = new DefaultTableModel(new String[]{
            "ID", "Date", "Amount", "Sender Account", "Sender Name", 
            "Receiver Account", "Receiver Name", "Type"}, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Transaction transaction : transactions) {
            BankAccount senderAccount = dbManager.findBankAccount(transaction.getSenderAccountNumber());
            BankAccount receiverAccount = dbManager.findBankAccount(transaction.getReceiverAccountNumber());

            String formattedDate = dateFormat.format(transaction.getTransactionDate());

            model.addRow(new Object[]{
                transaction.getTransactionId(),
                formattedDate,
                transaction.getAmount(),
                transaction.getSenderAccountNumber(),
                senderAccount != null ? senderAccount.getAccountHolderName() : "Unknown",
                transaction.getReceiverAccountNumber(),
                receiverAccount != null ? receiverAccount.getAccountHolderName() : "Unknown",
                transaction.getTransactionType()
            });
        }
        allTransactionsTable.setModel(model);
    }


    private void updateSearchResultTable(List<Transaction> transactions) {
        DefaultTableModel model = new DefaultTableModel(new String[]{
            "ID", "Date", "Amount", "Sender Account", "Sender Name", 
            "Receiver Account", "Receiver Name", "Type"}, 0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (Transaction transaction : transactions) {
            BankAccount senderAccount = dbManager.findBankAccount(transaction.getSenderAccountNumber());
            BankAccount receiverAccount = dbManager.findBankAccount(transaction.getReceiverAccountNumber());

            String formattedDate = dateFormat.format(transaction.getTransactionDate());

            model.addRow(new Object[]{
                transaction.getTransactionId(),
                formattedDate,
                transaction.getAmount(),
                transaction.getSenderAccountNumber(),
                senderAccount != null ? senderAccount.getAccountHolderName() : "Unknown",
                transaction.getReceiverAccountNumber(),
                receiverAccount != null ? receiverAccount.getAccountHolderName() : "Unknown",
                transaction.getTransactionType()
            });
        }
        searchResultTable.setModel(model);
    }

    private void clearBankAccountFields() {
        accountNumberField.setText("");
        accountHolderNameField.setText("");
    }

    private void clearTransactionFields() {
        transactionDateField.setText("");
        amountField.setText("");
        senderAccountComboBox.setSelectedIndex(0);
        receiverAccountComboBox.setSelectedIndex(0);
        transactionTypeField.setSelectedIndex(0);
    }

    private void updateAccountComboBoxes() {
        List<BankAccount> accounts = dbManager.getAllBankAccounts();
        senderAccountComboBox.removeAllItems();
        receiverAccountComboBox.removeAllItems();
        for (BankAccount account : accounts) {
            String displayText = account.getAccountNumber() + " - " + account.getAccountHolderName();
            senderAccountComboBox.addItem(displayText);
            receiverAccountComboBox.addItem(displayText);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankingApp().setVisible(true));
    }
}
