package oodj_project.features.dashboard.module_management;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;

public record Module(Integer id, String name, String description) implements Identifiable<Module> {
    
    public Module(String name, String description) {
        this(null, name, description);
    }

    public Module {
        Model.require(name, "Name");
        name = Model.normalize(name);
        description = Model.normalize(description);
    }

    @Override
    public Module withId(int id) {
        return new Module(id, name, description);
    }
}
