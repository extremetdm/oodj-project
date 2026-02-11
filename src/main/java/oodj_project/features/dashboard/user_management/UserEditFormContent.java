package oodj_project.features.dashboard.user_management;

import java.awt.Insets;
import java.util.List;
import java.util.function.BiPredicate;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;

import oodj_project.core.security.Permission;
import oodj_project.core.ui.components.form.FormComboBox;
import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.role_management.Role;

public class UserEditFormContent extends JPanel {

    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final FormTextField 
        usernameField = new FormTextField(),
        passwordField = new FormTextField(PasswordGenerator.generate(10)),
        nameField = new FormTextField(),
        emailField = new FormTextField(),
        phoneField = new FormTextField();

    private final FormComboBox<Role> roleField;
    private final FormComboBox<Gender> genderField = new FormComboBox<>(Gender::name, Gender.values());
    private final FormComboBox<User> supervisorField;

    private final JDateChooser dateOfBirthField = new JDateChooser();

    public UserEditFormContent(
        List<Role> roles,
        List<User> supervisors,
        BiPredicate<Role, Permission> permissionChecker
    ) {
        this(roles, supervisors, permissionChecker, null);
    }

    public UserEditFormContent(
        List<Role> roles,
        List<User> supervisors,
        BiPredicate<Role, Permission> permissionChecker,
        User user
    ) {
        super();

        var builder = new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(5, 5, 5, 5));

        var supervisorLabel = new FormLabel("Academic Leader");
        supervisorField = new FormComboBox<>(
            supervisor -> supervisor.name(),
            supervisors
        );

        roleField = new FormComboBox<>(
            role -> role.name(),
            roles
        );
        // Enable/Disable Leader Box based on Role
        roleField.addActionListener(evt -> {
            var role = roleField.getSelectedItem();
            var visibility = isLecturer(permissionChecker, role);
            supervisorLabel.setVisible(visibility);
            supervisorField.setVisible(visibility);
            builder.build();
        });

        dateOfBirthField.setDateFormatString("yyyy-MM-dd");
        
        if (user != null) {
            builder.add(
                COLUMN_WEIGHTS,
                new FormLabel("ID"),
                new FormTextField(user.id().toString(), false)
            );
            usernameField.setText(user.username());
            passwordField.setText(user.password());
            nameField.setText(user.name());
            emailField.setText(user.email());
            phoneField.setText(user.phoneNumber());

            roleField.setSelectedItem(user.role());
            genderField.setSelectedItem(user.gender());

            dateOfBirthField.setDate(user.dateOfBirth());

            if (isLecturer(permissionChecker, user.role())) {
                supervisorField.setSelectedItem(null);
            }
        }

        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("Role"),
            roleField,
            new FormLabel("Username"),
            usernameField
        );

        if (user == null) {
            builder.add(
                COLUMN_WEIGHTS,
                new FormLabel("Password"),
                passwordField
            );
        }

        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("Name"),
            nameField,
            new FormLabel("Gender"),
            genderField,
            new FormLabel("Email"),
            emailField,
            new FormLabel("Phone"),
            phoneField,
            new FormLabel("Date of birth"),
            dateOfBirthField,
            supervisorLabel,
            supervisorField
        )   
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    private static boolean isLecturer(BiPredicate<Role, Permission> permissionChecker, Role role) {
        return permissionChecker.test(role, Permission.TEACH_CLASSES);
    }

    public User getFormData() {
        return new User(
            usernameField.getText(),
            nameField.getText(),
            passwordField.getText(),
            genderField.getSelectedItem(),
            roleField.getSelectedItem(),
            emailField.getText(),
            phoneField.getText(),
            dateOfBirthField.getDate()
        );
    }
}