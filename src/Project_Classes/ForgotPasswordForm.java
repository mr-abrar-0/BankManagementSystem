package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ForgotPasswordForm extends JFrame implements ActionListener {
    private JFrame frame;
    private JLabel image;
    private String userGmail;

    private JTextField emailField;
    private JPasswordField newPasswordField, confirmNewPasswordField;
    private JButton resetPasswordButton, backToLoginButton;

    // Constructor accepts the email, primarily for pre-filling convenience
    public ForgotPasswordForm(String currentGmail) {
        this.userGmail = currentGmail;
        setupFrame();
    }

    // Constructor for users starting the reset from the Login screen
    public ForgotPasswordForm() {
        this.userGmail = "";
        setupFrame();
    }

    private void setupFrame() {
        int frameWidth = 900;
        int frameHeight = 600;

        frame = new JFrame("Forgot Password - Reset Login Credentials");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Background Image Setup
        ImageIcon BackgroundImage = new ImageIcon(getClass().getResource("/Images/Login.jpg"));
        Image bgImage = BackgroundImage.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
        image = new JLabel(new ImageIcon(bgImage));
        image.setBounds(0, 0, frameWidth, frameHeight);
        frame.setContentPane(image);
        image.setLayout(null);

        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
        Color textColor = Color.BLACK;

        JLabel header = new JLabel("Forgot Login Password", SwingConstants.CENTER);
        header.setFont(new Font("Times New Roman", Font.BOLD, 24));
        header.setForeground(textColor);
        header.setBounds(400, 250, 400, 35);
        image.add(header);

        // --- 1. Email Input Field ---
        JLabel emailLabel = new JLabel("Email ID:");
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(textColor);
        emailLabel.setBounds(450, 300, 170, 30);
        image.add(emailLabel);
        emailField = new JTextField(userGmail); // Pre-fill if known
        emailField.setBounds(620, 300, 150, 30);
        image.add(emailField);

        // New Password
        JLabel newPasswordLabel = new JLabel("New Pwd (Min 6 chars):");
        newPasswordLabel.setFont(labelFont);
        newPasswordLabel.setForeground(textColor);
        newPasswordLabel.setBounds(450, 350, 170, 30);
        image.add(newPasswordLabel);
        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(620, 350, 150, 30);
        image.add(newPasswordField);

        // Confirm New Password
        JLabel confirmNewPasswordLabel = new JLabel("Confirm New Pwd:");
        confirmNewPasswordLabel.setFont(labelFont);
        confirmNewPasswordLabel.setForeground(textColor);
        confirmNewPasswordLabel.setBounds(450, 390, 170, 30);
        image.add(confirmNewPasswordLabel);
        confirmNewPasswordField = new JPasswordField();
        confirmNewPasswordField.setBounds(620, 390, 150, 30);
        image.add(confirmNewPasswordField);

        // Reset Button
        resetPasswordButton = new JButton("Change Pwd");
        resetPasswordButton.setFont(labelFont);
        resetPasswordButton.setForeground(Color.WHITE);
        resetPasswordButton.setBackground(new Color(50,100,255));
        resetPasswordButton.setBounds(620, 450, 150, 30);
        resetPasswordButton.addActionListener(this);
        image.add(resetPasswordButton);

        // Back Button
        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setFont(labelFont);
        backToLoginButton.setForeground(Color.DARK_GRAY);
        backToLoginButton.setBackground(Color.LIGHT_GRAY);
        backToLoginButton.setBounds(450, 450, 160, 30);
        backToLoginButton.addActionListener(this);
        image.add(backToLoginButton);

        frame.setVisible(true);
    }

    private boolean executePasswordReset(String email, String newPassword) {
        // SQL updates the password where the gmail matches
        String sqlUpdate = "UPDATE personal_info SET password = ? WHERE gmail = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);

            // Check if exactly one row was updated (meaning the email was found)
            if (pstmt.executeUpdate() == 1) {
                return true;
            } else {
                JOptionPane.showMessageDialog(frame, "The entered Email ID was not found in our records.",
                        "Update Failed", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error during password reset: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToLoginButton) {
            frame.dispose();
            // User goes back to the main login screen
            new LoginClass();

        } else if (e.getSource() == resetPasswordButton) {
            String email = emailField.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());
            String confirmNewPassword = new String(confirmNewPasswordField.getPassword());

            // 1. Validation Checks
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your Email ID.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (newPassword.length() < 6) {
                JOptionPane.showMessageDialog(frame, "New password must be at least 6 characters long.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPassword.equals(confirmNewPassword)) {
                JOptionPane.showMessageDialog(frame, "New passwords do not match.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Execute Password Reset
            if (executePasswordReset(email, newPassword)) {
                JOptionPane.showMessageDialog(frame, "Password reset successfully! " +
                                "Please log in with your new password.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Successful reset completes the session and sends the user to login
                frame.dispose();
                new LoginClass();
            }
        }
    }
}