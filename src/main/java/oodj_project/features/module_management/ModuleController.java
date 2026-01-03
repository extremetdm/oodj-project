package oodj_project.features.module_management;

import java.io.IOException;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;

public class ModuleController {

    private final ModuleRepository repository;

    public ModuleController(ModuleRepository repository) {
        this.repository = repository;
    }

    public PaginatedResult<Module> index(Query<Module> query) {
        return repository.get(query);
    }

    public synchronized void create(Module module) throws IOException {
        validate(module);
        repository.create(module);
    }

    public synchronized void update(int id, Module module) throws IOException {
        validate(module);
        repository.update(id, module);
    }

    public synchronized void delete(Module module) throws IOException {
        repository.delete(module);
    }

    private void validate(Module module) throws IllegalArgumentException {
        validateString(module.name(), "Name");
        validateString(module.description(), "Description");
    }

    private void validateString(String string, String fieldName) throws IllegalArgumentException {
        var bannedCharacter = ModuleRepository.DELIMITER;
        if (string.contains(bannedCharacter)) {
            throw new IllegalArgumentException(fieldName + " cannot contain \"" + bannedCharacter + "\".");
        }
    }
}