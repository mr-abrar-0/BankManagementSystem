package Project_Classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Vector; // For JTable data

public class TransactionHistory extends JFrame implements ActionListener {
    private JFrame frame;
    private String accountNumber;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    private JButton backButton;
    private JTextField startDateField, endDateField;
    private JButton filterButton;

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");

    public TransactionHistory(String accNumber) {
        this.accountNumber = accNumber;
        setupFrame();
        // Initial load: Pass null, null to load all history by default
        loadHistory(null, null);
    }

    private void setupFrame() {
        frame = new JFrame("Transaction History - Account: " + accountNumber);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // --- Header Panel Setup (North) ---
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Back Button (West)
        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.addActionListener(this);
        headerPanel.add(backButton, BorderLayout.WEST);

        // 3. Date Filter Controls (East)
        JPanel filterControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterControls.setOpaque(false);
        Font controlFont = new Font("Segoe UI", Font.PLAIN, 14);

        filterControls.add(new JLabel("Start Date (YYYY-MM-DD):"));
        startDateField = new JTextField(10);
        startDateField.setFont(controlFont);
        filterControls.add(startDateField);

        filterControls.add(new JLabel("End Date (YYYY-MM-DD):"));
        endDateField = new JTextField(10);
        endDateField.setFont(controlFont);
        filterControls.add(endDateField);

        filterButton = new JButton("Filter");
        filterButton.setFont(controlFont);
        filterButton.addActionListener(this);
        filterControls.add(filterButton);

        headerPanel.add(filterControls, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);

        // Table Setup (Center)
        String[] columnNames = {"Date/Time", "Type", "Amount (PKR)", "Source/Target Account", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        historyTable = new JTable(tableModel);
        historyTable.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        historyTable.setRowHeight(25);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(historyTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    // In TransactionHistory.java

    private void loadHistory(String startDate, String endDate) {
        // Clear old data first
        tableModel.setRowCount(0);

        // Default: Load all history
        boolean useStartDate = startDate != null && !startDate.trim().isEmpty();
        boolean useEndDate = endDate != null && !endDate.trim().isEmpty();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT transaction_date, transaction_type, amount, recipient_account, sender_account, status ");
        sqlBuilder.append("FROM transaction_history WHERE account_number = ? ");

        if (useStartDate) {
            sqlBuilder.append("AND transaction_date >= ? ");
        }
        if (useEndDate) {
            sqlBuilder.append("AND transaction_date < DATE_ADD(?, INTERVAL 1 DAY) ");
        }

        sqlBuilder.append("ORDER BY transaction_date DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            if (conn == null) return;

            int paramIndex = 1;
            pstmt.setString(paramIndex++, accountNumber);

            if (useStartDate) {
                pstmt.setString(paramIndex++, startDate.trim());
            }
            if (useEndDate) {
                // The JDBC driver sends the date string to the DATE_ADD function placeholder.
                pstmt.setString(paramIndex++, endDate.trim());
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();

                    // 1. Date/Time (Formatted)
                    row.add(rs.getTimestamp("transaction_date").toString().substring(0, 19));

                    // 2. Type
                    String type = rs.getString("transaction_type");
                    row.add(type);

                    // 3. Amount (Formatted)
                    double amount = rs.getDouble("amount");
                    String amountStr = CURRENCY_FORMAT.format(amount);
                    row.add(amountStr);

                    // 4. Source/Target Account (Fixed retrieval logic)
                    String targetAcc;
                    if (type.contains("TRANSFER_OUT")) {
                        // This account is the sender, display the recipient
                        targetAcc = rs.getString("recipient_account");
                    } else if (type.contains("TRANSFER_IN")) {
                        // This account is the recipient, display the sender
                        targetAcc = rs.getString("sender_account");
                    } else {
                        targetAcc = "N/A"; // For INITIAL DEPOSIT, WITHDRAW, etc.
                    }

                    // If the target is null (no transfer occurred), set it to N/A
                    row.add(targetAcc != null ? targetAcc : "N/A");

                    // 5. Status
                    row.add(rs.getString("status"));

                    tableModel.addRow(row);
                }

                if (tableModel.getRowCount() == 0 && (useStartDate || useEndDate)) {
                    JOptionPane.showMessageDialog(frame, "No transaction history found for the selected range.",
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                } else if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(frame, "No transaction history found for this account.",
                            "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading transaction history: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
            new DashBoard(DataClass.getFullName(), DataClass.getAccountNumber());
        } else if (e.getSource() == filterButton) {
            String startDate = startDateField.getText().trim();
            String endDate = endDateField.getText().trim();

            String datePattern = "^\\d{4}-\\d{2}-\\d{2}$";

            // --- Date Validation ---
            if (!startDate.isEmpty() && !startDate.matches(datePattern)) {
                JOptionPane.showMessageDialog(frame, "Start Date must be in YYYY-MM-DD format (e.g., 2023-10-25).",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!endDate.isEmpty() && !endDate.matches(datePattern)) {
                JOptionPane.showMessageDialog(frame, "End Date must be in YYYY-MM-DD format (e.g., 2023-10-25).",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if start date is after end date (only if both are provided and valid)
            if (!startDate.isEmpty() && !endDate.isEmpty()) {
                // String comparison works for YYYY-MM-DD format
                if (startDate.compareTo(endDate) > 0) {
                    JOptionPane.showMessageDialog(frame, "Start Date cannot be after End Date.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // If validation passes, load history with filters
            loadHistory(startDate, endDate);
        }
    }
}