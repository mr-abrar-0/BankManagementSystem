package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteAccount extends JFrame implements ActionListener {
    private JFrame frame;
    private JPasswordField passwordField, pinField;
    private JButton deleteButton, backButton;

    public DeleteAccount() {
        setupFrame();
    }

    private void setupFrame() {
        frame = new JFrame("Delete Account - IRREVERSIBLE ACTION");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(550, 350);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);

        JLabel header = new JLabel("Confirm Account Deletion", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(Color.RED);
        header.setBounds(100, 20, 350, 30);
        panel.add(header);

        JLabel warningLine1 = new JLabel("WARNING: Deletion requires Admin approval. Your request will", SwingConstants.CENTER);
        JLabel warningLine2 = new JLabel("be submitted after validation.", SwingConstants.CENTER);

        Font warningFont = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14);
        Color warningColor = new Color(0, 0, 150);

        warningLine1.setFont(warningFont);
        warningLine1.setForeground(warningColor);
        warningLine1.setBounds(50, 60, 450, 20);
        panel.add(warningLine1);

        warningLine2.setFont(warningFont);
        warningLine2.setForeground(warningColor);
        warningLine2.setBounds(50, 80, 450, 20);
        panel.add(warningLine2);

        Font labelFont = new Font("Times New Roman", Font.BOLD, 14);

        // Login Password
        JLabel passLabel = new JLabel("Confirm Login Password:");
        passLabel.setFont(labelFont);
        passLabel.setBounds(100, 120, 180, 25);
        panel.add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(280, 120, 170, 25);
        panel.add(passwordField);

        // Account PIN
        JLabel pinLabel = new JLabel("Confirm 4-Digit PIN:");
        pinLabel.setFont(labelFont);
        pinLabel.setBounds(100, 160, 180, 25);
        panel.add(pinLabel);
        pinField = new JPasswordField(4);
        pinField.setBounds(280, 160, 170, 25);
        panel.add(pinField);

        // Buttons
        deleteButton = new JButton("Submit Request");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBounds(280, 230, 170, 35);
        deleteButton.addActionListener(this);
        panel.add(deleteButton);

        backButton = new JButton("Cancel");
        backButton.setFont(labelFont);
        backButton.setBounds(100, 230, 100, 35);
        backButton.addActionListener(this);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private boolean verifyCredentials(String password, String pin) {
        String userGmail = DataClass.getGmail();
        String accountNumber = DataClass.getAccountNumber();

        String sqlCheckPin = "SELECT account_pin FROM account_info WHERE account_number = ?";
        String sqlCheckPass = "SELECT password FROM personal_info WHERE gmail = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) return false;

            // 1. Verify PIN
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheckPin)) {
                checkStmt.setString(1, accountNumber);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next() || !rs.getString("account_pin").equals(pin)) {
                        JOptionPane.showMessageDialog(frame, "Incorrect Account PIN.",
                                "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }

            // 2. Verify Login Password
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheckPass)) {
                checkStmt.setString(1, userGmail);
                checkStmt.setString(2, password);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(frame, "Incorrect Login Password.",
                                "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error during credential verification: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean submitDeleteRequest() {
        String userGmail = DataClass.getGmail();
        String accountNumber = DataClass.getAccountNumber();

        String applicationData = "DELETION_ACCOUNT:" + accountNumber + ",DELETION_GMAIL:" + userGmail;

        String sql = "INSERT INTO pending_requests (user_gmail, request_type, application_data) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            pstmt.setString(1, userGmail);
            pstmt.setString(2, "ACCOUNT_DELETION");
            pstmt.setString(3, applicationData);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error: Failed to submit deletion request. " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
        } else if (e.getSource() == deleteButton) {
            String password = new String(passwordField.getPassword());
            String pin = new String(pinField.getPassword());
            if (password.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both Password and PIN.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 1. Verify Credentials
            if (!verifyCredentials(password, pin)) {
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Credentials verified. Submit account deletion request?",
                    "FINAL CONFIRMATION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                // 2. Submit Request
                if (submitDeleteRequest()) {
                    JOptionPane.showMessageDialog(frame,
                            "Deletion request submitted successfully! Your account will remain active" +
                                    " until approved by an administrator.",
                            "Request Submitted", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to submit request.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}