package oodj_project.features.navigation;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import oodj_project.core.security.Session;

public class NavigationMenu extends JPanel {

    private final Dimension BUTTON_SIZE = new Dimension(300, 50);
    private final Font FONT = new Font("Swis721 BlkCn BT", Font.PLAIN, 20);

    public NavigationMenu(Session session, Navigator navigator) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (var item: NavigationItem.visibleFor(session)) {
            addButton(item.label(), event -> navigator.goTo(item));
        }
    }

    private void addButton(String label, ActionListener action) {
        var button = new JButton(label);
        button.addActionListener(action);

        button.setMaximumSize(BUTTON_SIZE);
        button.setPreferredSize(BUTTON_SIZE);
        button.setFont(FONT);
        add(button);
    }
}
