package oodj_project.features.class_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.validation.Rule;
import oodj_project.features.module_management.ModuleRepository;
import oodj_project.features.user_management.UserRepository;

public class ClassRepository extends IdentifiableRepository<ClassGroup> {
    public ClassRepository(
        File sourceFile,
        ModuleRepository moduleRepository,
        UserRepository userRepository
    ) throws IOException {
        super(
            sourceFile,
            getParser(moduleRepository, userRepository),
            ClassRepository::format,
            Rule.compose(
                Rule.unique(ClassGroup::id, model -> new IllegalStateException("Duplicate Class ID: " + model.id())),
                Rule.in(
                    ClassGroup::module,
                    moduleRepository::all,
                    model -> new IllegalStateException("Invalid Module ID: " + model.module().id())
                ),
                Rule.in(
                    ClassGroup::lecturer,
                    userRepository::all,
                    model -> new IllegalStateException("Invalid Lecturer ID: " + model.id())
                )
            )
        );
    }

    private static LineParser<ClassGroup> getParser(ModuleRepository moduleRepository, UserRepository userRepository) {
        return args -> {
            LineParser.checkArgCount(args, 4);

            int i = 0;
            return new ClassGroup(
                LineParser.parseInt(args[i++], "Class ID"),
                LineParser.parseField(args[i++], "Module", moduleRepository),
                LineParser.parseInt(args[i++], "Max capacity"),
                LineParser.parseField(args[i++], "Lecturer", userRepository)
            );
        };
    }

    private static String[] format(ClassGroup classGroup) {
        return new String[] {
            LineFormatter.formatField(classGroup),
            LineFormatter.formatField(classGroup.module()),
            String.valueOf(classGroup.maxCapacity()),
            LineFormatter.formatField(classGroup.lecturer()),
        };
    }
}
