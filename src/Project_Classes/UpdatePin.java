package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdatePin extends JFrame implements ActionListener {
    private JFrame frame;
    private JLabel image;
    private String accountNumber;
    private String gmail;

    private JPasswordField oldPinField, newPinField, confirmNewPinField;
    private JButton updateButton, backButton;
    private JButton forgotPinButton;

    public UpdatePin(String accNumber, String userGmail) {
        this.accountNumber = accNumber;
        this.gmail = userGmail;
        setupFrame();
    }

    private void setupFrame() {
        int frameWidth = 900;
        int frameHeight = 600;

        frame = new JFrame("Update PIN - Account: " + accountNumber);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Background Image Setup
        ImageIcon BackgroundImage = new ImageIcon(getClass().getResource("/Images/Login.jpg"));
        Image bgImage = BackgroundImage.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
        BackgroundImage = new ImageIcon(bgImage);

        image = new JLabel(BackgroundImage);
        image.setBounds(0, 0, frameWidth, frameHeight);
        frame.setContentPane(image);
        image.setLayout(null);

        // Icon
        ImageIcon logo = new ImageIcon(getClass().getResource("/Images/Logo.jpg"));
        frame.setIconImage(logo.getImage());

        JLabel header = new JLabel("Change 4-Digit PIN", SwingConstants.CENTER);
        header.setFont(new Font("Times New Roman", Font.BOLD, 24));
        header.setForeground(new Color(0, 0, 0));
        header.setBounds(450, 250, 300, 35);
        image.add(header);

        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
        Color textColor = Color.BLACK;

        // Old PIN
        JLabel oldPinLabel = new JLabel("Old PIN:");
        oldPinLabel.setFont(labelFont);
        oldPinLabel.setForeground(textColor);
        oldPinLabel.setBounds(450,320,170,30);
        image.add(oldPinLabel);
        oldPinField = new JPasswordField(4);
        oldPinField.setBounds(600,320,170,30);
        image.add(oldPinField);

        // New PIN
        JLabel newPinLabel = new JLabel("New PIN (4 Digits):");
        newPinLabel.setFont(labelFont);
        newPinLabel.setForeground(textColor);
        newPinLabel.setBounds(450,360,170,30);
        image.add(newPinLabel);
        newPinField = new JPasswordField(4);
        newPinField.setBounds(600,360,170,30);
        image.add(newPinField);

        // Confirm New PIN
        JLabel confirmNewPinLabel = new JLabel("Confirm New PIN:");
        confirmNewPinLabel.setFont(labelFont);
        confirmNewPinLabel.setForeground(textColor);
        confirmNewPinLabel.setBounds(450,400,170,30);
        image.add(confirmNewPinLabel);
        confirmNewPinField = new JPasswordField(4);
        confirmNewPinField.setBounds(600,400,170,30);
        image.add(confirmNewPinField);

        // Forgot PIN Button
        forgotPinButton = new JButton("Forgot PIN?");
        forgotPinButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        forgotPinButton.setForeground(new Color(50, 100, 255)); // Blue color
        forgotPinButton.setBackground(Color.WHITE);
        forgotPinButton.setBounds(600, 435, 170, 30);
        forgotPinButton.addActionListener(this);
        image.add(forgotPinButton);

        // Buttons
        updateButton = new JButton("Change PIN");
        updateButton.setFont(labelFont);
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(50, 100, 255)); // Blue
        updateButton.setBounds(620,480,150,30);
        updateButton.addActionListener(this);
        image.add(updateButton);

        backButton = new JButton("Back");
        backButton.setFont(labelFont);
        backButton.setForeground(Color.DARK_GRAY);
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setBounds(450,480,150,30);
        backButton.addActionListener(this);
        image.add(backButton);

        frame.setVisible(true);
    }

    private boolean validateInput(String oldPin, String newPin, String confirmNewPin) {
        if (oldPin.isEmpty() || newPin.isEmpty() || confirmNewPin.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!newPin.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(frame, "New PIN must be exactly 4 digits.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!newPin.equals(confirmNewPin)) {
            JOptionPane.showMessageDialog(frame, "New PINs do not match.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (oldPin.equals(newPin)) {
            JOptionPane.showMessageDialog(frame, "New PIN must be different from the Old PIN.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean executePinUpdate(String oldPin, String newPin) {
        // Query to check PIN is correct
        String sqlCheck = "SELECT account_pin FROM account_info WHERE account_number = ?";
        // Query to update PIN
        String sqlUpdate = "UPDATE account_info SET account_pin = ? WHERE account_number = ? AND account_pin = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return false;

            // 1. Verify Old PIN
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                checkStmt.setString(1, accountNumber);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPin = rs.getString("account_pin");
                        if (!storedPin.equals(oldPin)) {
                            JOptionPane.showMessageDialog(frame, "Incorrect Old PIN.",
                                    "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Account not found.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }

            // 2. Execute Update
            try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
                updateStmt.setString(1, newPin);
                updateStmt.setString(2, accountNumber);
                updateStmt.setString(3, oldPin);

                if (updateStmt.executeUpdate() == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error during PIN update: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
            new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());
        } else if (e.getSource() == updateButton) {
            String oldPin = new String(oldPinField.getPassword());
            String newPin = new String(newPinField.getPassword());
            String confirmNewPin = new String(confirmNewPinField.getPassword());

            if (validateInput(oldPin, newPin, confirmNewPin)) {
                if (executePinUpdate(oldPin, newPin)) {
                    JOptionPane.showMessageDialog(frame, "PIN updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Clear fields upon success
                    oldPinField.setText("");
                    newPinField.setText("");
                    confirmNewPinField.setText("");
                }
            }
        } else if (e.getSource() == forgotPinButton) {
            frame.dispose();
            new ForgotPinForm(this.accountNumber, this.gmail);
        }
    }
}