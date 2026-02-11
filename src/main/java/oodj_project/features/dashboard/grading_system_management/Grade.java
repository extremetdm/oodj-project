package oodj_project.features.dashboard.grading_system_management;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;

public record Grade(
    Integer id,
    String name,
    int min,
    int max,
    Classification classification
) implements Identifiable<Grade> {
    public Grade {
        Model.require(name, "Name");
        Model.require(classification, "Classification");
        name = Model.normalize(name);
        if (min < 0) throw new IllegalArgumentException("Min mark must be at least 0.");
        if (max > 100) throw new IllegalArgumentException("Max mark must be less than 100.");
        if (min > max) throw new IllegalArgumentException("Min mark cannot be more than max mark.");
    }

    public Grade(
        String name,
        int min,
        int max,
        Classification classification
    ) {
        this(null, name, min, max, classification);
    }

    @Override
    public Grade withId(int id) {
        return new Grade(id, name, min, max, classification);
    }

    public enum Classification {
        Distinction,
        Credit,
        Pass,
        Fail
    }
}