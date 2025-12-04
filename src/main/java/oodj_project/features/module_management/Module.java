package oodj_project.features.module_management;

import oodj_project.core.data.model.Identifiable;

public record Module(Integer id) implements Identifiable<Module> {
    public Module() {
        this(null);
    }

    public Module {

    }

    @Override
    public Module withId(int id) {
        return new Module(id);
    }
}
