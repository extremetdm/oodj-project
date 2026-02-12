package oodj_project.features.dashboard.assessment_grading;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.assessment_management.Assessment;
import oodj_project.features.dashboard.user_management.User;

public record AssessmentResult(
    Integer id,
    Assessment assessment,
    User student,
    int marks,
    String feedback
) implements Identifiable<AssessmentResult> {
    public AssessmentResult {
        Model.require(assessment, "Assessment");
        Model.require(student, "Student");

        var maxMarks = assessment.marks();
        if (marks > maxMarks)
            throw new IllegalArgumentException("Given marks cannot exceed " + maxMarks);

        feedback = Model.normalize(feedback);
    }

    public AssessmentResult(
        Assessment assessment,
        User student,
        int marks,
        String feedback
    ) {
        this(null, assessment, student, marks, feedback);
    }

    @Override
    public AssessmentResult withId(int id) {
        return new AssessmentResult(id, assessment, student, marks, feedback);
    }

    public String displayId() {
        return String.format("R%03d", id);
    }
}
