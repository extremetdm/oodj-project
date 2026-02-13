package oodj_project.features.dashboard.student_feedback;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentRepository;
import oodj_project.features.dashboard.feedback_management.Feedback;
import oodj_project.features.dashboard.feedback_management.FeedbackRepository;

public class StudentFeedbackController {
    private final Session session;
    private final FeedbackRepository feedbackRepository;
    private final EnrollmentRepository enrollmentRepository;

    public StudentFeedbackController(
        Session session,
        FeedbackRepository feedbackRepository,
        EnrollmentRepository enrollmentRepository
    ) {
        this.session = session;
        this.feedbackRepository = feedbackRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<StudentFeedback> index() {
        var feedbacks = feedbackRepository.all()
            .stream()
            .filter(feedback -> feedback.enrollment().student().equals(session.currentUser()))
            .collect(Collectors.toMap(Feedback::enrollment, feedback -> feedback));

        return enrollmentRepository.all()
            .stream()
            .filter(enrollment -> enrollment.student().equals(session.currentUser()))
            .map(enrollment -> new StudentFeedback(enrollment, feedbacks.getOrDefault(enrollment, null)))
            .toList();
    }

    public PaginatedResult<StudentFeedback> index(Query<StudentFeedback> query) {
        var feedbacks = index();
        
        return query != null?
            query.apply(feedbacks):
            PaginatedResult.singlePage(feedbacks);
    }

    public synchronized void create(Feedback feedback) throws IOException {
        validate(feedback);
        feedbackRepository.create(feedback);
    }

    public synchronized void update(int id, Feedback feedback) throws IOException {
        validate(feedback);
        feedbackRepository.update(id, feedback);
    }

    private void validate(Feedback feedback) throws IllegalArgumentException {
        validateString(feedback.comment(), "Comment");
    }

    private void validateString(String string, String fieldName) throws IllegalArgumentException {
        var bannedCharacter = FeedbackRepository.DELIMITER;
        if (string.contains(bannedCharacter)) {
            throw new IllegalArgumentException(fieldName + " cannot contain \"" + bannedCharacter + "\".");
        }
    }
}
