package oodj_project.features.dashboard.permission_management;

import oodj_project.core.data.model.Model;
import oodj_project.core.security.Permission;
import oodj_project.features.dashboard.role_management.Role;

public record RolePermission(Role role, Permission permission) {
    public RolePermission {
        Model.require(role, "Role");
        Model.require(permission, "Permission");
    }
}