package oodj_project.features.dashboard.enrollment_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.validation.Rule;
import oodj_project.features.dashboard.class_management.ClassRepository;
import oodj_project.features.dashboard.user_management.UserRepository;

public class EnrollmentRepository extends IdentifiableRepository<Enrollment> {
    public EnrollmentRepository(
        File sourceFile,
        UserRepository userRepository,
        ClassRepository classRepository
    ) throws IOException {
        super(
            sourceFile,
            getParser(userRepository, classRepository),
            EnrollmentRepository::format,
            Rule.compose(
                Rule.unique(
                    Enrollment::id,
                    model -> new IllegalStateException("Duplicate Enrollment ID: " + model.id())
                ),
                Rule.in(
                    Enrollment::student,
                    userRepository::all,
                    model -> new IllegalStateException("Invalid student User ID: " + model.student().id())
                ),
                Rule.in(
                    Enrollment::classGroup,
                    classRepository::all,
                    model -> new IllegalStateException("Invalid Class ID: " + model.classGroup().id())
                )
            )
        );
    }

    private static LineParser<Enrollment> getParser(UserRepository userRepository, ClassRepository classRepository) {
        return args -> {
            LineParser.checkArgCount(args, 5);
            int i = 0;
            
            return new Enrollment(
                LineParser.parseInt(args[i++], "ID"),
                LineParser.parseField(args[i++], "Student", userRepository),
                LineParser.parseField(args[i++], "Class", classRepository),
                LineParser.parseDate(args[i++], "Register date"),
                LineParser.parseDate(args[i++], "Dropout date")
            );
        };   
    }

    private static String[] format(Enrollment enrollment) {
        return new String[] {
            enrollment.id().toString(),
            LineFormatter.formatField(enrollment.student()),
            LineFormatter.formatField(enrollment.classGroup()),
            LineFormatter.formatDate(enrollment.registerDate()),
            LineFormatter.formatDate(enrollment.dropoutDate())
        };
    }
}
