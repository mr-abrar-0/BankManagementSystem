package Project_Classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ViewRegisteredAccounts extends JFrame implements ActionListener {
    private JFrame frame;
    private JTable accountsTable;
    private DefaultTableModel tableModel;
    private JButton backButton;
    private JTextField searchField;
    private JButton searchButton;

    private String adminUsername;

    public ViewRegisteredAccounts(String adminUsername) {
        this.adminUsername = adminUsername;
        setupFrame();
        loadRegisteredAccounts(null); // Load all accounts initially
    }

    private void setupFrame() {
        frame = new JFrame("Admin - View Registered Accounts");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(5, 5));

        ImageIcon logo = new ImageIcon(getClass().getResource("/Images/Logo.jpg"));
        frame.setIconImage(logo.getImage());

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JLabel title = new JLabel("All Registered User Accounts", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(title);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Defined Columns as per requirement
        String[] columnNames = {"Account No.", "Holder Name", "Account Type", "Gmail", "Gender", "Time of Creation"};
        tableModel = new DefaultTableModel(columnNames, 0);
        accountsTable = new JTable(tableModel);
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(accountsTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Controls Panel (SOUTH)
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("Search by Account/Name/Gmail:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchButton);

        controlsPanel.add(searchPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));

        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(this);

        buttonPanel.add(backButton);
        controlsPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(controlsPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadRegisteredAccounts(String searchTerm) {
        tableModel.setRowCount(0);

        String sql = "SELECT " +
                "    ai.account_number, " +
                "    pi.full_name, " +
                "    ai.account_type, " +
                "    pi.gmail, " +
                "    pi.gender, " +
                "    ai.creation_date " +
                "FROM account_info ai " +
                "JOIN personal_info pi ON ai.user_gmail = pi.gmail ";

        String whereClause = "";
        String term = null;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            term = "%" + searchTerm.trim() + "%";
            // Use placeholders (?) for PreparedStatement
            whereClause = "WHERE ai.account_number LIKE ? OR pi.full_name LIKE ? OR pi.gmail LIKE ? ";
            sql += whereClause;
        }

        sql += "ORDER BY ai.creation_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!whereClause.isEmpty()) {
                pstmt.setString(1, term);
                pstmt.setString(2, term);
                pstmt.setString(3, term);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getString("account_number"));
                    row.add(rs.getString("full_name"));
                    row.add(rs.getString("account_type"));
                    row.add(rs.getString("gmail"));
                    row.add(rs.getString("gender"));
                    // Formatting time (assuming creation_date is a TIMESTAMP or DATETIME)
                    Timestamp creationTime = rs.getTimestamp("creation_date");
                    row.add(creationTime != null ? creationTime.toString().substring(0, 19) : "N/A");
                    tableModel.addRow(row);
                }
            }

            if (tableModel.getRowCount() == 0 && searchTerm != null && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No accounts found matching the criteria.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            } else if (tableModel.getRowCount() == 0 && (searchTerm == null || searchTerm.trim().isEmpty())) {
                JOptionPane.showMessageDialog(frame, "No registered accounts found in the database.",
                        "Information", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading registered accounts: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
            new AdminDashboard(adminUsername);
        } else if (e.getSource() == searchButton) {
            loadRegisteredAccounts(searchField.getText());
        }
    }
}