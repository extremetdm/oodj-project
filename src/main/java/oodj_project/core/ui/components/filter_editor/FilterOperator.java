package oodj_project.core.ui.components.filter_editor;

import java.util.List;
import java.util.function.BiPredicate;

public record FilterOperator<FieldT>(
    String label,
    BiPredicate<FieldT, FieldT> operation,
    String briefLabel
) {
    public FilterOperator(String label, BiPredicate<FieldT, FieldT> operation) {
        this(label, operation, label);
    }

    public FilterOperator {
        if (operation == null) throw new IllegalArgumentException("Operation logic cannot be null.");
    }

    public static List<FilterOperator<String>> STRING_OPERATORS = List.of(
        new FilterOperator<>("Starts with", (value, criteria) -> value.toLowerCase().startsWith(criteria.toLowerCase())),
        new FilterOperator<>("Ends with", (value, criteria) -> value.toLowerCase().endsWith(criteria.toLowerCase())),
        new FilterOperator<>("Contains", (value, criteria) -> value.toLowerCase().contains(criteria.toLowerCase())),
        new FilterOperator<>("Is", String::equalsIgnoreCase)
    );

    public static <FieldT extends Comparable<? super FieldT>>
    List<FilterOperator<FieldT>> getCompareOperators() {
        return List.of(
            new FilterOperator<>("< (Is less than)", (value, criteria) -> value.compareTo(criteria) < 0, "<"),
            new FilterOperator<>("≤ (Is less than or equal to)", (value, criteria) -> value.compareTo(criteria) <= 0, "≤"),
            new FilterOperator<>("= (Is equal to)", (value, criteria) -> value.compareTo(criteria) == 0, "="),
            new FilterOperator<>("> (Is more than)", (value, criteria) -> value.compareTo(criteria) > 0, ">"),
            new FilterOperator<>("≥ (Is more than or equal to)", (value, criteria) -> value.compareTo(criteria) >= 0, "≥")
        );
    }

    public static <FieldT> FilterOperator<FieldT> getEqualOperator() {
        return new FilterOperator<>("Is", FieldT::equals);
    }
}
