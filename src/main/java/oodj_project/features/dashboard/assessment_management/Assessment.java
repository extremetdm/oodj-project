package oodj_project.features.dashboard.assessment_management;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.class_management.ClassGroup;

public record Assessment(
    Integer id,
    String name,
    ClassGroup classGroup,
    Type type,
    int marks
) implements Identifiable<Assessment> {
    public Assessment {
        Model.require(name, "Name");
        Model.require(type, "Type");
        Model.require(classGroup, "Class");

        name = Model.normalize(name);
    }

    public Assessment(
        String name,
        ClassGroup classGroup,
        Type type,
        int marks
    ) {
        this(null, name, classGroup, type, marks);
    }

    @Override
    public Assessment withId(int id) {
        return new Assessment(id, name, classGroup, type, marks);
    }

    public String displayId() {
        return String.format("A%03d", id);
    }

    public enum Type {
        Assignment, 
        Test, 
        Exam, 
        Presentation
    }
}
