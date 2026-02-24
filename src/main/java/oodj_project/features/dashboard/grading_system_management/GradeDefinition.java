package oodj_project.features.dashboard.grading_system_management;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormComboBox;
import oodj_project.core.ui.components.sort_editor.SortOption;

public class GradeDefinition {

    // public static final SortOption<Grade>

    public static final List<SortOption<Grade>> SORT_OPTIONS = List.of(
        SortOption.text("Grade", Grade::name),
        SortOption.of("Min marks", Grade::min),
        SortOption.of("Max marks", Grade::max)
    );
    
    public static final FilterOption<Grade, Grade.Classification, FormComboBox<Grade.Classification>>
        FILTER_CLASSIFICATION = FilterOption.sameAs(
            "Classification",
            Grade::classification,
            InputStrategy.selectField(
                Grade.Classification::name,
                List.of(Grade.Classification.values())
            ),
            Grade.Classification::name
        );

    public static final List<FilterOption<Grade, ?, ?>> FILTER_OPTIONS = List.of(
        FilterOption.text("Grade", Grade::name, InputStrategy.textField()),
        FILTER_CLASSIFICATION
    );
}
