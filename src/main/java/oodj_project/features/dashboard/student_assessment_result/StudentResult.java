package oodj_project.features.dashboard.student_assessment_result;

import oodj_project.features.dashboard.assessment_grading.GradeBook;
import oodj_project.features.dashboard.grading_system_management.Grade;

public record StudentResult(
    GradeBook gradeBook,
    Grade grade
) {}
