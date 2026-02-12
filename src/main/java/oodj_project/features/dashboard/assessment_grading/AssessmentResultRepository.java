package oodj_project.features.dashboard.assessment_grading;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.validation.Rule;
import oodj_project.features.dashboard.assessment_management.AssessmentRepository;
import oodj_project.features.dashboard.user_management.UserRepository;

public class AssessmentResultRepository extends IdentifiableRepository<AssessmentResult> {
    public AssessmentResultRepository(
        File file,
        AssessmentRepository assessmentRepository,
        UserRepository userRepository
    ) throws IOException {
        super(
            file,
            getParser(assessmentRepository, userRepository),
            AssessmentResultRepository::format,
            Rule.unique(
                AssessmentResult::id,
                model -> new IllegalStateException("Duplicate Assessment Result ID: " + model.id())
            )
        );
    }

    private static LineParser<AssessmentResult> getParser(
        AssessmentRepository assessmentRepository,
        UserRepository userRepository
    ) {
        return args -> {
            LineParser.checkArgCount(args, 5);
            int i = 0;
            return new AssessmentResult(
                LineParser.parseInt(args[i++], "ID"),
                LineParser.parseField(args[i++], "Assessment", assessmentRepository),
                LineParser.parseField(args[i++], "Student", userRepository),
                LineParser.parseInt(args[i++], "Marks"),
                args[i++]
            );
        };
    }

    private static String[] format(AssessmentResult result) {
        return new String[] {
            LineFormatter.formatField(result),
            LineFormatter.formatField(result.assessment()),
            LineFormatter.formatField(result.student()),
            String.valueOf(result.marks()), 
            result.feedback()
        };
    }
}
