package oodj_project.features.dashboard.student_feedback_report;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.feedback_management.Feedback;

public record FeedbackReport(
    ClassGroup classGroup,
    List<Feedback> feedbacks,
    int totalStudents
) {
    public int feedbackCount() {
        return feedbacks.size();
    }

    public IntSummaryStatistics summary() {
        return feedbacks.stream()
            .collect(Collectors.summarizingInt(Feedback::score));
    }

    public int minScore() {
        return summary().getMin();
    }

    public double avgScore() {
        return summary().getAverage();
    }

    public int roundedAvgScore() {
        return (int) avgScore();
    }

    public int maxScore() {
        return summary().getMax();
    }

    public double feedbackRate() {
        return (double) feedbackCount() / totalStudents;
    }
}
