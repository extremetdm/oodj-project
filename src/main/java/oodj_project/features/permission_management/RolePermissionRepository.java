package oodj_project.features.permission_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.validation.Rule;
import oodj_project.core.security.Permission;
import oodj_project.features.role_management.RoleRepository;

public class RolePermissionRepository extends IdentifiableRepository<RolePermission> {

    public RolePermissionRepository(File file, RoleRepository roleRepository) throws IOException {
        super(
            file,
            args -> new RolePermission(
                Integer.valueOf(args[0]),
                roleRepository.find(Integer.parseInt(args[1])).orElseThrow(),
                Permission.valueOf(args[2])
            ),
            RolePermissionRepository::format,
            Rule.compose(
                Rule.unique(
                    RolePermission::id,
                    model -> new IllegalStateException("Duplicate Role Permission ID: " + model.id())
                ),
                Rule.in(
                    RolePermission::role,
                    roleRepository::get,
                    model -> new IllegalStateException("Invalid Role ID: " + model.role().id())
                ),
                Rule.unique(
                    model -> model.withId(0),
                    "Duplicate Role Permission"
                )
            )
        );
    }

    private static String format(RolePermission rolePermission) {
        return rolePermission.id() + "|" + rolePermission.role().id() + "|" + rolePermission.permission();
    }
}