package oodj_project.features.dashboard.assessment_management;

import java.io.IOException;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;

public class AssessmentController {

    private final Session session;
    private final AssessmentRepository repository;

    public AssessmentController(
        Session session,
        AssessmentRepository repository
    ) {
        this.session = session;
        this.repository = repository;
    }

    public PaginatedResult<Assessment> index(Query<Assessment> query) {
        if (!session.can(Permission.READ_ALL_ASSESSMENTS)) {
            query = query.toBuilder()
                .addFilter(assessment -> session.currentUser().equals(assessment.classGroup().lecturer()))
                .build();
        }
        return repository.get(query);
    }

    public synchronized void create(Assessment classGroup) throws IOException {
        repository.create(classGroup);
    }

    public synchronized void update(int id, Assessment classGroup) throws IOException {
        repository.update(id, classGroup);
    }

    public synchronized void delete(Assessment classGroup) throws IOException {
        repository.delete(classGroup);
    }
}