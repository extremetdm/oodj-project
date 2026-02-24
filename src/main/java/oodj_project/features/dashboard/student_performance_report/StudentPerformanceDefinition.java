package oodj_project.features.dashboard.student_performance_report;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormComboBox;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentDefinition;
import oodj_project.features.dashboard.grading_system_management.Grade;
import oodj_project.features.dashboard.grading_system_management.GradeDefinition;

public class StudentPerformanceDefinition {
    public static final SortOption<StudentPerformance>
        SORT_STUDENT_ID = EnrollmentDefinition.SORT_STUDENT_ID.derive(StudentPerformance::enrollment),
        SORT_STUDENT_NAME = EnrollmentDefinition.SORT_STUDENT_NAME.derive(StudentPerformance::enrollment),
        SORT_CLASS_ID = EnrollmentDefinition.SORT_CLASS_ID.derive(StudentPerformance::enrollment),
        SORT_MODULE_ID = EnrollmentDefinition.SORT_MODULE_ID.derive(StudentPerformance::enrollment),
        SORT_MODULE_NAME = EnrollmentDefinition.SORT_MODULE_NAME.derive(StudentPerformance::enrollment),
        SORT_MARKS = SortOption.of("Total Marks (%)", StudentPerformance::percentage);
    
    public static final List<SortOption<StudentPerformance>> SORT_OPTIONS = List.of(
        SORT_STUDENT_ID,
        SORT_STUDENT_NAME,
        SORT_CLASS_ID,
        SORT_MODULE_ID,
        SORT_MODULE_NAME,
        SORT_MARKS
    );

    public static final FilterOption<StudentPerformance, Integer, FormSpinner<Integer>>
        FILTER_STUDENT_ID = EnrollmentDefinition.FILTER_STUDENT_ID.derive(StudentPerformance::enrollment),
        FILTER_CLASS_ID = EnrollmentDefinition.FILTER_CLASS_ID.derive(StudentPerformance::enrollment),
        FILTER_MODULE_ID = EnrollmentDefinition.FILTER_MODULE_ID.derive(StudentPerformance::enrollment);

    public static final FilterOption<StudentPerformance, Double, FormSpinner<Double>>
        FILTER_MARKS = FilterOption.compare("Total Marks (%)", StudentPerformance::percentage, InputStrategy.percentageField());
    
    public static final FilterOption<StudentPerformance, String, FormTextField>
        FILTER_STUDENT_NAME = EnrollmentDefinition.FILTER_STUDENT_NAME.derive(StudentPerformance::enrollment),
        FILTER_MODULE_NAME = EnrollmentDefinition.FILTER_MODULE_NAME.derive(StudentPerformance::enrollment);

    public static final FilterOption<StudentPerformance, Grade.Classification, FormComboBox<Grade.Classification>>
        FILTER_GRADE_CLASSIFICATION = GradeDefinition.FILTER_CLASSIFICATION.derive("Grade Classification", StudentPerformance::grade);

    public static final List<FilterOption<StudentPerformance, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_STUDENT_ID,
        FILTER_STUDENT_NAME,
        FILTER_CLASS_ID,
        FILTER_MODULE_ID,
        FILTER_MODULE_NAME,
        FILTER_MARKS,
        FILTER_GRADE_CLASSIFICATION
    );
}
