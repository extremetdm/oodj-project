package oodj_project.features.navigation;

import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import oodj_project.core.data.Context;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.assessment_grading.AssessmentResultController;
import oodj_project.features.dashboard.assessment_grading.AssessmentResultRepository;
import oodj_project.features.dashboard.assessment_grading.AssessmentResultView;
import oodj_project.features.dashboard.assessment_grading.GradeBookService;
import oodj_project.features.dashboard.assessment_management.AssessmentController;
import oodj_project.features.dashboard.assessment_management.AssessmentRepository;
import oodj_project.features.dashboard.assessment_management.AssessmentView;
import oodj_project.features.dashboard.class_enrollment.ClassRegistrationView;
import oodj_project.features.dashboard.class_enrollment_report.EnrollmentReportController;
import oodj_project.features.dashboard.class_enrollment_report.EnrollmentReportView;
import oodj_project.features.dashboard.class_management.ClassController;
import oodj_project.features.dashboard.class_management.ClassRepository;
import oodj_project.features.dashboard.class_management.ClassView;
import oodj_project.features.dashboard.class_performance_report.ClassPerformanceController;
import oodj_project.features.dashboard.class_performance_report.ClassPerformanceView;
import oodj_project.features.dashboard.enrolled_classes.EnrolledClassController;
import oodj_project.features.dashboard.enrolled_classes.EnrolledClassView;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentRepository;
import oodj_project.features.dashboard.feedback_management.FeedbackController;
import oodj_project.features.dashboard.feedback_management.FeedbackRepository;
import oodj_project.features.dashboard.feedback_management.FeedbackView;
import oodj_project.features.dashboard.grading_system_management.GradeController;
import oodj_project.features.dashboard.grading_system_management.GradeRepository;
import oodj_project.features.dashboard.grading_system_management.GradeView;
import oodj_project.features.dashboard.grading_system_management.GradingService;
import oodj_project.features.dashboard.lecturer_workload_report.LecturerWorkloadController;
import oodj_project.features.dashboard.lecturer_workload_report.LecturerWorkloadView;
import oodj_project.features.dashboard.module_management.ModuleController;
import oodj_project.features.dashboard.module_management.ModuleRepository;
import oodj_project.features.dashboard.module_management.ModuleView;
import oodj_project.features.dashboard.permission_management.RolePermissionController;
import oodj_project.features.dashboard.permission_management.RolePermissionRepository;
import oodj_project.features.dashboard.role_management.RoleController;
import oodj_project.features.dashboard.role_management.RoleRepository;
import oodj_project.features.dashboard.student_assessment_result.StudentResultController;
import oodj_project.features.dashboard.student_assessment_result.StudentResultView;
import oodj_project.features.dashboard.student_feedback.StudentFeedbackController;
import oodj_project.features.dashboard.student_feedback.StudentFeedbackView;
import oodj_project.features.dashboard.student_feedback_report.FeedbackReportController;
import oodj_project.features.dashboard.student_feedback_report.FeedbackReportView;
import oodj_project.features.dashboard.student_performance_report.StudentPerformanceController;
import oodj_project.features.dashboard.student_performance_report.StudentPerformanceService;
import oodj_project.features.dashboard.student_performance_report.StudentPerformanceView;
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
        (context, session, navigator) -> new UserView(
            session,
            new UserController(
                session,
                context.get(UserRepository.class), 
                context.get(UserPermissionService.class),
                context.get(EmailService.class)
            ),
            new RoleController(context.get(RoleRepository.class)),
            new RolePermissionController(context.get(RolePermissionRepository.class))
        )
    ),
    GRADING_SYSTEM_MANAGEMENT(
        "Grading System Management",
        null,
        Permission.READ_GRADES,
        (context, session, navigator) -> new GradeView(
            session,
            new GradeController(context.get(GradeRepository.class))
        )
    ),
    CLASS_MANAGEMENT(
        "Class Management",
        null,
        Permission.READ_CLASSES,
        (context, session, navigator) -> new ClassView(
            session,
            new ClassController(session, context.get(ClassRepository.class)),
            new ModuleController(context.get(ModuleRepository.class)),
            new TeamMemberController(
                session,
                context.get(TeamMemberRepository.class),
                context.get(UserPermissionService.class)
            )
        )
    ),
    MODULE_MANAGEMENT(
        "Module Management",
        // new ImageIcon("src/main/resources/icons/user-management.png"),
        null,
        Permission.READ_MODULES,
        (context, session, navigator) -> new ModuleView(
            session,
            new ModuleController(context.get(ModuleRepository.class))
        )
    ),
    TEAM_MANAGEMENT(
        "Lecturer Management",
        null,
        Permission.READ_TEAM_MANAGEMENT,
        (context, session, navigator) -> new TeamMemberView(
            session,
            new TeamMemberController(
                session,
                context.get(TeamMemberRepository.class),
                context.get(UserPermissionService.class)),
            new UserController(
                session,
                context.get(UserRepository.class),
                context.get(UserPermissionService.class),
                context.get(EmailService.class)
            )
        )
    ),
    ASSESSMENT_MANAGEMENT(
        "Assessment Management",
        null,
        Permission.READ_ASSESSMENTS,
        (context, session, navigator) -> new AssessmentView(
            session,
            new AssessmentController(
                session,
                context.get(AssessmentRepository.class)
            ),
            new ClassController(session, context.get(ClassRepository.class))
        )
    ),
    ASSESSMENT_RESULTS(
        "Assessment Grading",
        null,
        Permission.READ_ASSESSMENT_RESULTS,
        (context, session, navigator) -> new AssessmentResultView(
            session,
            new AssessmentResultController(
                session, 
                context.get(AssessmentResultRepository.class),
                context.get(ClassRepository.class),
                context.get(GradeBookService.class)
            )
        )
    ),
    LECTURER_WORKLOAD_REPORT(
        "Lecturer Workload Report",
        null,
        Permission.READ_LECTURER_WORKLOAD,
        (context, session, navigator) -> new LecturerWorkloadView(
            session,
            new LecturerWorkloadController(
                session,
                context.get(TeamMemberRepository.class),
                context.get(ClassRepository.class),
                context.get(EnrollmentRepository.class)
            )
        )
    ),
    ENROLLMENT_REPORT(
        "Enrollment Report",
        null,
        Permission.READ_ENROLLMENT_REPORT,
        (context, session, navigator) -> new EnrollmentReportView(
            session,
            new EnrollmentReportController(
                context.get(EnrollmentRepository.class),
                context.get(ClassRepository.class)
            )
        )
    ),
    CLASS_ENROLLMENT(
        "Class Registration",
        null,
        Permission.ENROLL_CLASSES,
        (context, session, navigator) -> new ClassRegistrationView(
            session,
            new EnrolledClassController(
                session,
                context.get(EnrollmentRepository.class),
                context.get(ClassRepository.class)
            )
        )
    ),
    ENROLLED_CLASSES(
        "My Classes",
        null,
        Permission.ENROLL_CLASSES,
        (context, session, navigator) -> new EnrolledClassView(
            session,
            navigator,
            new EnrolledClassController(
                session,
                context.get(EnrollmentRepository.class),
                context.get(ClassRepository.class)
            )
        )
    ),
    STUDENT_ASSESSMENT_RESULTS(
        "My Assessment Results",
        null,
        Permission.ENROLL_CLASSES,
        (context, session, navigator) -> new StudentResultView(
            session,
            new StudentResultController(
                session,
                context.get(GradeBookService.class),
                context.get(GradingService.class)
            )
        )
    ),
    STUDENT_FEEDBACKS(
        "My Feedbacks",
        null,
        Permission.GIVE_FEEDBACKS,
        (context, session, navigator) -> new StudentFeedbackView(
            new StudentFeedbackController(
                session,
                context.get(FeedbackRepository.class),
                context.get(EnrollmentRepository.class)
            )
        )
    ),
    FEEDBACK_MANAGEMENT(
        "Feedback Management",
        null,
        Permission.READ_FEEDBACKS,
        (context, session, navigator) -> new FeedbackView(
            session,
            new FeedbackController(
                session,
                context.get(FeedbackRepository.class)
            )
        )
    ),
    STUDENT_PERFORMANCE_REPORT(
        "Student Performance Report",
        null,
        Permission.READ_STUDENT_PERFORMANCE,
        (context, session, navigator) -> new StudentPerformanceView(
            session,
            new StudentPerformanceController(
                session,
                context.get(EnrollmentRepository.class),
                context.get(StudentPerformanceService.class)
            )
        )
    ),
    CLASS_PERFORMANCE_REPORT(
        "Class Performance Report",
        null,
        Permission.READ_CLASS_PERFORMANCE,
        (context, session, navigator) -> new ClassPerformanceView(
            session,
            new ClassPerformanceController(
                session,
                context.get(EnrollmentRepository.class),
                context.get(StudentPerformanceService.class),
                context.get(GradingService.class)
            )
        )
    ),
    STUDENT_FEEDBACK_REPORT(
        "Student Feedback Report",
        null,
        Permission.READ_FEEDBACK_REPORT,
        (context, session, navigator) -> new FeedbackReportView(
            session,
            new FeedbackReportController(
                session,
                context.get(FeedbackRepository.class),
                context.get(EnrollmentRepository.class)
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

    public JPanel createView(Context context, Session session, Navigator navigator) {
        return view.create(context, session, navigator);
    }

    public static List<NavigationItem> visibleFor(Session session) {
        return Arrays.stream(values())
                .filter(item -> session.can(item.requiredPermission))
                .toList();
    }
}
