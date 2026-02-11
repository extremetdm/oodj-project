package oodj_project.features.dashboard.user_management;

import java.util.List;
import java.util.stream.Collectors;

import oodj_project.core.security.Permission;
import oodj_project.features.dashboard.permission_management.RolePermission;
import oodj_project.features.dashboard.permission_management.RolePermissionRepository;

public class UserPermissionService {
    private final UserRepository userRepository;
    private final RolePermissionRepository permissionRepository;

    public UserPermissionService(
        UserRepository userRepository,
        RolePermissionRepository permissionRepository
    ) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<User> findUserByPermission(Permission permission) {
        var roles = permissionRepository.all()
            .stream()
            .filter(rolePermission -> rolePermission.permission() == permission)
            .map(RolePermission::role)
            .collect(Collectors.toSet());
        
        return userRepository.all()
            .stream()
            .filter(user -> roles.contains(user.role()))
            .toList();
    }

    public List<User> getSupervisors() {
        return findUserByPermission(Permission.ASSIGN_TEACHER);
    }

    public List<User> getLecturers() {
        return findUserByPermission(Permission.TEACH_CLASSES);
    }
}
