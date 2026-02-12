package oodj_project.features.dashboard.user_management;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.management_view.FormFactory;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.permission_management.RolePermissionController;
import oodj_project.features.dashboard.role_management.Role;
import oodj_project.features.dashboard.role_management.RoleController;

public class UserFormFactory extends FormFactory<User> {

    private static final List<SortOption<User>> SORT_OPTIONS = List.of(
            SortOption.of("User ID", User::id));

    private final UserController userController;
    private final RoleController roleController;
    private final RolePermissionController permissionController;

    public UserFormFactory(
            Component component,
            UserController userController,
            RoleController roleController,
            RolePermissionController permissionController) {
        super(component, SORT_OPTIONS, List.of(
                FilterOption.compare("User ID", User::id, InputStrategy.nonNegativeIntegerField()),
                FilterOption.sameAs(
                        "Role",
                        User::role,
                        InputStrategy.selectField(Role::name, roleController.index()),
                        Role::name)));
        this.userController = userController;
        this.roleController = roleController;
        this.permissionController = permissionController;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort User";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter User";
    }

    public Form getCreateForm(Runnable onCreate) {
        var content = new UserEditFormContent(
                roleController.index(),
                userController.getSupervisors(),
                permissionController::roleHasPermission);

        var form = Form.builder(getParentWindow(), content, handleCreate(content, onCreate))
                .windowTitle("Create User")
                .formTitle("Create User")
                .confirmText("Create")
                .build();

        form.setVisible(true);

        return form;
    }

    private ActionListener handleCreate(UserEditFormContent content, Runnable onCreate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                userController.create(content.getFormData());
                if (onCreate != null)
                    onCreate.run();
                window.dispose();

            } catch (IllegalArgumentException | IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error creating user", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    public Form getEditForm(User user, Runnable onUpdate) {
        var content = new UserEditFormContent(
                roleController.index(),
                userController.getSupervisors(),
                permissionController::roleHasPermission,
                user);

        var form = Form.builder(getParentWindow(), content, handleEdit(user, content, onUpdate))
                .windowTitle("Edit User")
                .formTitle("Edit User")
                .confirmText("Update")
                .build();

        form.setVisible(true);

        return form;
    }

    private ActionListener handleEdit(User user, UserEditFormContent content, Runnable onUpdate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                userController.update(user.id(), content.getFormData());
                if (onUpdate != null)
                    onUpdate.run();
                window.dispose();

            } catch (IllegalArgumentException | IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error editing user", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}
