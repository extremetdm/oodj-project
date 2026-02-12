package oodj_project.features.login;

import oodj_project.core.data.Context;
import oodj_project.features.dashboard.user_management.User;
import oodj_project.features.dashboard.user_management.UserController;
import oodj_project.features.dashboard.user_management.UserRepository;
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
import javax.swing.JCheckBox;
import java.awt.event.ItemEvent;

public class LoginView extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    private final Session session;
    private final Context context;
    private final Runnable onSuccess;

    public LoginView(
            Session session,
            Context context,
            Runnable onSuccess) {
        this.session = session;
        this.context = context;
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
        if (session.lastUsername() != null) {
            usernameField.setText(session.lastUsername());
        }
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(15);
        if (session.lastPassword() != null) {
            passwordField.setText(session.lastPassword());
        }
        gbc.gridx = 1;
        add(passwordField, gbc);

        JCheckBox showPassword = new JCheckBox("Show Password");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(showPassword, gbc);

        char defaultEchoChar = passwordField.getEchoChar();
        showPassword.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar(defaultEchoChar);
            }
        });

        passwordField.addActionListener(e -> performLogin());
        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        JButton forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPasswordButton.setBorderPainted(false);
        forgotPasswordButton.setContentAreaFilled(false);
        forgotPasswordButton.setFocusPainted(false);
        forgotPasswordButton.setOpaque(false);
        forgotPasswordButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        gbc.gridy = 5;
        add(forgotPasswordButton, gbc);

        forgotPasswordButton.addActionListener(e -> performForgotPassword());

        loginButton.addActionListener(evt -> performLogin());

        pack();
        setLocationRelativeTo(null);
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
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        onSuccess.run();
        dispose();
    }

    private void performForgotPassword() {
        String input = JOptionPane.showInputDialog(this, "Enter your Username or Email:", "Reset Password",
                JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            var userRepository = context.get(UserRepository.class);
            User user = userRepository
                    .findFirst(u -> u.username().equalsIgnoreCase(input) || u.email().equalsIgnoreCase(input))
                    .orElse(null);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var userController = new UserController(
                    userRepository,
                    context.get(oodj_project.features.dashboard.user_management.UserPermissionService.class),
                    context.get(oodj_project.features.services.EmailService.class));

            userController.resetPassword(user);
            JOptionPane.showMessageDialog(this, "A new password has been sent to your email.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
