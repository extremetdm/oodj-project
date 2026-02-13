package oodj_project.features.dashboard.assessment_grading;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.features.dashboard.assessment_management.Assessment;
import oodj_project.features.dashboard.class_management.ClassRepository;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.user_management.User;

public class AssessmentResultController {

    private final Session session;
    private final AssessmentResultRepository resultRepository;
    private final ClassRepository classRepository;
    private final GradeBookService gradeBookService;

    public AssessmentResultController(
        Session session,
        AssessmentResultRepository resultRepository,
        ClassRepository classRepository,
        GradeBookService gradeBookService
    ) {
        this.session = session;
        this.resultRepository = resultRepository;
        this.classRepository = classRepository;
        this.gradeBookService = gradeBookService;
    }

    public List<GradeBook> index() {
        var classes = classRepository.all();

        if (!session.can(Permission.READ_ALL_ASSESSMENTS)) {
            classes = classes.stream()
                .filter(classGroup -> classGroup.lecturer() != null
                    && classGroup.lecturer().equals(session.currentUser())
                )
                .toList();
        }

        return gradeBookService.getForClasses(classes);
    }

    public PaginatedResult<GradeBook> index(Query<GradeBook> query) {
        var gradeBooks = index();
        return query != null?
            query.apply(gradeBooks):
            PaginatedResult.singlePage(gradeBooks);
    }

    public List<Assessment> getUnmarkedAssessments() {
        return getUnmarkedAssessments(null);
    }

    public List<Assessment> getUnmarkedAssessments(User student) {
        var gradeBooks = getUnmarked();

        if (student != null) 
            gradeBooks = gradeBooks.filter(gradeBook -> gradeBook.enrollment().student().equals(student));

        return gradeBooks
            .map(GradeBook::assessment)
            .distinct()
            .toList();
    }

    public List<Enrollment> getUnmarkedStudents() {
        return getUnmarkedStudents(null);
    }

    public List<Enrollment> getUnmarkedStudents(Assessment assessment) {
        var gradeBooks = getUnmarked();

        if (assessment != null) 
            gradeBooks = gradeBooks.filter(gradeBook -> gradeBook.assessment().equals(assessment));

        return gradeBooks
            .map(GradeBook::enrollment)
            .distinct()
            .toList();
    }

    private Stream<GradeBook> getUnmarked() {
        return index()
            .stream()
            .filter(gradeBook -> gradeBook.result() == null);
    }

    public synchronized void create(AssessmentResult result) throws IOException {
        resultRepository.create(result);
    }

    public synchronized void update(int id, AssessmentResult result) throws IOException {
        resultRepository.update(id, result);
    }

    public synchronized void delete(AssessmentResult result) throws IOException {
        resultRepository.delete(result);
    }
}