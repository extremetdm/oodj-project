package oodj_project.features.navigation;

import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import oodj_project.core.data.Context;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.class_enrollment_report.EnrollmentReportController;
import oodj_project.features.class_enrollment_report.EnrollmentReportView;
import oodj_project.features.class_management.ClassRepository;
import oodj_project.features.enrollment_management.EnrollmentRepository;
import oodj_project.features.module_management.ModuleController;
import oodj_project.features.module_management.ModuleRepository;
import oodj_project.features.module_management.ModuleView;

public enum NavigationItem {
    USER_MANAGEMENT(
        "User Management",
        new ImageIcon("src/main/resources/icons/user-management.png"),
        Permission.READ_USERS,
        (a,b) -> new JPanel()
        // (context, session) -> 
    ),
    MODULE_MANAGEMENT(
        "Module Management",
        // new ImageIcon("src/main/resources/icons/user-management.png"),
        null,
        Permission.READ_MODULES,
        (context, session) -> new ModuleView(
            session,
            new ModuleController(context.get(ModuleRepository.class))
        )
    ),
    ENROLLMENT_REPORT(
        "Enrollment Report",
        null,
        Permission.READ_ENROLLMENT_REPORT,
        (context, session) -> new EnrollmentReportView(
            session,
            new EnrollmentReportController(
                context.get(EnrollmentRepository.class),
                context.get(ClassRepository.class)
            )
        )
    );

    private final String label;
    private final Icon icon;
    private final Permission requiredPermission;
    private final ViewGenerator view;

    private NavigationItem(String label, Icon icon, Permission requiredPermission, ViewGenerator view) {
        this.label = label;
        this.icon = icon;
        this.requiredPermission = requiredPermission;
        this.view = view;
    }

    public String label() {
        return label;
    }

    public Icon icon() {
        return icon;
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
