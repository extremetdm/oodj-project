package oodj_project.core.data.validation;

import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class for creating common, reusable {@link Validator} instances.
 */
public final class Rule {
    private Rule() {}

    private static <DataT> Function<DataT, IllegalStateException> 
    toExceptionGenerator(String message) {
        return model -> new IllegalStateException(message);
    }

    private static <DataT> Function<DataT, IllegalStateException>
    toExceptionGenerator(IllegalStateException exception) {
        return model -> exception;
    }

    /**
     * Convenience overload that accepts a static exception instance.
     * @see #unique(Function, Function)
     */
    public static <DataT, FieldT> Validator<DataT> unique(
        Function<DataT, FieldT> fieldGetter,
        IllegalStateException exception
    ) {
        return unique(fieldGetter, toExceptionGenerator(exception));
    }

    /**
     * Convenience overload that accepts a static error message string.
     * @see #unique(Function, Function)
     */
    public static <DataT, FieldT> Validator<DataT> unique(
        Function<DataT, FieldT> fieldGetter,
        String errorMessage
    ) {
        return unique(fieldGetter, toExceptionGenerator(errorMessage));
    }

    /**
     * Creates a {@link Validator} that checks for the uniqueness of a specific field across all models.
     * <p>
     * Example usage:
     * <pre>{@code
     * Rule.unique(
     *     User::username,
     *     model -> new IllegalStateException("Duplicate username found: " + model.username())
     * )
     * }</pre>
     * @param <DataT> The type of the model being validated (e.g., User).
     * @param <FieldT> The type of the field being checked for uniqueness (e.g., Integer).
     * @param fieldGetter Getter function that extracts the field from the model.
     * @param exceptionGenerator Exception generator function for the model that failed validation.
     * @return {@code Validator} that enforces the uniqueness rule.
     */
    public static <DataT, FieldT> Validator<DataT> unique(
        Function<DataT, FieldT> fieldGetter,
        Function<DataT, IllegalStateException> exceptionGenerator
    ) {
        return models -> {
            var seenValues = new HashSet<FieldT>();
            for (var model: models) {
                var value = fieldGetter.apply(model);
                if (!seenValues.add(value)) {
                    throw exceptionGenerator.apply(model);
                }
            }
        };
    }

    /**
     * Convenience overload that accepts a static exception instance.
     * @see #notNull(Function, Function)
     */
    public static <DataT> Validator<DataT> notNull(
        Function<DataT, ?> fieldGetter,
        IllegalStateException exception
    ) {
        return notNull(fieldGetter, toExceptionGenerator(exception));
    }
    
    /**
     * Convenience overload that accepts a static error message string.
     * @see #notNull(Function, Function)
     */
    public static <DataT> Validator<DataT> notNull(
        Function<DataT, ?> fieldGetter,
        String errorMessage
    ) {
        return notNull(fieldGetter, toExceptionGenerator(errorMessage));
    }

    /**
     * Creates a {@link Validator} that ensures a specific field is not {@code null}.
     * @param <DataT> The type of the model being validated. (e.g., User).
     * @param fieldGetter Getter function that extracts the field from the model.
     * @param exceptionGenerator Exception generator function for the model that failed validation.
     * @return {@code Validator} that enforces the not-null rule.
     */
    public static <DataT> Validator<DataT> notNull(
        Function<DataT, ?> fieldGetter,
        Function<DataT, IllegalStateException> exceptionGenerator
    ) {
        return models -> {
            for (var model: models) {
                if (fieldGetter.apply(model) == null) {
                    throw exceptionGenerator.apply(model);
                }
            }
        };
    }

    /**
     * Convenience overload that accepts a static exception instance.na
     * @see #notBlank(Function, Function)
     */
    public static <DataT> Validator<DataT> notBlank(
        Function<DataT, String> fieldGetter,
        IllegalStateException exception
    ) {
        return notBlank(fieldGetter, toExceptionGenerator(exception));
    }

    /**
     * Convenience overload that accepts a static error message string.
     * @see #notNull(Function, Function)
     */
    public static <DataT> Validator<DataT> notBlank(
        Function<DataT, String> fieldGetter,
        String errorMessage
    ) {
        return notBlank(fieldGetter, toExceptionGenerator(errorMessage));
    }

    /**
     * Creates a {@link Validator} that ensures a specific {@code String} field is not {@code null},
     * empty, or contains only whitespace.
     * @param <DataT> The type of the model being validated.
     * @param fieldGetter Getter function that extracts the {@code String} field from the model.
     * @param exceptionGenerator Exception generator function for the model that failed validation.
     * @return {@code Validator} that enforces the not-blank rule for Strings.
     */
    public static <DataT> Validator<DataT> notBlank(
        Function<DataT, String> fieldGetter,
        Function<DataT, IllegalStateException> exceptionGenerator
    ) {
        return models -> {
            for (var model: models) {
                var value = fieldGetter.apply(model);
                if (value == null || value.isBlank()) {
                    throw exceptionGenerator.apply(model);
                }
            }
        };
    }

    public static <DataT, FieldT> Validator<DataT> in(
        Function<DataT, FieldT> fieldGetter,
        Supplier<List<FieldT>> haystackGetter,
        String errorMessage
    ) {
        return in(fieldGetter, haystackGetter, toExceptionGenerator(errorMessage));
    }

    public static <DataT, FieldT> Validator<DataT> in(
        Function<DataT, FieldT> fieldGetter,
        Supplier<List<FieldT>> haystackGetter,
        IllegalStateException exception
    ) {
        return in(fieldGetter, haystackGetter, toExceptionGenerator(exception));
    }

    public static <DataT, FieldT> Validator<DataT> in(
        Function<DataT, FieldT> fieldGetter,
        Supplier<List<FieldT>> haystackGetter,
        Function<DataT, IllegalStateException> exceptionGenerator
    ) {
        return models -> {
            for (var model: models) {
                var value = fieldGetter.apply(model);
                if (value != null && !haystackGetter.get().contains(value)) {
                    throw exceptionGenerator.apply(model);
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
