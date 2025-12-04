package oodj_project.core.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import oodj_project.core.data.repository.Repository;
import oodj_project.features.module_management.ModuleRepository;
import oodj_project.features.permission_management.RolePermissionRepository;
import oodj_project.features.role_management.RoleRepository;
import oodj_project.features.user_management.UserRepository;

public class Context {
    private final File dataDir = new File("data");
    private static final Context INSTANCE = new Context();
    private final Map<Class<?>, Repository<?>> repositories = new HashMap<>();

    public static Context instance() {
        return INSTANCE;
    }

    private Context() {}

    public <RepositoryT> RepositoryT get(Class<RepositoryT> repositoryClass) {
        var instance = repositories.get(repositoryClass);
        if (instance == null) throw new NoSuchElementException("Repository not registered.");
        return repositoryClass.cast(instance);
    }

    private void register(Repository<?> repository) {
        repositories.put(repository.getClass(), repository);        
    }

    public void initialize() throws IOException {
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
        
        var roles = new RoleRepository(
            checkFile("roles.txt")
        );
        register(roles);

        var rolePermissions = new RolePermissionRepository(
            checkFile("role-permissions.txt"),
            roles
        );
        register(rolePermissions);

        var users = new UserRepository(
            checkFile("users.txt"),
            roles
        );
        register(users);

        var modules = new ModuleRepository(
            checkFile("modules.txt")
        );
        register(modules);
    }

    private File checkFile(String filePath) throws IOException {
        File file = new File(dataDir, filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
