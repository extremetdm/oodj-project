package oodj_project.features.user_management;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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
                args[3],
                Gender.valueOf(args[4]),
                roleRepository.find(Integer.parseInt(args[5])).orElseThrow(),
                args[6],
                args[7],
                new Date(Long.parseLong(args[8]))
            ),
            UserRepository::format,
            Rule.unique(
                User::id,
                new IllegalStateException("dupe")
            )
        );
    }

    public static String[] format(User user) {
        return new String[] {
            user.id().toString(),
            user.username(),
            user.name(),
            user.password(),
            user.gender().name(),
            user.role().id().toString(),
            user.email(),
            user.phoneNumber(),
            Long.toString(user.dateOfBirth().getTime())
        };
    }
}
