package oodj_project.core.data;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import oodj_project.core.model.Identifiable;
import oodj_project.core.validation.Validator;

public abstract class IdentifiableRepository<DataT extends Record & Identifiable<DataT>> extends Repository<DataT> {
    /**
     * @see #Repository(File, Function, Function)
     */
    protected IdentifiableRepository(File sourceFile, Function<String[], DataT> lineParser, Function<DataT, String> lineFormatter) throws IOException {
        super(sourceFile, lineParser, lineFormatter);
    }

    /**
     * @see #Repository(File, Function, Function, Validator)
     */
    protected IdentifiableRepository(File sourceFile, Function<String[], DataT> lineParser, Function<DataT, String> lineFormatter, Validator<DataT> validator) throws IOException, IllegalStateException {
        super(sourceFile, lineParser, lineFormatter, validator);
    }

    private int nextId() {
        return models.stream()
            .mapToInt(DataT::id)
            .max()
            .orElse(0) + 1;
    }

    @Override
    public void create(DataT model) throws IOException {
        super.create(model.withId(nextId()));
    }

    public Optional<DataT> find(int id) {
        return findFirst(model -> model.id() == id);
    }

    public void update(int id, DataT model) throws IOException, NoSuchElementException {
        update(
            find(id).orElseThrow(),
            model.withId(id)
        );
    }

    public void delete(int id) throws IOException, NoSuchElementException {
        delete(find(id).orElseThrow());
    }
}
