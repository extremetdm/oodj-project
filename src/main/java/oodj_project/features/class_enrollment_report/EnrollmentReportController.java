package oodj_project.features.class_enrollment_report;

import java.util.stream.Collectors;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.class_management.ClassRepository;
import oodj_project.features.enrollment_management.Enrollment;
import oodj_project.features.enrollment_management.EnrollmentRepository;

public class EnrollmentReportController {
    
    private final Session session;
    private final EnrollmentRepository enrollmentRepository;
    private final ClassRepository classRepository;
    
    public EnrollmentReportController(
        Session session,
        EnrollmentRepository enrollmentRepository,
        ClassRepository classRepository
    ) {
        this.session = session;
        this.enrollmentRepository = enrollmentRepository;
        this.classRepository = classRepository;
    }

    public PaginatedResult<EnrollmentReport> index(Query<EnrollmentReport> query) {

        var enrollmentList = enrollmentRepository.all()
            .stream()
            .collect(Collectors.groupingBy(Enrollment::classGroup));

        var classList = classRepository.all()
            .stream();
            
        if (!session.can(Permission.READ_CLASSES))
            classList = classList.filter(classGroup -> classGroup.lecturer() != null);

        var reportList = classList
            .map(classGroup -> new EnrollmentReport(classGroup, enrollmentList.get(classGroup)))
            .toList();
    
        return query != null?
            query.apply(reportList):
            PaginatedResult.singlePage(reportList);
    }
}
