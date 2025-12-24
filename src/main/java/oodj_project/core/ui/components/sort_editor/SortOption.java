package oodj_project.core.ui.components.sort_editor;

import java.util.Comparator;
import java.util.function.Function;

public record SortOption<DataT>(String label, Comparator<DataT> comparator) {
    public SortOption {
        if (comparator == null) throw new IllegalArgumentException("Comparator cannot be null.");
    }

    public static <DataT, FieldT extends Comparable<? super FieldT>> SortOption<DataT> of(String label, Function<DataT, FieldT> fieldExtractor) {
        return new SortOption<>(label, Comparator.comparing(fieldExtractor));
    }

    public static <DataT> SortOption<DataT> text(String label, Function<DataT, String> textExtractor) {
        return new SortOption<>(label, Comparator.comparing(textExtractor, String.CASE_INSENSITIVE_ORDER));
    }
}
