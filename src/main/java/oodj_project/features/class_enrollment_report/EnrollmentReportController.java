package oodj_project.features.class_enrollment_report;

import java.util.stream.Collectors;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.features.class_management.ClassRepository;
import oodj_project.features.enrollment_management.Enrollment;
import oodj_project.features.enrollment_management.EnrollmentRepository;

public class EnrollmentReportController {
    
    private final EnrollmentRepository enrollmentRepository;
    private final ClassRepository classRepository;
    
    public EnrollmentReportController(
        EnrollmentRepository enrollmentRepository,
        ClassRepository classRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.classRepository = classRepository;
    }

    public PaginatedResult<EnrollmentReport> index(Query<EnrollmentReport> query) {

        var enrollmentList = enrollmentRepository.all()
            .stream()
            .collect(Collectors.groupingBy(Enrollment::classGroup));
        
        var reportList = classRepository.all()
            .stream()
            .filter(classGroup -> classGroup.lecturer() != null)
            .map(classGroup -> new EnrollmentReport(classGroup, enrollmentList.get(classGroup)))
            .toList();
        
        if (query != null) {
            return query.apply(reportList);
        }
        
        int totalItems = reportList.size();
        return new PaginatedResult<>(reportList, 1, totalItems, totalItems);
    }
}
