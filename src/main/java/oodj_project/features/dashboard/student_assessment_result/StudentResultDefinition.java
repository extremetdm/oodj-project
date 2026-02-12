package oodj_project.features.dashboard.student_assessment_result;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.class_management.ClassDefinition;

public class StudentResultDefinition {
    
    public static final SortOption<StudentResult>
        SORT_CLASS_ID = ClassDefinition.SORT_ID.derive("Class ID", studentResult -> studentResult.gradeBook().assessment().classGroup());

    public static final List<SortOption<StudentResult>> SORT_OPTIONS = List.of(
        SORT_CLASS_ID
    );

    public static final FilterOption<StudentResult, Integer, FormSpinner<Integer>>
        FILTER_CLASS_ID = ClassDefinition.FILTER_ID.derive("Class ID", studentResult -> studentResult.gradeBook().assessment().classGroup());

    public static final List<FilterOption<StudentResult, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_CLASS_ID
    );
}
