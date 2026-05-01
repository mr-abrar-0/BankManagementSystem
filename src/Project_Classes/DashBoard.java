package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class DashBoard extends JFrame implements ActionListener {
    // Frame
    private JFrame frame;
    // Labels
    private JLabel welcomeLabel;
    private JLabel nameLabel;
    private JLabel accNoLabel;
    private JLabel background;
    // Buttons
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton fundTransferButton;
    private JButton helpButton;
    private JButton logoutButton;
    private JButton exitButton;
    private JButton balanceInquiryButton;
    // Profile Buttons
    private JButton viewProfileButton;
    private JButton updateProfileButton;
    // Other Buttons
    private JButton transactionHistoryButton;
    private JButton updatePinButton;
    private JButton updatePasswordButton;
    private JButton deleteAccountButton;
    // Store user data
    private String HolderName;
    private String accountNumber;
    // Format
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");
    public DashBoard(String name, String nmbr) {
        this.HolderName = name;
        this.accountNumber = nmbr;
        FrameSetUp();
    }
    // Method
    private void FrameSetUp() {
        // Frame
        frame = new JFrame("Bank Management System - Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        // Background
        ImageIcon bg = new ImageIcon(getClass().getResource("/Images/DashBoard.PNG"));
        Image bgImage = bg.getImage().getScaledInstance(900, 700, Image.SCALE_SMOOTH);
        bg = new ImageIcon(bgImage);
        background = new JLabel(bg);
        background.setBounds(0, 0, 900, 700);
        frame.setContentPane(background);
        background.setLayout(null);
        // Icon
        ImageIcon logo = new ImageIcon(getClass().getResource("/Images/Logo.jpg"));
        frame.setIconImage(logo.getImage());
        // Fonts and colors
        Font headerFont = new Font("Segoe UI", Font.BOLD, 20);
        Color textColor = Color.black;
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 15);
        // Labels
        welcomeLabel = new JLabel("Welcome To The Bank Dashboard!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        welcomeLabel.setForeground(textColor);
        welcomeLabel.setBounds(300, 80, 600, 30);
        background.add(welcomeLabel);
        // Name and number
        nameLabel = new JLabel("Account Holder:     " + HolderName);
        nameLabel.setFont(headerFont);
        nameLabel.setForeground(textColor);
        nameLabel.setBounds(180, 200, 600, 30);
        background.add(nameLabel);
        accNoLabel = new JLabel("Account Number:   " + accountNumber);
        accNoLabel.setFont(headerFont);
        accNoLabel.setForeground(textColor);
        accNoLabel.setBounds(180, 230, 600, 30);
        background.add(accNoLabel);
        // BUTTONS
        updateProfileButton = createButton("Update Profile", 170, 300, buttonFont);
        depositButton = createButton("Deposit", 370, 300, buttonFont);
        withdrawButton = createButton("Withdraw", 570, 300, buttonFont);

        balanceInquiryButton = createButton("View Balance", 170, 350, buttonFont);
        fundTransferButton = createButton("Fund Transfer", 370, 350, buttonFont);
        viewProfileButton = createButton("View Profile", 570, 350, buttonFont);

        transactionHistoryButton = createButton("Txn History", 170, 400, buttonFont);
        updatePinButton = createButton("Change PIN", 370, 400, buttonFont);
        updatePasswordButton = createButton("Change Pwd", 570, 400, buttonFont);

        exitButton = createButton("Exit", 70, 600, buttonFont);
        logoutButton = createButton("Logout", 270, 600, buttonFont);
        helpButton = createButton("Help", 470, 600, buttonFont);
        deleteAccountButton = createButton("Delete Account", 670, 600, buttonFont);

        JButton[] buttons = {depositButton, withdrawButton,
                fundTransferButton, logoutButton, exitButton, helpButton,
                balanceInquiryButton, transactionHistoryButton, updatePinButton,
                updatePasswordButton, updateProfileButton, deleteAccountButton,
                viewProfileButton};

        for (JButton btn : buttons) {
            btn.addActionListener(this);
            background.add(btn);
        }

        frame.setVisible(true);
    }
    private JButton createButton(String text, int x, int y, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 100, 255));
        button.setBounds(x, y, 150, 35);
        return button;
    }
    private void fetchAndUpdateBalance() {
        double balance = DataClass.getBalance();
        String formattedBalance = "PKR " + CURRENCY_FORMAT.format(balance);
        JOptionPane.showMessageDialog(frame,
                "Your Current Account Balance is:\n" + formattedBalance,
                "Balance Inquiry Result",
                JOptionPane.INFORMATION_MESSAGE);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == depositButton) {
            frame.dispose();
            new Deposit(DataClass.getAccountNumber());
        } else if (e.getSource() == withdrawButton) {
            frame.dispose();
            new Withdraw(DataClass.getAccountNumber());
        } else if (e.getSource() == fundTransferButton) {
            frame.dispose();
            new Fund_Transfer(DataClass.getAccountNumber());
        }
        else if (e.getSource() == balanceInquiryButton){
            fetchAndUpdateBalance();
        } else if (e.getSource() == transactionHistoryButton){
            frame.dispose();
            new TransactionHistory(DataClass.getAccountNumber());
        }
        else if (e.getSource() == updatePinButton){
            frame.dispose();
            new UpdatePin(DataClass.getAccountNumber(), DataClass.getGmail());
        } else if (e.getSource() == updatePasswordButton){
            frame.dispose();
            new UpdatePassword(DataClass.getAccountNumber(), DataClass.getGmail());
        }
        else if (e.getSource() == viewProfileButton){
            frame.dispose();
            new ViewProfile();
        }
        else if (e.getSource() == updateProfileButton){
            frame.dispose();
            new UpdateProfile();
        } else if (e.getSource() == deleteAccountButton){
            new DeleteAccount();
        }
        else if (e.getSource() == logoutButton) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?",
                    "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DataClass.setFullName(null);
                DataClass.setAccountNumber(null);
                DataClass.setBalance(0.00);
                DataClass.setGmail(null);
                DataClass.setPhoneNumber(null);
                DataClass.setMonthlyIncome(null);
                frame.dispose();
                new LoginClass();
            }
        } else if (e.getSource() == exitButton) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?",
                    "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else if (e.getSource() == helpButton) {
            JOptionPane.showMessageDialog(frame,
                    "Bank Management System Dashboard Help:\n" +
                            "\n--- Transactions ---\n" +
                            "- Deposit / Withdraw / Fund Transfer: Basic financial operations.\n" +
                            "\n--- Account Updates ---\n" +
                            "- Txn History: View transaction records.\n" +
                            "- Change PIN/Password: Change security credentials.\n" +
                            "- View Profile: Displays current profile information.\n" +
                            "- Update Profile: Modify personal details (Phone, Address, etc.).\n" +
                            "- Delete Account: Permanently close the account.\n" +
                            "\n--- System ---\n" +
                            "- View Balance: Check current funds.\n" +
                            "- Logout / Exit: End session or close application.",
                    "Help",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}