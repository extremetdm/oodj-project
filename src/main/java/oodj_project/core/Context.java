package oodj_project.core;

import java.io.File;
import java.io.IOException;

import oodj_project.features.permission_management.RolePermissionRepository;
import oodj_project.features.role_management.RoleRepository;

public class Context {
    private final File dataDir = new File("data");

    private static final Context INSTANCE = new Context();

    public static Context instance() {
        return INSTANCE;
    }

    private Context() {}

    private RoleRepository roles;
    public RoleRepository roles() {
        return roles;
    }

    private RolePermissionRepository rolePermissions;
    public RolePermissionRepository rolePermissions() {
        return rolePermissions;
    }

    public void initialize() throws IOException {
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        
        roles = new RoleRepository(
            checkFile("roles.txt")
        );

        rolePermissions = new RolePermissionRepository(
            checkFile("role-permissions.txt"),
            roles
        );
    }

    private File checkFile(String filePath) throws IOException {
        File file = new File(dataDir, filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
