package oodj_project.features.role_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
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
        return new Role(
            Integer.valueOf(args[0]),
            args[1]
        );
    }

    private static String[] format(Role role) {
        return new String[] {
            role.id().toString(),
            role.name()
        };
    }
}