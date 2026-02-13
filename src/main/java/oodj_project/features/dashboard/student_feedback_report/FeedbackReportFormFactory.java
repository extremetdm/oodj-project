package oodj_project.features.dashboard.student_feedback_report;

import java.awt.Component;

import oodj_project.core.ui.components.management_view.FormFactory;

public class FeedbackReportFormFactory extends FormFactory<FeedbackReport> {    
    public FeedbackReportFormFactory(Component component) {
        super(
            component,
            FeedbackReportDefinition.SORT_OPTIONS,
            FeedbackReportDefinition.FILTER_OPTIONS
        );
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Feedback Report";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Feedback Report";
    }
}
