package oodj_project.features.dashboard.feedback_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.validation.Rule;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentRepository;

public class FeedbackRepository extends IdentifiableRepository<Feedback> {
    public FeedbackRepository(
        File file,
        EnrollmentRepository enrollmentRepository
    ) throws IOException {
        super(
            file,
            getParser(enrollmentRepository),
            FeedbackRepository::format,
            Rule.unique(
                Feedback::id,
                model -> new IllegalStateException("Duplicate Assessment Result ID: " + model.id())
            )
        );
    }

    private static LineParser<Feedback> getParser(
        EnrollmentRepository enrollmentRepository
    ) {
        return args -> {
            LineParser.checkArgCount(args, 4);
            int i = 0;
            return new Feedback(
                LineParser.parseInt(args[i++], "ID"),
                LineParser.parseField(args[i++], "Enrollment", enrollmentRepository),
                LineParser.parseInt(args[i++], "Score"),
                args[i++]
            );
        };
    }

    private static String[] format(Feedback result) {
        return new String[] {
            LineFormatter.formatField(result),
            LineFormatter.formatField(result.enrollment()),
            String.valueOf(result.score()), 
            result.comment()
        };
    }
}
