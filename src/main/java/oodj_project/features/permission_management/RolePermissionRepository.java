package oodj_project.features.permission_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.repository.Repository;
import oodj_project.core.data.validation.Rule;
import oodj_project.core.security.Permission;
import oodj_project.features.role_management.Role;
import oodj_project.features.role_management.RoleRepository;

public class RolePermissionRepository extends Repository<RolePermission> {

    public RolePermissionRepository(File file, RoleRepository roleRepository) throws IOException {
        super(
            file,
            getParser(roleRepository),
            RolePermissionRepository::format,
            Rule.compose(
                Rule.in(
                    RolePermission::role,
                    roleRepository::all,
                    model -> new IllegalStateException("Invalid Role ID: " + model.role().id())
                ),
                Rule.unique(
                    model -> model,
                    "Duplicate Role Permission"
                )
            )
        );
    }

    private static LineParser<RolePermission> getParser(RoleRepository roleRepository) {
        return args -> {
            LineParser.checkArgCount(args, 2);
            int i = 0;
            return new RolePermission(
                LineParser.parseField(args[i++], "Role", roleRepository),
                LineParser.parseEnum(args[i++], "Permission", Permission.class)
            );
        };
    }

    private static String[] format(RolePermission rolePermission) {
        return new String[] {
            LineFormatter.formatField(rolePermission.role()),
            rolePermission.permission().name()
        };
    }

    public boolean roleHasPermission(Role role, Permission permission) {
        return findFirst(
            model -> model.role() == role
                && model.permission() == permission
        )   .isPresent();
    }
}