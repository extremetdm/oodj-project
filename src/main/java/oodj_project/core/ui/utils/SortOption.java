package oodj_project.core.ui.utils;

import java.util.Comparator;
import java.util.function.Function;

public record SortOption<DataT>(String label, Comparator<DataT> comparator) {
    public <FieldT extends Comparable<? super FieldT>> SortOption(String label, Function<DataT, FieldT> fieldExtractor) {
        this(label, Comparator.comparing(fieldExtractor));
    }

    public SortOption {
        if (comparator == null) throw new IllegalArgumentException("Comparator cannot be null.");
    }
}
