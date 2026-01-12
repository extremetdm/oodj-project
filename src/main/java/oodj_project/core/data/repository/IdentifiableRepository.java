package oodj_project.core.data.repository;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import oodj_project.core.data.model.Identifiable;
import oodj_project.core.data.validation.Validator;

public abstract class IdentifiableRepository<DataT extends Record & Identifiable<DataT>> extends Repository<DataT> {
    /**
     * @see #Repository(File, LineParser, LineFormatter)
     */
    protected IdentifiableRepository(File sourceFile, LineParser<DataT> lineParser, LineFormatter<DataT> lineFormatter) throws IOException {
        super(sourceFile, lineParser, lineFormatter);
    }

    /**
     * @see #Repository(File, LineParser, LineFormatter, Validator)
     */
    protected IdentifiableRepository(File sourceFile, LineParser<DataT> lineParser, LineFormatter<DataT> lineFormatter, Validator<DataT> validator) throws IOException, IllegalStateException {
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
