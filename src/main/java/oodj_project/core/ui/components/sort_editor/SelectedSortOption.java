package oodj_project.core.ui.components.sort_editor;

import java.util.Comparator;

public record SelectedSortOption<DataT>(SortOption<DataT> option, boolean isDescending) {
    public SelectedSortOption {
        if (option == null) throw new IllegalArgumentException("Sort option cannot be null.");
    }

    public Comparator<DataT> comparator() {
        var comparator = option.comparator();
        if (isDescending) return comparator.reversed();
        else return comparator;
    }
}
