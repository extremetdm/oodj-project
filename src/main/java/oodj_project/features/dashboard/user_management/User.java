package oodj_project.features.dashboard.user_management;

import java.util.Date;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.role_management.Role;

public record User(
        Integer id,
        String username,
        String name,
        String password,
        Gender gender,
        Role role,
        String email,
        String phoneNumber,
        Date dateOfBirth) implements Identifiable<User> {
    public User(
            String username,
            String name,
            String password,
            Gender gender,
            Role role,
            String email,
            String phoneNumber,
            Date dateOfBirth) {
        this(null, username, name, password, gender, role, email, phoneNumber, dateOfBirth);
    }

    public User {
        Model.require(username, "Username");
        Model.require(name, "Full name");
        Model.require(email, "Email");
        Model.checkEmail(email);
        Model.require(phoneNumber, "Phone number");
        Model.checkPhoneNumber(phoneNumber);
        Model.require(role, "Role");
        Model.require(dateOfBirth, "Date of birth");
        Model.checkDatePast(dateOfBirth);
        username = Model.normalize(username);
        name = Model.normalize(name);
        email = Model.normalize(email);
        phoneNumber = Model.normalize(phoneNumber);
    }

    @Override
    public User withId(int id) {
        return new User(id, username, name, password, gender, role, email, phoneNumber, dateOfBirth);
    }

    public User withPassword(String password) {
        return new User(id, username, name, password, gender, role, email, phoneNumber, dateOfBirth);
    }
}