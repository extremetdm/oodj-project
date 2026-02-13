package oodj_project.features.dashboard.class_performance_report;

import java.util.stream.Collectors;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentRepository;
import oodj_project.features.dashboard.grading_system_management.GradingService;
import oodj_project.features.dashboard.student_performance_report.StudentPerformanceService;

public class ClassPerformanceController {
    
    private final Session session;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentPerformanceService performanceService;
    private final GradingService gradingService;
    
    public ClassPerformanceController(
        Session session,
        EnrollmentRepository enrollmentRepository,
        StudentPerformanceService performanceService,
        GradingService gradingService
    ) {
        this.session = session;
        this.enrollmentRepository = enrollmentRepository;
        this.performanceService = performanceService;
        this.gradingService = gradingService;
    }

    public PaginatedResult<ClassPerformance> index(Query<ClassPerformance> query) {
        var enrollments = enrollmentRepository.all();

        if (!session.can(Permission.READ_ALL_CLASS_PERFORMANCE)) {
            enrollments = enrollments.stream()
                .filter(enrollment -> {
                    var lecturer = enrollment.classGroup().lecturer();
                    return lecturer != null && lecturer.equals(session.currentUser());
                })
                .toList();
        }

        var performanceList = performanceService.getForEnrollments(enrollments)
            .stream()
            .collect(Collectors.groupingBy(
                performance -> performance.enrollment().classGroup()
            ))
            .entrySet()
            .stream()
            .map(map -> {
                var performance = new ClassPerformance(map.getKey(), map.getValue());
                var summary = performance.summary();
                var maxMarks = performance.maxMarks();

                return performance.withGrades(
                    gradingService.calculate(summary.getMin(), maxMarks),
                    gradingService.calculate(summary.getAverage(), maxMarks),
                    gradingService.calculate(summary.getMax(), maxMarks)
                );
            })
            .toList();
    
        return query != null?
            query.apply(performanceList):
            PaginatedResult.singlePage(performanceList);
    }
}
