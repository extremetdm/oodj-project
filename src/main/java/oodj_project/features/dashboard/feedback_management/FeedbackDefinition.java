package oodj_project.features.dashboard.feedback_management;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentDefinition;

public class FeedbackDefinition {
    
    public static final SortOption<Feedback>
        SORT_CLASS_ID = EnrollmentDefinition.SORT_CLASS_ID.derive(Feedback::enrollment);
        
    public static final List<SortOption<Feedback>> SORT_OPTIONS = List.of(
        SORT_CLASS_ID
        // SortOption.of("Class ID", Feedback -> Feedback.classGroup().id()),
        // SortOption.of("Module ID", Feedback -> Feedback.classGroup().module().id()),
        // SortOption.text("Module name", Feedback -> Feedback.classGroup().module().name()),
        // SortOption.of("Lecturer ID", Feedback -> Feedback.classGroup().lecturer().id()),
        // SortOption.text("Lecturer name", Feedback -> Feedback.classGroup().lecturer().name()),
        // SortOption.of("Occupancy rate (%)", Feedback::occupancyRate),
        // SortOption.of("Total occupants", Feedback::totalOccupants),
        // SortOption.of("Total vacancies", Feedback::totalVacancies),
        // SortOption.of("Max class capacity", Feedback -> Feedback.classGroup().maxCapacity()),
        // SortOption.of("Dropout rate (%)", Feedback::dropoutRate),
        // SortOption.of("Total dropouts", Feedback::totalDropouts),
        // SortOption.of("Total enrollments", Feedback -> Feedback.enrollments().size())
    );

    public static final FilterOption<Feedback, Integer, FormSpinner<Integer>>
        FILTER_CLASS_ID = EnrollmentDefinition.FILTER_CLASS_ID.derive(Feedback::enrollment);

    public static final List<FilterOption<Feedback, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_CLASS_ID
        // FilterOption.compare("Class ID", Feedback -> Feedback.classGroup().id(), InputStrategy.positiveIntegerField()),
        // FilterOption.compare("Module ID", Feedback -> Feedback.classGroup().module().id(), InputStrategy.positiveIntegerField()),
        // FilterOption.text("Module name", Feedback -> Feedback.classGroup().module().name(), InputStrategy.textField()),
        // FilterOption.compare("Lecturer ID", Feedback -> Feedback.classGroup().lecturer().id(), InputStrategy.positiveIntegerField()),
        // FilterOption.text("Lecturer name", Feedback -> Feedback.classGroup().lecturer().name(), InputStrategy.textField()),
        // FilterOption.compare("Occupancy rate (%)", Feedback::occupancyRate, InputStrategy.percentageField()),
        // FilterOption.compare("Total occupants", Feedback::totalOccupants, InputStrategy.nonNegativeIntegerField()),
        // FilterOption.compare("Total vacancies", Feedback::totalVacancies, InputStrategy.nonNegativeIntegerField()),
        // FilterOption.compare("Max class capacity", Feedback -> Feedback.classGroup().maxCapacity(), InputStrategy.positiveIntegerField()),
        // FilterOption.compare("Dropout rate (%)", Feedback::dropoutRate, InputStrategy.percentageField()),
        // FilterOption.compare("Total dropouts", Feedback::totalDropouts, InputStrategy.nonNegativeIntegerField()),
        // FilterOption.compare("Total enrollments", Feedback -> Feedback.enrollments().size(), InputStrategy.nonNegativeIntegerField())
    );
}
