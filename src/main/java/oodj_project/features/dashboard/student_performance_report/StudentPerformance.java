package oodj_project.features.dashboard.student_performance_report;

import java.util.List;
import java.util.stream.Collectors;

import oodj_project.features.dashboard.assessment_grading.AssessmentResult;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.grading_system_management.Grade;

public record StudentPerformance(
    Enrollment enrollment,
    List<AssessmentResult> results,
    Grade grade
) {
    public int marks() {
        return results.stream()   
            .collect(Collectors.summingInt(result -> result.marks()));
    }

    public int maxMarks() {
        return results.stream()
            .collect(Collectors.summingInt(result -> result.assessment().marks()));
    }

    public double percentage() {
        return (double) marks() / maxMarks();
    }

    public StudentPerformance withGrade(Grade grade) {
        return new StudentPerformance(enrollment, results, grade);
    }
}