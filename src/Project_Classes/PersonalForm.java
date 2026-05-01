package Project_Classes;

import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;
import java.text.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class PersonalForm extends JFrame implements ActionListener {
    // Frame
    private JFrame frame;
    //Labels
    private JLabel image;
    private JLabel infoLabel;
    private JLabel nameLabel;
    private JLabel nicLabel;
    private JLabel gmailLabel;
    private JLabel dobLabel;
    private JLabel genderLabel;
    private JLabel maritalStatusLabel;
    private JLabel filerStatusLabel;
    private JLabel phoneLabel;
    private JLabel addressLabel;
    private JLabel cityLabel;
    private JLabel fatherNameLabel;
    private JLabel fatherPhoneLabel;
    private JLabel fatherNicLabel;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;
    // TextFields
    private JTextField nameTextField;
    private JTextField nicTextField;
    private JTextField phoneTextField;
    private JTextField gmailTextField;
    private JTextField cityTextField;
    private JTextField fatherNameTextField;
    private JTextField fatherPhoneTextField;
    private JTextField fatherNicTextField;
    // PasswordFields
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    // TextArea
    private JTextArea addressTextArea;
    // Date
    private JDateChooser dobDateChooser;
    // Combo Boxes
    private JComboBox<String> genderComboBox;
    private JComboBox<String> maritalStatusComboBox;
    private JComboBox<String> filerStatusComboBox;
    // Check Box
    private JCheckBox confirmationCheckBox;
    // Buttons
    private JButton confirmNextButton;
    private JButton resetButton;
    private JButton backToLoginButton;
    // Constructor
    public PersonalForm() {
        InfoFrame();
    }
    // Method
    private void InfoFrame(){
        // Frame
        int frameWidth = 980;
        int frameHeight = 800;
        frame = new JFrame("Bank Management System - Sign-Up - Personal Information Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        // Image
        ImageIcon BackgroundImage = new ImageIcon(getClass().getResource("/Images/SignUp.jpg"));
        Image bgImage = BackgroundImage.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
        BackgroundImage = new ImageIcon(bgImage);
        image = new JLabel(BackgroundImage);
        image.setBounds(0, 0, frameWidth, frameHeight);
        frame.setContentPane(image);
        image.setLayout(null);
        ImageIcon logo = new ImageIcon(getClass().getResource("/Images/Logo.jpg"));
        frame.setIconImage(logo.getImage());
        // Components
        Font Label = new Font("Times New Roman", Font.BOLD, 18);
        Font TextField = new Font("Times New Roman", Font.PLAIN,16);
        Color color = Color.BLACK;
        // Labels with TextFields
        infoLabel = new JLabel("Personal Information:");
        infoLabel.setBounds(170, 120, 280, 40);
        infoLabel.setForeground(color);
        infoLabel.setFont(new Font("Times New Roman", Font.BOLD,28));
        image.add(infoLabel);
        // Name
        nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(80, 220, 150, 30);
        nameLabel.setForeground(color);
        nameLabel.setFont(Label);
        image.add(nameLabel);
        nameTextField = new JTextField();
        nameTextField.setBounds(230, 220, 230, 30);
        nameTextField.setFont(TextField);
        image.add(nameTextField);
        // NIC number
        nicLabel = new JLabel("CNIC Number:");
        nicLabel.setBounds(80, 260, 150, 30);
        nicLabel.setForeground(color);
        nicLabel.setFont(Label);
        image.add(nicLabel);
        nicTextField = new JTextField();
        nicTextField.setBounds(230, 260, 230, 30);
        nicTextField.setFont(TextField);
        image.add(nicTextField);
        // Phone number
        phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(80, 300, 150, 30);
        phoneLabel.setForeground(color);
        phoneLabel.setFont(Label);
        image.add(phoneLabel);
        phoneTextField = new JTextField();
        phoneTextField.setBounds(230, 300, 230, 30);
        phoneTextField.setFont(TextField);
        image.add(phoneTextField);
        // Email Id
        gmailLabel = new JLabel("Email ID:");
        gmailLabel.setBounds(80, 340, 150, 30);
        gmailLabel.setForeground(color);
        gmailLabel.setFont(Label);
        image.add(gmailLabel);
        gmailTextField = new JTextField();
        gmailTextField.setBounds(230, 340, 230, 30);
        gmailTextField.setFont(TextField);
        image.add(gmailTextField);
        // DOB
        dobLabel = new JLabel("Date of Birth:");
        dobLabel.setBounds(80, 380, 150, 30);
        dobLabel.setForeground(color);
        dobLabel.setFont(Label);
        image.add(dobLabel);
        dobDateChooser = new JDateChooser();
        dobDateChooser.setBounds(230, 380, 230, 30);
        dobDateChooser.setFont(TextField);
        dobDateChooser.setDateFormatString("yyyy-MM-dd");
        image.add(dobDateChooser);
        // Gender
        genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(80, 420, 150, 30);
        genderLabel.setForeground(color);
        genderLabel.setFont(Label);
        image.add(genderLabel);
        String[] genders = {"Select Gender", "Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBounds(230, 420, 230, 30);
        genderComboBox.setFont(new Font("Times New Roman", Font.BOLD, 18));
        image.add(genderComboBox);
        // Marital Status
        maritalStatusLabel = new JLabel("Marital Status:");
        maritalStatusLabel.setBounds(80, 460, 150, 30);
        maritalStatusLabel.setForeground(color);
        maritalStatusLabel.setFont(Label);
        image.add(maritalStatusLabel);
        String[] maritalStatuses = {"Select Status", "Married", "Unmarried", "Divorced", "Widowed"};
        maritalStatusComboBox = new JComboBox<>(maritalStatuses);
        maritalStatusComboBox.setBounds(230, 460, 230, 30);
        maritalStatusComboBox.setFont(Label);
        image.add(maritalStatusComboBox);
        // Filer Status
        filerStatusLabel = new JLabel("Filer Status:");
        filerStatusLabel.setBounds(80, 500, 150, 30);
        filerStatusLabel.setForeground(color);
        filerStatusLabel.setFont(Label);
        image.add(filerStatusLabel);
        String[] filerStatuses = {"Select Status", "Filer", "Non-Filer"};
        filerStatusComboBox = new JComboBox<>(filerStatuses);
        filerStatusComboBox.setBounds(230, 500, 230, 30);
        filerStatusComboBox.setFont(Label);
        image.add(filerStatusComboBox);
        // Father name
        fatherNameLabel = new JLabel("Father Name:");
        fatherNameLabel.setBounds(500, 220, 150, 30);
        fatherNameLabel.setForeground(color);
        fatherNameLabel.setFont(Label);
        image.add(fatherNameLabel);
        fatherNameTextField = new JTextField();
        fatherNameTextField.setBounds(665, 220, 230, 30);
        fatherNameTextField.setFont(TextField);
        image.add(fatherNameTextField);
        // Father NIC number
        fatherNicLabel = new JLabel("Father's CNIC:");
        fatherNicLabel.setBounds(500, 260, 180, 30);
        fatherNicLabel.setForeground(color);
        fatherNicLabel.setFont(Label);
        image.add(fatherNicLabel);
        fatherNicTextField = new JTextField();
        fatherNicTextField.setBounds(665, 260, 230, 30);
        fatherNicTextField.setFont(TextField);
        image.add(fatherNicTextField);
        // Father phone number
        fatherPhoneLabel = new JLabel("Father's Phone No.:");
        fatherPhoneLabel.setBounds(500, 300, 200, 30);
        fatherPhoneLabel.setForeground(color);
        fatherPhoneLabel.setFont(Label);
        image.add(fatherPhoneLabel);
        fatherPhoneTextField = new JTextField();
        fatherPhoneTextField.setBounds(665, 300, 230, 30);
        fatherPhoneTextField.setFont(TextField);
        image.add(fatherPhoneTextField);
        // Address
        addressLabel = new JLabel("Address:");
        addressLabel.setBounds(500, 340, 150, 30);
        addressLabel.setForeground(color);
        addressLabel.setFont(Label);
        image.add(addressLabel);
        addressTextArea = new JTextArea();
        addressTextArea.setFont(TextField);
        JScrollPane scrollPane = new JScrollPane(addressTextArea);
        scrollPane.setBounds(665, 340, 230, 30);
        image.add(scrollPane);
        // City
        cityLabel = new JLabel("City:");
        cityLabel.setBounds(500, 380, 150, 30);
        cityLabel.setForeground(color);
        cityLabel.setFont(Label);
        image.add(cityLabel);
        cityTextField = new JTextField();
        cityTextField.setBounds(665, 380, 230, 30);
        cityTextField.setFont(TextField);
        image.add(cityTextField);
        // Password
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(500, 420, 150, 30);
        passwordLabel.setForeground(color);
        passwordLabel.setFont(Label);
        image.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(665, 420, 230, 30);
        passwordField.setFont(TextField);
        image.add(passwordField);
        // Confirm password
        confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setBounds(500, 460, 200, 30);
        confirmPasswordLabel.setForeground(color);
        confirmPasswordLabel.setFont(Label);
        image.add(confirmPasswordLabel);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(665, 460, 230, 30);
        confirmPasswordField.setFont(TextField);
        image.add(confirmPasswordField);
        // confirmation check box
        confirmationCheckBox = new JCheckBox("I confirm that all the data entered above is correct.");
        confirmationCheckBox.setBounds(80, 540, 815, 30);
        confirmationCheckBox.setForeground(Color.BLACK);
        confirmationCheckBox.setFont(new Font("Times New Roman", Font.BOLD, 16));
        confirmationCheckBox.setOpaque(false);
        image.add(confirmationCheckBox);
        //  BUTTONS
        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setBounds(200, 650, 170, 30);
        backToLoginButton.setBackground(Color.LIGHT_GRAY);
        backToLoginButton.setForeground(Color.BLACK);
        backToLoginButton.setFont(Label);
        backToLoginButton.addActionListener(this);
        image.add(backToLoginButton);
        resetButton = new JButton("Reset");
        resetButton.setBounds(400, 650, 170, 30);
        resetButton.setForeground(Color.BLACK);
        resetButton.setBackground(Color.LIGHT_GRAY);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.setFont(Label);
        resetButton.addActionListener(this);
        image.add(resetButton);
        confirmNextButton = new JButton("Confirm & Next");
        confirmNextButton.setBounds(600, 650, 170, 30);
        confirmNextButton.setForeground(Color.WHITE);
        confirmNextButton.setBackground(new Color(50, 100, 255));
        confirmNextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmNextButton.setFont(Label);
        confirmNextButton.addActionListener(this);
        image.add(confirmNextButton);

        frame.setVisible(true);
    }
    private boolean validateInput() {
        if (nameTextField.getText().isEmpty() ||
                nicTextField.getText().isEmpty() ||
                phoneTextField.getText().isEmpty() ||
                gmailTextField.getText().isEmpty() ||
                addressTextArea.getText().isEmpty() ||
                cityTextField.getText().isEmpty() ||
                fatherNameTextField.getText().isEmpty() ||
                fatherNicTextField.getText().isEmpty() ||
                fatherPhoneTextField.getText().isEmpty() ||
                passwordField.getPassword().length == 0 ||
                confirmPasswordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(frame,
                    "Please fill in all required fields.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dobDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(frame,
                    "Please select a valid Date of Birth.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Date dob = dobDateChooser.getDate();
        Calendar dobCal = Calendar.getInstance();
        dobCal.setTime(dob);
        Calendar now = Calendar.getInstance();
        int age = now.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);
        if (now.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        if (age < 20) {
            JOptionPane.showMessageDialog(frame,
                    "Applicant must be at least 20 years old to open an account.",
                    "Age Restriction", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (genderComboBox.getSelectedIndex() == 0 ||
                maritalStatusComboBox.getSelectedIndex() == 0 ||
                filerStatusComboBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(frame,
                    "Please select values for Gender, Marital Status, and Filer Status.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String nic = nicTextField.getText();
        if (!nic.matches("\\d{13}")) {
            JOptionPane.showMessageDialog(frame,
                    "CNIC must be exactly 13 digits (no dashes).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String fatherNic = fatherNicTextField.getText();
        if (!fatherNic.matches("\\d{13}")) {
            JOptionPane.showMessageDialog(frame,
                    "Father's CNIC must be exactly 13 digits (no dashes).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String phone = phoneTextField.getText();
        if (!phone.matches("03\\d{9}")) {
            JOptionPane.showMessageDialog(frame,
                    "Phone number must be 11 digits and start with 03 (e.g., 03XXXXXXXXX).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String fatherPhone = fatherPhoneTextField.getText();
        if (!fatherPhone.matches("03\\d{9}")) {
            JOptionPane.showMessageDialog(frame,
                    "Father's Phone number must be 11 digits and start with 03 (e.g., 03XXXXXXXXX).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String password = new String(passwordField.getPassword());
        String confirmpw = new String(confirmPasswordField.getPassword());
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(frame,
                    "Password must be at least 6 characters long.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!password.equals(confirmpw)) {
            JOptionPane.showMessageDialog(frame,
                    "Passwords do not match.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!gmailTextField.getText().endsWith("@gmail.com")) {
            JOptionPane.showMessageDialog(frame,
                    "Email must end with @gmail.com.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!confirmationCheckBox.isSelected()) {
            JOptionPane.showMessageDialog(frame,
                    "Please confirm that all entered data is correct by checking the box.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    private void resetForm() {
        nameTextField.setText("");
        nicTextField.setText("");
        phoneTextField.setText("");
        gmailTextField.setText("");
        fatherNameTextField.setText("");
        fatherNicTextField.setText("");
        fatherPhoneTextField.setText("");
        cityTextField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        addressTextArea.setText("");
        dobDateChooser.setDate(null);
        genderComboBox.setSelectedIndex(0);
        maritalStatusComboBox.setSelectedIndex(0);
        filerStatusComboBox.setSelectedIndex(0);
        confirmationCheckBox.setSelected(false);
    }
    private boolean savePersonalInfoToDataClass() {
        // Collect data
        String fullName = nameTextField.getText();
        String nicNumber = nicTextField.getText();
        String phoneNumber = phoneTextField.getText();
        String gmail = gmailTextField.getText();
        String dob = new SimpleDateFormat("yyyy-MM-dd").format(dobDateChooser.getDate());
        String gender = (String) genderComboBox.getSelectedItem();
        String maritalStatus = (String) maritalStatusComboBox.getSelectedItem();
        String filerStatus = (String) filerStatusComboBox.getSelectedItem();
        String fatherName = fatherNameTextField.getText();
        String fatherNic = fatherNicTextField.getText();
        String fatherPhone = fatherPhoneTextField.getText();
        String address = addressTextArea.getText();
        String city = cityTextField.getText();
        String password = new String(passwordField.getPassword());
        // Duplication check
        String sqlCheck = "SELECT gmail FROM personal_info WHERE gmail = ? OR nic_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlCheck)) {
            pstmt.setString(1, gmail);
            pstmt.setString(2, nicNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Account already exists for this CNIC or Gmail.",
                            "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database error during duplicate check: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
        DataClass.setFullName(fullName);
        DataClass.setNICNumber(nicNumber);
        DataClass.setPhoneNumber(phoneNumber);
        DataClass.setGmail(gmail);
        DataClass.setDateOfBirth(dob);
        DataClass.setGender(gender);
        DataClass.setMaritalStatus(maritalStatus);
        DataClass.setFilerStatus(filerStatus);
        DataClass.setAddress(address);
        DataClass.setCity(city);
        DataClass.setFatherName(fatherName);
        DataClass.setFatherNICNumber(fatherNic);
        DataClass.setFatherPhoneNumber(fatherPhone);
        DataClass.setPassword(password);
        return true;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToLoginButton) {
            frame.dispose();
            new LoginClass();
        } else if (e.getSource() == resetButton) {
            resetForm();
        } else if (e.getSource() == confirmNextButton) {
            if (validateInput()) {
                if (savePersonalInfoToDataClass()) {
                    JOptionPane.showMessageDialog(frame,
                            "Personal Information Saved Temporarily. Proceeding to Account Form.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    new AccountForm();
                }
            }
        }
    }
}