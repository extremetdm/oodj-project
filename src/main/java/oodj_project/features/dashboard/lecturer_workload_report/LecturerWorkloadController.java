package oodj_project.features.dashboard.lecturer_workload_report;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.class_management.ClassRepository;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentRepository;
import oodj_project.features.dashboard.team_management.TeamMember;
import oodj_project.features.dashboard.team_management.TeamMemberRepository;

public class LecturerWorkloadController {

    private final Session session;
    private final TeamMemberRepository teamMemberRepository;
    private final ClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;

    public LecturerWorkloadController(
        Session session,
        TeamMemberRepository teamMemberRepository,
        ClassRepository classRepository,
        EnrollmentRepository enrollmentRepository
    ) {
        this.session = session;
        this.teamMemberRepository = teamMemberRepository;
        this.classRepository = classRepository;
        this.enrollmentRepository = enrollmentRepository;

    }

    private Stream<ClassGroup> getClasses() {
        var classes = classRepository.all()
            .stream();
            
        if (session.can(Permission.READ_ALL_LECTURER_WORKLOAD)) {
            return classes;       
        }

        var lecturers = teamMemberRepository.all()
            .stream()
            .filter(model -> model.supervisor().equals(session.currentUser()))
            .collect(Collectors.mapping(TeamMember::member, Collectors.toSet()));

        return classes.filter(classGroup -> lecturers.contains(classGroup.lecturer()));
    }

    public PaginatedResult<LecturerWorkload> index(Query<LecturerWorkload> query) {
        var classes = getClasses()
            .collect(Collectors.toSet());

        var enrollments = enrollmentRepository.all()
            .stream()
            .filter(enrollment -> 
                enrollment.status() != Enrollment.Status.DROPPED
                    && classes.contains(enrollment.classGroup())
            )
            .collect(Collectors.groupingBy(Enrollment::classGroup));
        
        var workloads = classes.stream()
            .collect(Collectors.groupingBy(
                ClassGroup::lecturer,
                Collectors.toMap(
                    Function.identity(),
                    classGroup -> enrollments.getOrDefault(classGroup, List.of())
                )
            ))
            .entrySet()
            .stream()
            .map(map -> new LecturerWorkload(map.getKey(), map.getValue()))
            .toList();
    
        return query != null?
            query.apply(workloads):
            PaginatedResult.singlePage(workloads);        
    }
}
