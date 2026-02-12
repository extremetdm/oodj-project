package oodj_project.features.dashboard.assessment_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.validation.Rule;
import oodj_project.features.dashboard.class_management.ClassRepository;

public class AssessmentRepository extends IdentifiableRepository<Assessment> {
    public AssessmentRepository(
        File sourceFile,
        ClassRepository classRepository
    ) throws IOException {
        super(
            sourceFile,
            getParser(classRepository),
            AssessmentRepository::format,
            Rule.compose(
                Rule.unique(Assessment::id, model -> new IllegalStateException("Duplicate Class ID: " + model.id())),
                Rule.in(
                    Assessment::classGroup,
                    classRepository::all,
                    model -> new IllegalStateException("Invalid Class ID: " + model.classGroup().id())
                )
            )
        );
    }

    private static LineParser<Assessment> getParser(ClassRepository classRepository) {
        return args -> {
            LineParser.checkArgCount(args, 5);

            int i = 0;
            return new Assessment(
                LineParser.parseInt(args[i++], "ID"),
                args[i++],
                LineParser.parseField(args[i++], "Class", classRepository),
                LineParser.parseEnum(args[i++], "Type", Assessment.Type.class),
                LineParser.parseInt(args[i++], "Marks")
            );
        };
    }

    private static String[] format(Assessment assessment) {
        return new String[] {
            LineFormatter.formatField(assessment),
            assessment.name(),
            LineFormatter.formatField(assessment.classGroup()),
            assessment.type().name(),
            String.valueOf(assessment.marks())
        };
    }
}
