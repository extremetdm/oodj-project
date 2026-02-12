package oodj_project.features.dashboard.enrolled_classes;

import java.util.Date;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.user_management.User;

public record Enrollment(Integer id, User student, ClassGroup classGroup, Date registerDate, Date dropoutDate) implements Identifiable<Enrollment> {
    public Enrollment {
        Model.require(student, "Student");
        Model.require(classGroup, "Class");
        Model.require(registerDate, "Register date");
    }

    public Enrollment(User student, ClassGroup classGroup) {
        this(student, classGroup, new Date(), null);
    }

    public Enrollment(User student, ClassGroup classGroup, Date registerDate, Date dropoutDate) {
        this(null, student, classGroup, registerDate, dropoutDate);
    }

    @Override
    public Enrollment withId(int id) {
       return new Enrollment(id, student, classGroup, registerDate, dropoutDate);
    }

    public Enrollment withDropoutDate(Date dropoutDate) {
       return new Enrollment(id, student, classGroup, registerDate, dropoutDate);
    }

    public Status status() {
        if (dropoutDate != null) return Status.DROPPED;
        var now = new Date();
        if (classGroup.startDate().before(now)) {
            return Status.UPCOMING;
        }
        if (classGroup.endDate().before(now)) {
            return Status.ONGOING;
        }
        return Status.COMPLETED;
    }

    public enum Status {
        UPCOMING,
        ONGOING,
        COMPLETED,
        DROPPED
    }
}
