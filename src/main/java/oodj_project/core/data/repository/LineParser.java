package oodj_project.core.data.repository;

import java.util.Date;

import oodj_project.core.data.model.Identifiable;

@FunctionalInterface
public interface LineParser<DataT> {
    public DataT parse(String... line) throws IllegalArgumentException;

    public static void checkArgCount(String[] args, int count) throws IllegalArgumentException {
        if (args.length != count)
            throw new IllegalArgumentException(String.format(
                "Incorrect number of fields given. %d expected, but %d given.",
                count, args.length
            ));
    }

    public static int parseInt(String field, String fieldName) throws IllegalArgumentException {
        int parsedField;
        try {
            parsedField = Integer.parseInt(field);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a number.");
        }
        return parsedField;
    }

    public static <FieldT extends Record & Identifiable<FieldT>> FieldT parseField(
        String field,
        String fieldName,
        IdentifiableRepository<FieldT> fieldRepository
    ) throws IllegalArgumentException {
        var fieldId = parseInt(field, fieldName + " ID");
        if (fieldId == 0) return null;
        return fieldRepository.find(fieldId)
            .orElseThrow(() -> new IllegalArgumentException(fieldName + " not found."));
    }

    public static long parseLong(String field, String fieldName) throws IllegalArgumentException {
        try {
            return Long.parseLong(field);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a number.");
        }
    }

    public static Date parseDate(String field, String fieldName) throws IllegalArgumentException {
        if (field.equals("null")) return null;
        try {
            return new Date(parseLong(field, fieldName));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(fieldName + " must be a unix timestamp.");
        }
    }

    public static <FieldT extends Enum<FieldT>> FieldT parseEnum(
        String field,
        String fieldName,
        Class<FieldT> enumClass
    ) {
        if (field.equals("null")) return null;
        try {
            return FieldT.valueOf(enumClass, field);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid " + fieldName + " given.");
        }
    }
}
