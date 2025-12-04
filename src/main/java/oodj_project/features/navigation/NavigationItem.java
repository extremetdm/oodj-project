package oodj_project.features.navigation;

import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import oodj_project.core.data.Context;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.module_management.ModuleController;
import oodj_project.features.module_management.ModuleRepository;
import oodj_project.features.module_management.ModuleView;

public enum NavigationItem {
    USER_MANAGEMENT(
        "User Management",
        Permission.READ_USERS,
        (a,b) -> new JPanel()
        // (context, session) -> 
    ),
    MODULE_MANAGEMENT(
        "Module Management",
        Permission.READ_MODULES,
        (context, session) -> new ModuleView(
            session,
            new ModuleController(context.get(ModuleRepository.class))
        )
    );

    private final String label;
    private final Permission requiredPermission;
    private final ViewGenerator view;

    private NavigationItem(String label, Permission requiredPermission, ViewGenerator view) {
        this.label = label;
        this.requiredPermission = requiredPermission;
        this.view = view;
    }

    public String label() {
        return label;
    }

    public Permission requiredPermission() {
        return requiredPermission;
    }

    public JPanel createView(Context context, Session session) {
        return view.create(context, session);
    }
    
    public static List<NavigationItem> visibleFor(Session session) {
        return Arrays.stream(values())
            .filter(item -> session.can(item.requiredPermission))
            .toList();
    }
}
