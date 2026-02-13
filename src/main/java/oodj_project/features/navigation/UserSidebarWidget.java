package oodj_project.features.navigation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import oodj_project.core.data.Context;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.user_management.UserController;
import oodj_project.features.dashboard.user_management.UserPermissionService;
import oodj_project.features.dashboard.user_management.UserRepository;
import oodj_project.features.edit_profile.EditProfileFormFactory;
import oodj_project.features.services.EmailService;

public class UserSidebarWidget extends JPanel {

    public UserSidebarWidget(
        Context context, Session session, Runnable onLogoutClick
    ) {
        setLayout(new BorderLayout(10, 0));
        setOpaque(false); // Match sidebar color

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)));

        JLabel avatar = new JLabel(Icons.PROFILE);
        add(avatar, BorderLayout.WEST);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(session.currentUser().name());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel roleLabel = new JLabel(session.currentUser().role().name());
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        roleLabel.setForeground(Color.GRAY);

        textPanel.add(nameLabel);
        textPanel.add(roleLabel);
        add(textPanel, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actionsPanel.setOpaque(false);

        var editProfile = new EditProfileFormFactory(
            this,
            new UserController(
                session,
                context.get(UserRepository.class), 
                context.get(UserPermissionService.class),
                context.get(EmailService.class)
            )
        );

        var editProfileButton = new IconButton(Icons.EDIT);
        editProfileButton.setToolTipText("Edit Profile");
        editProfileButton.addActionListener(event -> 
            editProfile.getEditForm(session.currentUser(), () -> {
                repaint();
                revalidate();
            })
        );
        actionsPanel.add(editProfileButton);

        IconButton logoutButton = new IconButton(Icons.LOGOUT);
        logoutButton.setToolTipText("Logout");
        logoutButton.addActionListener(e -> onLogoutClick.run());
        actionsPanel.add(logoutButton);

        add(actionsPanel, BorderLayout.EAST);
    }
}