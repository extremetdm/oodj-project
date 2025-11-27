package oodj_project.core.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Base class for creating an in-memory repository that persists
 * data to a plain text file. 
 * <p>
 * Supports all CRUD operations.
 * 
 * @param <DataT> The type of the record this repository will manage (e.g., User).
 *                Must be a Java {@code Record}.
 */
public abstract class Repository<DataT extends Record> {
    protected final ArrayList<DataT> models = new ArrayList<>();
    protected final File sourceFile;

    /** Function that converts file lines into the {@code DataT} record object. */
    protected final Function<String[], DataT> lineParser;
    
    /** Function that converts {@code DataT} record object into a file line. */
    protected final Function<DataT, String> lineFormatter;

    /** Optional dataset integrity validator. */
    protected final Validator<DataT> validator;

    /**
     * Constructor for repositories that do not require data validation.
     * @param sourceFile    The file from which to load and to which to save data.
     * @param lineParser    Function that converts file lines into the {@code DataT} record object.
     * @param lineFormatter Function that converts {@code DataT} record object into a file line.
     * @throws IOException            If an I/O error occurs while reading the file.
     * @throws IllegalStateException  If there is data integrity violation.
     * @see #Repository(File, Function, Function, Validator)
     */
    protected Repository(
        File sourceFile,
        Function<String[], DataT> lineParser,
        Function<DataT, String> lineFormatter
    ) throws IOException {
        this(sourceFile, lineParser, lineFormatter, null);
    }

    /**
     * Loads all data from the source file into memory and validates data integrity.
     * @param sourceFile    The file from which to load and to which to save data.
     * @param lineParser    Function that converts file lines into the {@code DataT} record object.
     * @param lineFormatter Function that converts {@code DataT} record object into a file line.
     * @param validator     Dataset integrity validator. Can be {@code null}.
     * @throws IOException            If an I/O error occurs while reading the file.
     * @throws IllegalStateException  If there is data integrity violation.
     */
    protected Repository(
        File sourceFile,
        Function<String[], DataT> lineParser,
        Function<DataT, String> lineFormatter,
        Validator<DataT> validator
    ) throws IOException, IllegalStateException {
        this.sourceFile = sourceFile;
        this.lineParser = lineParser;
        this.lineFormatter = lineFormatter;
        this.validator = validator;
        read();
        validate();
    }

    /**
     * Reads the source file line by line, using the line parser to populate the in-memory model list.
     * @throws IOException If an I/O error occurs.
     */
    private void read() throws IOException {
        try (var reader = new BufferedReader(new FileReader(sourceFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                models.add(lineParser.apply(line.split("[|]")));
            }
        }
    }

    /**
     * Runs the provided validator against the entire list of models. Does nothing if the validator is null.
     * @throws IllegalStateException If the validator finds a data integrity violation.
     */
    private void validate() throws IllegalStateException {
        if (validator == null) return;
        validator.validate(models);
    }
    
    /**
     * Persists the entire in-memory list of models to the source file, overwriting its contents. 
     * <p>
     * Validation is ran before saving.
     * @throws IOException If an I/O error occurs during writing.
     * @throws IllegalStateException If validation fails before saving.
     */
    public synchronized void save() throws IOException, IllegalStateException {
        validate();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile))) {
            for (var model : models) {
                writer.write(lineFormatter.apply(model));
                writer.newLine();
            }
        }
    }

    /**
     * Adds a new model to the in-memory list and persists the entire dataset to the file.
     * @param newData The new model record to add.
     * @throws IOException If the save operation fails.
     */
    public synchronized void create(DataT newData) throws IOException {
        models.add(newData);
        save();
    }

    /**
     * Retrieves all models from the in-memory repository.
     * @return An unmodifiable {@code List} of all models.
     */
    public List<DataT> get() {
        return get(null, null);
    }

    /**
     * Retrieves all models that match the given filter.
     * @param filterBy A {@code Predicate} to filter the models.
     * @return A {@code List} of filtered models.
     */
    public List<DataT> get(Predicate<DataT> filterBy) {
        return get(filterBy, null);
    }

    /**
     * Retrieves all models, sorted according to the given comparator.
     * @param sortBy A {@code Comparator} to sort the models.
     * @return A sorted {@code List} of all models.
     */
    public List<DataT> get(Comparator<DataT> sortBy) {
        return get(null, sortBy);
    }

    /**
     * Retrieves all models, optionally filtered and sorted.
     * @param filterBy A {@code Predicate} to filter the models. Can be {@code null}.
     * @param sortBy A {@code Comparator} to sort the models. Can be {@code null}.
     * @return A potentially filtered and sorted {@code List} of models.
     */
    public List<DataT> get(Predicate<DataT> filterBy, Comparator<DataT> sortBy) {
        var stream = models.stream();
        if (filterBy != null) stream = stream.filter(filterBy);
        if (sortBy != null) stream = stream.sorted(sortBy);
        return stream.toList();
    }

    /**
     * Finds the first model that matches the given filter.
     * @param filterBy A {@code Predicate} to apply.
     * @return An {@code Optional} containing the first matching model, or an empty {@code Optional} if none found.
     */
    public Optional<DataT> findFirst(Predicate<DataT> filterBy) {
        return models.stream()
            .filter(filterBy)
            .findFirst();
    }

    /**
     * Replaces an existing model with a new one and persists the changes.
     * @param oldData The model to be replaced.
     * @param newData The model to replace it with.
     * @throws NoSuchElementException If the {@code oldData} model is not found in the repository.
     * @throws IOException If the save operation fails.
     */
    public synchronized void update(
        DataT oldData,
        DataT newData
    ) throws IOException, NoSuchElementException {
        int index = models.indexOf(oldData);
        if (index == -1) throw new NoSuchElementException();
        models.set(index, newData);
        save();
    }

    /**
     * Applies a change to all models that match the given filter and persists the changes.
     * @param filterBy A {@code Predicate} to select which models to update.
     * @param dataChange A {@code UnaryOperator} that defines the update to apply.
     * @throws IOException If the save operation fails.
     */
    public synchronized void update(
        Predicate<DataT> filterBy,
        UnaryOperator<DataT> dataChange
    ) throws IOException {
        models.replaceAll(model -> filterBy.test(model)? dataChange.apply(model): model);
        save();
    }

    /**
     * Deletes a model from the repository and persists the changes.
     * @param dataToDelete The model to delete.
     * @throws NoSuchElementException If the model to delete is not found.
     * @throws IOException If the save operation fails.
     */
    public synchronized void delete(DataT dataToDelete) throws IOException, NoSuchElementException {
        int index = models.indexOf(dataToDelete);
        if (index == -1) throw new NoSuchElementException();
        models.remove(index);
        save();
    }

    /**
     * Deletes all models that match the given filter and persists the changes.
     * @param filterBy A {@code Predicate} to select which models to delete.
     * @throws IOException If the save operation fails.
     */
    public synchronized void delete(Predicate<DataT> filterBy) throws IOException {
        models.removeIf(filterBy);
        save();
    }
}