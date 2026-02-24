package oodj_project.features.dashboard.class_management;

import java.util.Date;
import java.util.List;

import com.toedter.calendar.JDateChooser;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.module_management.ModuleDefinition;
import oodj_project.features.dashboard.user_management.UserDefinition;

public class ClassDefinition {
    public static final SortOption<ClassGroup>
        SORT_ID = SortOption.of("ID", ClassGroup::id),
        SORT_MODULE_ID = ModuleDefinition.SORT_ID.derive("Module ID", ClassGroup::module),
        SORT_MODULE_NAME = ModuleDefinition.SORT_NAME.derive("Module Name", ClassGroup::module),
        SORT_LECTURER_ID = UserDefinition.SORT_ID.derive("Lecturer ID", ClassGroup::lecturer),
        SORT_LECTURER_NAME = UserDefinition.SORT_NAME.derive("Lecturer Name", ClassGroup::lecturer),
        SORT_MAX_CAPACITY = SortOption.of("Max capacity", ClassGroup::maxCapacity),
        SORT_START_DATE = SortOption.of("Start Date", ClassGroup::startDate),
        SORT_END_DATE = SortOption.of("End Date", ClassGroup::endDate);

    public static final List<SortOption<ClassGroup>> SORT_OPTIONS = List.of(
        SORT_ID,
        SORT_MODULE_ID,
        SORT_MODULE_NAME,
        SORT_LECTURER_ID,
        SORT_LECTURER_NAME,
        SORT_MAX_CAPACITY,
        SORT_START_DATE,
        SORT_END_DATE
    );

    public static final FilterOption<ClassGroup, Integer, FormSpinner<Integer>>
        FILTER_ID = FilterOption.compare("ID", ClassGroup::id, InputStrategy.nonNegativeIntegerField()),
        FILTER_MODULE_ID = ModuleDefinition.FILTER_ID.derive("Module ID", ClassGroup::module),
        FILTER_LECTURER_ID = UserDefinition.FILTER_ID.derive("Lecturer ID", ClassGroup::lecturer),
        FILTER_MAX_CAPACITY = FilterOption.compare("Max Capacity", ClassGroup::maxCapacity, InputStrategy.nonNegativeIntegerField());
    public static final FilterOption<ClassGroup, String, FormTextField>
        FILTER_MODULE_NAME = ModuleDefinition.FILTER_NAME.derive("Module Name", ClassGroup::module),
        FILTER_LECTURER_NAME = UserDefinition.FILTER_NAME.derive("Lecturer Name", ClassGroup::lecturer);
    public static final FilterOption<ClassGroup, Date, JDateChooser>
        FILTER_START_DATE = FilterOption.compare("Start Date", ClassGroup::startDate, InputStrategy.dateField()),
        FILTER_END_DATE = FilterOption.compare("End Date", ClassGroup::endDate, InputStrategy.dateField());

    public static final List<FilterOption<ClassGroup, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_ID,
        FILTER_MODULE_ID,
        FILTER_MODULE_NAME,
        FILTER_LECTURER_ID,
        FILTER_LECTURER_NAME,
        FILTER_MAX_CAPACITY,
        FILTER_START_DATE,
        FILTER_END_DATE
    );
}