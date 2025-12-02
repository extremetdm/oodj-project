package oodj_project.core.validation;

import java.util.List;

@FunctionalInterface
public interface Validator<DataT> {
    /**
     * Checks the model list for data integrity.
     * <p>
     * Throws exception if integrity violation is found.
     * @param models The complete list of models to validate.
     * @throws IllegalStateException if data integrity is compromised.
     */
    void validate(List<DataT> models) throws IllegalStateException;
}
