package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminLogin extends JFrame implements ActionListener {
    // Frame
    private JFrame frame;
    // Labels
    private JLabel image;
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    // Text Fields
    private JTextField usernameField;
    // Password Fields
    private JPasswordField passwordField;
    // Buttons
    private JButton loginButton;
    private JButton backButton;
    // Constructor
    public AdminLogin() {
        AdminLoginFrame();
    }
    // Method to set up the Admin Login GUI
    private void AdminLoginFrame() {
        int frameWidth = 900;
        int frameHeight = 600;
        // Frame
        frame = new JFrame("Bank Management System - Admin Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        // Logo Image
        ImageIcon logo = new ImageIcon(getClass().getResource("/Images/Logo.jpg"));
        frame.setIconImage(logo.getImage());

        // Font and Color
        Font titleFont = new Font("Times New Roman", Font.BOLD, 24);
        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
        Color color = Color.BLACK;

        // Heading Label
        titleLabel = new JLabel("Administrator Login");
        titleLabel.setBounds(500, 240, 300, 40);
        titleLabel.setForeground(new Color(0, 0, 150));
        titleLabel.setFont(titleFont);
        image.add(titleLabel);

        // Username Label
        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(500, 275, 100, 30);
        usernameLabel.setForeground(color);
        usernameLabel.setFont(labelFont);
        image.add(usernameLabel);

        // Username TextField
        usernameField = new JTextField();
        usernameField.setBounds(500, 300, 250, 30);
        image.add(usernameField);

        // Password Label
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(500, 330, 100, 30);
        passwordLabel.setForeground(color);
        passwordLabel.setFont(labelFont);
        image.add(passwordLabel);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(500, 355, 250, 30);
        image.add(passwordField);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(630, 400, 120, 30);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(50, 100, 255));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setFont(labelFont);
        loginButton.addActionListener(this);
        image.add(loginButton);

        // Back Button
        backButton = new JButton("Back");
        backButton.setBounds(500, 400, 120, 30);
        backButton.setForeground(color);
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setFont(labelFont);
        backButton.addActionListener(this);
        image.add(backButton);

        frame.setVisible(true);
    }

    private boolean authenticateAdmin(String username, String password) {
        String sql = "SELECT username FROM admin_users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,
                    "Database error during admin login. Check database structure/connection.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter both Username and Password!",
                        "Empty Fields", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (authenticateAdmin(username, password)) {
                JOptionPane.showMessageDialog(frame,
                        "Admin Login Successful! Welcome, " + username + ".",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                frame.dispose();
                new AdminDashboard(username);

            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid Username or Password for Administrator.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == backButton) {
            frame.dispose();
            new LoginClass();
        }
    }
}