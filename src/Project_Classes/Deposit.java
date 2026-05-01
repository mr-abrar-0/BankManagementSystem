package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Deposit extends JFrame implements ActionListener {
    private JFrame frame;
    private JLabel image;
    private JTextField amountField;
    private JButton[] numButtons = new JButton[10];
    private JButton clrButton, delButton, depositButton, backButton;
    // Labels
    private JLabel depositHeaderLabel, enterAmountLabel, limitLabel, deposittype;
    // Account Number
    private String accountNumber;
    public Deposit(String accountNumber) {
        this.accountNumber = accountNumber;
        DepositFrame();
    }
    private void DepositFrame() {
        int frameWidth = 800;
        int frameHeight = 650;

        frame = new JFrame("Bank Management System - Deposit");
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
        deposittype = new JLabel("Cash On Deposit");
        deposittype.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        deposittype.setForeground(Color.BLACK);
        deposittype.setBounds(330, 100, 300, 40);
        image.add(deposittype);

        depositHeaderLabel = new JLabel("Deposit:");
        depositHeaderLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        depositHeaderLabel.setForeground(Color.BLACK);
        depositHeaderLabel.setBounds(330, 50, 300, 40);
        image.add(depositHeaderLabel);

        enterAmountLabel = new JLabel("Enter Amount:");
        enterAmountLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        enterAmountLabel.setForeground(Color.BLACK);
        enterAmountLabel.setBounds(300, 150, 200, 30);
        image.add(enterAmountLabel);

        limitLabel = new JLabel("Limit: Min 500/-PKR - Max 100,000/-PKR");
        limitLabel.setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 16));
        limitLabel.setForeground(Color.BLACK);
        limitLabel.setBounds(260, 220, 300, 20);
        image.add(limitLabel);

        // Amount TextField
        amountField = new JTextField();
        amountField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 24));
        amountField.setBounds(300, 180, 190, 30);
        amountField.setHorizontalAlignment(JTextField.RIGHT);
        image.add(amountField);

        int xStart = 300, yStart = 250;
        int width = 50, height = 30, gap = 20;

        // Loop for buttons 1 to 9
        for (int i = 1; i <= 9; i++) {
            numButtons[i] = new JButton(String.valueOf(i));
            numButtons[i].setFont(new Font("Segoe UI", java.awt.Font.BOLD, 20));

            int row = (i - 1) / 3;
            int col = (i - 1) % 3;

            numButtons[i].setBounds(xStart + col * (width + gap), yStart + row * (height + gap), width, height);
            numButtons[i].setForeground(color);
            numButtons[i].addActionListener(this);
            image.add(numButtons[i]);
        }
        // Button 0
        numButtons[0] = new JButton("0");
        numButtons[0].setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        numButtons[0].setBounds(xStart + width + gap, yStart + 3 * (height + gap), width, height);
        numButtons[0].setForeground(color);
        numButtons[0].addActionListener(this);
        image.add(numButtons[0]);
        // CLR Button
        clrButton = new JButton("C");
        clrButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        clrButton.setBounds(xStart, yStart + 3 * (height + gap), width, height);
        clrButton.setForeground(color);
        clrButton.addActionListener(this);
        image.add(clrButton);
        // DEL Button
        delButton = new JButton("D");
        delButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        delButton.setBounds(xStart + 2 * (width + gap), yStart + 3 * (height + gap), width, height);
        delButton.setForeground(color);
        delButton.addActionListener(this);
        image.add(delButton);
        // Deposit Button
        depositButton = new JButton("Deposit");
        depositButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        depositButton.setBounds(320, 450, 150, 30);
        depositButton.setForeground(color);
        depositButton.addActionListener(this);
        image.add(depositButton);
        // Back Button
        backButton = new JButton("Back");
        backButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        backButton.setBounds(320, 500, 150, 30);
        backButton.setForeground(color);
        backButton.addActionListener(this);
        image.add(backButton);

        // Set initial focus to the amount field
        amountField.requestFocusInWindow();

        frame.setVisible(true);
    }
    private boolean verifyPin(String pin) {
        String sql = "SELECT account_pin FROM account_info WHERE account_number = ?";
        if (pin == null || pin.isEmpty()) return false;
        if (!pin.matches("\\d{4}")) return false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return false;

            pstmt.setString(1, this.accountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPin = rs.getString("account_pin");
                    return storedPin.equals(pin);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during PIN verification: " + e.getMessage());
            return false;
        }
        return false;
    }
    private boolean executeDeposit(double amount) {
        Connection conn = null;
        boolean success = false;

        if (this.accountNumber == null || this.accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Account number not found. Please log in again.",
                    "Configuration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String sqlUpdate = "UPDATE account_balance SET current_balance = current_balance + ? WHERE account_number = ?";
        String sqlInsert = "INSERT INTO transaction_history (account_number, transaction_type, amount, status) VALUES (?, ?, ?, ?)";
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;
            conn.setAutoCommit(false);

            // 1. UPDATE Balance
            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
                updateStmt.setDouble(1, amount);
                updateStmt.setString(2, this.accountNumber);
                if (updateStmt.executeUpdate() != 1) {
                    throw new SQLException("Failed to update account balance (Account not found).");
                }
            }
            try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                insertStmt.setString(1, this.accountNumber);
                insertStmt.setString(2, "DEPOSIT");
                insertStmt.setDouble(3, amount);
                insertStmt.setString(4, "COMPLETED");
                insertStmt.executeUpdate();
            }
            conn.commit();
            success = true;
        } catch (SQLException e) {
            System.err.println("Deposit Transaction Failed. Rolling back: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            JOptionPane.showMessageDialog(frame, "Deposit failed due to a server error or account not found.",
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
        // Numeric button handling
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
        if (e.getSource() == depositButton) {
            String amountText = amountField.getText();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter amount to deposit.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    double amount = Double.parseDouble(amountText);
                    if (amount < 500.00 || amount > 100000.00) {
                        JOptionPane.showMessageDialog(frame, "Amount must be between Rs. 500 and Rs. 100,000.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // 2. Security Check (PIN)
                        String pin = JOptionPane.showInputDialog(frame, "Enter your 4-Digit " +
                                "Account PIN to confirm deposit:", "Security Check", JOptionPane.QUESTION_MESSAGE);
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
                        // 3. Execute DB Transaction
                        if (executeDeposit(amount)) {
                            // 4. Update DataClass Balance
                            double currentBalance = DataClass.getBalance();
                            DataClass.setBalance(currentBalance + amount);
                            JOptionPane.showMessageDialog(frame,
                                    "Rs " + String.format("%.2f", amount) + " deposited successfully!\nNew Balance: " +
                                            String.format("%.2f", DataClass.getBalance()) + " PKR",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            amountField.setText("");
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount entered. Please use digits only.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource() == backButton) {
            frame.dispose();
            new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());
        }
    }
}