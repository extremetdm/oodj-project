package oodj_project.features.permission_management;

import oodj_project.core.security.Permission;
import oodj_project.features.role_management.Role;

public record RolePermission(Role role, Permission permission) {
    public RolePermission {
        if (role == null) throw new IllegalArgumentException("Role cannot be blank.");
        if (permission == null) throw new IllegalArgumentException("Role cannot be blank.");
    }
}