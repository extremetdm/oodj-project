package oodj_project.features.edit_profile;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.form.Form;
import oodj_project.features.dashboard.user_management.User;
import oodj_project.features.dashboard.user_management.UserController;

public class EditProfileFormFactory {
    
    private final Component component;
    private final UserController userController;
    
    public EditProfileFormFactory(
        Component component,
        UserController userController
    ) {
        this.component = component;
        this.userController = userController;   
    }

    private Window getParentWindow() {
        return SwingUtilities.getWindowAncestor(component);
    }

    public Form getEditForm(User user, Runnable onUpdate) {
        var content = new EditProfileFormContent(user);

        var form = Form.builder(getParentWindow(), content, handleEdit(user, content, onUpdate))
            .windowTitle("Edit Profile")
            .formTitle("Edit Profile")
            .confirmText("Update")
            .build();

        form.setVisible(true);

        return form;
    }

    private ActionListener handleEdit(User user, EditProfileFormContent content, Runnable onUpdate) {
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
