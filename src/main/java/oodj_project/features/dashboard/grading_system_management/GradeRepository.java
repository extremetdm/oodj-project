package oodj_project.features.dashboard.grading_system_management;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.validation.Rule;

public class GradeRepository extends IdentifiableRepository<Grade> {
    public GradeRepository(File file) throws IOException {
        super(
            file,
            GradeRepository::parse,
            GradeRepository::format,
            Rule.unique(
                Grade::id,
                new IllegalStateException("dupe")
            )
        );
    }

    public static Grade parse(String... args) {
        LineParser.checkArgCount(args, 5);
        int i = 0;
        return new Grade(
            LineParser.parseInt(args[i++], "ID"),
            args[i++],
            LineParser.parseInt(args[i++], "Minimum marks"),
            LineParser.parseInt(args[i++], "Maximum marks"),
            LineParser.parseEnum(args[i++], "Classification", Grade.Classification.class)
        );
    }

    public static String[] format(Grade grade) {
        return new String[] {
            LineFormatter.formatField(grade),
            grade.name(),
            String.valueOf(grade.min()),
            String.valueOf(grade.max()),
            grade.classification().name()
        };
    }
}
