package oodj_project.features.dashboard.grading_system_management;

import java.util.Comparator;

public class GradingService {

    private final GradeRepository repository;
    
    public GradingService(GradeRepository repository) {
        this.repository = repository;
    }

    public Grade calculate(int marks, int maxMarks) {
        var grades = repository.all()
            .stream()
            .sorted(
                Comparator.comparing(Grade::max).reversed().thenComparing(
                    Comparator.comparing(Grade::min).reversed()
                )
            )
            .toList();

        var percentage = 100d * marks / maxMarks;
        for (var grade : grades) {
            if (percentage <= grade.max() && percentage >= grade.min()) {
                return grade;
            }
        }
        
        return null;
    }
}
