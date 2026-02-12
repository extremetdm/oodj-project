package oodj_project.features.dashboard.assessment_management;

import java.io.IOException;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;

public class AssessmentController {

    private final AssessmentRepository repository;

    public AssessmentController(
        AssessmentRepository repository
    ) {
        this.repository = repository;
    }

    public PaginatedResult<Assessment> index(Query<Assessment> query) {
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