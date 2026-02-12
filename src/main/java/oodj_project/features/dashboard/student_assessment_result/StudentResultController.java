package oodj_project.features.dashboard.student_assessment_result;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.assessment_grading.GradeBookService;
import oodj_project.features.dashboard.grading_system_management.GradingService;

public class StudentResultController {

    private final Session session;
    private final GradeBookService gradeBookService;
    private final GradingService gradingService;

    public StudentResultController(
        Session session,
        GradeBookService gradeBookService,
        GradingService gradingService
    ) {
        this.session = session;
        this.gradeBookService = gradeBookService;
        this.gradingService = gradingService;
    }

    public PaginatedResult<StudentResult> index(Query<StudentResult> query) {
        var results = gradeBookService.getForStudent(session.currentUser())
            .stream()
            .map(gradeBook -> {
                var result = gradeBook.result();
                var grade = result == null? null:
                    gradingService.calculate(
                        result.marks(), 
                        gradeBook.assessment().marks()
                    );
                return new StudentResult(gradeBook, grade);
            })
            .toList();
        
        return query != null?
            query.apply(results):
            PaginatedResult.singlePage(results);
    }
}
