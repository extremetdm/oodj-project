package oodj_project.features.dashboard.grading_system_management;

import java.io.IOException;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;

public class GradeController {

    private final GradeRepository repository;

    public GradeController(GradeRepository repository) {
        this.repository = repository;
    }

    public PaginatedResult<Grade> index(Query<Grade> query) {
        return repository.get(query);
    }

    public synchronized void create(Grade module) throws IOException {
        validate(module);
        repository.create(module);
    }

    public synchronized void update(int id, Grade module) throws IOException {
        validate(module);
        repository.update(id, module);
    }

    public synchronized void delete(Grade module) throws IOException {
        repository.delete(module);
    }

    private void validate(Grade module) throws IllegalArgumentException {
        validateString(module.name(), "Name");
    }

    private void validateString(String string, String fieldName) throws IllegalArgumentException {
        var bannedCharacter = GradeRepository.DELIMITER;
        if (string.contains(bannedCharacter)) {
            throw new IllegalArgumentException(fieldName + " cannot contain \"" + bannedCharacter + "\".");
        }
    }
}