package oodj_project.features.permission_management;

import oodj_project.core.auth.Permission;
import oodj_project.core.model.Identifiable;
import oodj_project.features.role_management.Role;

public record RolePermission(Integer id, Role role, Permission permission) implements Identifiable<RolePermission> {
    public RolePermission(Role role, Permission permission) {
        this(null, role, permission);
    }

    public RolePermission {
        if (role == null) throw new IllegalArgumentException("Role cannot be blank.");
        if (permission == null) throw new IllegalArgumentException("Role cannot be blank.");
    }

    @Override
    public RolePermission withId(int id) {
        return new RolePermission(id, role, permission);
    }
}