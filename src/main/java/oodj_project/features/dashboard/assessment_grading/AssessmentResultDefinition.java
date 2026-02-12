package oodj_project.features.dashboard.assessment_grading;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.sort_editor.SortOption;

public class AssessmentResultDefinition {
    
    public static final SortOption<GradeBook> 
        SORT_ASSESSMENT_ID = SortOption.of("Assessment ID", gradeBook -> gradeBook.assessment().id()),
        SORT_STUDENT_ID = SortOption.of("Student ID", gradeBook -> gradeBook.student().id());

    public static final List<SortOption<GradeBook>> SORT_OPTIONS = List.of(
        SORT_ASSESSMENT_ID,
        SORT_STUDENT_ID
    );

    public static final FilterOption<GradeBook, ?, ?>
        FILTER_ASSESSMENT_ID = FilterOption.compare("Lecturer ID", gradeBook -> gradeBook.assessment().id(), InputStrategy.nonNegativeIntegerField()),
        FILTER_STUDENT_ID = FilterOption.compare("Academic Leader ID", gradeBook -> gradeBook.student().id(), InputStrategy.nonNegativeIntegerField());

    public static final List<FilterOption<GradeBook, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_ASSESSMENT_ID,
        FILTER_STUDENT_ID       
    );
}
