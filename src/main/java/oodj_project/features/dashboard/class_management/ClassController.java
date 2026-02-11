package oodj_project.features.dashboard.class_management;

import java.io.IOException;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;

public class ClassController {

    private final ClassRepository repository;

    public ClassController(ClassRepository repository) {
        this.repository = repository;
    }

    public PaginatedResult<ClassGroup> index(Query<ClassGroup> query) {
        return repository.get(query);
    }

    public synchronized void create(ClassGroup classGroup) throws IOException {
        repository.create(classGroup);
    }

    public synchronized void update(int id, ClassGroup classGroup) throws IOException {
        repository.update(id, classGroup);
    }

    public synchronized void delete(ClassGroup classGroup) throws IOException {
        repository.delete(classGroup);
    }
}