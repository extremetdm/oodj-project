package oodj_project.features.dashboard.assessment_grading;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import oodj_project.features.dashboard.assessment_management.Assessment;
import oodj_project.features.dashboard.assessment_management.AssessmentRepository;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.enrollment_management.Enrollment;
import oodj_project.features.dashboard.enrollment_management.EnrollmentRepository;

public class GradeBookService {
    
    private final AssessmentResultRepository resultRepository;
    private final AssessmentRepository assessmentRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    public GradeBookService(
        AssessmentResultRepository resultRepository,
        AssessmentRepository assessmentRepository,
        EnrollmentRepository enrollmentRepository
    ) {
        this.resultRepository = resultRepository;
        this.assessmentRepository = assessmentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<GradeBook> getForClasses(List<ClassGroup> classes) {
        var classSet = new HashSet<>(classes);

        var classStudents = enrollmentRepository.all()
            .stream()
            .filter(enrollment -> 
                classSet.contains(enrollment.classGroup())
                   && enrollment.dropoutDate() == null
            )
            .collect(Collectors.groupingBy(
                Enrollment::classGroup,
                Collectors.mapping(Enrollment::student, Collectors.toList())
            ));

        var classAssessments = assessmentRepository.all()
            .stream()
            .filter(assessment -> classSet.contains(assessment.classGroup()))
            .collect(Collectors.groupingBy(Assessment::classGroup));

        var assessmentResults = resultRepository.all()
            .stream()
            .filter(result -> classSet.contains(result.assessment().classGroup()))
            .collect(Collectors.toMap(GradeBook.Key::new, result -> result));

        return classes.stream()
            .flatMap(classGroup -> {
                var students = classStudents.getOrDefault(classGroup, List.of());
                var assessments = classAssessments.getOrDefault(classGroup, List.of());
                
                return assessments.stream().flatMap(assessment ->
                    students.stream().map(student -> {
                        var key = new GradeBook.Key(assessment, student);
                        return new GradeBook(key, assessmentResults.getOrDefault(key, null));
                    })
                );
            })
            .toList();
    }
}
