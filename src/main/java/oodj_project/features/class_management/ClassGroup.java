package oodj_project.features.class_management;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;
import oodj_project.features.module_management.Module;
import oodj_project.features.user_management.User;

public record ClassGroup(Integer id, Module module, int maxCapacity, User lecturer) implements Identifiable<ClassGroup> {

    public ClassGroup(Module module, int maxCapacity, User lecturer) {
        this(null, module, maxCapacity, lecturer);
    }

    public ClassGroup {
        Model.require(module, "Module");
    }

    @Override
    public ClassGroup withId(int id) {
        return new ClassGroup(id, module, maxCapacity, lecturer);
    }
}
