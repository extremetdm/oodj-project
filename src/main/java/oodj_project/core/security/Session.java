package oodj_project.core.security;

import oodj_project.core.data.Context;
import oodj_project.features.dashboard.permission_management.RolePermissionRepository;
import oodj_project.features.dashboard.user_management.User;
import oodj_project.features.dashboard.user_management.UserRepository;

public class Session {
    private final Context context;
    private User currentUser;
    private String lastUsername;
    private String lastPassword;

    public Session(Context context) {
        this.context = context;
    }

    public boolean login(String username, String password) {
        this.currentUser = context.get(UserRepository.class)
                .findFirst(
                        user -> user.username().equals(username)
                                && user.password().equals(password))
                .orElse(null);

        if (isLoggedIn()) {
            this.lastUsername = username;
            this.lastPassword = password;
        }

        return isLoggedIn();
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public User currentUser() {
        return currentUser;
    }

    public boolean can(Permission permission) {
        if (!isLoggedIn())
            return false;
        if (permission == null)
            return true;
        return context.get(RolePermissionRepository.class)
                .roleHasPermission(currentUser.role(), permission);
    }

    public String lastUsername() {
        return lastUsername;
    }

    public String lastPassword() {
        return lastPassword;
    }
}
