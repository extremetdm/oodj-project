package oodj_project.features.dashboard.team_management;

import oodj_project.features.dashboard.user_management.User;

public record MemberAssignment(User member, User supervisor) {
    public MemberAssignment {

    }

    public boolean isUnassigned() {
        return supervisor == null;
    }

    public TeamMember toModel() {
        if (isUnassigned()) return null;
        return new TeamMember(supervisor, member);
    }
}
