package Project_Classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ViewDeletionRequests extends JFrame implements ActionListener {
    private JFrame frame;
    private JTable requestsTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JButton acceptButton;
    private JButton rejectButton;
    private String adminUsername;

    public ViewDeletionRequests(String adminUsername) {
        this.adminUsername = adminUsername;
        setupFrame();
        loadPendingRequests();
    }

    private void setupFrame() {
        frame = new JFrame("Admin - Account Deletion Requests");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JLabel title = new JLabel("Pending Deletion Requests", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(title);
        frame.add(headerPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columnNames = {"ID", "User Gmail", "Account No.", "Request Date", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        requestsTable = new JTable(tableModel);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(requestsTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        acceptButton = new JButton("Accept & Delete Account");
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

        // We join to account_info to display the account number, which is stored there.
        String sql = "SELECT pr.request_id, pr.user_gmail, pr.request_date, ai.account_number " +
                "FROM pending_requests pr " +
                "JOIN account_info ai ON pr.user_gmail = ai.user_gmail " +
                "WHERE pr.request_type = 'ACCOUNT_DELETION' AND pr.status = 'PENDING' " +
                "ORDER BY pr.request_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("request_id"));
                row.add(rs.getString("user_gmail"));
                row.add(rs.getString("account_number"));
                row.add(rs.getTimestamp("request_date").toString().substring(0, 19));
                row.add("PENDING");
                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(frame, "No pending deletion requests found.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading requests: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void executeDeletion(int requestId, String userGmail) {
        // 1. Delete user from personal_info (CASCADE will handle account_info, balance, history)
        String sqlDelete = "DELETE FROM personal_info WHERE gmail = ?";

        // 2. FIX: sqlUpdateStatus simplified to 2 parameters, matching the two values set below.
        String sqlUpdateStatus = "UPDATE pending_requests SET status = 'ACCEPTED', approved_by_admin = ? WHERE request_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // Execute Cascading Delete
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {
                pstmt.setString(1, userGmail);
                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("User record not found during final delete.");
                }
            }

            // Update Request Status (Line 136 is now here, but using the correct parameter count)
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdateStatus)) {
                pstmt.setString(1, adminUsername); // Parameter 1
                pstmt.setInt(2, requestId);          // Parameter 2
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit Transaction
            JOptionPane.showMessageDialog(frame, "Account for " + userGmail + " successfully deleted.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(frame, "CRITICAL ERROR: Failed to delete account. Transaction rolled back. " + e.getMessage(),
                    "Deletion Failure", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            loadPendingRequests(); // Reload list regardless of success/failure
        }
    }

    /**
     * Rejects the request and updates its status.
     */
    private void executeReject(int requestId) {
        // This SQL has 2 parameters: approved_by_admin and request_id.
        String sqlUpdateStatus = "UPDATE pending_requests SET status = 'REJECTED', approved_by_admin = ? WHERE request_id = ?";

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
        if (e.getSource() == backButton) {
            frame.dispose();
            // Assuming AdminDashboard takes username only
            new AdminDashboard(adminUsername);
        } else if (e.getSource() == acceptButton || e.getSource() == rejectButton) {
            int selectedRow = requestsTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a request from the table.",
                        "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int requestId = (int) tableModel.getValueAt(selectedRow, 0);
            String userGmail = (String) tableModel.getValueAt(selectedRow, 1);

            if (e.getSource() == rejectButton) {
                executeReject(requestId);
            } else {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to approve and permanently delete the account for: " + userGmail + "?",
                        "Confirm Approval", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    executeDeletion(requestId, userGmail);
                }
            }
        }
    }
}