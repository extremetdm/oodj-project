package oodj_project.core.ui.components.filter_editor;

import java.awt.Component;
import java.util.List;
import java.util.function.Function;

public record FilterOption<DataT, FieldT, ComponentT extends Component>(
    String label,
    Function<DataT, FieldT> fieldExtractor,
    List<FilterOperator<FieldT>> operations,
    InputStrategy<ComponentT, FieldT> inputStrategy,
    Function<FieldT, String> fieldDescriptor
) {
    public FilterOption(
        String label,
        Function<DataT, FieldT> fieldExtractor,
        List<FilterOperator<FieldT>> operations,
        InputStrategy<ComponentT, FieldT> inputStrategy
    ) {
        this(label, fieldExtractor, operations, inputStrategy, FieldT::toString);
    }

    public FilterOption {
        if (label == null) throw new IllegalArgumentException("Label cannot be null.");
        if (fieldExtractor == null) throw new IllegalArgumentException("Field extractor cannot be null.");
        if (operations == null || operations.isEmpty()) throw new IllegalArgumentException("Operation list cannot be empty.");
        if (inputStrategy == null) throw new IllegalArgumentException("Input strategy cannot be null.");
        if (fieldDescriptor == null) throw new IllegalArgumentException("Field descriptor cannot be null.");
    }

    public static <DataT, ComponentT extends Component>
    FilterOption<DataT, String, ComponentT> text(
        String label,
        Function<DataT, String> fieldExtractor,
        InputStrategy<ComponentT, String> inputStrategy
    ) {
        return new FilterOption<>(
            label,
            fieldExtractor,
            FilterOperator.STRING_OPERATORS,
            inputStrategy
        );
    }

    public static <DataT, FieldT extends Comparable<? super FieldT>, ComponentT extends Component>
    FilterOption<DataT, FieldT, ComponentT> compare(
        String label,
        Function<DataT, FieldT> fieldExtractor,
        InputStrategy<ComponentT, FieldT> inputStrategy
    ) {
        return new FilterOption<>(
            label,
            fieldExtractor,
            FilterOperator.getCompareOperators(),
            inputStrategy
        );
    }

    public static <DataT, FieldT, ComponentT extends Component>
    FilterOption<DataT, FieldT, ComponentT> sameAs(
        String label,
        Function<DataT, FieldT> fieldExtractor,
        InputStrategy<ComponentT, FieldT> inputStrategy,
        Function<FieldT, String> fieldDescriptor
    ) {
        return new FilterOption<>(
            label,
            fieldExtractor,
            List.of(FilterOperator.getEqualOperator()),
            inputStrategy,
            fieldDescriptor
        );
    }
}

