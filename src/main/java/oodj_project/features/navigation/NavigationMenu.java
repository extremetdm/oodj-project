package oodj_project.features.navigation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.security.Session;

public class NavigationMenu extends JPanel {

    private final Dimension BUTTON_SIZE = new Dimension(300, 50);

    private final JPanel listPanel = new JPanel();

    public NavigationMenu(Session session, Navigator navigator, Runnable onLogout) {
        super(new BorderLayout());

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        for (var item: NavigationItem.visibleFor(session)) {
            addButton(item.label(), item.icon(), event -> navigator.goTo(item));
        }
        listPanel.add(Box.createVerticalGlue());

        add(listPanel, BorderLayout.CENTER);

        add(new UserSidebarWidget(session, null, onLogout), BorderLayout.SOUTH);
    }

    private void addButton(String label, Icon icon, ActionListener action) {
        var button = new JButton(label, icon);
        button.addActionListener(action);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setBackground(Color.WHITE);

        button.setMaximumSize(BUTTON_SIZE);
        button.setPreferredSize(BUTTON_SIZE);
        button.setFont(new Font("Swis721 BlkCn BT", Font.PLAIN, 20));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        listPanel.add(button);
    }
}
