package oodj_project.features.dashboard.user_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.IdentifiableRepository;
import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.validation.Rule;
import oodj_project.features.dashboard.role_management.RoleRepository;

public class UserRepository extends IdentifiableRepository<User> {
    public UserRepository(File file, RoleRepository roleRepository) throws IOException {
        super(
            file,
            getParser(roleRepository),
            UserRepository::format,
            Rule.unique(
                User::id,
                new IllegalStateException("dupe")
            )
        );
    }

    private static LineParser<User> getParser(RoleRepository roleRepository) {
        return args -> {
            LineParser.checkArgCount(args, 9);
            int i = 0;
            return new User(
                LineParser.parseInt(args[i++], "ID"),
                args[i++],
                args[i++],
                args[i++],
                LineParser.parseEnum(args[i++], "Gender", Gender.class),
                LineParser.parseField(args[i++], "Role", roleRepository),
                args[i++],
                args[i++],
                LineParser.parseDate(args[i++], "Date of birth")
            );
        };
    }

    private static String[] format(User user) {
        return new String[] {
            LineFormatter.formatField(user),
            user.username(),
            user.name(),
            user.password(),
            user.gender().name(),
            LineFormatter.formatField(user.role()),
            user.email(),
            user.phoneNumber(),
            LineFormatter.formatDate(user.dateOfBirth())
        };
    }
}
