package oodj_project.features.role_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.IdentifiableRepository;
import oodj_project.core.validation.Rule;

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

    private static String format(Role role) {
        return role.id() + "|" + role.name();
    }

    public static void main(String[] args) throws IOException {
        File file = new File("src\\main\\resources\\roles.txt");
        file.createNewFile();
        var a = new RoleRepository(file);
        a.create(new Role("Admin Staff"));
    }
}