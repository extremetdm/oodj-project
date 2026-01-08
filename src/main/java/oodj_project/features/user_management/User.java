package oodj_project.features.user_management;

import java.util.Date;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;
import oodj_project.features.role_management.Role;

public record User(
    Integer id,
    String username,
    String name,
    String password,
    Gender gender,
    Role role,
    String email,
    String phoneNumber,
    Date dateOfBirth
) implements Identifiable<User> {
    public User(
        String username,
        String name,
        String password,
        Gender gender,
        Role role,
        String email,
        String phoneNumber,
        Date dateOfBirth
    ) {
        this(null, username, name, password, gender, role, email, phoneNumber, dateOfBirth);
    }

    public User {
        Model.normalize(name);
        // new Date
    }

    @Override
    public User withId(int id) {
        return new User(id, username, name, password, gender, role, email, phoneNumber, dateOfBirth);
    }
}