package oodj_project.features.dashboard.student_feedback;

import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.feedback_management.Feedback;

public record StudentFeedback(
    Enrollment enrollment,
    Feedback feedback
) {}
