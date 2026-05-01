package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;

public class Withdraw extends JFrame implements ActionListener, Tax {
    private JFrame frame;
    private JLabel image;
    private JTextField amountField;
    private JButton[] numButtons = new JButton[10];
    private JButton clrButton, delButton, withdrawButton, backButton;
    // Labels
    private JLabel withdrawHeaderLabel, enterAmountLabel, limitLabel;
    private String accountNumber;
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");
    private double calculatedTax = 0.0;

    public Withdraw(String accountNumber) {
        this.accountNumber = accountNumber;
        WithdrawFrame();
    }
    private void WithdrawFrame() {
        int frameWidth = 800;
        int frameHeight = 650;

        frame = new JFrame("Bank Management System - Withdraw");
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
        withdrawHeaderLabel = new JLabel("Withdraw:");
        withdrawHeaderLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        withdrawHeaderLabel.setForeground(Color.BLACK);
        withdrawHeaderLabel.setBounds(320, 50, 300, 40);
        image.add(withdrawHeaderLabel);

        enterAmountLabel = new JLabel("Enter Amount:");
        enterAmountLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        enterAmountLabel.setForeground(Color.BLACK);
        enterAmountLabel.setBounds(300, 150, 200, 30);
        image.add(enterAmountLabel);

        limitLabel = new JLabel("Limit: Min 500/-PKR - Max 100,000/-PKR (Tax Applies)");
        limitLabel.setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 16));
        limitLabel.setForeground(Color.BLACK);
        limitLabel.setBounds(230, 220, 400, 20);
        image.add(limitLabel);
        // Amount TextField
        amountField = new JTextField();
        amountField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        amountField.setBounds(300, 180, 190, 30);
        amountField.setHorizontalAlignment(JTextField.RIGHT);
        image.add(amountField);

        // Numeric buttons layout
        int xStart = 300, yStart = 250;
        int width = 50, height = 30, gap = 20;

        // Loop for buttons 1 to 9
        for (int i = 1; i <= 9; i++) {
            numButtons[i] = new JButton(String.valueOf(i));
            numButtons[i].setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

            int row = (i - 1) / 3;
            int col = (i - 1) % 3;

            numButtons[i].setBounds(xStart + col * (width + gap),
                    yStart + row * (height + gap), width, height);
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

        // Withdraw Button
        withdrawButton = new JButton("Withdraw");
        withdrawButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        withdrawButton.setBounds(320, 450, 150, 30);
        withdrawButton.setForeground(color);
        withdrawButton.addActionListener(this);
        withdrawButton.setFocusable(false);
        image.add(withdrawButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        backButton.setBounds(320, 500, 150, 30);
        backButton.setForeground(color);
        backButton.addActionListener(this);
        backButton.setFocusable(false);
        image.add(backButton);

        amountField.requestFocusInWindow();

        frame.setVisible(true);
    }
    public double calculateTaxedAmount(double baseAmount) {
        String filerStatus = DataClass.getFilerStatus();
        double taxRate;
        // Filer
        if ("Filer".equalsIgnoreCase(filerStatus)) {
            if (baseAmount >= 500.0 && baseAmount <= 50000.0) {
                taxRate = 0.02; // 2% for 500 - 50k
            } else if (baseAmount > 50000.0 && baseAmount <= 100000.0) {
                taxRate = 0.05; // 5% for 50k - 100k
            } else {
                taxRate = 0.0;
            }
        } else { // Non-Filer
            if (baseAmount >= 500.0 && baseAmount <= 50000.0) {
                taxRate = 0.05; // 5% for 500 - 50k
            } else if (baseAmount > 50000.0 && baseAmount <= 100000.0) {
                taxRate = 0.10; // 10% for 50k - 100k
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
            pstmt.setString(1, this.accountNumber);
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
    private boolean executeWithdraw(double baseAmount, double totalDebitAmount, double taxAmount) {
        Connection conn = null;
        boolean success = false;

        String sqlUpdate = "UPDATE account_balance SET current_balance = current_balance" +
                " - ? WHERE account_number = ?";
        String sqlInsert = "INSERT INTO transaction_history (account_number, transaction_type," +
                " amount, status) VALUES (?, ?, ?, ?)";
        String sqlTaxLog = "INSERT INTO tax_history (account_number, filer_status_at_time, " +
                "base_amount, tax_rate, tax_amount, tax_type) VALUES (?, ?, ?, ?, ?, ?)";
        double taxRate = (baseAmount != 0) ? taxAmount / baseAmount : 0.0;
        String filerStatus = DataClass.getFilerStatus();
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            conn.setAutoCommit(false);
            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
                updateStmt.setDouble(1, totalDebitAmount);
                updateStmt.setString(2, this.accountNumber);
                if (updateStmt.executeUpdate() != 1) {
                    throw new SQLException("Failed to debit account balance (Account not found or insufficient balance).");
                }
            }
            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                insertStmt.setString(1, this.accountNumber);
                insertStmt.setString(2, "WITHDRAWAL (Tax: " + CURRENCY_FORMAT.format(taxAmount) + ")");
                insertStmt.setDouble(3, totalDebitAmount);
                insertStmt.setString(4, "COMPLETED");
                insertStmt.executeUpdate();
            }
            if (taxAmount > 0) {
                try (PreparedStatement taxLogStmt = conn.prepareStatement(sqlTaxLog)) {
                    taxLogStmt.setString(1, this.accountNumber);
                    taxLogStmt.setString(2, filerStatus);
                    taxLogStmt.setDouble(3, baseAmount);
                    taxLogStmt.setDouble(4, taxRate);
                    taxLogStmt.setDouble(5, taxAmount);
                    taxLogStmt.setString(6, "WITHDRAWAL_WHT");
                    taxLogStmt.executeUpdate();
                }
            }
            conn.commit();
            success = true;
        } catch (SQLException e) {
            System.err.println("Withdrawal Transaction Failed. Rolling back: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            JOptionPane.showMessageDialog(frame, "Withdrawal failed due to a server error or data issue.",
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
                amountField.setText(amountField.getText() + i);
                return;
            }
        }

        if (e.getSource() == clrButton) {
            amountField.setText("");
            return;
        }

        if (e.getSource() == delButton) {
            String text = amountField.getText();
            if (!text.isEmpty()) {
                amountField.setText(text.substring(0, text.length() - 1));
            }
            return;
        }

        if (e.getSource() == withdrawButton) {
            String amountText = amountField.getText();

            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter amount to withdraw.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double baseAmount = Double.parseDouble(amountText);
                double currentBalance = DataClass.getBalance();

                if (baseAmount < 500 || baseAmount > 100000) {
                    JOptionPane.showMessageDialog(frame,
                            "Amount must be between Rs. 500 and Rs. 100,000.",
                            "Withdrawal Limit Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // b. Calculate Taxed Amount
                double totalDebitAmount = calculateTaxedAmount(baseAmount);
                double taxAmount = this.calculatedTax;

                // c. Check Sufficient Funds
                if (totalDebitAmount > currentBalance) {
                    JOptionPane.showMessageDialog(frame,
                            "Insufficient funds. Total debit required (including tax of PKR " +
                                    CURRENCY_FORMAT.format(taxAmount) + "): PKR " + CURRENCY_FORMAT.
                                    format(totalDebitAmount) + ".\n" +
                                    "Current balance: PKR " + CURRENCY_FORMAT.format(currentBalance),
                            "Insufficient Balance", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // d. Security Check (PIN)
                String pin = JOptionPane.showInputDialog(frame, "Enter your 4-Digit Account PIN to confirm withdrawal:",
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
                // e. Execute Transaction
                if (executeWithdraw(baseAmount, totalDebitAmount, taxAmount)) {
                    // Update DataClass only if DB transaction is successful
                    double newBalance = currentBalance - totalDebitAmount;
                    DataClass.setBalance(newBalance);

                    JOptionPane.showMessageDialog(frame,
                            "Rs. " + CURRENCY_FORMAT.format(baseAmount) + " withdrawn successfully!\n" +
                                    "Tax Deducted: PKR " + CURRENCY_FORMAT.format(taxAmount) + "\n" +
                                    "Total Debited: PKR " + CURRENCY_FORMAT.format(totalDebitAmount) + "\n" +
                                    "New Balance: PKR " + CURRENCY_FORMAT.format(newBalance),
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    amountField.setText("");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        if (e.getSource() == backButton) {
            frame.dispose();
            new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());
        }
    }
}