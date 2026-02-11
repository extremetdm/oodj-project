package oodj_project.features.dashboard.permission_management;

import oodj_project.core.security.Permission;
import oodj_project.features.dashboard.role_management.Role;

public class RolePermissionController {
    private final RolePermissionRepository repository;

    public RolePermissionController(RolePermissionRepository repository) {
        this.repository = repository;
    }

    public boolean roleHasPermission(Role role, Permission permission) {
        return repository.roleHasPermission(role, permission);
    }
}
