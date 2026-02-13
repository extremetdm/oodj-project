package oodj_project.features.dashboard.student_feedback;

import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextArea;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;
import oodj_project.features.dashboard.feedback_management.Feedback;

public class StudentFeedbackFormContent extends JPanel {

    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final FormSpinner<Integer> scoreField = new FormSpinner<>(
        new SpinnerNumberModel(1, 1, 10, 1)
    );
    private final FormTextArea commentField = new FormTextArea(5);

    private final Enrollment enrollment;

    public StudentFeedbackFormContent(StudentFeedback studentFeedback) {
        super();

        enrollment = studentFeedback.enrollment();

        var builder = new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(5, 5, 5, 5));

        var feedback = studentFeedback.feedback();
        
        if (feedback != null) {
            scoreField.setValue(feedback.score());
            commentField.setText(feedback.comment());
        }

        var classGroup = enrollment.classGroup();
        
        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("Class"),
            new FormTextField(classGroup.id().toString(), false),
            new FormLabel("Score"),
            scoreField,
            new FormLabel("Comment"),
            commentField
        )   
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public Feedback getFormData() {
        return new Feedback(
            enrollment,
            scoreField.getValue(),
            commentField.getText()
        );
    }
}