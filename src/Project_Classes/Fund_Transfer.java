package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;

public class Fund_Transfer extends JFrame implements ActionListener,Tax {
    private JFrame frame;
    private JLabel image;
    private JTextField amountField, recipientField;
    private JButton[] numButtons = new JButton[10];
    private JButton clrButton, delButton, transferButton, backButton;
    // Labels
    private JLabel headerLabel, recipientLabel, enterAmountLabel, limitLabel;
    private String senderAccountNumber;
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");

    private double calculatedTax = 0.0;

    public Fund_Transfer(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
        FundTransferFrame();
    }
    private void FundTransferFrame() {
        int frameWidth = 800;
        int frameHeight = 650;

        frame = new JFrame("Bank Management System - Fund Transfer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        ImageIcon BackgroundImage = new ImageIcon(getClass().getResource("/Images/DashBoard.PNG"));
        Image bgImage = BackgroundImage.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
        BackgroundImage = new ImageIcon(bgImage);

        image = new JLabel(BackgroundImage);
        image.setBounds(0, 0, frameWidth, frameHeight);
        frame.setContentPane(image);
        image.setLayout(null);

        ImageIcon logo = new ImageIcon(getClass().getResource("/Images/Logo.jpg"));
        frame.setIconImage(logo.getImage());

        Color color = new Color(255,0,0);

        // Labels
        headerLabel = new JLabel("Fund Transfer:");
        headerLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setBounds(300, 40, 400, 40);
        image.add(headerLabel);

        recipientLabel = new JLabel("Recipient Account No:");
        recipientLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        recipientLabel.setForeground(Color.BLACK);
        recipientLabel.setBounds(290, 100, 250, 30);
        image.add(recipientLabel);

        enterAmountLabel = new JLabel("Enter Amount:");
        enterAmountLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        enterAmountLabel.setForeground(Color.BLACK);
        enterAmountLabel.setBounds(300, 170, 200, 30);
        image.add(enterAmountLabel);

        limitLabel = new JLabel("Limit: Min 500/-PKR - Max 100,000/-PKR (Tax Applies)");
        limitLabel.setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 16));
        limitLabel.setForeground(Color.BLACK);
        limitLabel.setBounds(210, 240, 400, 20);
        image.add(limitLabel);

        // Recipient Account TextField
        recipientField = new JTextField();
        recipientField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        recipientField.setBounds(270, 140, 250, 30);
        image.add(recipientField);

        // Amount TextField
        amountField = new JTextField();
        amountField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        amountField.setBounds(300, 200, 190, 30);
        amountField.setHorizontalAlignment(JTextField.RIGHT);
        image.add(amountField);

        // Numeric buttons layout
        int xStart = 300, yStart = 270;
        int width = 50, height = 30, gap = 20;

        // Loop for buttons 1 to 9
        for (int i = 1; i <= 9; i++) {
            numButtons[i] = new JButton(String.valueOf(i));
            numButtons[i].setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

            int row = (i - 1) / 3;
            int col = (i - 1) % 3;

            numButtons[i].setBounds(xStart + col * (width + gap), yStart + row * (height + gap), width, height);
            numButtons[i].setForeground(color);
            numButtons[i].addActionListener(this);
            numButtons[i].setFocusable(false);
            image.add(numButtons[i]);
        }

        // Button 0
        numButtons[0] = new JButton("0");
        numButtons[0].setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        numButtons[0].setBounds(xStart + width + gap, yStart + 3 * (height + gap), width, height);
        numButtons[0].setForeground(color);
        numButtons[0].addActionListener(this);
        numButtons[0].setFocusable(false);
        image.add(numButtons[0]);

        // CLR Button
        clrButton = new JButton("C");
        clrButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        clrButton.setBounds(xStart, yStart + 3 * (height + gap), width, height);
        clrButton.setForeground(color);
        clrButton.addActionListener(this);
        clrButton.setFocusable(false);
        image.add(clrButton);

        // DEL Button
        delButton = new JButton("D");
        delButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        delButton.setBounds(xStart + 2 * (width + gap), yStart + 3 * (height + gap), width, height);
        delButton.setForeground(color);
        delButton.addActionListener(this);
        delButton.setFocusable(false);
        image.add(delButton);

