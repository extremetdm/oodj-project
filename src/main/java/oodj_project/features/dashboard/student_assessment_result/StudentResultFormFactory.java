package oodj_project.features.dashboard.student_assessment_result;

import java.awt.Component;

import oodj_project.core.ui.components.management_view.FormFactory;

public class StudentResultFormFactory extends FormFactory<StudentResult> {
    public StudentResultFormFactory(Component component) {
        super(
            component,
            StudentResultDefinition.SORT_OPTIONS,
            StudentResultDefinition.FILTER_OPTIONS
        );
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Enrollment Report";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Enrollment Report";
    }
}
