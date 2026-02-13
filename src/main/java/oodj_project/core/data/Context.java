package oodj_project.core.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import oodj_project.features.dashboard.assessment_grading.AssessmentResultRepository;
import oodj_project.features.dashboard.assessment_grading.GradeBookService;
import oodj_project.features.dashboard.assessment_management.AssessmentRepository;
import oodj_project.features.dashboard.class_management.ClassRepository;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentRepository;
import oodj_project.features.dashboard.feedback_management.FeedbackRepository;
import oodj_project.features.dashboard.grading_system_management.GradeRepository;
import oodj_project.features.dashboard.grading_system_management.GradingService;
import oodj_project.features.dashboard.module_management.ModuleRepository;
import oodj_project.features.dashboard.permission_management.RolePermissionRepository;
import oodj_project.features.dashboard.role_management.RoleRepository;
import oodj_project.features.dashboard.student_performance_report.StudentPerformanceService;
import oodj_project.features.dashboard.team_management.TeamMemberRepository;
import oodj_project.features.dashboard.user_management.UserPermissionService;
import oodj_project.features.dashboard.user_management.UserRepository;
import oodj_project.features.services.EmailService;

public class Context {
    private final File dataDir;
    private static final Context INSTANCE = new Context();
    private final Map<Class<?>, Object> services = new HashMap<>();

    public static Context instance() {
        return INSTANCE;
    }

    private Context() {
        File data = new File("data");
        File parentData = new File("../data");

        if (parentData.exists() && parentData.isDirectory() && new File("pom.xml").exists()) {
            this.dataDir = parentData;
        } else {
            this.dataDir = data;
        }
    }

    public <ServiceT> ServiceT get(Class<ServiceT> repositoryClass) {
        var instance = services.get(repositoryClass);
        if (instance == null)
            throw new NoSuchElementException("Repository not registered.");
        return repositoryClass.cast(instance);
    }

    private void register(Object repository) {
        services.put(repository.getClass(), repository);
    }

    public void initialize() throws IOException {
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        var roles = new RoleRepository(
                checkFile("roles.txt"));
        register(roles);

        var rolePermissions = new RolePermissionRepository(
                checkFile("role-permissions.txt"),
                roles);
        register(rolePermissions);

        var users = new UserRepository(
                checkFile("users.txt"),
                roles);
        register(users);

        var grades = new GradeRepository(
                checkFile("grades.txt"));
        register(grades);

        var modules = new ModuleRepository(
                checkFile("modules.txt"));
        register(modules);

        var classes = new ClassRepository(
                checkFile("classes.txt"),
                modules,
                users);
        register(classes);

        var enrollments = new EnrollmentRepository(
                checkFile("enrollments.txt"),
                users,
                classes);
        register(enrollments);

        var teamMembers = new TeamMemberRepository(
                checkFile("team-members.txt"),
                users);
        register(teamMembers);

        var assessments = new AssessmentRepository(
            checkFile("assessments.txt"),
            classes
        );
        register(assessments);

        var results = new AssessmentResultRepository(
            checkFile("assessment-results.txt"),
            assessments,
            enrollments
        );
        register(results);

        var feedbacks = new FeedbackRepository(
            checkFile("feedbacks.txt"),
            enrollments
        );
        register(feedbacks);

        // teamMembers.create(new TeamMember(users.findFirst(a -> true).get(), users.findFirst(a -> true).get()));

        // classes.create(new ClassGroup(
        // modules.findFirst(a -> true).get(),
        // 10,
        // null
        // ));

        // enrollments.create(new Enrollment(
        // users.findFirst(a -> true).get(),
        // classes.findFirst(a -> true).get()
        // ));

        var emailService = new EmailService(
                "https://api.emailjs.com/api/v1.0/email/send",
                "service_oodjafs",
                "template_9h24en8",
                "template_w0czcsa",
                "7f6GWPVgA3ok7tUsF");
        register(emailService);

        var userPermissionService = new UserPermissionService(users, rolePermissions);
        register(userPermissionService);

        var gradeBookService = new GradeBookService(results, assessments, enrollments);
        register(gradeBookService);

        var gradingService = new GradingService(grades);
        register(gradingService);

        var performanceService = new StudentPerformanceService(results, gradingService);
        register(performanceService);
    }

    private File checkFile(String filePath) throws IOException {
        File file = new File(dataDir, filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
