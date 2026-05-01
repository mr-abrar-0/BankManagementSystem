package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;
import java.text.*;

public class AccountForm extends JFrame implements ActionListener {
    // JFrame
    private JFrame frame;
    // JLabels
    private JLabel image;
    private JLabel infoLabel;
    private JLabel accNoLabel;
    private JLabel accNumLabel;
    private JLabel accTypeLabel;
    private JLabel religionLabel;
    private JLabel purposeLabel;
    private JLabel occupationLabel;
    private JLabel incomeLabel;
    private JLabel qualificationLabel;
    private JLabel currencyLabel;
    private JLabel cardLabel;
    private JLabel smsLabel;
    private JLabel emailLabel;
    private JLabel servicesLabel;
    private JLabel pinLabel;
    private JLabel confirmPinLabel;
    // JComboBoxes
    private JComboBox<String> accTypeComboBox;
    private JComboBox<String> religionComboBox;
    private JComboBox<String> purposeBox;
    private JComboBox<String> occupationBox;
    private JComboBox<String> incomeBox;
    private JComboBox<String> qualificationBox;
    private JComboBox<String> currencyBox;
    private JComboBox<String> cardBox;
    // Yes and No Buttons
    private JRadioButton smsYes;
    private JRadioButton smsNo;
    private JRadioButton emailYes;
    private JRadioButton emailNo;
    // Buttons Group
    private ButtonGroup smsGroup;
    private ButtonGroup emailGroup;
    // Check Box Buttons
    private JCheckBox atmCard;
    private JCheckBox mobileBanking;
    private JCheckBox infoCheck;
    private JCheckBox chequeBook;
    // Password Fields
    private JPasswordField pinField;
    private JPasswordField confirmPinField;
    // Buttons
    private JButton backButton;
    private JButton resetButton;
    private JButton submitButton;
    // Constructor
    public AccountForm(){
        AccountFrame();
    }
    // Method
    private void AccountFrame(){
        int frameWidth = 980;
        int frameHeight = 800;
        frame = new JFrame("Bank Management System - Sign-Up - Account Information Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        ImageIcon BackgroundImage = new ImageIcon(getClass().getResource("/Images/SignUp.jpg"));
        Image bgImage = BackgroundImage.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
        BackgroundImage = new ImageIcon(bgImage);
        image = new JLabel(BackgroundImage);
        image.setBounds(0, 0, frameWidth, frameHeight);
        frame.setContentPane(image);
        image.setLayout(null);
        ImageIcon logo = new ImageIcon(getClass().getResource("/Images/Logo.jpg"));
        frame.setIconImage(logo.getImage());

        Font Label = new Font("Times New Roman", Font.BOLD, 18);
        Color color = Color.BLACK;

        // Labels, TextFields, Check Boxes, and ComboBoxes
        infoLabel = new JLabel("Account Information:");
        infoLabel.setBounds(170, 120, 280, 40);
        infoLabel.setForeground(color);
        infoLabel.setFont(new Font("Times New Roman", Font.BOLD,28));
        image.add(infoLabel);

        accNoLabel = new JLabel("Account No:");
        accNoLabel.setFont(Label);
        accNoLabel.setForeground(color);
        accNoLabel.setBounds(110, 180, 150, 30);
        image.add(accNoLabel);
        String accnmbr = generateRandomAccNo();
        DataClass.setAccountNumber(accnmbr);
        accNumLabel = new JLabel(accnmbr);
        accNumLabel.setFont(Label);
        accNumLabel.setForeground(color);
        accNumLabel.setBounds(310, 180, 250, 30);
        image.add(accNumLabel);

        accTypeLabel = new JLabel("Account Type:");
        accTypeLabel.setFont(Label);
        accTypeLabel.setForeground(color);
        accTypeLabel.setBounds(110, 220, 150, 30);
        image.add(accTypeLabel);
        String[] accountTypes = {"Savings", "Current"};
        accTypeComboBox = createComboBox(accountTypes, 280, 220);
        accTypeComboBox.setBounds(280, 220, 170, 30);
        image.add(accTypeComboBox);

        religionLabel = new JLabel("Religion:");
        religionLabel.setFont(Label);
        religionLabel.setForeground(color);
        religionLabel.setBounds(110, 260, 150, 30);
        image.add(religionLabel);
        String[] religions = {"Islam", "Christianity", "Hinduism", "Sikhism", "Buddhism", "Other"};
        religionComboBox = createComboBox(religions,280,260);
        religionComboBox.setBounds(280, 260, 170, 30);
        image.add(religionComboBox);

        purposeLabel = new JLabel("Account Purpose:");
        purposeLabel.setFont(Label);
        purposeLabel.setForeground(color);
        purposeLabel.setBounds(110, 300, 200, 30);
        image.add(purposeLabel);
        String[] purposes = {"Salary Account", "Business", "Student", "Savings", "Personal Use"};
        purposeBox = createComboBox(purposes, 280, 300);
        purposeBox.setBounds(280, 300, 170, 30);
        image.add(purposeBox);

        occupationLabel = new JLabel("Occupation:");
        occupationLabel.setFont(Label);
        occupationLabel.setForeground(color);
        occupationLabel.setBounds(110, 340, 200, 30);
        image.add(occupationLabel);
        String[] jobs = {"Student", "Employee", "Businessman", "Self-Employed", "Retired"};
        occupationBox = createComboBox(jobs, 280, 340);
        occupationBox.setBounds(280, 340, 170, 30);
        image.add(occupationBox);

        incomeLabel = new JLabel("Monthly Income:");
        incomeLabel.setFont(Label);
        incomeLabel.setForeground(color);
        incomeLabel.setBounds(110, 380, 200, 30);
        image.add(incomeLabel);
        String[] incomeRanges = {"Below 20k", "20k - 35k","35k - 50k", "50k - 100k", "Above 100k"};
        incomeBox = createComboBox(incomeRanges, 280, 380);
        incomeBox.setBounds(280, 380, 170, 30);
        image.add(incomeBox);

        qualificationLabel = new JLabel("Qualification:");
        qualificationLabel.setFont(Label);
        qualificationLabel.setForeground(color);
        qualificationLabel.setBounds(110, 420, 200, 30);
        image.add(qualificationLabel);
        String[] qualifications = {
                "Matric",
                "Intermediate",
                "Diploma",
                "Bachelor's",
                "Master's",
                "MPhil",
                "PhD"
        };
        qualificationBox = createComboBox(qualifications, 280, 420);
        qualificationBox.setBounds(280, 420, 170, 30);
        image.add(qualificationBox);

        currencyLabel = new JLabel("Currency Type:");
        currencyLabel.setFont(Label);
        currencyLabel.setForeground(color);
        currencyLabel.setBounds(110, 460, 200, 30);
        image.add(currencyLabel);
        String[] currencyTypes = {"Select Currency", "PKR"};
        currencyBox = createComboBox(currencyTypes, 280, 460);
        currencyBox.setBounds(280, 460, 170, 30);
        image.add(currencyBox);

        cardLabel = new JLabel("Card Type:");
        cardLabel.setFont(Label);
        cardLabel.setForeground(color);
        cardLabel.setBounds(110, 500, 200, 30);
        image.add(cardLabel);
        String[] cardTypes = {"Visa Debit", "MasterCard", "UnionPay", "No Card Needed"};
        cardBox = createComboBox(cardTypes, 280, 500);
        cardBox.setBounds(280, 500, 170, 30);
        image.add(cardBox);

        smsLabel = new JLabel("SMS Alerts:");
        smsLabel.setFont(Label);
        smsLabel.setForeground(color);
        smsLabel.setBounds(500, 220, 200, 30);
        image.add(smsLabel);
        smsYes = new JRadioButton("Yes");
        smsNo = new JRadioButton("No");
        smsYes.setOpaque(false);
        smsNo.setOpaque(false);
        smsNo.setSelected(true);
        smsGroup = new ButtonGroup();
        smsGroup.add(smsYes);
        smsGroup.add(smsNo);
        smsYes.setBounds(650, 220, 80, 30);
        smsNo.setBounds(750, 220, 80, 30);
        image.add(smsYes);
        image.add(smsNo);

        emailLabel = new JLabel("Email Alerts:");
        emailLabel.setFont(Label);
        emailLabel.setForeground(color);
        emailLabel.setBounds(500, 260, 200, 30);
        image.add(emailLabel);
        emailYes = new JRadioButton("Yes");
        emailNo = new JRadioButton("No");
        emailYes.setOpaque(false);
        emailNo.setOpaque(false);
        emailNo.setSelected(true);
        emailGroup = new ButtonGroup();
        emailGroup.add(emailYes);
        emailGroup.add(emailNo);
        emailYes.setBounds(650, 260, 80, 30);
        emailNo.setBounds(750, 260, 80, 30);
        image.add(emailYes);
        image.add(emailNo);

        servicesLabel = new JLabel("Select Services:");
        servicesLabel.setFont(Label);
        servicesLabel.setForeground(color);
        servicesLabel.setBounds(500, 300, 200, 30);
        image.add(servicesLabel);
        atmCard = new JCheckBox("ATM Card");
        chequeBook = new JCheckBox("Cheque Book");
        mobileBanking = new JCheckBox("Mobile Banking");
        atmCard.setOpaque(false);
        chequeBook.setOpaque(false);
        mobileBanking.setOpaque(false);
        atmCard.setBounds(650, 300, 95, 30);
        chequeBook.setBounds(750, 300, 110, 30);
        mobileBanking.setBounds(650, 330, 120, 30);
        image.add(atmCard);
        image.add(chequeBook);
        image.add(mobileBanking);

        pinLabel = new JLabel("Set 4-Digit PIN:");
        pinLabel.setFont(Label);
        pinLabel.setForeground(color);
        pinLabel.setBounds(500, 380, 200, 30);
        image.add(pinLabel);
        pinField = new JPasswordField();
        pinField.setBounds(650, 380, 200, 30);
        image.add(pinField);

        confirmPinLabel = new JLabel("Confirm PIN:");
        confirmPinLabel.setFont(Label);
        confirmPinLabel.setForeground(color);
        confirmPinLabel.setBounds(500, 420, 200, 30);
        image.add(confirmPinLabel);
        confirmPinField = new JPasswordField();
        confirmPinField.setBounds(650, 420, 200, 30);
        image.add(confirmPinField);

        infoCheck = new JCheckBox("I confirm that all the data entered above is correct.");
        infoCheck.setFont(new Font("Times New Roman", Font.BOLD, 16));
        infoCheck.setOpaque(false);
        infoCheck.setBounds(110, 550, 400, 25);
        image.add(infoCheck);

        // Buttons
        submitButton = new JButton("Submit");
        submitButton.setFont(Label);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(new Color(50, 100, 255));
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setBounds(600, 670, 170, 30);
        submitButton.addActionListener(this);
        image.add(submitButton);
        resetButton = new JButton("Reset");
        resetButton.setBounds(400, 670, 170, 30);
        resetButton.setForeground(Color.DARK_GRAY);
        resetButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.setFont(Label);
        resetButton.addActionListener(this);
        image.add(resetButton);
        backButton = new JButton("Back");
        backButton.setFont(Label);
        backButton.setForeground(Color.DARK_GRAY);
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setBounds(200,670,170,30);
        backButton.addActionListener(this);
        image.add(backButton);

        frame.setVisible(true);
    }
    private JComboBox<String> createComboBox(String[] items, int x, int y) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        box.setBackground(new Color(245, 245, 245));
        box.setForeground(Color.BLACK);
        box.setFocusable(false);
        return box;
    }
    // Generates a 12-digit random account number
    private static String generateRandomAccNo() {
        Random random = new Random();
        long min = 100000000000L;
        long max = 999999999999L;
        long randomLong = min + (long) (random.nextDouble() * (max - min + 1));
        return String.valueOf(randomLong);
    }
    private boolean validateInput() {
        String pin = new String(pinField.getPassword());
        String confirmPin = new String(confirmPinField.getPassword());
        if (pin.isEmpty() || confirmPin.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Please set and confirm your PIN.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!pin.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(frame,
                    "PIN must be exactly 4 digits.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!pin.equals(confirmPin)) {
            JOptionPane.showMessageDialog(frame,
                    "PIN and Confirm PIN do not match.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Currency Type must be PKR
        if (currencyBox.getSelectedItem() == null || !currencyBox.getSelectedItem().equals("PKR")) {
            JOptionPane.showMessageDialog(frame,
                    "Currently, only PKR currency type is supported. Please select PKR.",
                    "Currency Selection Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Ensure basic ComboBoxes are selected
        if (accTypeComboBox.getSelectedIndex() == -1 || religionComboBox.getSelectedIndex() == -1 ||
                purposeBox.getSelectedIndex() == -1 || occupationBox.getSelectedIndex() == -1 ||
                incomeBox.getSelectedIndex() == -1 || qualificationBox.getSelectedIndex() == -1 ||
                cardBox.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(frame,
                    "Please select a value for all required ComboBox fields " +
                            "(Type, Religion, Purpose, Occupation, Income, Qualification, Card).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!atmCard.isSelected() && !chequeBook.isSelected() && !mobileBanking.isSelected()
                && smsNo.isSelected() && emailNo.isSelected()) {
            int confirmServices = JOptionPane.showConfirmDialog(frame,
                    "You have not selected any additional services or alerts. Proceed without them?",
                    "Confirm Services", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirmServices == JOptionPane.NO_OPTION) {
                return false;
            }
        }
        if (!infoCheck.isSelected()) {
            JOptionPane.showMessageDialog(frame,
                    "Please confirm that your information is correct by checking the box.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    private void resetForm() {
        accTypeComboBox.setSelectedIndex(0);
        religionComboBox.setSelectedIndex(0);
        purposeBox.setSelectedIndex(0);
        occupationBox.setSelectedIndex(0);
        incomeBox.setSelectedIndex(0);
        qualificationBox.setSelectedIndex(0);
        currencyBox.setSelectedIndex(0);
        cardBox.setSelectedIndex(0);
        pinField.setText("");
        confirmPinField.setText("");
        smsNo.setSelected(true);
        emailNo.setSelected(true);
        atmCard.setSelected(false);
        chequeBook.setSelected(false);
        mobileBanking.setSelected(false);
        infoCheck.setSelected(false);
        String newAccNo = generateRandomAccNo();
        accNumLabel.setText(newAccNo);
        DataClass.setAccountNumber(newAccNo);
    }
    private boolean submitAccountCreationRequest() {
        String gmail = DataClass.getGmail();
        String accNumber = DataClass.getAccountNumber();
        // 1. Collect Account Data (and set DataClass)
        String accType = (String) accTypeComboBox.getSelectedItem();
        DataClass.setAccountType(accType);
        String religion = (String) religionComboBox.getSelectedItem();
        DataClass.setReligion(religion);
        String purpose = (String) purposeBox.getSelectedItem();
        DataClass.setAccountPurpose(purpose);
        String occupation = (String) occupationBox.getSelectedItem();
        DataClass.setOccupation(occupation);
        String monthlyIncome = (String) incomeBox.getSelectedItem();
        DataClass.setMonthlyIncome(monthlyIncome);
        String qualification = (String) qualificationBox.getSelectedItem();
        DataClass.setQualification(qualification);
        String currency = (String) currencyBox.getSelectedItem();
        DataClass.setCurrencyType(currency);
        String cardType = (String) cardBox.getSelectedItem();
        DataClass.setCardType(cardType);
        String pin = new String(pinField.getPassword());
        DataClass.setAccountPin(pin);
        String smsAlerts = smsYes.isSelected() ? "Yes" : "No";
        DataClass.setSmsAlerts(smsAlerts);
        String emailAlerts = emailYes.isSelected() ? "Yes" : "No";
        DataClass.setEmailAlerts(emailAlerts);
        String services = (atmCard.isSelected() ? "ATM," : "") +
                (chequeBook.isSelected() ? "Cheque," : "") +
                (mobileBanking.isSelected() ? "Mobile" : "");
        String safeServices = services.replaceAll(",$",
                "").replaceAll(",", "|");
        DataClass.setAdditionalServices(safeServices);
        String applicationData = serializeApplicationData();
        String sql = "INSERT INTO pending_requests (" +
                "user_gmail, request_type, application_data) " +
                "VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            pstmt.setString(1, gmail);
            pstmt.setString(2, "ACCOUNT_CREATION");
            pstmt.setString(3, applicationData);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame,
                    "Database Error: Failed to submit application request: " + e.getMessage(),
                    "SQL Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
    private String serializeApplicationData() {
        StringBuilder sb = new StringBuilder();
        sb.append("fullName:").append(DataClass.getFullName()).append(",");
        sb.append("nicNumber:").append(DataClass.getNICNumber()).append(",");
        sb.append("phoneNumber:").append(DataClass.getPhoneNumber()).append(",");
        sb.append("gmail:").append(DataClass.getGmail()).append(",");
        sb.append("dob:").append(DataClass.getDateOfBirth()).append(",");
        sb.append("gender:").append(DataClass.getGender()).append(",");
        sb.append("maritalStatus:").append(DataClass.getMaritalStatus()).append(",");
        sb.append("address:").append(DataClass.getAddress()).append(",");
        sb.append("city:").append(DataClass.getCity()).append(",");
        sb.append("fatherName:").append(DataClass.getFatherName()).append(",");
        sb.append("fatherNic:").append(DataClass.getFatherNICNumber()).append(",");
        sb.append("fatherPhone:").append(DataClass.getFatherPhoneNumber()).append(",");
        sb.append("password:").append(DataClass.getPassword()).append(",");
        // Account Info
        sb.append("accountNumber:").append(DataClass.getAccountNumber()).append(",");
        sb.append("accountType:").append(DataClass.getAccountType()).append(",");
        sb.append("religion:").append(DataClass.getReligion()).append(",");
        sb.append("purpose:").append(DataClass.getAccountPurpose()).append(",");
        sb.append("occupation:").append(DataClass.getOccupation()).append(",");
        sb.append("monthlyIncome:").append(DataClass.getMonthlyIncome()).append(",");
        sb.append("qualification:").append(DataClass.getQualification()).append(",");
        sb.append("currencyType:").append(DataClass.getCurrencyType()).append(",");
        sb.append("cardType:").append(DataClass.getCardType()).append(",");
        sb.append("pin:").append(DataClass.getAccountPin()).append(",");
        sb.append("smsAlerts:").append(DataClass.getSmsAlerts()).append(",");
        sb.append("emailAlerts:").append(DataClass.getEmailAlerts()).append(",");
        sb.append("services:").append(DataClass.getAdditionalServices());
        return sb.toString();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to go back? Account data will be lost.",
                    "Confirm Navigation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                new PersonalForm();
            }
        } else if (e.getSource() == resetButton) {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to clear all entered data?",
                    "Confirm Reset",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                resetForm();
                JOptionPane.showMessageDialog(
                        frame,
                        "All fields have been cleared.",
                        "Form Reset",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else if (e.getSource() == submitButton) {
            if (validateInput()) {
                if (submitAccountCreationRequest()) {
                    JOptionPane.showMessageDialog(frame,
                            "Application Submitted Successfully!\nYour account " +
                                    "is now pending administrative approval.",
                            "Review Pending", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    new LoginClass();
                }
            }
        }
    }
}