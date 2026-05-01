package Project_Classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ViewCreationRequests extends JFrame implements ActionListener {
    private JFrame frame;
    private JTable requestsTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JButton acceptButton;
    private JButton rejectButton;
    private String adminUsername;

    public ViewCreationRequests(String adminUsername) {
        this.adminUsername = adminUsername;
        setupFrame();
        loadPendingRequests();
    }

    private void setupFrame() {
        frame = new JFrame("Admin - Account Creation Requests");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 550);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JLabel title = new JLabel("Pending Account Creation Requests", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(title);
        frame.add(headerPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "User Gmail", "Full Name", "CNIC", "Account No.", "Request Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        requestsTable = new JTable(tableModel);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(requestsTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        acceptButton = new JButton("Approve & Create Account");
        acceptButton.setBackground(new Color(46, 204, 113));
        acceptButton.setForeground(Color.WHITE);
        acceptButton.addActionListener(this);

        rejectButton = new JButton("Reject Request");
        rejectButton.setBackground(new Color(231, 76, 60));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.addActionListener(this);

        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(this);

        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadPendingRequests() {
        tableModel.setRowCount(0);
        String sql = "SELECT request_id, user_gmail, application_data, request_date " +
                "FROM pending_requests " +
                "WHERE request_type = 'ACCOUNT_CREATION' AND status = 'PENDING' " +
                "ORDER BY request_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                String data = rs.getString("application_data");

                String fullName = extractValue(data, "fullName");
                String nic = extractValue(data, "nicNumber");
                String accNumber = extractValue(data, "accountNumber");

                row.add(rs.getInt("request_id"));
                row.add(rs.getString("user_gmail"));
                row.add(fullName);
                row.add(nic);
                row.add(accNumber);
                row.add(rs.getTimestamp("request_date").toString().substring(0, 19));
                tableModel.addRow(row);
            }
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "No pending account creation requests found.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading requests: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String extractValue(String data, String key) {
        String search = key + ":";
        int start = data.indexOf(search) + search.length();
        if (start == -1 + search.length()) return "N/A";
        int end = data.indexOf(",", start);
        if (end == -1) end = data.length();
        return data.substring(start, end).trim();
    }

    private void executeCreation(int requestId) {
        String applicationData = null;
        String sqlFetch = "SELECT application_data FROM pending_requests WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlFetch)) {
            pstmt.setInt(1, requestId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) applicationData = rs.getString("application_data");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching application data.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (applicationData == null) {
            JOptionPane.showMessageDialog(frame, "Application data not found or already processed.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] data = deserialize(applicationData);
        if (data == null) {
            JOptionPane.showMessageDialog(frame, "Failed to deserialize application data.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (performFinalInserts(data, requestId)) {
            JOptionPane.showMessageDialog(frame, "Account for " + data[3] + " created successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        loadPendingRequests();
    }

    private String[] deserialize(String data) {
        if (data == null || data.trim().isEmpty()) {
            return new String[0];
        }
        String[] parts = data.split(",");
        String[] result = new String[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i].trim();
                if (part.isEmpty()) continue;
                int colonIndex = part.indexOf(':');
                if (colonIndex > 0) {
                    result[i] = part.substring(colonIndex + 1).trim();
                } else {
                    System.err.println("Deserialization Error: Found non-key:value part: " + part);
                    result[i] = "CORRUPT_DATA";
                }
            }
            return result;
        } catch (Exception e) {
            System.err.println("CRITICAL DESERIALIZATION FAILURE: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // CORRECTED performFinalInserts method (Ensures perfect indexing)
    private boolean performFinalInserts(String[] data, int requestId) {
        if (data == null || data.length < 25) {
            JOptionPane.showMessageDialog(frame, "Data corruption: Expected 25 fields but got "
                            + (data == null ? "0" : data.length) + " fields.",
                    "Integrity Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            String sqlPersonal = "INSERT INTO personal_info (full_name, nic_number, phone_number, " +
                    "gmail, date_of_birth, gender, marital_status, address, city, father_name, " +
                    "father_nic, father_phone, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String sqlAccount = "INSERT INTO account_info (user_gmail, account_number, account_type, " +
                    "religion, purpose, occupation, monthly_income, qualification, currency_type, " +
                    "card_type, account_pin, sms_alerts, email_alerts, additional_services) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String sqlBalance = "INSERT INTO account_balance (account_number, " +
                    "current_balance) VALUES (?, ?)";
            String sqlUpdateStatus = "UPDATE pending_requests SET status = 'ACCEPTED', " +
                    "approved_by_admin = ? WHERE request_id = ?";

            // 1. Insert into personal_info
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPersonal)) {
                for (int i = 0; i < 13; i++) {
                    pstmt.setString(i + 1, data[i]);
                }
                if (pstmt.executeUpdate() == 0) throw new SQLException("Personal Insert failed.");
            }

            // 2. Insert into account_info
            try (PreparedStatement pstmt = conn.prepareStatement(sqlAccount)) {
                pstmt.setString(1, data[3]);
                pstmt.setString(2, data[13]);

                int dataIndex = 14;
                for (int sqlParam = 3; sqlParam <= 14; sqlParam++) {
                    String value = data[dataIndex++];
                    if (sqlParam == 14) {
                        value = value.replaceAll("\\|", ",");
                    }
                    pstmt.setString(sqlParam, value);
                }

                if (pstmt.executeUpdate() == 0) throw new SQLException("Account Insert failed.");
            }

            // 3. Insert into account_balance
            try (PreparedStatement pstmt = conn.prepareStatement(sqlBalance)) {
                pstmt.setString(1, data[13]);
                pstmt.setDouble(2, 0.00);
                pstmt.executeUpdate();
            }

            // 4. Update Request Status
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateStatus)) {
                pstmt.setString(1, adminUsername);
                pstmt.setInt(2, requestId);
                pstmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }

            String errorMessage = "CRITICAL FAILURE: Account creation transaction rolled back. Error: " + e.getMessage();
            if (e.getErrorCode() == 1062) errorMessage = "ERROR: Account creation failed (Duplicate Key/CNIC/Gmail).";
            JOptionPane.showMessageDialog(frame, errorMessage, "Database Transaction Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void executeReject(int requestId) {
        String sqlUpdateStatus = "UPDATE pending_requests SET status = 'REJECTED', " +
                "approved_by_admin = ? WHERE request_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdateStatus)) {

            pstmt.setString(1, adminUsername);
            pstmt.setInt(2, requestId);

            if (pstmt.executeUpdate() == 1) {
                JOptionPane.showMessageDialog(frame, "Request ID " + requestId + " rejected.",
                        "Request Rejected", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error rejecting request: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            loadPendingRequests();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = requestsTable.getSelectedRow();

        if (e.getSource() == backButton) {
            frame.dispose();
            new AdminDashboard(adminUsername);
        } else if (selectedRow == -1 && (e.getSource() == acceptButton || e.getSource() == rejectButton )) {
            JOptionPane.showMessageDialog(frame, "Please select a request from the table.",
                    "Selection Required", JOptionPane.WARNING_MESSAGE);
        } else if (e.getSource() == acceptButton) {
            int requestId = (int) tableModel.getValueAt(selectedRow, 0);
            String userGmail = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Approve account creation for: " + userGmail + "?",
                    "Confirm Approval", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                executeCreation(requestId);
            }
        } else if (e.getSource() == rejectButton) {
            int requestId = (int) tableModel.getValueAt(selectedRow, 0);
            executeReject(requestId);
        }
    }
}