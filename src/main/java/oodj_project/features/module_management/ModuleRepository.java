package oodj_project.features.module_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
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
        return new Module(
            Integer.valueOf(args[0])
        );
    }

    private static String format(Module module) {
        return module.id() + "";
    }
}
