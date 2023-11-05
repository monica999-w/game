package auth;

import game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthFrame extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton actionButton;
    private JLabel actionLabel;
    private JLabel titleLabel;
    private ConnectionDB connectionDB;
    private boolean isLoginMode;

    public AuthFrame(boolean isLoginMode) {
        this.isLoginMode = isLoginMode;
        setTitle(isLoginMode ? "Login" : "Register");
        setSize(400, 300);
        setLocationRelativeTo(null);

        connectionDB = new ConnectionDB("jdbc:mysql://localhost:3306/game", "root", "toor");
        connectionDB.connect();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.setBackground(new Color(255, 255, 200));

        titleLabel = new JLabel(isLoginMode ? "Login" : "Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(50, 100, 200));

        actionLabel = new JLabel(isLoginMode ? "New user? Register here." : "Already have an account? Log in.");
        actionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        actionLabel.setForeground(new Color(50, 100, 200));
        actionLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                actionLabelClicked();
            }
        });

        usernameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        passwordField = new JPasswordField();
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 15));

        actionButton = new JButton(isLoginMode ? "Login" : "Register");
        actionButton.addActionListener(this);
        actionButton.setBackground(new Color(100, 200, 100));
        actionButton.setForeground(Color.WHITE);
        actionButton.setFont(new Font("Arial", Font.BOLD, 15));
        actionButton.setFocusPainted(false);

        panel.add(titleLabel);
        panel.add(new JLabel());
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(actionLabel);
        panel.add(actionButton);

        add(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (isLoginMode) {
            if (connectionDB.login(username, password)) {
                int userId = connectionDB.getUserId(username);

                dispose();
                connectionDB.listUsers();
                connectionDB.close();
                SwingUtilities.invokeLater(() -> new GameFrame());
            } else {
                JOptionPane.showMessageDialog(this, "Login failed. Check username and password.");
            }
        } else {
            if (connectionDB.register(username, password)) {
                int userId = connectionDB.getUserId(username);

                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                SwingUtilities.invokeLater(() -> new GameFrame());

            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
            }
        }
    }
    private void actionLabelClicked() {
        dispose();
        SwingUtilities.invokeLater(() -> new AuthFrame(!isLoginMode));
    }
}
