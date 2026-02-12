package oodj_project.features.dashboard.lecturer_workload_report;

import java.util.stream.Collectors;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.class_enrollment_report.EnrollmentReport;
import oodj_project.features.dashboard.class_management.ClassRepository;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentRepository;
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

    public PaginatedResult<LecturerWorkload> index(Query<LecturerWorkload> query) {
        var enrollmentList = enrollmentRepository.all()
            .stream()
            .collect(Collectors.groupingBy(Enrollment::classGroup));

        var classList = classRepository.all()
            .stream()
            .map(classGroup -> new EnrollmentReport(classGroup, enrollmentList.get(classGroup)))
            .filter(report -> report.classGroup().lecturer() != null)
            .collect(Collectors.groupingBy(report -> report.classGroup().lecturer()));
        
        var workloadList = teamMemberRepository.all()            
            .stream()
            .filter(model -> model.supervisor() == session.currentUser())
            .map(team -> new LecturerWorkload(team.member(), classList.get(team.member())))
            .toList();
    
        return query != null?
            query.apply(workloadList):
            PaginatedResult.singlePage(workloadList);        
    }
}
