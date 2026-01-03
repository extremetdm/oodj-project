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
        if (args.length != 3) throw new IllegalArgumentException("Incorrect number of fields given.");
        
        Integer id;

        try {
            id = Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID must be a number.");
        }

        return new Module(id, args[1], args[2]);
    }

    private static String[] format(Module module) {
        return new String[] {
            module.id().toString(),
            module.name(), 
            module.description()
        };
    }
}
