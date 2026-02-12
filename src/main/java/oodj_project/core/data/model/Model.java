package oodj_project.core.data.model;

public class Model {
    public static String normalize(String string) {
        if (string == null)
            return "";
        return string.strip();
    }

    public static <FieldT> void require(FieldT field) throws IllegalArgumentException {
        require(field, "This field");
    }

    public static <FieldT> void require(FieldT field, String fieldName) throws IllegalArgumentException {
        if (field == null)
            throw new IllegalArgumentException(fieldName + " cannot be null.");
    }

    public static void require(String field) throws IllegalArgumentException {
        require(field, "This field");
    }

    public static void require(String field, String fieldName) throws IllegalArgumentException {
        if (field == null || field.isBlank())
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
    }

    public static void checkEmail(String email) throws IllegalArgumentException {
        if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid Email Format.");
        }
    }

    public static void checkPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        if (phoneNumber == null || !phoneNumber.matches("^(\\+?6?01)[0-46-9]-*[0-9]{7,8}$")) {
            throw new IllegalArgumentException("Invalid Phone Number Format (e.g., 012-3456789).");
        }
    }

    public static void checkDatePast(java.util.Date date) throws IllegalArgumentException {
        if (date == null || date.after(new java.util.Date())) {
            throw new IllegalArgumentException("Date must be in the past.");
        }
    }

    // public static void checkIntPositive(int field, String fieldName) throws
    // IllegalArgumentException {
    // if (field <= 0)
    // throw new IllegalArgumentException(fieldName + " cannot be blank.");
    // }
}
