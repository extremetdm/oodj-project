package oodj_project.features.dashboard.student_feedback;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentDefinition;

public class StudentFeedbackDefinition {
    
    public static final SortOption<StudentFeedback>
        SORT_CLASS_ID = EnrollmentDefinition.SORT_CLASS_ID.derive(StudentFeedback::enrollment);
    public static final List<SortOption<StudentFeedback>> SORT_OPTIONS = List.of(
        SORT_CLASS_ID
        // SortOption.of("Class ID", studentFeedback -> studentFeedback.classGroup().id()),
        // SortOption.of("Module ID", studentFeedback -> studentFeedback.classGroup().module().id()),
        // SortOption.text("Module name", studentFeedback -> studentFeedback.classGroup().module().name()),
        // SortOption.of("Lecturer ID", studentFeedback -> studentFeedback.classGroup().lecturer().id()),
        // SortOption.text("Lecturer name", studentFeedback -> studentFeedback.classGroup().lecturer().name()),
        // SortOption.of("Occupancy rate (%)", StudentFeedback::occupancyRate),
        // SortOption.of("Total occupants", StudentFeedback::totalOccupants),
        // SortOption.of("Total vacancies", StudentFeedback::totalVacancies),
        // SortOption.of("Max class capacity", studentFeedback -> studentFeedback.classGroup().maxCapacity()),
        // SortOption.of("Dropout rate (%)", StudentFeedback::dropoutRate),
        // SortOption.of("Total dropouts", StudentFeedback::totalDropouts),
        // SortOption.of("Total enrollments", studentFeedback -> studentFeedback.enrollments().size())
    );

    public static final FilterOption<StudentFeedback, Integer, FormSpinner<Integer>>
        FILTER_CLASS_ID = EnrollmentDefinition.FILTER_CLASS_ID.derive(StudentFeedback::enrollment);

    public static final List<FilterOption<StudentFeedback, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_CLASS_ID
        // FilterOption.compare("Class ID", studentFeedback -> studentFeedback.classGroup().id(), InputStrategy.positiveIntegerField()),
        // FilterOption.compare("Module ID", studentFeedback -> studentFeedback.classGroup().module().id(), InputStrategy.positiveIntegerField()),
        // FilterOption.text("Module name", studentFeedback -> studentFeedback.classGroup().module().name(), InputStrategy.textField()),
        // FilterOption.compare("Lecturer ID", studentFeedback -> studentFeedback.classGroup().lecturer().id(), InputStrategy.positiveIntegerField()),
        // FilterOption.text("Lecturer name", studentFeedback -> studentFeedback.classGroup().lecturer().name(), InputStrategy.textField()),
        // FilterOption.compare("Occupancy rate (%)", StudentFeedback::occupancyRate, InputStrategy.percentageField()),
        // FilterOption.compare("Total occupants", StudentFeedback::totalOccupants, InputStrategy.nonNegativeIntegerField()),
        // FilterOption.compare("Total vacancies", StudentFeedback::totalVacancies, InputStrategy.nonNegativeIntegerField()),
        // FilterOption.compare("Max class capacity", studentFeedback -> studentFeedback.classGroup().maxCapacity(), InputStrategy.positiveIntegerField()),
        // FilterOption.compare("Dropout rate (%)", StudentFeedback::dropoutRate, InputStrategy.percentageField()),
        // FilterOption.compare("Total dropouts", StudentFeedback::totalDropouts, InputStrategy.nonNegativeIntegerField()),
        // FilterOption.compare("Total enrollments", studentFeedback -> studentFeedback.enrollments().size(), InputStrategy.nonNegativeIntegerField())
    );

}
