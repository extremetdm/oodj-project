package oodj_project.features.dashboard.student_feedback_report;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentRepository;
import oodj_project.features.dashboard.feedback_management.FeedbackRepository;

public class FeedbackReportController {
    private final Session session;
    private final FeedbackRepository feedbackRepository;
    private final EnrollmentRepository enrollmentRepository;

    public FeedbackReportController(
        Session session,
        FeedbackRepository feedbackRepository,
        EnrollmentRepository enrollmentRepository
    ) {
        this.session = session;
        this.feedbackRepository = feedbackRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    private List<Enrollment> getEnrollments() {
        var enrollments = enrollmentRepository.all();

        if (!session.can(Permission.READ_ALL_FEEDBACK_REPORT)) {
            enrollments = enrollments.stream()
                .filter(enrollment -> enrollment.classGroup().lecturer().equals(session.currentUser()))
                .toList();
        }

        return enrollments;
    }

    public List<FeedbackReport> index() {
        var enrollments = new HashSet<>(getEnrollments());

        var totalStudents = enrollments.stream()
            .collect(Collectors.groupingBy(Enrollment::classGroup, Collectors.counting()));

        return feedbackRepository.all()
            .stream()
            .filter(feedback -> enrollments.contains(feedback.enrollment()))
            .collect(Collectors.groupingBy(feedback -> feedback.enrollment().classGroup()))
            .entrySet()
            .stream()
            .map(map -> new FeedbackReport(map.getKey(), map.getValue(), totalStudents.get(map.getKey()).intValue()))
            .toList();
    }

    public PaginatedResult<FeedbackReport> index(Query<FeedbackReport> query) {
        var feedbacks = index();
        
        return query != null?
            query.apply(feedbacks):
            PaginatedResult.singlePage(feedbacks);
    }
}
