package oodj_project.features.role_management;

import oodj_project.core.model.Identifiable;

public record Role(Integer id, String name) implements Identifiable<Role> {
    public Role(String name) {
        this(null, name);
    }

    public Role {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be blank.");
        name = name.strip();   
    }

    @Override
    public Role withId(int id) {
        return new Role(id, name);
    }
}