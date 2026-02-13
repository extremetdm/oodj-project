package oodj_project.features.dashboard.grading_system_management;

import java.util.Comparator;

public class GradingService {

    private final GradeRepository repository;
    
    public GradingService(GradeRepository repository) {
        this.repository = repository;
    }

    public Grade calculate(Number marks, Number maxMarks) {
        return calculate(100d * marks.doubleValue() / maxMarks.doubleValue());
    }

    public Grade calculate(double percentage) {
        var grades = repository.all()
            .stream()
            .sorted(
                Comparator.comparing(Grade::max).reversed().thenComparing(
                    Comparator.comparing(Grade::min).reversed()
                )
            )
            .toList();

        for (var grade : grades) {
            if (percentage <= grade.max() && percentage >= grade.min()) {
                return grade;
            }
        }
        
        return null;
    }
}
