package oodj_project.features.module_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.validation.Rule;

public class ModuleRepository extends IdentifiableRepository<Module> {
    public ModuleRepository(File file) throws IOException {
        super(
            file,
            ModuleRepository::parse,
            ModuleRepository::format,
            Rule.unique(
                Module::id,
                model -> new IllegalStateException("Duplicate Module ID: " + model.id())
            )
        );
    }

    private static Module parse(String... args) {
        LineParser.checkArgCount(args, 3);
        int i = 0;
        return new Module(
            LineParser.parseInt(args[i++], "ID"),
            args[i++],
            args[i++]
        );
    }

    private static String[] format(Module module) {
        return new String[] {
            LineFormatter.formatField(module),
            module.name(), 
            module.description()
        };
    }
}
