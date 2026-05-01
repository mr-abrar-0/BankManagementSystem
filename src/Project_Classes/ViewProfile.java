package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewProfile extends JFrame implements ActionListener {
    private JFrame frame;

    private JLabel fullNameLabel;
    private JLabel gmailLabel;
    private JLabel nicNumberLabel;
    private JLabel phoneNumberLabel;
    private JLabel fatherNameLabel;
    private JLabel fatherNicNumberLabel;
    private JLabel fatherPhoneNumberLabel;
    private JLabel addressLabel;
    private JLabel cityLabel;

    // --- ADDED: Filer Status Label Variable ---
    private JLabel filerStatusLabel;
    // ------------------------------------------

    private JLabel accountNumberLabel;
    private JLabel accountTypeLabel;
    private JLabel religionLabel;
    private JLabel purposeLabel;
    private JLabel occupationLabel;
    private JLabel incomeLabel;
    private JLabel qualificationLabel;
    private JLabel currencyTypeLabel;
    private JLabel cardTypeLabel;
    private JLabel servicesLabel;
    private JLabel balanceLabel;

    private JButton backButton;

    public ViewProfile() {
        setupFrame();
    }

    private void setupFrame() {
        int frameWidth = 900;
        int frameHeight = 700;

        frame = new JFrame("View Profile - User: " + DataClass.getFullName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // --- Background Image Setup (Using DashBoard.PNG) ---
        ImageIcon bg = new ImageIcon(getClass().getResource("/Images/DashBoard.PNG"));
        Image bgImage = bg.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
        bg = new ImageIcon(bgImage);
        JLabel background = new JLabel(bg);
        background.setBounds(0, 0, frameWidth, frameHeight);
        frame.setContentPane(background);
        background.setLayout(null);

        // Icon
        ImageIcon logo = new ImageIcon(getClass().getResource("/Images/Logo.jpg"));
        frame.setIconImage(logo.getImage());

        Font font = new Font("Times New Roman", Font.BOLD, 18);

        // =======================================================
        // Column 1: Personal Info
        // =======================================================

        fullNameLabel = new JLabel("Full Name:     " + DataClass.getFullName());
        fullNameLabel.setFont(font);
        fullNameLabel.setForeground(Color.BLACK);
        fullNameLabel.setBounds(100, 200, 400, 30);
        background.add(fullNameLabel);

        gmailLabel = new JLabel("Gmail:     " + DataClass.getGmail());
        gmailLabel.setFont(font);
        gmailLabel.setForeground(Color.BLACK);
        gmailLabel.setBounds(100, 240, 400, 30);
        background.add(gmailLabel);

        nicNumberLabel = new JLabel("CNIC Number:     " + DataClass.getNICNumber());
        nicNumberLabel.setFont(font);
        nicNumberLabel.setForeground(Color.BLACK);
        nicNumberLabel.setBounds(100, 280, 400, 30);
        background.add(nicNumberLabel);

        phoneNumberLabel = new JLabel("Phone Number:     " + DataClass.getPhoneNumber());
        phoneNumberLabel.setFont(font);
        phoneNumberLabel.setForeground(Color.BLACK);
        phoneNumberLabel.setBounds(100, 320, 400, 30);
        background.add(phoneNumberLabel);

        fatherNameLabel = new JLabel("Father's Name:     " + DataClass.getFatherName());
        fatherNameLabel.setFont(font);
        fatherNameLabel.setForeground(Color.BLACK);
        fatherNameLabel.setBounds(100, 360, 400, 30);
        background.add(fatherNameLabel);

        fatherNicNumberLabel = new JLabel("Father's CNIC:     " + DataClass.getFatherNICNumber());
        fatherNicNumberLabel.setFont(font);
        fatherNicNumberLabel.setForeground(Color.BLACK);
        fatherNicNumberLabel.setBounds(100, 400, 400, 30);
        background.add(fatherNicNumberLabel);

        fatherPhoneNumberLabel = new JLabel("Father's Phone No.:     " + DataClass.getFatherPhoneNumber());
        fatherPhoneNumberLabel.setFont(font);
        fatherPhoneNumberLabel.setForeground(Color.BLACK);
        fatherPhoneNumberLabel.setBounds(100, 440, 400, 30);
        background.add(fatherPhoneNumberLabel);

        addressLabel = new JLabel("Address:     " + DataClass.getAddress());
        addressLabel.setFont(font);
        addressLabel.setForeground(Color.BLACK);
        addressLabel.setBounds(100, 480, 600, 30);
        background.add(addressLabel);

        cityLabel = new JLabel("City:     " + DataClass.getCity());
        cityLabel.setFont(font);
        cityLabel.setForeground(Color.BLACK);
        cityLabel.setBounds(100, 520, 400, 30);
        background.add(cityLabel);

        // =======================================================
        // Column 2: Account Info
        // =======================================================

        balanceLabel = new JLabel("Current Balance: PKR " + DataClass.getBalance());
        balanceLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        balanceLabel.setForeground(Color.RED);
        balanceLabel.setBounds(450, 80, 400, 30);
        background.add(balanceLabel);

        accountNumberLabel = new JLabel("Account Number:     " + DataClass.getAccountNumber());
        accountNumberLabel.setFont(font);
        accountNumberLabel.setForeground(Color.BLACK);
        accountNumberLabel.setBounds(450, 120, 400, 30);
        background.add(accountNumberLabel);

        accountTypeLabel = new JLabel("Account Type:    " + DataClass.getAccountType());
        accountTypeLabel.setFont(font);
        accountTypeLabel.setForeground(Color.BLACK);
        accountTypeLabel.setBounds(450, 160, 400, 30);
        background.add(accountTypeLabel);

        religionLabel = new JLabel("Religion:     " + DataClass.getReligion());
        religionLabel.setFont(font);
        religionLabel.setForeground(Color.BLACK);
        religionLabel.setBounds(450, 200, 400, 30);
        background.add(religionLabel);

        purposeLabel = new JLabel("Account Purpose:     " + DataClass.getAccountPurpose());
        purposeLabel.setFont(font);
        purposeLabel.setForeground(Color.BLACK);
        purposeLabel.setBounds(450, 240, 400, 30);
        background.add(purposeLabel);

        occupationLabel = new JLabel("Occupation:     " + DataClass.getOccupation());
        occupationLabel.setFont(font);
        occupationLabel.setForeground(Color.BLACK);
        occupationLabel.setBounds(450, 280, 400, 30);
        background.add(occupationLabel);

        incomeLabel = new JLabel("Monthly Income:     " + DataClass.getMonthlyIncome());
        incomeLabel.setFont(font);
        incomeLabel.setForeground(Color.BLACK);
        incomeLabel.setBounds(450, 320, 400, 30);
        background.add(incomeLabel);

        qualificationLabel = new JLabel("Qualification:     " + DataClass.getQualification());
        qualificationLabel.setFont(font);
        qualificationLabel.setForeground(Color.BLACK);
        qualificationLabel.setBounds(450, 360, 400, 30);
        background.add(qualificationLabel);

        currencyTypeLabel = new JLabel("Currency Type:     " + DataClass.getCurrencyType());
        currencyTypeLabel.setFont(font);
        currencyTypeLabel.setForeground(Color.BLACK);
        currencyTypeLabel.setBounds(450, 400, 400, 30);
        background.add(currencyTypeLabel);

        cardTypeLabel = new JLabel("Card Type:     " + DataClass.getCardType());
        cardTypeLabel.setFont(font);
        cardTypeLabel.setForeground(Color.BLACK);
        cardTypeLabel.setBounds(450, 440, 400, 30);
        background.add(cardTypeLabel);

        // --- NEW: Filer Status Label (Positioned at Y=480) ---
        filerStatusLabel = new JLabel("Tax Status:     " + DataClass.getFilerStatus());
        filerStatusLabel.setFont(font);
        // Highlight Filer status as it affects WHT
        if ("Non-Filer".equalsIgnoreCase(DataClass.getFilerStatus())) {
            filerStatusLabel.setForeground(Color.RED);
        } else {
            filerStatusLabel.setForeground(new Color(0, 128, 0)); // Dark Green for Filer
        }
        filerStatusLabel.setBounds(450, 480, 400, 30);
        background.add(filerStatusLabel);
        // ------------------------------------------------------


        String services = DataClass.getAdditionalServices();
        if (services != null) {
            // Replace internal pipe/comma separators with a comma and space for clean display
            services = services.replaceAll("[\\|,]", ", ");
        } else {
            services = "None";
        }
        // Services Label (Y adjusted from 520 to 520) - Error in original comment, services was at 520, now must be 520+40=520
        servicesLabel = new JLabel("Services:     " + services);
        servicesLabel.setFont(font);
        servicesLabel.setForeground(Color.BLACK);
        servicesLabel.setBounds(450, 520, 550, 30);
        background.add(servicesLabel);

        // --- NOTE: servicesLabel was originally at 520, now it must be shifted down to 520, so the coordinates are the same ---
        // servicesLabel was at 520, but the next item (balanceLabel) was at 80. The original code's spacing was inconsistent.
        // We will keep servicesLabel at Y=520 and adjust backButton accordingly.
        // We will place the back button lower.

        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        backButton.setForeground(Color.white);
        backButton.setBounds(370, 580, 180, 30);
        backButton.setBackground(new Color(0,100,255));
        backButton.addActionListener(this);
        background.add(backButton);

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
            new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());
        }
    }
}