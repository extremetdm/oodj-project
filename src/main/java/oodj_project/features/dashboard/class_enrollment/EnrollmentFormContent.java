package oodj_project.features.dashboard.class_enrollment;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.class_management.ClassGroup;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.user_management.User;

public class EnrollmentFormContent extends JPanel {

    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final User student;
    private final ClassGroup classGroup;

    public EnrollmentFormContent(User student, ClassGroup classGroup) {
        super();
        this.student = student;
        this.classGroup = classGroup;

        var builder = new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(5, 5, 5, 5));

        var classPeriod = new FormTextField(
            LineFormatter.formatDate(classGroup.startDate(), "dd-MM-yyyy") + " - " +
            LineFormatter.formatDate(classGroup.endDate(), "dd-MM-yyyy"),
            false
        );

        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("Class ID"),
            new FormTextField(classGroup.id().toString(), false),
            new FormLabel("Class Period"),
            classPeriod
            // new FormLabel("-"),
            // endDateFieldComponent
        )   
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public Enrollment getFormData() {
        return new Enrollment(student, classGroup);
    }
}