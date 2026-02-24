package oodj_project.features.dashboard.student_feedback_report;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.class_management.ClassDefinition;

public class FeedbackReportDefinition {
    public static final SortOption<FeedbackReport>
        SORT_CLASS_ID = ClassDefinition.SORT_ID.derive("Class ID", FeedbackReport::classGroup),
        SORT_MODULE_ID = ClassDefinition.SORT_MODULE_ID.derive(FeedbackReport::classGroup),
        SORT_MODULE_NAME = ClassDefinition.SORT_MODULE_NAME.derive(FeedbackReport::classGroup),
        SORT_FEEDBACK_RATE = SortOption.of("Feedback Rate (%)", FeedbackReport::feedbackRate),
        SORT_MIN_SCORE = SortOption.of("Min. Score", FeedbackReport::minScore),
        SORT_AVG_SCORE = SortOption.of("Avg. Score", FeedbackReport::avgScore),
        SORT_MAX_SCORE = SortOption.of("Max. Score", FeedbackReport::maxScore);
        
    public static final List<SortOption<FeedbackReport>> SORT_OPTIONS = List.of(
        SORT_CLASS_ID,
        SORT_MODULE_ID,
        SORT_MODULE_NAME,
        SORT_FEEDBACK_RATE,
        SORT_MIN_SCORE,
        SORT_AVG_SCORE,
        SORT_MAX_SCORE
    );

    public static final FilterOption<FeedbackReport, Integer, FormSpinner<Integer>>
        FILTER_CLASS_ID = ClassDefinition.FILTER_ID.derive("Class ID", FeedbackReport::classGroup),
        FILTER_MODULE_ID = ClassDefinition.FILTER_MODULE_ID.derive(FeedbackReport::classGroup),
        FILTER_MIN_SCORE = FilterOption.compare("Min. Score", FeedbackReport::minScore, InputStrategy.integerField(1, 1, 10, 1)),
        FILTER_AVG_SCORE = FilterOption.compare("Avg. Score", FeedbackReport::roundedAvgScore, InputStrategy.integerField(1, 1, 10, 1)),
        FILTER_MAX_SCORE = FilterOption.compare("Max. Score", FeedbackReport::maxScore, InputStrategy.integerField(1, 1, 10, 1));

    public static final FilterOption<FeedbackReport, String, FormTextField>    
        FILTER_MODULE_NAME = ClassDefinition.FILTER_MODULE_NAME.derive(FeedbackReport::classGroup);

    public static final FilterOption<FeedbackReport, Double, FormSpinner<Double>>
        FILTER_FEEDBACK_RATE = FilterOption.compare("Feedback Rate (%)", FeedbackReport::feedbackRate, InputStrategy.percentageField());

    public static final List<FilterOption<FeedbackReport, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_CLASS_ID,
        FILTER_MODULE_ID,
        FILTER_MODULE_NAME,
        FILTER_FEEDBACK_RATE,
        FILTER_MIN_SCORE,
        FILTER_AVG_SCORE,
        FILTER_MAX_SCORE
    );
}
