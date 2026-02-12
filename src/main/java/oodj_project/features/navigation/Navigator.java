package oodj_project.features.navigation;

import java.awt.CardLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import oodj_project.core.data.Context;
import oodj_project.core.security.Session;

public class Navigator {
    private final Context context;
    private final Session session;
    private final CardLayout layout;
    private final JPanel panel;

    private final HashMap<NavigationItem, JPanel> cachedView = new HashMap<>();

    public Navigator(Context context, Session session, CardLayout layout, JPanel panel) {
        this.context = context;
        this.session = session;
        this.layout = layout;
        this.panel = panel;
    }

    public void goTo(NavigationItem item) {
        goTo(item, null);
    }

    public void goTo(NavigationItem item, Object payload) {
        var view = cachedView.get(item);
        
        if (view == null) {
            view = item.createView(context, session, this);
            panel.add(view, item.name());
            cachedView.put(item, view);
        }

        switch (view) {
            case Navigable navigable -> navigable.onNavigate(payload);
            default -> {}
        }

        layout.show(panel, item.name());
    }
}
