package oodj_project.features.dashboard.role_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.validation.Rule;

public class RoleRepository extends IdentifiableRepository<Role> {
    public RoleRepository(File file) throws IOException {
        super(
            file, 
            RoleRepository::parse,
            RoleRepository::format,
            Rule.unique(
                Role::id,
                model -> new IllegalStateException("Duplicate Role ID: " + model.id())
            )
        );
    }

    private static Role parse(String... args) {
        LineParser.checkArgCount(args, 2);
        int i = 0;
        return new Role(
            LineParser.parseInt(args[i++], "ID"),
            args[i++]
        );
    }

    private static String[] format(Role role) {
        return new String[] {
            LineFormatter.formatField(role),
            role.name()
        };
    }
}