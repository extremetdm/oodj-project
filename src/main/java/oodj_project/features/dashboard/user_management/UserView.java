package oodj_project.features.dashboard.user_management;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.permission_management.RolePermissionController;
import oodj_project.features.dashboard.role_management.RoleController;

public class UserView extends ManagementView<User> {

    private static final double[] COLUMN_WEIGHTS_WITH_ACTION = { 1, 9, 6, 6, 2 };
    private static final double[] COLUMN_WEIGHTS_WITHOUT_ACTION = { 1, 9, 7, 7 };
    
    private final Session session;
    private final UserController controller;

    private final DataList<User> dataTable;
    private final UserFormFactory formFactory;

    private final boolean hasActions;
    
    public UserView(
        Session session,
        UserController userController,
        RoleController roleController,
        RolePermissionController permissionController
    ) {
        super(
            "User Management",
            userController::index,
            UserView::buildSearchLogic
        );

        this.session = session;
        controller = userController;

        hasActions = session.can(Permission.UPDATE_USERS) 
            || session.can(Permission.DELETE_USERS);

        var columnWeight = hasActions?
            COLUMN_WEIGHTS_WITH_ACTION:
            COLUMN_WEIGHTS_WITHOUT_ACTION;

        dataTable = new DataList<>(
            columnWeight,
            createTableHeader(),
            this::createTableRow
        );

        formFactory = new UserFormFactory(
            this,
            userController,
            roleController,
            permissionController
        );
    
        if (session.can(Permission.CREATE_USERS)) {
            var addButton = new IconLabelButton("Add", Icons.ADD);
            addButton.addActionListener(event -> {
                formFactory.getCreateForm(this::refreshData);
            });
            toolbarComponents.add(addButton);
        }    

        init();
    }
    
    private static Predicate<User> buildSearchLogic(String searchQuery) {
        return user -> {
            return user.id().toString().contains(searchQuery) ||
                user.username().toLowerCase().contains(searchQuery) ||
                user.name().toLowerCase().contains(searchQuery);
        };
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
            DataList.createHeaderText("ID"),
            DataList.createHeaderText("Username"),
            DataList.createHeaderText("Full name"),
            DataList.createHeaderText("Role")
        ));

        if (hasActions) {
            var actionLabel = DataList.createHeaderText("Action");
            actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            components.add(actionLabel);
        }

        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(User user) {
        var components = new ArrayList<>(List.<Component>of(
            DataList.createText(user.id().toString()),
            DataList.createText(user.username()),
            DataList.createText(user.name()),
            DataList.createText(user.role().name())
        ));

        if (hasActions) {
            components.add(createActionMenu(user));
        }

        return components.toArray(Component[]::new);
    } 

    private IconButton createActionMenu(User user) {
        var actionMenu = new JPopupMenu();
        actionMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        if (session.can(Permission.UPDATE_USERS)) {
            actionMenu.add(createUpdateOption(user));   
            actionMenu.add(createResetPasswordOption(user));
        }

        if (session.can(Permission.DELETE_USERS)) {    
            actionMenu.add(createDeleteOption(user));
        }

        var menuButton = new IconButton(Icons.EDIT);
        menuButton.setToolTipText("Edit User");
        menuButton.addActionListener(event -> {
            actionMenu.show(menuButton, menuButton.getWidth() / 2, menuButton.getHeight() / 2);
        });

        return menuButton;
    }

    private JMenuItem createUpdateOption(User user) {
        var editOption = new JMenuItem("Edit user");
        editOption.addActionListener(event -> 
            formFactory.getEditForm(user, this::refreshData)
        );
        return editOption;
    }

    private JMenuItem createResetPasswordOption(User user) {
        var resetPasswordOption = new JMenuItem("Reset password");
        resetPasswordOption.addActionListener(evt -> {
            var action = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reset password for " + user.username() + "?\n" +
                    "A new random password will be generated and emailed.",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION
            );

            if (action == JOptionPane.YES_OPTION) {
                try {
                    controller.resetPassword(user);
                    JOptionPane.showMessageDialog(
                        this,
                        "Password reset successfully.\nNew password sent to: " + user.email()
                    );
                } catch (IOException e) {
                    String message =  "Failed to find user.";
                    JOptionPane.showMessageDialog(this, message, "Error deleting user", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return resetPasswordOption;  
    }

    private JMenuItem createDeleteOption(User user) {
        var deleteOption = new JMenuItem("Delete user");
        deleteOption.addActionListener(event -> {
            var action = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete user \"" + user.username() + "\"?",
                "Confirm delete user",
                JOptionPane.YES_NO_OPTION
            );

            if (action == JOptionPane.YES_OPTION) {
                try {
                    controller.delete(user);
                    refreshData();                
                } catch (IOException | NoSuchElementException e) {
                    String message = switch (e) {
                        case IOException _ -> "Failed to save changes.";
                        case NoSuchElementException _ -> "Failed to find user.";
                        default -> "Failed to delete user.";
                    };
                    JOptionPane.showMessageDialog(this, message, "Error deleting user", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return deleteOption;
    }

    @Override
    protected DataList<User> getContent() {
        return dataTable;
    }
    @Override
    protected UserFormFactory getFormFactory() {
        return formFactory;
    }
}
