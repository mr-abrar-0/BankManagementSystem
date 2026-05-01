package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdatePassword extends JFrame implements ActionListener {
    private JFrame frame;
    private JLabel image;
    private String accountNumber;
    private String gmail;

    private JPasswordField oldPasswordField, newPasswordField, confirmNewPasswordField;
    private JButton updateButton, backButton;
    private JButton forgotPasswordButton;

    public UpdatePassword(String accNumber, String userGmail) {
        this.accountNumber = accNumber;
        this.gmail = userGmail;
        setupFrame();
    }

    private void setupFrame() {
        int frameWidth = 900;
        int frameHeight = 600;

        frame = new JFrame("Update Password - User: " + gmail);
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

        JLabel header = new JLabel("Change Login Password", SwingConstants.CENTER);
        header.setFont(new Font("Times New Roman", Font.BOLD, 24));
        header.setForeground(new Color(0, 0, 0));
        header.setBounds(450, 250, 300, 35);
        image.add(header);

        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
        Color textColor = Color.BLACK;

        // Old Password
        JLabel oldPassLabel = new JLabel("Old Password:");
        oldPassLabel.setFont(labelFont);
        oldPassLabel.setForeground(textColor);
        oldPassLabel.setBounds(420,320,170,30);
        image.add(oldPassLabel);
        oldPasswordField = new JPasswordField();
        oldPasswordField.setBounds(600,320,170,30);
        image.add(oldPasswordField);

        // New Password
        JLabel newPassLabel = new JLabel("New Pwd (Min 6 chars):");
        newPassLabel.setFont(labelFont);
        newPassLabel.setForeground(textColor);
        newPassLabel.setBounds(420,360,170,30);
        image.add(newPassLabel);
        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(600,360,170,30);
        image.add(newPasswordField);

        // Confirm New Password
        JLabel confirmNewPassLabel = new JLabel("Confirm New Pwd:");
        confirmNewPassLabel.setFont(labelFont);
        confirmNewPassLabel.setForeground(textColor);
        confirmNewPassLabel.setBounds(420,400,170,30);
        image.add(confirmNewPassLabel);
        confirmNewPasswordField = new JPasswordField();
        confirmNewPasswordField.setBounds(600,400,170,30);
        image.add(confirmNewPasswordField);

        // Buttons
        updateButton = new JButton("Change Pwd");
        updateButton.setFont(labelFont);
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(50, 100, 255));
        updateButton.setBounds(600,450,170,30);
        updateButton.addActionListener(this);
        image.add(updateButton);

        backButton = new JButton("Back");
        backButton.setFont(labelFont);
        backButton.setBackground(Color.lightGray);
        backButton.setBounds(420,450,150,30);
        backButton.addActionListener(this);
        image.add(backButton);

        // Forgot Password Button
        forgotPasswordButton = new JButton("Forgot Login Password?");
        forgotPasswordButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        forgotPasswordButton.setForeground(Color.BLUE);
        forgotPasswordButton.setBackground(Color.WHITE);
        forgotPasswordButton.setBounds(420, 485, 350, 30);
        forgotPasswordButton.addActionListener(this);
        image.add(forgotPasswordButton);

        frame.setVisible(true);
    }

    private boolean validateInput(String oldPass, String newPass, String confirmNewPass) {
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmNewPass.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields are required.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (newPass.length() < 6) {
            JOptionPane.showMessageDialog(frame, "New password must be at least 6 characters long.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!newPass.equals(confirmNewPass)) {
            JOptionPane.showMessageDialog(frame, "New Passwords do not match.", "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (oldPass.equals(newPass)) {
            JOptionPane.showMessageDialog(frame, "New Password must be different from the Old Password.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean executePasswordUpdate(String oldPass, String newPass) {
        // Query checks the old password and updates the new one in the personal_info table.
        String sqlUpdate = "UPDATE personal_info SET password = ? WHERE gmail = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {

            if (conn == null) return false;

            updateStmt.setString(1, newPass);
            updateStmt.setString(2, gmail);
            updateStmt.setString(3, oldPass);

            if (updateStmt.executeUpdate() == 1) {
                return true;
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect Old Password or Account not found.",
                        "Authentication Failed", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error during Password update: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
            // Assuming DataClass still holds valid data for returning to the Dashboard
            new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());
        } else if (e.getSource() == updateButton) {
            String oldPass = new String(oldPasswordField.getPassword());
            String newPass = new String(newPasswordField.getPassword());
            String confirmNewPass = new String(confirmNewPasswordField.getPassword());

            if (validateInput(oldPass, newPass, confirmNewPass)) {
                if (executePasswordUpdate(oldPass, newPass)) {
                    JOptionPane.showMessageDialog(frame, "Password updated successfully! You must log in again.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    new LoginClass();
                }
            }
        } else if (e.getSource() == forgotPasswordButton) {
            frame.dispose();
            new ForgotPasswordForm(this.gmail);
        }
    }
}