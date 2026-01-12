package oodj_project.features.permission_management;

import oodj_project.core.data.model.Model;
import oodj_project.core.security.Permission;
import oodj_project.features.role_management.Role;

public record RolePermission(Role role, Permission permission) {
    public RolePermission {
        Model.require(role, "Role");
        Model.require(permission, "Permission");
    }
}