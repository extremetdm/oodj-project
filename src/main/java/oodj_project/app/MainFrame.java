package oodj_project.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import oodj_project.core.data.Context;
import oodj_project.core.security.Session;
import oodj_project.features.navigation.Navigator;
import oodj_project.features.navigation.NavigationItem;
import oodj_project.features.navigation.NavigationMenu;

public class MainFrame extends JFrame {
    public MainFrame(Context context, Session session) {
        super("Assessment Feedback System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        var contentLayout = new CardLayout();
        var contentPanel = new JPanel(contentLayout);

        var navigator = new Navigator(context, session, contentLayout, contentPanel);
        
        var menu = new NavigationMenu(session, navigator);

        var availableContent = NavigationItem.visibleFor(session);
        if (!availableContent.isEmpty()) navigator.goTo(availableContent.get(0));

        add(menu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        // setSize(1000,100);
        pack();
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }
}
