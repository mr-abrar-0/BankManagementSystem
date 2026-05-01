package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginClass extends JFrame implements ActionListener {
    // Frame
    private JFrame frame;
    // Labels
    private JLabel image;
    private JLabel loginLabel;
    private JLabel gmailLabel;
    private JLabel passwordLabel;
    private JLabel createAccountLabel;
    private JLabel adminLoginLabel;
    // Text Field
    private JTextField gmailField;
    // Password Field
    private JPasswordField passwordField;
    // Buttons
    private JButton loginButton;
    private JButton signupButton;
    private JButton adminLoginButton;
    private JButton forgotPasswordButton;
    // Constructor
    public LoginClass() {
        LoginFrame();
    }
    // Method
    private void LoginFrame() {
        int frameWidth = 900;
        int frameHeight = 600;
        // Frame
        frame = new JFrame("Bank Management System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        // GUI Setup
        // Background Image
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
        Font Label = new Font("Times New Roman", Font.BOLD, 16);
        Color color = Color.BLACK;
        // Heading Label
        loginLabel = new JLabel("Login Here To Continue!");
        loginLabel.setBounds(500, 250, 400, 30);
        loginLabel.setForeground(color);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.setFont(Label);
        image.add(loginLabel);
        // Gmail Label
        gmailLabel = new JLabel("Email:");
        gmailLabel.setBounds(500, 275, 100, 30);
        gmailLabel.setForeground(color);
        gmailLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gmailLabel.setFont(Label);
        image.add(gmailLabel);
        // Gmail TextField
        gmailField = new JTextField();
        gmailField.setBounds(500, 300, 250, 30);
        image.add(gmailField);
        // Password Label
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(500, 330, 100, 30);
        passwordLabel.setForeground(color);
        passwordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        passwordLabel.setFont(Label);
        image.add(passwordLabel);
        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(500, 355, 250, 30);
        image.add(passwordField);
        // Forgot Password Button
        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setBounds(550, 390, 150, 15);
        forgotPasswordButton.setForeground(Color.BLUE);
        forgotPasswordButton.setBackground(Color.WHITE);
        forgotPasswordButton.setFont(new Font("Times New Roman", Font.BOLD, 12));
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setOpaque(false);
        forgotPasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.addActionListener(this);
        image.add(forgotPasswordButton);
        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(575, 410, 100, 30);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(50, 100, 255));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setFont(Label);
        loginButton.addActionListener(this);
        image.add(loginButton);
        // "Create Account Here" Label
        createAccountLabel = new JLabel("Create Account Here");
        createAccountLabel.setBounds(555, 440, 200, 30);
        createAccountLabel.setForeground(color);
        createAccountLabel.setFont(Label);
        createAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        image.add(createAccountLabel);
        // signup Button
        signupButton = new JButton("Sign-Up");
        signupButton.setBounds(575, 470, 100, 30);
        signupButton.setForeground(Color.WHITE);
        signupButton.setBackground(new Color(50, 100, 255));
        signupButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupButton.setFont(Label);
        signupButton.addActionListener(this);
        image.add(signupButton);
        // Admin Label
        adminLoginLabel = new JLabel("Admin Login Here");
        adminLoginLabel.setBounds(180, 280, 200, 30);
        adminLoginLabel.setForeground(Color.BLACK);
        adminLoginLabel.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 14));
        adminLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        image.add(adminLoginLabel);
        // Admin Button
        adminLoginButton = new JButton("Admin Login");
        adminLoginButton.setBounds(180, 310, 120, 30);
        adminLoginButton.setForeground(Color.WHITE);
        adminLoginButton.setBackground(new Color(50, 100, 255));
        adminLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminLoginButton.setFont(Label);
        adminLoginButton.addActionListener(this);
        image.add(adminLoginButton);

        frame.setVisible(true);
    }
    // User Authentication and Data Retrieval
    private String[] authenticateAndLoadUser(String gmail, String password) {
        String fullName = null;
        String accountNumber = null;
        Double balance = null;
        // 1. Initial Authentication and Primary Info Retrieval
        String sqlAuth = "SELECT full_name, nic_number, phone_number, father_name, father_nic, father_phone, " +
                "address, city, filer_status FROM personal_info WHERE gmail = ? AND password = ?";
        // 2. Account Details Retrieval (Joined by gmail)
        String sqlAccount = "SELECT acc.account_number, acc.account_type, acc.religion, acc.purpose, " +
                "acc.occupation, acc.monthly_income, acc.qualification, acc.currency_type, acc.card_type, " +
                "acc.additional_services, bal.current_balance FROM account_info acc " +
                "JOIN account_balance bal ON acc.account_number = bal.account_number WHERE acc.user_gmail = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                return null;
            }
            // AUTHENTICATE and Get Personal Info
            try (PreparedStatement pstmt = conn.prepareStatement(sqlAuth)) {
                pstmt.setString(1, gmail);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        fullName = rs.getString("full_name");
                        // Store Personal Info
                        DataClass.setFullName(fullName);
                        DataClass.setGmail(gmail);
                        DataClass.setNICNumber(rs.getString("nic_number"));
                        DataClass.setPhoneNumber(rs.getString("phone_number"));
                        DataClass.setFatherName(rs.getString("father_name"));
                        DataClass.setFatherNICNumber(rs.getString("father_nic"));
                        DataClass.setFatherPhoneNumber(rs.getString("father_phone"));
                        DataClass.setAddress(rs.getString("address"));
                        DataClass.setCity(rs.getString("city"));
                        DataClass.setFilerStatus(rs.getString("filer_status"));
                    } else {
                        return null;
                    }
                }
            }
            // Get Account Info and Balance
            if (fullName != null) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlAccount)) {
                    pstmt.setString(1, gmail);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            accountNumber = rs.getString("account_number");
                            balance = rs.getDouble("current_balance");
                            // Store Account Info
                            DataClass.setAccountNumber(accountNumber);
                            DataClass.setBalance(balance);
                            DataClass.setAccountType(rs.getString("account_type"));
                            DataClass.setReligion(rs.getString("religion"));
                            DataClass.setAccountPurpose(rs.getString("purpose"));
                            DataClass.setOccupation(rs.getString("occupation"));
                            DataClass.setMonthlyIncome(rs.getString("monthly_income"));
                            DataClass.setQualification(rs.getString("qualification"));
                            DataClass.setCurrencyType(rs.getString("currency_type"));
                            DataClass.setCardType(rs.getString("card_type"));
                            DataClass.setAdditionalServices(rs.getString("additional_services"));
                        } else {
                            JOptionPane.showMessageDialog(frame,
                                    "Account setup incomplete or balance record missing.",
                                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                            return null;
                        }
                    }
                }
            }
            if (fullName != null && accountNumber != null && balance != null) {
                return new String[]{fullName, accountNumber};
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,
                    "Database error during login. Check database structure/connection and column names.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton){
            frame.dispose();
//            frame.setVisible(false);
            new PersonalForm();
        }
        else if (e.getSource() == loginButton){
            String gmail = gmailField.getText();
            String password = new String(passwordField.getPassword());
            if (gmail.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter both Gmail and Password!",
                        "Empty Fields", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] userData = authenticateAndLoadUser(gmail, password);
            if (userData != null) {
                String fullName = userData[0];
                String accountNumber = userData[1];
                JOptionPane.showMessageDialog(frame,
                        "Login Successful! Welcome, " + fullName + ".",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new DashBoard(fullName, accountNumber);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid Gmail or Password. Please try again.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (e.getSource() == forgotPasswordButton){
            frame.dispose();
            new ForgotPasswordForm();
        }
        else if (e.getSource() == adminLoginButton){
            frame.dispose();
            new AdminLogin();
        }
    }
    public static void main(String[] args) {
        new LoginClass();
    }
}