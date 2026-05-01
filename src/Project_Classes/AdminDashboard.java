package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;

public class AdminDashboard extends JFrame implements ActionListener {
    private JFrame frame;
    // Labels
    private JLabel image;
    private JLabel titleLabel;
    private JLabel welcomeLabel;
    // Buttons
    private JButton accountRequestButton;
    private JButton deleteRequestButton;
    private JButton transactionHistoryButton;
    private JButton taxHistoryButton;
    private JButton viewAccountsButton;
    private JButton logoutButton;
    private JButton totalTaxButton;

    private String adminUsername;
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");
    // Constructor
    public AdminDashboard(String username) {
        this.adminUsername = username;
        DashboardFrame();
    }
    private void DashboardFrame() {
        int frameWidth = 900;
        int frameHeight = 600;

        frame = new JFrame("Bank Management System - Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

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
        Font titleFont = new Font("Times New Roman", Font.BOLD | Font.ITALIC, 30);
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 18);
        Color accentColor = new Color(52, 152, 219); // Blue accent for buttons
        Color titleColor = new Color(0, 0, 150); // Dark Blue

        // Title Label
        titleLabel = new JLabel("Admin Control Panel", SwingConstants.CENTER);
        titleLabel.setBounds(450, 240, 400, 45);
        titleLabel.setForeground(titleColor);
        titleLabel.setFont(titleFont);
        image.add(titleLabel);

        // Welcome Label
        welcomeLabel = new JLabel("Welcome, Admin " + adminUsername);
        welcomeLabel.setBounds(120, 280, 400, 30);
        welcomeLabel.setForeground(Color.DARK_GRAY);
        welcomeLabel.setFont(new Font("Times New Roman", Font.BOLD|Font.ITALIC, 23));
        image.add(welcomeLabel);

        // 1. Account Request
        accountRequestButton = new JButton("Account Request");
        accountRequestButton.setBounds(120,320,250,30);
        accountRequestButton.setFont(buttonFont);
        accountRequestButton.setBackground(accentColor);
        accountRequestButton.setForeground(Color.WHITE);
        accountRequestButton.addActionListener(this);
        image.add(accountRequestButton);

        // 2. View Registered Accounts
        viewAccountsButton = new JButton("View Registered Accounts");
        viewAccountsButton.setBounds(120, 360, 250, 30);
        viewAccountsButton.setFont(buttonFont);
        viewAccountsButton.setBackground(accentColor);
        viewAccountsButton.setForeground(Color.WHITE);
        viewAccountsButton.addActionListener(this);
        image.add(viewAccountsButton);

        // 3. Transaction History
        transactionHistoryButton = new JButton("Transaction History");
        transactionHistoryButton.setBounds(120,400,250,30);
        transactionHistoryButton.setFont(buttonFont);
        transactionHistoryButton.setBackground(accentColor);
        transactionHistoryButton.setForeground(Color.WHITE);
        transactionHistoryButton.addActionListener(this);
        image.add(transactionHistoryButton);

        // 4. Tax History
        taxHistoryButton = new JButton("Tax History (View Details)");
        taxHistoryButton.setBounds(120, 440, 250, 30);
        taxHistoryButton.setFont(buttonFont);
        taxHistoryButton.setBackground(accentColor);
        taxHistoryButton.setForeground(Color.WHITE);
        taxHistoryButton.addActionListener(this);
        image.add(taxHistoryButton);

        // 5. Total Tax Button
        totalTaxButton = new JButton("Total Tax Collected");
        totalTaxButton.setBounds(520, 300, 250, 30);
        totalTaxButton.setFont(buttonFont);
        totalTaxButton.setBackground(new Color(46, 204, 113)); // Bright Green
        totalTaxButton.setForeground(Color.WHITE);
        totalTaxButton.addActionListener(this);
        image.add(totalTaxButton);

        // 6. Delete Request
        deleteRequestButton = new JButton("Delete Request");
        deleteRequestButton.setBounds(120,480,250,30);
        deleteRequestButton.setFont(buttonFont);
        deleteRequestButton.setBackground(accentColor);
        deleteRequestButton.setForeground(Color.WHITE);
        deleteRequestButton.addActionListener(this);
        image.add(deleteRequestButton);

        // Logout Button
        logoutButton = new JButton("Logout");
        logoutButton.setBounds(690, 480, 100, 30);
        logoutButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        logoutButton.setBackground(Color.LIGHT_GRAY);
        logoutButton.addActionListener(this);
        image.add(logoutButton);

        frame.setVisible(true);
    }
    private void displayTotalTax() {
        String sql = "SELECT SUM(tax_amount) AS total_tax FROM tax_history";
        double totalTax = 0.0;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                totalTax = rs.getDouble("total_tax");
            }
            String message = "The total cumulative tax collected from all the accounts is:\n\n" +
                    "PKR " + CURRENCY_FORMAT.format(totalTax);

            JOptionPane.showMessageDialog(frame,
                    message,
                    "Total Tax Collection Summary",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,
                    "Error calculating total tax: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == accountRequestButton) {
            frame.dispose();
            new ViewCreationRequests(adminUsername);
        } else if (e.getSource() == viewAccountsButton) {
            frame.dispose();
            new ViewRegisteredAccounts(adminUsername);
        } else if (e.getSource() == transactionHistoryButton) {
            frame.dispose();
            new AdminTransactionHistory(adminUsername);
        } else if (e.getSource() == taxHistoryButton) {
            frame.dispose();
            new ViewTaxHistory(adminUsername);
        } else if (e.getSource() == totalTaxButton) {
            displayTotalTax();
        }
        else if (e.getSource() == deleteRequestButton) {
            frame.dispose();
            new ViewDeletionRequests(adminUsername);
        } else if (e.getSource() == logoutButton) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to log out?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                new AdminLogin();
            }
        }
    }
}