package oodj_project.features.dashboard.enrolled_classes;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.class_management.ClassDefinition;
import oodj_project.features.dashboard.user_management.UserDefinition;

public class EnrollmentDefinition {
    public static final SortOption<Enrollment>
        SORT_ID = SortOption.of("ID", Enrollment::id),
        SORT_STUDENT_ID = UserDefinition.SORT_ID.derive("Student ID", Enrollment::student),
        SORT_STUDENT_NAME = UserDefinition.SORT_NAME.derive("Student Name", Enrollment::student),
        SORT_CLASS_ID = ClassDefinition.SORT_ID.derive("Class ID", Enrollment::classGroup),
        SORT_MODULE_ID = ClassDefinition.SORT_MODULE_ID.derive(Enrollment::classGroup),
        SORT_MODULE_NAME = ClassDefinition.SORT_MODULE_NAME.derive(Enrollment::classGroup);

    public static final List<SortOption<Enrollment>> SORT_OPTIONS = List.of(
        SORT_ID,
        SORT_CLASS_ID
    );

    public static final FilterOption<Enrollment, Integer, FormSpinner<Integer>>
        FILTER_ID = FilterOption.compare("ID", Enrollment::id, InputStrategy.nonNegativeIntegerField()),
        FILTER_STUDENT_ID = UserDefinition.FILTER_ID.derive("Student ID", Enrollment::student),
        FILTER_CLASS_ID = ClassDefinition.FILTER_ID.derive("Class ID", Enrollment::classGroup),
        FILTER_MODULE_ID = ClassDefinition.FILTER_MODULE_ID.derive(Enrollment::classGroup);

    public static final FilterOption<Enrollment, String, FormTextField>
        FILTER_STUDENT_NAME = UserDefinition.FILTER_NAME.derive("Student Name", Enrollment::student),
        FILTER_MODULE_NAME = ClassDefinition.FILTER_MODULE_NAME.derive(Enrollment::classGroup);

    public static final List<FilterOption<Enrollment, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_ID,
        FILTER_CLASS_ID
    );
}
