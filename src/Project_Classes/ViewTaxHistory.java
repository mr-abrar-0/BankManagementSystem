package Project_Classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;

public class ViewTaxHistory extends JFrame implements ActionListener {
    private JFrame frame;
    private JTable taxTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private String adminUsername;

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");

    public ViewTaxHistory(String adminUsername) {
        this.adminUsername = adminUsername;
        setupFrame();
        loadTaxData();
    }

    private void setupFrame() {
        int frameWidth = 1000;
        int frameHeight = 600;

        frame = new JFrame("Admin Dashboard - Tax History");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);

        // Icon
        ImageIcon logo = new ImageIcon(getClass().getResource("/Images/Logo.jpg"));
        frame.setIconImage(logo.getImage());

        // --- Layout and Header ---
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JLabel header = new JLabel("Collected Withholding Tax History", SwingConstants.CENTER);
        header.setFont(new Font("Times New Roman", Font.BOLD, 28));
        header.setForeground(new Color(0, 128, 0)); // Green color for tax/finance
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        contentPane.add(header, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columnNames = {
                "Acc. Number", "Filer Status", "Current Balance (PKR)", "Transaction Type",
                "Base Amount (PKR)", "Tax Amount (PKR)", "Tax Rate (%)", "Date/Time"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        taxTable = new JTable(tableModel);
        taxTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taxTable.setRowHeight(25);
        taxTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(taxTable);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // --- Footer/Button Panel ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        backButton.setBackground(new Color(52, 152, 219));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(this);

        footerPanel.add(backButton);
        contentPane.add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadTaxData() {
        // Query joins tax_history with account_balance to get the real-time balance
        String sql = "SELECT th.account_number, th.filer_status_at_time, bal.current_balance, " +
                "th.tax_type, th.base_amount, th.tax_amount, th.tax_rate, th.tax_date " +
                "FROM tax_history th " +
                "JOIN account_balance bal ON th.account_number = bal.account_number " +
                "ORDER BY th.tax_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Clear existing data
            tableModel.setRowCount(0);

            while (rs.next()) {
                String accNum = rs.getString("account_number");
                String filerStatus = rs.getString("filer_status_at_time");
                double currentBalance = rs.getDouble("current_balance");
                String taxType = rs.getString("tax_type");
                double baseAmount = rs.getDouble("base_amount");
                double taxAmount = rs.getDouble("tax_amount");
                double taxRateDecimal = rs.getDouble("tax_rate");
                Timestamp taxDate = rs.getTimestamp("tax_date");

                // Convert decimal rate (e.g., 0.05) to percentage string (e.g., "5.0%")
                String ratePercentage = String.format("%.1f%%", taxRateDecimal * 100);

                // Format the transaction type for display
                String displayType;
                if (taxType.contains("TRANSFER")) {
                    displayType = "Fund Transfer WHT";
                } else if (taxType.contains("WITHDRAWAL")) {
                    displayType = "Withdrawal WHT";
                } else {
                    displayType = taxType;
                }

                // Add row to the table
                tableModel.addRow(new Object[]{
                        accNum,
                        filerStatus,
                        CURRENCY_FORMAT.format(currentBalance),
                        displayType,
                        CURRENCY_FORMAT.format(baseAmount),
                        CURRENCY_FORMAT.format(taxAmount),
                        ratePercentage,
                        taxDate.toString()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading tax history: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
            // Go back to the Admin Dashboard
            new AdminDashboard(adminUsername);
        }
    }
}