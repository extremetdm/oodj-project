package oodj_project.features.dashboard.student_performance_report;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentRepository;

public class StudentPerformanceController {
    
    private final Session session;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentPerformanceService performanceService;
    
    public StudentPerformanceController(
        Session session,
        EnrollmentRepository enrollmentRepository,
        StudentPerformanceService performanceService
    ) {
        this.session = session;
        this.enrollmentRepository = enrollmentRepository;
        this.performanceService = performanceService;
    }

    public PaginatedResult<StudentPerformance> index(Query<StudentPerformance> query) {
        var enrollments = enrollmentRepository.all();

        if (!session.can(Permission.READ_ALL_STUDENT_PERFORMANCE)) {
            enrollments = enrollments.stream()
                .filter(enrollment -> {
                    var lecturer = enrollment.classGroup().lecturer();
                    return lecturer != null && lecturer.equals(session.currentUser());
                })
                .toList();
        }

        var performanceList = performanceService.getForEnrollments(enrollments);
    
        return query != null?
            query.apply(performanceList):
            PaginatedResult.singlePage(performanceList);
    }
}
