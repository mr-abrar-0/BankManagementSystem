package Project_Classes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import com.toedter.calendar.JDateChooser;

public class AdminTransactionHistory extends JFrame implements ActionListener {
    private JFrame frame;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JButton backButton, refreshButton, filterButton;
    private JTextField accFilterField;
    private JComboBox<String> txnCountFilterBox;
    private JComboBox<String> amountFilterBox;
    private JDateChooser startDateChooser, endDateChooser;

    private String adminUsername;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public AdminTransactionHistory(String adminUsername) {
        this.adminUsername = adminUsername;
        setupFrame();
        loadAllTransactions(null);
    }
    private void setupFrame() {
        frame = new JFrame("Admin - Transaction History");
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(5, 5));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("All System Transactions", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        frame.add(title, BorderLayout.NORTH);

        String[] columns = {
                "Txn ID", "Account Number", "Txn Type", "Amount",
                "Counterparty Account", "Date/Time"
        };

        tableModel = new DefaultTableModel(columns, 0);
        transactionTable = new JTable(tableModel);
        frame.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        frame.add(createFilterPanel(), BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private JPanel createFilterPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createTitledBorder("Filtering Options"));

        JPanel grid = new JPanel(new GridLayout(2, 6, 10, 10));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font f = new Font("Times New Roman", Font.BOLD, 14);

        grid.add(createLabel("Account No:", f));
        accFilterField = new JTextField();
        grid.add(accFilterField);

        grid.add(createLabel("Start Date:", f));
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString(DATE_FORMAT);
        grid.add(startDateChooser);

        grid.add(createLabel("End Date:", f));
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString(DATE_FORMAT);
        grid.add(endDateChooser);

        grid.add(createLabel("TXN Count:", f));
        txnCountFilterBox = new JComboBox<>(new String[]{
                "None", "Max Transactions", "Min Transactions"
        });
        grid.add(txnCountFilterBox);

        grid.add(createLabel("Amount Filter:", f));
        amountFilterBox = new JComboBox<>(new String[]{
                "None",
                "Max Single Amount",
                "Min Single Amount",
                "Max Total Amount",
                "Min Total Amount"
        });
        grid.add(amountFilterBox);

        main.add(grid, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout());
        refreshButton = new JButton("Reset");
        filterButton = new JButton("Apply");
        backButton = new JButton("Back");

        refreshButton.addActionListener(this);
        filterButton.addActionListener(this);
        backButton.addActionListener(this);

        buttons.add(refreshButton);
        buttons.add(filterButton);
        buttons.add(backButton);

        main.add(buttons, BorderLayout.SOUTH);
        return main;
    }

    private JLabel createLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        return lbl;
    }

    private void loadAllTransactions(String sql) {
        tableModel.setRowCount(0);

        if (sql == null) {
            sql = "SELECT * FROM transaction_history ORDER BY transaction_id DESC LIMIT 500";
        }

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();

                String type = rs.getString("transaction_type");
                String sender = rs.getString("sender_account");
                String receiver = rs.getString("recipient_account");

                String primary = rs.getString("account_number");
                String counter = "N/A";

                if (type.contains("TRANSFER_OUT")) {
                    primary = sender;
                    counter = receiver;
                } else if (type.contains("TRANSFER_IN")) {
                    primary = receiver;
                    counter = sender;
                }

                row.add(rs.getInt("transaction_id"));
                row.add(primary);
                row.add(type);
                row.add(rs.getDouble("amount"));
                row.add(counter);
                row.add(rs.getTimestamp("transaction_date"));

                tableModel.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
    }

    private String getWinningAccount(String mode, boolean isMax,
                                     String dateCondition) throws SQLException {

        String dateSQL = dateCondition.isEmpty() ? "" : " AND " + dateCondition;

        String metric = mode.equals("COUNT") ? "COUNT(*)" : "SUM(amount)";
        String order = isMax ? "DESC" : "ASC";

        String sql =
                "SELECT account_number FROM transaction_history " +
                        "WHERE account_number IS NOT NULL " + dateSQL +
                        " GROUP BY account_number " +
                        " HAVING " + metric + " > 0 " +
                        " ORDER BY " + metric + " " + order +
                        " LIMIT 1";

        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery(sql)) {

            if (r.next()) return r.getString(1);
        }
        return null;
    }

    private String buildFilterQuery() throws SQLException, ParseException {

        String acc = accFilterField.getText().trim();
        String txnOpt = txnCountFilterBox.getSelectedItem().toString();
        String amtOpt = amountFilterBox.getSelectedItem().toString();

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateCond = "";

        if (startDateChooser.getDate() != null) {
            dateCond = "transaction_date >= '" + sdf.format(startDateChooser.getDate()) + " 00:00:00'";
        }
        if (endDateChooser.getDate() != null) {
            dateCond += (dateCond.isEmpty() ? "" : " AND ") +
                    "transaction_date <= '" + sdf.format(endDateChooser.getDate()) + " 23:59:59'";
        }

        if (!txnOpt.equals("None")) {
            String winner = getWinningAccount("COUNT", txnOpt.contains("Max"), dateCond);
            return "SELECT * FROM transaction_history WHERE account_number='" + winner + "'";
        }

        if (amtOpt.contains("Total")) {
            String winner = getWinningAccount("AMOUNT", amtOpt.contains("Max"), dateCond);
            return "SELECT * FROM transaction_history WHERE account_number='" + winner + "'";
        }

        if (amtOpt.contains("Single")) {
            return "SELECT * FROM transaction_history ORDER BY amount " +
                    (amtOpt.contains("Max") ? "DESC" : "ASC") + " LIMIT 1";
        }

        String where = "";
        if (!acc.isEmpty()) where = " WHERE account_number='" + acc + "'";
        if (!dateCond.isEmpty()) where += (where.isEmpty() ? " WHERE " : " AND ") + dateCond;

        return "SELECT * FROM transaction_history" + where;
    }

    public void actionPerformed(ActionEvent e) {

        try {
            if (e.getSource() == backButton) {
                frame.dispose();
                new AdminDashboard(adminUsername);
            }

            if (e.getSource() == refreshButton) {
                accFilterField.setText("");
                txnCountFilterBox.setSelectedIndex(0);
                amountFilterBox.setSelectedIndex(0);
                startDateChooser.setDate(null);
                endDateChooser.setDate(null);
                loadAllTransactions(null);
            }

            if (e.getSource() == filterButton) {
                loadAllTransactions(buildFilterQuery());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }
}
