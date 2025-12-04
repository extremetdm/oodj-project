package oodj_project.features.user_management;

import oodj_project.core.data.model.Identifiable;
import oodj_project.features.role_management.Role;

public record User(Integer id, String name, String password, Role role) implements Identifiable<User> {
    public User(String name, String password, Role role) {
        this(null, name, password, role);
    }

    public User {

    }

    @Override
    public User withId(int id) {
        return new User(id, name, password, role);
    }
}