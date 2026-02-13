package oodj_project.features.dashboard.lecturer_workload_report;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.user_management.User;

public record LecturerWorkload(User lecturer, Map<ClassGroup, List<Enrollment>> enrollments) {
    public LecturerWorkload {
        Model.require(lecturer);
        if (enrollments == null) enrollments = Map.of();
    }

    public Map<ClassGroup.Status, Long> classTotals() {
        return enrollments.keySet()
            .stream()
            .collect(Collectors.groupingBy(ClassGroup::status, Collectors.counting()));
    }

    public Map<ClassGroup.Status, Long> studentTotals() {
        return enrollments.values()
            .stream()
            .flatMap(values -> values.stream())
            .collect(Collectors.groupingBy(
                enrollment -> enrollment.classGroup().status(),
                Collectors.counting()
            ));
    }
}
