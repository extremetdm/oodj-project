package oodj_project.features.dashboard.team_management;

import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.user_management.User;

public record TeamMember(User supervisor, User member) {
    public TeamMember {
        Model.require(supervisor, "Supervisor");
        Model.require(member, "Team member");
    }
}
