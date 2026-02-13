package oodj_project.features.edit_profile;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.user_management.User;

public class EditProfileFormContent extends JPanel {

    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final JPasswordField 
        newPasswordField = new JPasswordField(),
        confirmPasswordField = new JPasswordField();

    private final FormTextField 
        usernameField = new FormTextField(),
        emailField = new FormTextField(),
        phoneField = new FormTextField();

    private final User user;

    public EditProfileFormContent(User user) {
        super();
        this.user = user;

        var builder = new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(5, 5, 5, 5));

        usernameField.setText(user.username());
        emailField.setText(user.email());
        phoneField.setText(user.phoneNumber());

        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("ID"),
            new FormTextField(user.id().toString(), false),
            new FormLabel("Role"),
            new FormTextField(user.role().name(), false),
            new FormLabel("Username"),
            usernameField,
            new FormLabel("Name"),
            new FormTextField(user.name(), false),
            new FormLabel("Gender"),
            new FormTextField(user.gender().name(), false),
            new FormLabel("Email"),
            emailField,
            new FormLabel("Phone"),
            phoneField,
            new FormLabel("Date of birth"),
            new FormTextField(LineFormatter.formatDate(user.dateOfBirth(), "yyyy-MM-dd"), false),
            new FormLabel("New Password"),
            newPasswordField,
            new FormLabel("Confirm Password"),
            confirmPasswordField
        )
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public User getFormData() {
        
        var newPassword = new String(newPasswordField.getPassword());
        var confirmPassword = new String(confirmPasswordField.getPassword());
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password Mismatch!");
        }

        var password = newPassword.isBlank()? user.password(): newPassword;

        return new User(
            usernameField.getText(),
            user.name(),
            password,
            user.gender(),
            user.role(),
            emailField.getText(),
            phoneField.getText(),
            user.dateOfBirth()
        );
    }
}