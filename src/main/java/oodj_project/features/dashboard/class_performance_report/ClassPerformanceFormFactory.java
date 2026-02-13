package oodj_project.features.dashboard.class_performance_report;

import java.awt.Component;

import oodj_project.core.ui.components.management_view.FormFactory;

public class ClassPerformanceFormFactory extends FormFactory<ClassPerformance> {    
    public ClassPerformanceFormFactory(Component component) {
        super(
            component,
            ClassPerformanceDefinition.SORT_OPTIONS,
            ClassPerformanceDefinition.FILTER_OPTIONS
        );
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Class Performance";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Class Performance";
    }
}
