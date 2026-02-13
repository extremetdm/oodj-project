package oodj_project.features.dashboard.student_performance_report;


import java.awt.Component;

import oodj_project.core.ui.components.management_view.FormFactory;

public class StudentPerformanceFormFactory extends FormFactory<StudentPerformance> {    
    public StudentPerformanceFormFactory(Component component) {
        super(
            component,
            StudentPerformanceDefinition.SORT_OPTIONS,
            StudentPerformanceDefinition.FILTER_OPTIONS
        );
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Student Performance";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Student Performance";
    }
}
