package oodj_project.features.dashboard.team_management;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.user_management.User;
import oodj_project.features.dashboard.user_management.UserPermissionService;

public class TeamMemberController {

    private final Session session;
    private final TeamMemberRepository repository;
    private final UserPermissionService permissionService;

    public TeamMemberController(
        Session session,
        TeamMemberRepository repository,
        UserPermissionService permissionService
    ) {
        this.session = session;
        this.repository = repository;
        this.permissionService = permissionService;
    }

    public List<User> getLecturersUnderMe() {
        return repository.all()
            .stream()
            .filter(teamMember -> teamMember.supervisor() == session.currentUser())
            .map(TeamMember::member)
            .toList();
    }

    public List<MemberAssignment> index() {
        var relationMap = repository.all()
            .stream()
            .collect(Collectors.toMap(TeamMember::member, TeamMember::supervisor));

        return permissionService.getLecturers()
            .stream()
            .map(lecturer -> new MemberAssignment(lecturer, relationMap.get(lecturer)))
            .toList();  
    }

    public List<User> getUnassigned() {
        return index()
            .stream()
            .filter(relation -> relation.supervisor() == null)
            .map(MemberAssignment::supervisor)
            .toList();
    }

    public PaginatedResult<MemberAssignment> index(Query<MemberAssignment> query) {
        var lecturers = index();
        return query != null?
            query.apply(lecturers):
            PaginatedResult.singlePage(lecturers);
    }

    public synchronized void create(TeamMember teamMember) throws IOException {
        repository.create(teamMember);
    }

    public synchronized void update(TeamMember oldTeamMember, TeamMember teamMember) throws IOException {
        repository.update(oldTeamMember, teamMember);
    }

    public synchronized void delete(TeamMember teamMember) throws IOException {
        repository.delete(teamMember);
    }
}