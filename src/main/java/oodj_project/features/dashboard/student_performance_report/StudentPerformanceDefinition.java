package oodj_project.features.dashboard.student_performance_report;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.enrolled_classes.EnrollmentDefinition;

public class StudentPerformanceDefinition {
    public static final SortOption<StudentPerformance>
        SORT_CLASS_ID = EnrollmentDefinition.SORT_CLASS_ID.derive(StudentPerformance::enrollment);
    
    public static final List<SortOption<StudentPerformance>> SORT_OPTIONS = List.of(
        SORT_CLASS_ID      
        // SortOption.of("Class ID", report -> report.classGroup().id()),
        // SortOption.of("Module ID", report -> report.classGroup().module().id()),
        // SortOption.text("Module name", report -> report.classGroup().module().name()),
        // SortOption.of("Lecturer ID", report -> report.classGroup().lecturer().id()),
        // SortOption.text("Lecturer name", report -> report.classGroup().lecturer().name()),
        // SortOption.of("Occupancy rate (%)", EnrollmentReport::occupancyRate),
        // SortOption.of("Total occupants", EnrollmentReport::totalOccupants),
        // SortOption.of("Total vacancies", EnrollmentReport::totalVacancies),
        // SortOption.of("Max class capacity", report -> report.classGroup().maxCapacity()),
        // SortOption.of("Dropout rate (%)", EnrollmentReport::dropoutRate),
        // SortOption.of("Total dropouts", EnrollmentReport::totalDropouts),
        // SortOption.of("Total enrollments", report -> report.enrollments().size())
    );

    public static final FilterOption<StudentPerformance, Integer, FormSpinner<Integer>>
        FILTER_CLASS_ID = EnrollmentDefinition.FILTER_CLASS_ID.derive(StudentPerformance::enrollment);

    public static final List<FilterOption<StudentPerformance, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_CLASS_ID
        // FilterOption.compare("Class ID", report -> report.classGroup().id(), InputStrategy.positiveIntegerField()),
        // FilterOption.compare("Module ID", report -> report.classGroup().module().id(), InputStrategy.positiveIntegerField()),
        // FilterOption.text("Module name", report -> report.classGroup().module().name(), InputStrategy.textField()),
        // FilterOption.compare("Lecturer ID", report -> report.classGroup().lecturer().id(), InputStrategy.positiveIntegerField()),
        // FilterOption.text("Lecturer name", report -> report.classGroup().lecturer().name(), InputStrategy.textField()),
        // FilterOption.compare("Occupancy rate (%)", EnrollmentReport::occupancyRate, InputStrategy.percentageField()),
        // FilterOption.compare("Total occupants", EnrollmentReport::totalOccupants, InputStrategy.nonNegativeIntegerField()),
        // FilterOption.compare("Total vacancies", EnrollmentReport::totalVacancies, InputStrategy.nonNegativeIntegerField()),
        // FilterOption.compare("Max class capacity", report -> report.classGroup().maxCapacity(), InputStrategy.positiveIntegerField()),
        // FilterOption.compare("Dropout rate (%)", EnrollmentReport::dropoutRate, InputStrategy.percentageField()),
        // FilterOption.compare("Total dropouts", EnrollmentReport::totalDropouts, InputStrategy.nonNegativeIntegerField()),
        // FilterOption.compare("Total enrollments", report -> report.enrollments().size(), InputStrategy.nonNegativeIntegerField())
    );
}