        // Transfer Button
        transferButton = new JButton("Transfer");
        transferButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        transferButton.setBounds(320, 480, 150, 30);
        transferButton.setForeground(color);
        transferButton.addActionListener(this);
        transferButton.setFocusable(false);
        image.add(transferButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        backButton.setBounds(320, 520, 150, 30);
        backButton.setForeground(color);
        backButton.addActionListener(this);
        backButton.setFocusable(false);
        image.add(backButton);

        recipientField.requestFocusInWindow();

        frame.setVisible(true);
    }
    @Override
    public double calculateTaxedAmount(double baseAmount) {
        String filerStatus = DataClass.getFilerStatus();
        double taxRate;
        if ("Filer".equalsIgnoreCase(filerStatus)) {
            if (baseAmount >= 500.0 && baseAmount <= 50000.0) {
                taxRate = 0.02;
            } else if (baseAmount > 50000.0 && baseAmount <= 100000.0) {
                taxRate = 0.05;
            } else {
                taxRate = 0.0;
            }
        } else {
            if (baseAmount >= 500.0 && baseAmount <= 50000.0) {
                taxRate = 0.05;
            } else if (baseAmount > 50000.0 && baseAmount <= 100000.0) {
                taxRate = 0.10;
            } else {
                taxRate = 0.0;
            }
        }
        this.calculatedTax = baseAmount * taxRate;
        DataClass.setFilerTax(this.calculatedTax);
        return baseAmount + this.calculatedTax;
    }
    private boolean verifyPin(String pin) {
        String sql = "SELECT account_pin FROM account_info WHERE account_number = ?";
        if (pin == null || pin.isEmpty() || !pin.matches("\\d{4}")) return false;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            pstmt.setString(1, this.senderAccountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("account_pin").equals(pin);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during PIN verification: " + e.getMessage());
        }
        return false;
    }
    private boolean checkRecipientExists(String recipientAcc) {
        String sql = "SELECT account_number FROM account_info WHERE account_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            pstmt.setString(1, recipientAcc);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database connection error during recipient check.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    private boolean executeFundTransfer(String recipientAcc, double baseAmount, double totalDebitAmount, double taxAmount) {
        // SQL Statements
        String sqlDebit = "UPDATE account_balance SET current_balance = current_balance - ? WHERE account_number = ?";
        String sqlCredit = "UPDATE account_balance SET current_balance = current_balance + ? WHERE account_number = ?";
        String sqlHistoryOut = "INSERT INTO transaction_history (account_number, " +
                "transaction_type, amount, recipient_account, status) VALUES (?, ?, ?, ?, ?)";
        String sqlHistoryIn = "INSERT INTO transaction_history (account_number, " +
                "transaction_type, amount, sender_account, status) VALUES (?, ?, ?, ?, ?)";
        String sqlTaxLog = "INSERT INTO tax_history (account_number, filer_status_at_time, " +
                "base_amount, tax_rate, tax_amount, tax_type) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        boolean success = false;

        double taxRate = (baseAmount != 0) ? taxAmount / baseAmount : 0.0;
        String filerStatus = DataClass.getFilerStatus();

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            conn.setAutoCommit(false);

            try (PreparedStatement debitStmt = conn.prepareStatement(sqlDebit)) {
                debitStmt.setDouble(1, totalDebitAmount);
                debitStmt.setString(2, this.senderAccountNumber);
                if (debitStmt.executeUpdate() != 1) {
                    throw new SQLException("Failed to debit sender account balance. (Insufficient funds or account issue)");
                }
            }

            try (PreparedStatement creditStmt = conn.prepareStatement(sqlCredit)) {
                creditStmt.setDouble(1, baseAmount);
                creditStmt.setString(2, recipientAcc);
                if (creditStmt.executeUpdate() != 1) {
                    throw new SQLException("Failed to credit recipient account balance.");
                }
            }

            try (PreparedStatement historyOutStmt = conn.prepareStatement(sqlHistoryOut)) {
                historyOutStmt.setString(1, this.senderAccountNumber);
                historyOutStmt.setString(2, "TRANSFER_OUT (Tax: " + CURRENCY_FORMAT.format(taxAmount) + ")");
                historyOutStmt.setDouble(3, totalDebitAmount);
                historyOutStmt.setString(4, recipientAcc);
                historyOutStmt.setString(5, "COMPLETED");
                historyOutStmt.executeUpdate();
            }

            try (PreparedStatement historyInStmt = conn.prepareStatement(sqlHistoryIn)) {
                historyInStmt.setString(1, recipientAcc);
                historyInStmt.setString(2, "TRANSFER_IN");
                historyInStmt.setDouble(3, baseAmount);
                historyInStmt.setString(4, this.senderAccountNumber);
                historyInStmt.setString(5, "COMPLETED");
                historyInStmt.executeUpdate();
            }
            if (taxAmount > 0) {
                try (PreparedStatement taxLogStmt = conn.prepareStatement(sqlTaxLog)) {
                    taxLogStmt.setString(1, this.senderAccountNumber);
                    taxLogStmt.setString(2, filerStatus);
                    taxLogStmt.setDouble(3, baseAmount);
                    taxLogStmt.setDouble(4, taxRate);
                    taxLogStmt.setDouble(5, taxAmount);
                    taxLogStmt.setString(6, "FUND_TRANSFER_WHT");
                    taxLogStmt.executeUpdate();
                }
            }
            conn.commit();
            success = true;
        } catch (SQLException e) {
            System.err.println("Fund Transfer Transaction Failed. Rolling back: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            JOptionPane.showMessageDialog(frame, "Transfer failed due to a server error or data issue. Check console for details.",
                    "Transaction Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return success;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i <= 9; i++) {
            if (e.getSource() == numButtons[i]) {
                if (amountField.isFocusOwner()) {
                    amountField.setText(amountField.getText() + i);
                } else if (recipientField.isFocusOwner()) {
                    recipientField.setText(recipientField.getText() + i);
                }
                return;
            }
        }
        if (e.getSource() == clrButton) {
            if (amountField.isFocusOwner()) {
                amountField.setText("");
            }
            if (recipientField.isFocusOwner()) {
                recipientField.setText("");
            }
            return;
        }
        if (e.getSource() == delButton) {
            JTextField target = amountField.isFocusOwner() ? amountField :
                    recipientField.isFocusOwner() ? recipientField : null;
            if (target != null) {
                String text = target.getText();
                if (!text.isEmpty()) {
                    target.setText(text.substring(0, text.length() - 1));
                }
            }
            return;
        }
        if (e.getSource() == transferButton) {
            String recipientAcc = recipientField.getText().trim();
            String amountText = amountField.getText();
            if (recipientAcc.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter recipient account number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter amount to transfer.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (recipientAcc.equals(this.senderAccountNumber)) {
                JOptionPane.showMessageDialog(frame, "Cannot transfer funds to your own account.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double baseAmount = Double.parseDouble(amountText);
                double currentBalance = DataClass.getBalance();
                if (baseAmount < 500 || baseAmount > 100000) {
                    JOptionPane.showMessageDialog(frame, "Amount must be between Rs. 500 and Rs. 100,000.",
                            "Transfer Limit Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 2. Calculate Taxed Amount
                double totalDebitAmount = calculateTaxedAmount(baseAmount);
                double taxAmount = this.calculatedTax;
                // 3. Check Sufficient Funds (Use totalDebitAmount)
                if (totalDebitAmount > currentBalance) {
                    JOptionPane.showMessageDialog(frame,
                            "Insufficient funds. Total debit required (including tax of PKR " +
                                    CURRENCY_FORMAT.format(taxAmount) + "): PKR " +
                                    CURRENCY_FORMAT.format(totalDebitAmount) + ".\n" +
                                    "Current balance: PKR " + CURRENCY_FORMAT.format(currentBalance),
                            "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 4. Check Recipient Existence
                if (!checkRecipientExists(recipientAcc)) {
                    JOptionPane.showMessageDialog(frame, "Recipient account number is not registered in this bank.",
                            "Invalid Recipient", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // SECURITY CHECK
                String pin = JOptionPane.showInputDialog(frame, "Enter your 4-Digit Account PIN to confirm transfer:",
                        "Security Check", JOptionPane.QUESTION_MESSAGE);

                if (pin == null || pin.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "PIN required to proceed.",
                            "Authorization Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!verifyPin(pin)) {
                    JOptionPane.showMessageDialog(frame, "Invalid PIN. Transaction cancelled.",
                            "Authorization Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // 5. Execute Transaction
                if (executeFundTransfer(recipientAcc, baseAmount, totalDebitAmount, taxAmount)) {
                    // Update DataClass Balance (Sender's new balance)
                    double newBalance = currentBalance - totalDebitAmount;
                    DataClass.setBalance(newBalance);

                    JOptionPane.showMessageDialog(frame,
                            "Rs. " + CURRENCY_FORMAT.format(baseAmount) + " transferred to Account No: "
                                    + recipientAcc + " successfully!\n" +
                                    "Tax Deducted: PKR " + CURRENCY_FORMAT.format(taxAmount) + "\n" +
                                    "Total Debited: PKR " + CURRENCY_FORMAT.format(totalDebitAmount) + "\n" +
                                    "Your New Balance: PKR " + CURRENCY_FORMAT.format(newBalance),
                            "Transfer Success", JOptionPane.INFORMATION_MESSAGE);

                    amountField.setText("");
                    recipientField.setText("");
                    recipientField.requestFocusInWindow();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered. Please use digits only.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                recipientField.requestFocusInWindow();
            }
            return;
        }
        if (e.getSource() == backButton) {
            frame.dispose();
            new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());
        }
    }
}