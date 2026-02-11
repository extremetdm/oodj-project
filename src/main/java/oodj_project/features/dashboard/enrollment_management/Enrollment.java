package oodj_project.features.dashboard.enrollment_management;

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
}
