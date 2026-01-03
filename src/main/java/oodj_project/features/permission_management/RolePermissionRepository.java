package oodj_project.features.permission_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.Repository;
import oodj_project.core.data.validation.Rule;
import oodj_project.core.security.Permission;
import oodj_project.features.role_management.RoleRepository;

public class RolePermissionRepository extends Repository<RolePermission> {

    public RolePermissionRepository(File file, RoleRepository roleRepository) throws IOException {
        super(
            file,
            args -> new RolePermission(
                roleRepository.find(Integer.parseInt(args[0])).orElseThrow(),
                Permission.valueOf(args[1])
            ),
            RolePermissionRepository::format,
            Rule.compose(
                Rule.in(
                    RolePermission::role,
                    () -> roleRepository.get().data(),
                    model -> new IllegalStateException("Invalid Role ID: " + model.role().id())
                ),
                Rule.unique(
                    model -> model,
                    "Duplicate Role Permission"
                )
            )
        );
    }

    private static String[] format(RolePermission rolePermission) {
        return new String[] {
            rolePermission.role().id().toString(),
            rolePermission.permission().name()
        };
    }
}