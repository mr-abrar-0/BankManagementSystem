package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ForgotPinForm extends JFrame implements ActionListener {
    private JFrame frame;
    private JLabel image;
    private String accountNumber;
    private String userGmail;

    private JTextField emailField;
    private JPasswordField newPinField, confirmNewPinField;
    private JButton resetPinButton, backToDashboardButton;

    public ForgotPinForm(String accNumber, String userGmail) {
        this.accountNumber = accNumber;
        this.userGmail = userGmail;
        setupFrame();
    }

    private void setupFrame() {
        int frameWidth = 900;
        int frameHeight = 600;

        frame = new JFrame("Forgot PIN");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Background Image Setup (Simplified)
        ImageIcon BackgroundImage = new ImageIcon(getClass().getResource("/Images/Login.jpg"));
        Image bgImage = BackgroundImage.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
        image = new JLabel(new ImageIcon(bgImage));
        image.setBounds(0, 0, frameWidth, frameHeight);
        frame.setContentPane(image);
        image.setLayout(null);

        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
        Color textColor = Color.BLACK;

        JLabel header = new JLabel("Forgot PIN", SwingConstants.CENTER);
        header.setFont(new Font("Times New Roman", Font.BOLD, 24));
        header.setForeground(textColor);
        header.setBounds(320, 240, 400, 35);
        image.add(header);

        // 1. Email Input Field
        JLabel emailLabel = new JLabel("Email ID:");
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(textColor);
        emailLabel.setBounds(450, 300, 170, 30);
        image.add(emailLabel);
        emailField = new JTextField(userGmail);
        emailField.setBounds(620, 300, 150, 30);
        image.add(emailField);

        // New PIN
        JLabel newPinLabel = new JLabel("New PIN (4 Digits):");
        newPinLabel.setFont(labelFont);
        newPinLabel.setForeground(textColor);
        newPinLabel.setBounds(450, 350, 170, 30);
        image.add(newPinLabel);
        newPinField = new JPasswordField(4);
        newPinField.setBounds(620, 350, 150, 30);
        image.add(newPinField);

        // Confirm New PIN
        JLabel confirmNewPinLabel = new JLabel("Confirm New PIN:");
        confirmNewPinLabel.setFont(labelFont);
        confirmNewPinLabel.setForeground(textColor);
        confirmNewPinLabel.setBounds(450, 390, 170, 30);
        image.add(confirmNewPinLabel);
        confirmNewPinField = new JPasswordField(4);
        confirmNewPinField.setBounds(620, 390, 150, 30);
        image.add(confirmNewPinField);

        // Reset Button
        resetPinButton = new JButton("Change PIN");
        resetPinButton.setFont(labelFont);
        resetPinButton.setForeground(Color.WHITE);
        resetPinButton.setBackground(new Color(50,100,255));
        resetPinButton.setBounds(620, 450, 150, 30);
        resetPinButton.addActionListener(this);
        image.add(resetPinButton);

        // Back Button
        backToDashboardButton = new JButton("Back");
        backToDashboardButton.setFont(labelFont);
        backToDashboardButton.setForeground(Color.DARK_GRAY);
        backToDashboardButton.setBackground(Color.LIGHT_GRAY);
        backToDashboardButton.setBounds(450, 450, 160, 30);
        backToDashboardButton.addActionListener(this);
        image.add(backToDashboardButton);

        frame.setVisible(true);
    }
    private String getAccountNumberByEmail(String email) {
        // Query joins personal_info (for email) and account_info (for account_number)
        String sql = "SELECT T2.account_number FROM personal_info T1 JOIN account_info T2 ON T1.gmail = T2.user_gmail WHERE T1.gmail = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("account_number");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error during email lookup.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null; // Email not found or account info missing
    }

    private boolean resetAccountPin(String targetAccountNumber, String newPin) {
        // Update the account_pin using the validated account number
        String sqlUpdate = "UPDATE account_info SET account_pin = ? WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

            pstmt.setString(1, newPin);
            pstmt.setString(2, targetAccountNumber);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error during PIN reset.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToDashboardButton) {
            frame.dispose();
            // Go back to the DashBoard
            new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());

        } else if (e.getSource() == resetPinButton) {
            String email = emailField.getText().trim();
            String newPin = new String(newPinField.getPassword());
            String confirmNewPin = new String(confirmNewPinField.getPassword());

            // 1. Basic Validation
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your Email ID.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPin.matches("\\d{4}") || !newPin.equals(confirmNewPin)) {
                JOptionPane.showMessageDialog(frame, "New PIN must be 4 digits and match the confirmation.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Lookup Account Number using Email
            String targetAccountNumber = getAccountNumberByEmail(email);

            if (targetAccountNumber == null) {
                JOptionPane.showMessageDialog(frame, "Email ID not found or account setup is incomplete.",
                        "Reset Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Execute PIN Reset
            if (resetAccountPin(targetAccountNumber, newPin)) {
                JOptionPane.showMessageDialog(frame, "PIN reset successfully! Use your new PIN for transactions.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                frame.dispose();
                // Check if the current user is resetting their own PIN (by matching email)
                if (email.equals(DataClass.getGmail())) {
                    // If it's the current user, return to the Dashboard
                    new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());
                } else {
                    // If an administrative user or someone else used this screen, log them out
                    new LoginClass();
                }
            }
        }
    }
}