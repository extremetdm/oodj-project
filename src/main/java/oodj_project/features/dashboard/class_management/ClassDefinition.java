package oodj_project.features.dashboard.class_management;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.sort_editor.SortOption;

public class ClassDefinition {
    public static final SortOption<ClassGroup>
        SORT_ID = SortOption.of("ID", ClassGroup::id),
        SORT_START_DATE = SortOption.of("Start Date", ClassGroup::startDate),
        SORT_END_DATE = SortOption.of("End Date", ClassGroup::endDate);

    public static final List<SortOption<ClassGroup>> SORT_OPTIONS = List.of(
        SORT_ID,
        SORT_START_DATE,
        SORT_END_DATE
    );

    public static final FilterOption<ClassGroup, ?, ?>
        FILTER_ID = FilterOption.compare("ID", ClassGroup::id, InputStrategy.nonNegativeIntegerField()),
        FILTER_LECTURER_NAME = FilterOption.text("Lecturer Name", classGroup -> classGroup.lecturer().name(), InputStrategy.textField()),
        FILTER_START_DATE = FilterOption.compare("Start Date", ClassGroup::startDate, InputStrategy.dateField()),
        FILTER_END_DATE = FilterOption.compare("End Date", ClassGroup::endDate, InputStrategy.dateField());

    public static final List<FilterOption<ClassGroup, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_ID,
        FILTER_LECTURER_NAME,
        FILTER_START_DATE,
        FILTER_END_DATE
    );
}
