package oodj_project.features.dashboard.assessment_grading;

import oodj_project.features.dashboard.assessment_management.Assessment;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.user_management.User;

public record GradeBook(
    Assessment assessment,
    Enrollment enrollment,
    AssessmentResult result
) {
    public record Key(
        Assessment assessment,
        Enrollment enrollment
    ) {
        public Key(AssessmentResult result) {
            this(result.assessment(), result.enrollment());
        }
    }

    public GradeBook(Key key, AssessmentResult result) {
        this(key.assessment, key.enrollment, result);
    }
}
