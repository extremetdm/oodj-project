package oodj_project.features.dashboard.feedback_management;

import java.io.IOException;
import java.util.List;

import oodj_project.core.data.repository.PaginatedResult;
import oodj_project.core.data.repository.Query;

public class FeedbackController {

    private final FeedbackRepository repository;

    public FeedbackController(FeedbackRepository repository) {
        this.repository = repository;
    }

    public List<Feedback> index() {
        return repository.all();
    }

    public PaginatedResult<Feedback> index(Query<Feedback> query) {
        return repository.get(query);
    }

    public synchronized void create(Feedback feedback) throws IOException {
        validate(feedback);
        repository.create(feedback);
    }

    public synchronized void update(int id, Feedback feedback) throws IOException {
        validate(feedback);
        repository.update(id, feedback);
    }

    public synchronized void delete(Feedback feedback) throws IOException {
        repository.delete(feedback);
    }

    private void validate(Feedback feedback) throws IllegalArgumentException {
        validateString(feedback.comment(), "Comment");
    }

    private void validateString(String string, String fieldName) throws IllegalArgumentException {
        var bannedCharacter = FeedbackRepository.DELIMITER;
        if (string.contains(bannedCharacter)) {
            throw new IllegalArgumentException(fieldName + " cannot contain \"" + bannedCharacter + "\".");
        }
    }
}