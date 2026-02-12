package oodj_project.features.dashboard.assessment_grading;

import oodj_project.features.dashboard.assessment_management.Assessment;
import oodj_project.features.dashboard.user_management.User;

public record GradeBook(
    Assessment assessment,
    User student,
    AssessmentResult result
) {
    public record Key(
        Assessment assessment,
        User student
    ) {
        public Key(AssessmentResult result) {
            this(result.assessment(), result.student());
        }
    }

    public GradeBook(Key key, AssessmentResult result) {
        this(key.assessment, key.student, result);
    }
}
