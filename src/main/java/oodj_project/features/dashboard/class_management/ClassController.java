package oodj_project.features.dashboard.class_management;

import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import java.time.ZoneId;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.core.security.Session;

public class ClassController {

    private final Session session;
    private final ClassRepository repository;

    public ClassController(
            Session session,
            ClassRepository repository) {
        this.session = session;
        this.repository = repository;
    }

    public PaginatedResult<ClassGroup> index(Query<ClassGroup> query) {
        return repository.get(query);
    }

    public List<ClassGroup> getMyClasses() {
        return repository.all()
                .stream()
                .filter(classGroup -> classGroup.lecturer() == session.currentUser())
                .toList();
    }

    public synchronized void create(ClassGroup classGroup) throws IOException {
        validate(classGroup);
        repository.create(classGroup);
    }

    private void validate(ClassGroup classGroup) {
        var startDate = classGroup.startDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }
    }

    public synchronized void update(int id, ClassGroup classGroup) throws IOException {
        repository.update(id, classGroup);
    }

    public synchronized void delete(ClassGroup classGroup) throws IOException {
        repository.delete(classGroup);
    }
}