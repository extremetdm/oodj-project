package oodj_project.features.login;

import oodj_project.core.security.Session;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginView extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    private final Session session;
private final Runnable onSuccess;

    public LoginView(
        Session session,
        Runnable onSuccess
    ) {
        this.session = session;
        this.onSuccess = onSuccess;
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("AFS Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(evt -> performLogin());

        pack();
        setVisible(true);
    }

    private void performLogin() {
        
        String username = usernameField.getText(), 
            password = new String(passwordField.getPassword());

        if (!session.login(username, password)) {
            JOptionPane.showMessageDialog(
                this,
                "Invalid Username or Password!",
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        onSuccess.run();
        dispose();
    }
}
