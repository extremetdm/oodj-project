package oodj_project.features.dashboard.class_performance_report;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.class_management.ClassDefinition;

public class ClassPerformanceDefinition {
    public static final SortOption<ClassPerformance>
        SORT_CLASS_ID = ClassDefinition.SORT_ID.derive("Class ID", ClassPerformance::classGroup),
        SORT_MODULE_ID = ClassDefinition.SORT_MODULE_ID.derive(ClassPerformance::classGroup),
        SORT_MODULE_NAME = ClassDefinition.SORT_MODULE_NAME.derive(ClassPerformance::classGroup),
        SORT_MIN_MARK = SortOption.of("Min. marks (%)", ClassPerformance::minMarkPercentage),
        SORT_AVG_MARK = SortOption.of("Avg. marks (%)", ClassPerformance::avgMarkPercentage),
        SORT_MAX_MARK = SortOption.of("Max. marks (%)", ClassPerformance::maxMarkPercentage),
        SORT_PASS_RATE = SortOption.of("Pass Rate (%)", ClassPerformance::passRate);
    
    public static final List<SortOption<ClassPerformance>> SORT_OPTIONS = List.of(
        SORT_CLASS_ID,
        SORT_MODULE_ID,
        SORT_MODULE_NAME,
        SORT_MIN_MARK,
        SORT_AVG_MARK,
        SORT_MAX_MARK,
        SORT_PASS_RATE
    );

    public static final FilterOption<ClassPerformance, Integer, FormSpinner<Integer>>
        FILTER_CLASS_ID = ClassDefinition.FILTER_ID.derive("Class ID", ClassPerformance::classGroup),
        FILTER_MODULE_ID = ClassDefinition.FILTER_MODULE_ID.derive(ClassPerformance::classGroup);

    public static final FilterOption<ClassPerformance, String, FormTextField>    
        FILTER_MODULE_NAME = ClassDefinition.FILTER_MODULE_NAME.derive(ClassPerformance::classGroup);

    public static final FilterOption<ClassPerformance, Double, FormSpinner<Double>>
        FILTER_MIN_MARK = FilterOption.compare("Min. marks (%)", ClassPerformance::minMarkPercentage, InputStrategy.percentageField()),
        FILTER_AVG_MARK = FilterOption.compare("Avg. marks (%)", ClassPerformance::avgMarkPercentage, InputStrategy.percentageField()),
        FILTER_MAX_MARK = FilterOption.compare("Max. marks (%)", ClassPerformance::maxMarkPercentage, InputStrategy.percentageField()),
        FILTER_PASS_RATE = FilterOption.compare("Pass Rate (%)", ClassPerformance::passRate, InputStrategy.percentageField());

    public static final List<FilterOption<ClassPerformance, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_CLASS_ID,
        FILTER_MODULE_ID,
        FILTER_MODULE_NAME,
        FILTER_MIN_MARK,
        FILTER_AVG_MARK,
        FILTER_MAX_MARK,
        FILTER_PASS_RATE
    );
}
