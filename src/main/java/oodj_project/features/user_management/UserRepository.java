package oodj_project.features.user_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.validation.Rule;
import oodj_project.features.role_management.RoleRepository;

public class UserRepository extends IdentifiableRepository<User> {
    public UserRepository(File file, RoleRepository roleRepository) throws IOException {
        super(
            file,
            args -> new User(
                Integer.valueOf(args[0]),
                args[1],
                args[2],
                roleRepository.find(Integer.parseInt(args[3])).orElseThrow()
            ),
            UserRepository::format,
            Rule.unique(
                User::id,
                new IllegalStateException("dupe")
            )
        );
    }

    public static String format(User user) {
        return user.id() + "|" + user.name() + "|" + user.password() + "|" + user.role().id();
    }
}
