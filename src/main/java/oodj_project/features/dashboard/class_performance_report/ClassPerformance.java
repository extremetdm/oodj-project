package oodj_project.features.dashboard.class_performance_report;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.grading_system_management.Grade;
import oodj_project.features.dashboard.student_performance_report.StudentPerformance;

public record ClassPerformance(
    ClassGroup classGroup,
    List<StudentPerformance> studentPerformances,
    Grade minGrade,
    Grade avgGrade,
    Grade maxGrade
) {
    public ClassPerformance {}

    public ClassPerformance(
        ClassGroup classGroup,
        List<StudentPerformance> studentPerformances
    ) {
        this(classGroup, studentPerformances, null, null, null);
    }

    public IntSummaryStatistics summary() {
        return studentPerformances.stream()
            .collect(Collectors.summarizingInt(StudentPerformance::marks));
    }

    public int failCount() {
        return (int) studentPerformances.stream()
            .filter(performance -> {
                var grade = performance.grade();
                if (grade == null) return false; // idk
                return grade.classification() == Grade.Classification.Fail;
            })
            .count();
    }

    public int passCount() {
        return (int) studentPerformances.stream()
            .filter(performance -> {
                var grade = performance.grade();
                if (grade == null) return true; // idk
                return grade.classification() != Grade.Classification.Fail;
            })
            .count();
    }

    public double passRate() {
        return (double) passCount() / totalStudents();
    }

    public int totalStudents() {
        return studentPerformances.size();
    }

    public int maxMarks() {
        return studentPerformances.get(0).maxMarks();
    }

    public ClassPerformance withGrades(Grade minGrade, Grade avgGrade, Grade maxGrade) {
        return new ClassPerformance(classGroup, studentPerformances, minGrade, avgGrade, maxGrade);
    }

    public Map<Grade.Classification, Long> classifications() {
        return studentPerformances.stream()
            .collect(Collectors.groupingBy(
                performance -> Optional.ofNullable(performance.grade())
                    .map(Grade::classification)
                    .get(),
                Collectors.counting()
            ));
    }

    public Map<Grade, Long> grades() {
        return studentPerformances.stream()
            .collect(Collectors.groupingBy(
                performance -> performance.grade(),
                Collectors.counting()
            ));
    }

    public double minMarkPercentage() {
        return ((double) summary().getMin()) / maxMarks();
    }

    public double avgMarkPercentage() {
        return summary().getAverage() / maxMarks();
    }

    public double maxMarkPercentage() {
        return ((double) summary().getMax()) / maxMarks();
    }
}
