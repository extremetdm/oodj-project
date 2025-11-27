package oodj_project.core.repository;

import java.util.HashSet;
import java.util.function.Function;

/**
 * A utility class for creating common, reusable {@link Validator} instances.
 */
public final class Rule {
    private Rule() {}

    /**
     * Creates a {@link Validator} that checks for the uniqueness of a specific field across all models.
     * <p>
     * Example usage:
     * <pre>{@code
     * Rule.unique(User::id, new IllegalStateException("Duplicate ID found!"))
     * }</pre>
     *
     * @param <DataT> The type of the model being validated (e.g., User).
     * @param <FieldT> The type of the field being checked for uniqueness (e.g., Integer).
     * @param fieldGetter Getter function that extracts the field from the model.
     * @param exception The specific {@code IllegalStateException} to throw if a duplicate value is found.
     * @return {@code Validator} that enforces the uniqueness rule.
     */
    public static <DataT, FieldT> Validator<DataT> unique(
        Function<DataT, FieldT> fieldGetter,
        IllegalStateException exception
    ) {
        return models -> {
            var seenValues = new HashSet<FieldT>();
            for (var model: models) {
                var value = fieldGetter.apply(model);
                if (!seenValues.add(value)) {
                    throw exception;
                }
            }
        };
    }

    /**
     * Creates a {@link Validator} that ensures a specific field is not {@code null}.
     *
     * @param <DataT> The type of the model being validated. (e.g., User).
     * @param fieldGetter Getter function that extracts the field from the model.
     * @param exception The specific {@code IllegalStateException} to throw if a null value is found.
     * @return {@code Validator} that enforces the not-null rule.
     */
    public static <DataT> Validator<DataT> notNull(
        Function<DataT, ?> fieldGetter,
        IllegalStateException exception
    ) {
        return models -> {
            for (var model: models) {
                if (fieldGetter.apply(model) == null) {
                    throw exception;
                }
            }
        };
    }

    /**
     * Creates a {@link Validator} that ensures a specific {@code String} field is not {@code null},
     * empty, or contains only whitespace.
     *
     * @param <DataT> The type of the model being validated.
     * @param fieldGetter Getter function that extracts the {@code String} field from the model.
     * @param exception The specific {@code IllegalStateException} to throw if a blank value is found.
     * @return {@code Validator} that enforces the not-blank rule for Strings.
     */
    public static <DataT> Validator<DataT> notBlank(
        Function<DataT, String> fieldGetter,
        IllegalStateException exception
    ) {
        return models -> {
            for (var model: models) {
                var value = fieldGetter.apply(model);
                if (value == null || value.isBlank()) {
                    throw exception;
                }
            }
        };
    }

    /**
     * Composes multiple {@code Validator} instances into a single {@code Validator} that
     * executes them in the provided sequence.
     * @param <DataT> The type of the model being validated.
     * @param rules {@code Validator} instances to compose.
     * @return A single {@code Validator} that runs each of the provided rules.
     */
    @SafeVarargs
    public static <DataT> Validator<DataT> compose(Validator<DataT>... rules) {
        return models -> {
            for (var rule: rules) {
                rule.validate(models);
            }
        };
    }
}
