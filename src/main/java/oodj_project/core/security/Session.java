package oodj_project.core.security;

import oodj_project.core.data.Context;
import oodj_project.features.permission_management.RolePermissionRepository;
import oodj_project.features.user_management.User;
import oodj_project.features.user_management.UserRepository;

public class Session {
    private final Context context;
    private User currentUser;

    public Session(Context context) {
        this.context = context;
    }

    public boolean login(String username, String password) {
        this.currentUser = context.get(UserRepository.class)
            .findFirst(
                user -> user.name().equals(username)
                    && user.password().equals(password)
            ).orElse(null);

        return isLoggedIn();
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean can(Permission permission) {
        if (!isLoggedIn()) return false;
        return context.get(RolePermissionRepository.class)
            .findFirst(
                model -> model.role() == currentUser.role()
                    && model.permission() == permission
            ).isPresent();
    }
}
