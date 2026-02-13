package oodj_project.features.dashboard.student_performance_report;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import oodj_project.features.dashboard.assessment_grading.AssessmentResult;
import oodj_project.features.dashboard.assessment_grading.AssessmentResultRepository;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.grading_system_management.GradingService;

public class StudentPerformanceService {

    private final AssessmentResultRepository resultRepository;
    private final GradingService gradingService;

    public StudentPerformanceService(
        AssessmentResultRepository resultRepository,
        GradingService gradingService
    ) {
        this.resultRepository = resultRepository;
        this.gradingService = gradingService;
    }

    public List<StudentPerformance> getForEnrollments(
        List<Enrollment> enrollments
    ) {
        var enrollmentsSet = new HashSet<>(enrollments);
        
        return resultRepository.all()
            .stream()
            .filter(result -> enrollmentsSet.contains(result.enrollment()))
            .collect(Collectors.groupingBy(AssessmentResult::enrollment))
            .entrySet()
            .stream()
            .map(map -> {
                var performance = new StudentPerformance(map.getKey(), map.getValue(), null);
                var grade = gradingService.calculate(performance.marks(), performance.maxMarks());
                return performance.withGrade(grade);
            })
            .toList();
    }
}
