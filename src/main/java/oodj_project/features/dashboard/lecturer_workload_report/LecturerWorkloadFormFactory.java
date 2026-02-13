package oodj_project.features.dashboard.lecturer_workload_report;

import java.awt.Component;

import oodj_project.core.ui.components.management_view.FormFactory;

public class LecturerWorkloadFormFactory extends FormFactory<LecturerWorkload> {

    public LecturerWorkloadFormFactory(Component component) {
        super(
            component, 
            LecturerWorkloadDefinition.SORT_OPTIONS,
            LecturerWorkloadDefinition.FILTER_OPTIONS
        );
    }
}
