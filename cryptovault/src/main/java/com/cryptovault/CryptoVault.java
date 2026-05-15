package com.cryptovault;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.security.MessageDigest;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class CryptoVault extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private final String MASTER_HASH = "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa8228091";

    public CryptoVault() {
        setTitle("Secure Crypto Vault");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupLoginScreen();
        setupDashboard();

        add(mainPanel);
    }

    private void setupLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPasswordField passField = new JPasswordField(15);
        JButton btn = new JButton("Unlock Vault");

        btn.addActionListener(e -> {
            if (verify(new String(passField.getPassword()))) {
                cardLayout.show(mainPanel, "DASH");
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied");
            }
        });

        gbc.gridy = 0; panel.add(new JLabel("Master Password:"), gbc);
        gbc.gridy = 1; panel.add(passField, gbc);
        gbc.gridy = 2; panel.add(btn, gbc);
        mainPanel.add(panel, "LOGIN");
    }

    private void setupDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] cols = {"Asset", "Amount", "Status"};
        Object[][] data = {{"BTC", "0.52", "Safe"}, {"ETH", "4.1", "Safe"}, {"SOL", "120", "Safe"}};

        JTable table = new JTable(new DefaultTableModel(data, cols));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton lockBtn = new JButton("Lock Vault");
        lockBtn.addActionListener(e -> cardLayout.show(mainPanel, "LOGIN"));
        panel.add(lockBtn, BorderLayout.SOUTH);

        mainPanel.add(panel, "DASH");
    }

    private boolean verify(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString().startsWith(MASTER_HASH.substring(0, 10));
        } catch (Exception e) { return false; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CryptoVault().setVisible(true));
    }
}