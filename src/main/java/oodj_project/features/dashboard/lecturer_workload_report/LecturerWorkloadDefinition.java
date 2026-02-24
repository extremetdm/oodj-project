package oodj_project.features.dashboard.lecturer_workload_report;

import java.util.List;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.user_management.UserDefinition;

public class LecturerWorkloadDefinition {
    public static final SortOption<LecturerWorkload>
        SORT_LECTURER_ID = UserDefinition.SORT_ID.derive("Lecturer ID", LecturerWorkload::lecturer),
        SORT_LECTURER_NAME = UserDefinition.SORT_NAME.derive("Lecturer Name", LecturerWorkload::lecturer),
        SORT_UPCOMING_CLASS = getClassTotalSort("Upcoming Classes", ClassGroup.Status.UPCOMING),
        SORT_UPCOMING_STUDENTS = getStudentTotalSort("Upcoming Students", ClassGroup.Status.UPCOMING),
        SORT_ONGOING_CLASS = getClassTotalSort("Ongoing Classes", ClassGroup.Status.ONGOING),
        SORT_ONGOING_STUDENTS = getStudentTotalSort("Ongoing Students", ClassGroup.Status.ONGOING),
        SORT_COMPLETED_CLASS = getClassTotalSort("Completed Classes", ClassGroup.Status.COMPLETED),
        SORT_COMPLETED_STUDENTS = getStudentTotalSort("Completed Students", ClassGroup.Status.COMPLETED);

    public static SortOption<LecturerWorkload> getClassTotalSort(String label, ClassGroup.Status status) {
        return SortOption.of(label, workload -> workload.classTotals().getOrDefault(status, 0l));
    }

    public static SortOption<LecturerWorkload> getStudentTotalSort(String label, ClassGroup.Status status) {
        return SortOption.of(label, workload -> workload.studentTotals().getOrDefault(status, 0l));
    }
        
    public static final List<SortOption<LecturerWorkload>> SORT_OPTIONS = List.of(
        SORT_LECTURER_ID,
        SORT_LECTURER_NAME,
        SORT_UPCOMING_CLASS,
        SORT_UPCOMING_STUDENTS,
        SORT_ONGOING_CLASS,
        SORT_ONGOING_STUDENTS,
        SORT_COMPLETED_CLASS,
        SORT_COMPLETED_STUDENTS
    );

    public static final FilterOption<LecturerWorkload, Integer, FormSpinner<Integer>>
        FILTER_LECTURER_ID = UserDefinition.FILTER_ID.derive("Lecturer ID", LecturerWorkload::lecturer),
        FILTER_UPCOMING_CLASS = getClassTotalFilter("Upcoming Classes", ClassGroup.Status.UPCOMING),
        FILTER_UPCOMING_STUDENTS = getStudentTotalFilter("Upcoming Students", ClassGroup.Status.UPCOMING),
        FILTER_ONGOING_CLASS = getClassTotalFilter("Ongoing Classes", ClassGroup.Status.ONGOING),
        FILTER_ONGOING_STUDENTS = getStudentTotalFilter("Ongoing Students", ClassGroup.Status.ONGOING),
        FILTER_COMPLETED_CLASS = getClassTotalFilter("Completed Classes", ClassGroup.Status.COMPLETED),
        FILTER_COMPLETED_STUDENTS = getStudentTotalFilter("Completed Students", ClassGroup.Status.COMPLETED);

    public static final FilterOption<LecturerWorkload, String, FormTextField>
        FILTER_LECTURER_NAME = UserDefinition.FILTER_NAME.derive("Lecturer Name", LecturerWorkload::lecturer);

    public static FilterOption<LecturerWorkload, Integer, FormSpinner<Integer>> 
    getClassTotalFilter(String label, ClassGroup.Status status) {
        return FilterOption.compare(
            label,
            workload -> workload.classTotals().getOrDefault(status, 0l).intValue(),
            InputStrategy.nonNegativeIntegerField()
        );
    }

    public static FilterOption<LecturerWorkload, Integer, FormSpinner<Integer>> 
    getStudentTotalFilter(String label, ClassGroup.Status status) {
        return FilterOption.compare(
            label,
            workload -> workload.studentTotals().getOrDefault(status, 0l).intValue(),
            InputStrategy.nonNegativeIntegerField()
        );
    }

    public static final List<FilterOption<LecturerWorkload, ?, ?>> FILTER_OPTIONS = List.of(
        FILTER_LECTURER_ID,
        FILTER_LECTURER_NAME,
        FILTER_UPCOMING_CLASS,
        FILTER_UPCOMING_STUDENTS,
        FILTER_ONGOING_CLASS,
        FILTER_ONGOING_STUDENTS,
        FILTER_COMPLETED_CLASS,
        FILTER_COMPLETED_STUDENTS
    );
}
