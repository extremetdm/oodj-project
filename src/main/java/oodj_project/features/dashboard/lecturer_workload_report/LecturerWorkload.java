package oodj_project.features.dashboard.lecturer_workload_report;

import java.util.List;

import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.class_enrollment_report.EnrollmentReport;
import oodj_project.features.dashboard.user_management.User;

public record LecturerWorkload(User lecturer, List<EnrollmentReport> classes) {
    public LecturerWorkload {
        Model.require(lecturer);
        if (classes == null) classes = List.of();
    }
}
