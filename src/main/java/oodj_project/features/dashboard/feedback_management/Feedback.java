package oodj_project.features.dashboard.feedback_management;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;

public record Feedback(
    Integer id,
    Enrollment enrollment,
    int score,
    String comment
) implements Identifiable<Feedback> {
    public Feedback {
        Model.require(enrollment, "Enrollment");

        if (score > 10) throw new IllegalArgumentException("Score cannot exceed 10");
        if (score < 1) throw new IllegalArgumentException("Score cannot be under 1");

        comment = Model.normalize(comment);
    }

    public Feedback(
        Enrollment enrollment,
        int score,
        String comment
    ) {
        this(null, enrollment, score, comment);
    }

    @Override
    public Feedback withId(int id) {
        return new Feedback(null, enrollment, score, comment);
    }
}
