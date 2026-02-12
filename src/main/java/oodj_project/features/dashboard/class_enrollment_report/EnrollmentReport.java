package oodj_project.features.dashboard.class_enrollment_report;

import java.util.List;

import oodj_project.core.data.model.Model;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;

public record EnrollmentReport(ClassGroup classGroup, List<Enrollment> enrollments) {
    public EnrollmentReport {
        Model.require(classGroup);
        if (enrollments == null) enrollments = List.of();
    }

    public int totalOccupants() {
        return (int) enrollments.stream()
            .filter(enrollment -> enrollment.dropoutDate() == null)
            .count();
    }

    public int totalVacancies() {
        return classGroup.maxCapacity() - totalOccupants();
    }

    public int totalDropouts() {
        return (int) enrollments.stream()
            .filter(enrollment -> enrollment.dropoutDate() != null)
            .count();
    }

    public double occupancyRate() {
        return (double) totalOccupants() / classGroup.maxCapacity();
    }

    public double dropoutRate() {
        if (enrollments.isEmpty()) return 0;
        return (double) totalDropouts() / enrollments.size();
    }
}
