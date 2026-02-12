package oodj_project.features.navigation;

import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import oodj_project.core.data.Context;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.class_enrollment_report.EnrollmentReportController;
import oodj_project.features.dashboard.class_enrollment_report.EnrollmentReportView;
import oodj_project.features.dashboard.class_management.ClassController;
import oodj_project.features.dashboard.class_management.ClassRepository;
import oodj_project.features.dashboard.class_management.ClassView;
import oodj_project.features.dashboard.enrollment_management.EnrollmentRepository;
import oodj_project.features.dashboard.grading_system_management.GradeController;
import oodj_project.features.dashboard.grading_system_management.GradeRepository;
import oodj_project.features.dashboard.grading_system_management.GradeView;
import oodj_project.features.dashboard.lecturer_workload_report.LecturerWorkloadController;
import oodj_project.features.dashboard.lecturer_workload_report.LecturerWorkloadView;
import oodj_project.features.dashboard.module_management.ModuleController;
import oodj_project.features.dashboard.module_management.ModuleRepository;
import oodj_project.features.dashboard.module_management.ModuleView;
import oodj_project.features.dashboard.permission_management.RolePermissionController;
import oodj_project.features.dashboard.permission_management.RolePermissionRepository;
import oodj_project.features.dashboard.role_management.RoleController;
import oodj_project.features.dashboard.role_management.RoleRepository;
import oodj_project.features.dashboard.team_management.TeamMemberController;
import oodj_project.features.dashboard.team_management.TeamMemberRepository;
import oodj_project.features.dashboard.team_management.TeamMemberView;
import oodj_project.features.dashboard.user_management.UserController;
import oodj_project.features.dashboard.user_management.UserPermissionService;
import oodj_project.features.dashboard.user_management.UserRepository;
import oodj_project.features.dashboard.user_management.UserView;
import oodj_project.features.services.EmailService;

public enum NavigationItem {
    USER_MANAGEMENT(
            "User Management",
            new ImageIcon("src/main/resources/icons/user-management.png"),
            Permission.READ_USERS,
            (context, session) -> new UserView(
                    session,
                    new UserController(
                            context.get(UserRepository.class),
                            context.get(UserPermissionService.class),
                            context.get(EmailService.class)),
                    new RoleController(context.get(RoleRepository.class)),
                    new RolePermissionController(context.get(RolePermissionRepository.class)))),
    GRADING_SYSTEM_MANAGEMENT(
            "Grading System Management",
            null,
            Permission.READ_GRADES,
            (context, session) -> new GradeView(
                    session,
                    new GradeController(context.get(GradeRepository.class)))),
    CLASS_MANAGEMENT(
            "Class Management",
            null,
            Permission.READ_CLASSES,
            (context, session) -> new ClassView(
                    session,
                    new ClassController(context.get(ClassRepository.class)),
                    new ModuleController(context.get(ModuleRepository.class)),
                    new TeamMemberController(
                            session,
                            context.get(TeamMemberRepository.class),
                            context.get(UserPermissionService.class)))),
    MODULE_MANAGEMENT(
            "Module Management",
            // new ImageIcon("src/main/resources/icons/user-management.png"),
            null,
            Permission.READ_MODULES,
            (context, session) -> new ModuleView(
                    session,
                    new ModuleController(context.get(ModuleRepository.class)))),
    TEAM_MANAGEMENT(
            "Lecturer Management",
            null,
            Permission.READ_TEAM_MANAGEMENT,
            (context, session) -> new TeamMemberView(
                    session,
                    new TeamMemberController(
                            session,
                            context.get(TeamMemberRepository.class),
                            context.get(UserPermissionService.class)),
                    new UserController(
                            context.get(UserRepository.class),
                            context.get(UserPermissionService.class),
                            context.get(EmailService.class)))),
    LECTURER_WORKLOAD_REPORT(
            "Lecturer Workload Report",
            null,
            Permission.READ_LECTURER_WORKLOAD,
            (context, session) -> new LecturerWorkloadView(
                    session,
                    new LecturerWorkloadController(
                            session,
                            context.get(TeamMemberRepository.class),
                            context.get(ClassRepository.class),
                            context.get(EnrollmentRepository.class)))),
    ENROLLMENT_REPORT(
            "Enrollment Report",
            null,
            Permission.READ_ENROLLMENT_REPORT,
            (context, session) -> new EnrollmentReportView(
                    session,
                    new EnrollmentReportController(
                            context.get(EnrollmentRepository.class),
                            context.get(ClassRepository.class))));

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
