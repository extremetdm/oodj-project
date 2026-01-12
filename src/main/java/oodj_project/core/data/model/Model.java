package oodj_project.core.data.model;

public class Model {
    public static String normalize(String string) {
        if (string == null) return "";
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

    // public static void checkIntPositive(int field, String fieldName) throws IllegalArgumentException {
    //     if (field <= 0)
    //         throw new IllegalArgumentException(fieldName + " cannot be blank.");
    // }
}
