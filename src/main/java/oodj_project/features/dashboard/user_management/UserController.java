package oodj_project.features.dashboard.user_management;

import java.io.IOException;
import java.util.List;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;
import oodj_project.features.services.EmailService;

public class UserController {
    private final UserRepository repository;
    private final UserPermissionService permissionService;
    private final EmailService emailService;

    public UserController(
            UserRepository repository,
            UserPermissionService permissionService,
            EmailService emailService) {
        this.repository = repository;
        this.permissionService = permissionService;
        this.emailService = emailService;
    }

    public PaginatedResult<User> index(Query<User> query) {
        return repository.get(query);
    }

    public List<User> getSupervisors() {
        return permissionService.getSupervisors();
    }

    public List<User> getLecturers() {
        return permissionService.getLecturers();
    }

    public synchronized void create(User user) throws IOException {
        validate(user);
        repository.create(user);
        emailService.sendWelcomeEmail(user.email(), user.name(), user.username(), user.password());
    }

    public synchronized void update(int id, User user) throws IOException {
        validate(user);
        repository.update(id, user);
    }

    public synchronized void resetPassword(User user) throws IOException {
        var newPassword = PasswordGenerator.generate(10);
        var newUserData = user.withPassword(newPassword);
        update(user.id(), newUserData);
        emailService.sendPasswordResetEmail(newUserData.email(), newUserData.name(), newPassword);
    }

    public synchronized void delete(User user) throws IOException {
        repository.delete(user);
    }

    private void validate(User user) throws IllegalArgumentException {
        validateString(user.name(), "Name");
        validateString(user.username(), "Username");

        var existingUser = repository.findFirst(existing -> existing.username().equalsIgnoreCase(user.username()));

        if (existingUser.isPresent() && !existingUser.get().id().equals(user.id())) {
            throw new IllegalArgumentException("Username already exists!");
        }
    }

    private void validateString(String string, String fieldName) throws IllegalArgumentException {
        var bannedCharacter = UserRepository.DELIMITER;
        if (string.contains(bannedCharacter)) {
            throw new IllegalArgumentException(fieldName + " cannot contain \"" + bannedCharacter + "\".");
        }
    }

}
