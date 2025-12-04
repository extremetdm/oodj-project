package oodj_project.features.navigation;

import java.awt.CardLayout;
import java.util.HashSet;

import javax.swing.JPanel;

import oodj_project.core.data.Context;
import oodj_project.core.security.Session;

public class Navigator {
    private final Context context;
    private final Session session;
    private final CardLayout layout;
    private final JPanel panel;

    private final HashSet<NavigationItem> cachedView = new HashSet<>();

    public Navigator(Context context, Session session, CardLayout layout, JPanel panel) {
        this.context = context;
        this.session = session;
        this.layout = layout;
        this.panel = panel;
    }

    public void goTo(NavigationItem item) {
        if (!cachedView.contains(item)) {
            panel.add(item.createView(context, session), item.name());
            cachedView.add(item);
        }
        layout.show(panel, item.name());
    }
}
