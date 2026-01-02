package oodj_project.core.ui.components.filter_editor;

import java.awt.Component;

public record SelectedFilterOption<DataT, FieldT, ComponentT extends Component>(
    FilterOption<DataT, FieldT, ComponentT> option,
    FilterOperator<FieldT> operation,
    FieldT criteria
) {
    public SelectedFilterOption {
        if (option == null) throw new IllegalArgumentException("Filter option cannot be null.");
        if (operation == null) throw new IllegalArgumentException("Filter operation cannot be null.");
    }

    public String criteriaLabel() {
        return option.fieldDescriptor().apply(criteria);
    }

    public boolean test(DataT data) {
        return operation.operation()
            .test(option.fieldExtractor().apply(data), criteria);
    }
}
