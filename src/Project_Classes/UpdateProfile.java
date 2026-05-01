package Project_Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Pattern;

public class UpdateProfile extends JFrame implements ActionListener {
    private JFrame frame;
    private JLabel image; // Added for the background image

    private JTextField phoneField, cityField, gmailField;
    private JTextArea addressArea;
    private JButton updateButton, backButton;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@gmail\\.com$",
            Pattern.CASE_INSENSITIVE);

    public UpdateProfile() {
        setupFrame();
        loadExistingData();
    }

    private void setupFrame() {
        int frameWidth = 900;
        int frameHeight = 660;

        frame = new JFrame("Update Personal Profile - User: " + DataClass.getFullName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // --- Background Image Setup (Using /Images/Login.jpg) ---
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

        JLabel header = new JLabel("Update Profile Info", SwingConstants.CENTER);
        header.setFont(new Font("Times New Roman", Font.BOLD, 24));
        header.setForeground(new Color(0, 0, 0));
        header.setBounds(3750, 270, 400, 35);
        image.add(header);

        Font labelFont = new Font("Times New Roman", Font.BOLD, 16);
        Color textColor = Color.BLACK;

        // Gmail Field
        JLabel gmailLabel = new JLabel("Gmail:");
        gmailLabel.setFont(labelFont);
        gmailLabel.setForeground(textColor);
        gmailLabel.setBounds(400, 330, 150, 30);
        image.add(gmailLabel);
        gmailField = new JTextField();
        gmailField.setBounds(550, 330, 200, 30);
        image.add(gmailField);

        // Phone Number
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(labelFont);
        phoneLabel.setForeground(textColor);
        phoneLabel.setBounds(400, 370, 150, 30);
        image.add(phoneLabel);
        phoneField = new JTextField();
        phoneField.setBounds(550, 370, 200, 30);
        image.add(phoneField);

        // Address
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(labelFont);
        addressLabel.setForeground(textColor);
        addressLabel.setBounds(400, 410, 150, 30);
        image.add(addressLabel);
        addressArea = new JTextArea(5, 40);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(addressArea);
        scrollPane.setBounds(550, 410, 200, 30);
        image.add(scrollPane);

        // City
        JLabel cityLabel = new JLabel("City:");
        cityLabel.setFont(labelFont);
        cityLabel.setForeground(textColor);
        cityLabel.setBounds(400, 450, 150, 30);
        image.add(cityLabel);
        cityField = new JTextField();
        cityField.setBounds(550, 450, 200, 30);
        image.add(cityField);

        // Buttons
        updateButton = new JButton("Save Changes");
        updateButton.setFont(labelFont);
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(50, 100, 255));
        updateButton.setBounds(600, 530, 150, 30);
        updateButton.addActionListener(this);
        image.add(updateButton);

        backButton = new JButton("Back");
        backButton.setFont(labelFont);
        backButton.setBounds(400, 530, 150, 30);
        backButton.addActionListener(this);
        image.add(backButton);

        frame.setVisible(true);
    }

    private void loadExistingData() {
        String gmail = DataClass.getGmail();
        String sql = "SELECT gmail, phone_number, address, city FROM personal_info WHERE gmail = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return;
            pstmt.setString(1, gmail);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    gmailField.setText(rs.getString("gmail"));
                    phoneField.setText(rs.getString("phone_number"));
                    addressArea.setText(rs.getString("address"));
                    cityField.setText(rs.getString("city"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading existing profile data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean validateInput(String newGmail, String newPhone, String newAddress, String newCity) {
        if (newGmail.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty() || newCity.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Phone validation
        if (!newPhone.matches("03\\d{9}")) {
            JOptionPane.showMessageDialog(frame, "Phone number must be 11 digits and start with 03 " +
                    "(e.g., 03XXXXXXXXX).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Gmail validation
        if (!EMAIL_PATTERN.matcher(newGmail).matches()) {
            JOptionPane.showMessageDialog(frame, "Email must be a valid @gmail.com address.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean executeProfileUpdate() {
        String oldGmail = DataClass.getGmail();
        String newGmail = gmailField.getText();
        String newPhone = phoneField.getText();
        String newAddress = addressArea.getText();
        String newCity = cityField.getText();

        // Validation signature updated
        if (!validateInput(newGmail, newPhone, newAddress, newCity)) {
            return false;
        }

        String sqlPersonal = "UPDATE personal_info SET gmail = ?, phone_number = ?, address = ?, city = ? WHERE gmail = ?";
        String sqlAccount = "UPDATE account_info SET user_gmail = ? WHERE user_gmail = ?";

        Connection conn = null;
        boolean success = false;

        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) return false;

            conn.setAutoCommit(false); // Start transaction

            // Update Foreign Key in account_info ---
            if (!oldGmail.equals(newGmail)) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlAccount)) {
                    pstmt.setString(1, newGmail);
                    pstmt.setString(2, oldGmail);
                    pstmt.executeUpdate();
                }
            }

            // --- STEP 2: Update personal_info fields (including primary key gmail) ---
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPersonal)) {
                pstmt.setString(1, newGmail);
                pstmt.setString(2, newPhone);
                pstmt.setString(3, newAddress);
                pstmt.setString(4, newCity);
                pstmt.setString(5, oldGmail);

                if (pstmt.executeUpdate() != 1) {
                    throw new SQLException("Failed to update personal_info record.");
                }
            }

            conn.commit(); // Commit if both updates were successful
            success = true;

            // --- STEP 3: Update DataClass ---
            DataClass.setGmail(newGmail);
            DataClass.setPhoneNumber(newPhone);
            DataClass.setAddress(newAddress);
            DataClass.setCity(newCity);
            // DataClass setters for Province/Nationality are no longer needed here.

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            String errorMessage = "Database error during profile update. Changes rolled back.";
            if (e.getErrorCode() == 1062) { // 1062 is usually Duplicate entry error
                errorMessage = "Update failed: The new Gmail (" + newGmail + ") is already registered to another user.";
            } else {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(frame, errorMessage, "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return success;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
            new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());
        } else if (e.getSource() == updateButton) {
            if (executeProfileUpdate()) {
                JOptionPane.showMessageDialog(frame, "Profile updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}