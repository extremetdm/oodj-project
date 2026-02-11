package oodj_project.features.dashboard.class_management;

import java.util.Date;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.module_management.Module;
import oodj_project.features.dashboard.user_management.User;

public record ClassGroup(
    Integer id,
    Module module,
    int maxCapacity,
    User lecturer,
    Date startDate,
    Date endDate
) implements Identifiable<ClassGroup> {

    public ClassGroup(
        Module module,
        int maxCapacity,
        User lecturer,
        Date startDate,
        Date endDate
    ) {
        this(null, module, maxCapacity, lecturer, startDate, endDate);
    }

    public ClassGroup {
        Model.require(module, "Module");
        Model.require(startDate, "Start date");
        Model.require(endDate, "End date");
    }

    public Status status() {
        var currentDate = new Date();
        if (currentDate.before(startDate))
            return Status.UPCOMING;
        if (currentDate.before(endDate))
            return Status.ONGOING;
        return Status.COMPLETED;
    }

    @Override
    public ClassGroup withId(int id) {
        return new ClassGroup(id, module, maxCapacity, lecturer, startDate, endDate);
    }

    public enum Status {
        UPCOMING,
        ONGOING,
        COMPLETED
    }
}
