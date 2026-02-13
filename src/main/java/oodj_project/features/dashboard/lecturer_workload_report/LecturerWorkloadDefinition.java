package oodj_project.features.dashboard.lecturer_workload_report;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.sort_editor.SortOption;

public class LecturerWorkloadDefinition {
    public static final List<SortOption<LecturerWorkload>> SORT_OPTIONS = List.of(
        SortOption.of("Lecturer ID", workload -> workload.lecturer().id()),
        SortOption.of("Lecturer name", workload -> workload.lecturer().name())
    );

    public static final List<FilterOption<LecturerWorkload, ?, ?>> FILTER_OPTIONS = List.of(
        FilterOption.compare("Lecturer ID", workload -> workload.lecturer().id(), InputStrategy.nonNegativeIntegerField()),
        FilterOption.text("Lecturer name", workload -> workload.lecturer().name(), InputStrategy.textField())
    );
}
