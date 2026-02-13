package oodj_project.features.dashboard.enrolled_classes;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.class_management.ClassDefinition;

public class EnrollmentDefinition {
    public static final SortOption<Enrollment>
        SORT_ID = SortOption.of("ID", Enrollment::id),
        SORT_CLASS_ID = ClassDefinition.SORT_ID.derive("Class ID", Enrollment::classGroup);

    public static final List<SortOption<Enrollment>> SORT_OPTIONS = List.of(
        SORT_ID,
        SORT_CLASS_ID
    );

    public static final FilterOption<Enrollment, Integer, FormSpinner<Integer>>
        FILTER_ID = FilterOption.compare("ID", Enrollment::id, InputStrategy.nonNegativeIntegerField()),
        FILTER_CLASS_ID = ClassDefinition.FILTER_ID.derive("Class ID", Enrollment::classGroup);

    public static final List<FilterOption<Enrollment, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_ID,
        FILTER_CLASS_ID
    );
}
