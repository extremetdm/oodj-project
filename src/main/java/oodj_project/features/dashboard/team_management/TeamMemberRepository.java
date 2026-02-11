package oodj_project.features.dashboard.team_management;

import java.io.File;
import java.io.IOException;

import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.data.repository.LineParser;
import oodj_project.core.data.repository.Repository;
import oodj_project.core.data.validation.Rule;
import oodj_project.features.dashboard.user_management.UserRepository;

public class TeamMemberRepository extends Repository<TeamMember> {
    public TeamMemberRepository(File file, UserRepository userRepository) throws IOException {
        super(
            file,
            getParser(userRepository),
            TeamMemberRepository::format,
            Rule.compose(
                Rule.unique(model -> model, "Duplicate team relation."),
                Rule.in(
                    TeamMember::supervisor,
                    userRepository::all,
                    model -> new IllegalStateException("Invalid supervisor User ID:" + model.supervisor().id())
                ),
                Rule.in(
                    TeamMember::member,
                    userRepository::all,
                    model -> new IllegalStateException("Invalid member User ID:" + model.member().id())
                ),
                Rule.unique(TeamMember::member, "Member can only have 1 supervisor.")
            )
        );
    }

    private static LineParser<TeamMember> getParser(UserRepository userRepository) {
        return args -> {
            LineParser.checkArgCount(args, 2);

            int i = 0;
            return new TeamMember(
                LineParser.parseField(args[i++], "Supervisor", userRepository),
                LineParser.parseField(args[i++], "Team member", userRepository)
            );
        };
    }

    private static String[] format(TeamMember teamMember) {
        return new String[] {
            LineFormatter.formatField(teamMember.supervisor()),
            LineFormatter.formatField(teamMember.member()),
        };
    }
}
