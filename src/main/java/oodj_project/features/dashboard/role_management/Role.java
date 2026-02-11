package oodj_project.features.dashboard.role_management;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;

public record Role(Integer id, String name) implements Identifiable<Role> {
    public Role(String name) {
        this(null, name);
    }

    public Role {
        Model.require(name, "Name");
        name = Model.normalize(name);   
    }

    @Override
    public Role withId(int id) {
        return new Role(id, name);
    }
}