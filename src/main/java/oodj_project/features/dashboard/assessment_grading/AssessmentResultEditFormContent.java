package oodj_project.features.dashboard.assessment_grading;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;

import oodj_project.core.ui.components.form.FormComboBox;
import oodj_project.core.ui.components.form.FormLabel;
import oodj_project.core.ui.components.form.FormSpinner;
import oodj_project.core.ui.components.form.FormTextArea;
import oodj_project.core.ui.components.form.FormTextField;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.features.dashboard.assessment_management.Assessment;
import oodj_project.features.dashboard.enrolled_classes.Enrollment;

public class AssessmentResultEditFormContent extends JPanel {

    private static final double[] COLUMN_WEIGHTS = { 0, 1 };

    private final FormComboBox<Assessment> assessmentField;
    private final FormComboBox<Enrollment> studentField;

    private final SpinnerNumberModel marksModel = new SpinnerNumberModel(0, 0, null, 1);
    private final FormSpinner<Integer> marksField = new FormSpinner<>(marksModel);
    private final FormTextArea feedbackField = new FormTextArea(5);

    private boolean isUpdating = false;

    public AssessmentResultEditFormContent(
        AssessmentResultController controller
    ) {
        this(controller, null);
    }

    public AssessmentResultEditFormContent(
        AssessmentResultController controller,
        GradeBook gradeBook
    ) {
        super();

        var assessmentModel = new DefaultComboBoxModel<Assessment>();
        var studentModel = new DefaultComboBoxModel<Enrollment>();

        assessmentField = new FormComboBox<>(Assessment::name, assessmentModel);
        studentField = new FormComboBox<>(enrollment -> enrollment.student().name(), studentModel);

        var builder = new FlexibleGridBuilder(this, 2)
            .setInsets(new Insets(5, 5, 5, 5));

        Component assessmentFieldComponent = assessmentField,
            studentFieldComponent = studentField;

        if (gradeBook != null) {
            // builder.add(
            //     GRID_WIDTHS,
            //     COLUMN_WEIGHTS,
            //     new FormLabel("ID"),
            //     new FormTextField(classGroup.id().toString(), false)
            // );

            var result = gradeBook.result();

            if (result != null) {
                marksField.setValue(result.marks());
                feedbackField.setText(result.feedback());
            }
            
            assessmentModel.addElement(gradeBook.assessment());
            studentModel.addElement(gradeBook.enrollment());
            assessmentField.setSelectedItem(gradeBook.assessment());
            studentField.setSelectedItem(gradeBook.enrollment());

            assessmentFieldComponent = new FormTextField(gradeBook.assessment().name(), false);
            studentFieldComponent = new FormTextField(gradeBook.enrollment().student().name(), false);
            
        } else {
            assessmentModel.addAll(controller.getUnmarkedAssessments());
            studentModel.addAll(controller.getUnmarkedStudents());

            assessmentField.addActionListener(evt -> {
                if (isUpdating) return;

                isUpdating = true;

                var selectedStudent = studentField.getSelectedItem();
                var selectedAssessment = assessmentField.getSelectedItem();
                studentModel.removeAllElements();

                var students = controller.getUnmarkedStudents(selectedAssessment);
                studentModel.addAll(students);

                if (students.contains(selectedStudent))
                    studentModel.setSelectedItem(selectedStudent);

                marksModel.setMaximum(selectedAssessment == null? null: selectedAssessment.marks());

                isUpdating = false;
            });

            studentField.addActionListener(evt -> {
                if (isUpdating) return;

                isUpdating = true;

                var selectedAssessment = assessmentField.getSelectedItem();
                assessmentModel.removeAllElements();

                var assessments = controller.getUnmarkedAssessments(studentField.getSelectedItem().student());
                assessmentModel.addAll(assessments);

                if (assessments.contains(selectedAssessment))
                    assessmentModel.setSelectedItem(selectedAssessment);
                else {
                    marksModel.setMaximum(null);
                }
                
                isUpdating = false;
            });
        }

        builder.add(
            COLUMN_WEIGHTS,
            new FormLabel("Assessment"),
            assessmentFieldComponent,
            new FormLabel("Student"),
            studentFieldComponent,
            new FormLabel("Marks Awarded"),
            marksField,
            new FormLabel("Feedback"),
            feedbackField
        )   
            .addStrutGlue(600)
            .build();

        setBorder(BorderFactory.createEmptyBorder(20, 15, 10, 15));
    }

    public AssessmentResult getFormData() {
        return new AssessmentResult(
            assessmentField.getSelectedItem(),
            studentField.getSelectedItem(),
            marksField.getValue(),
            feedbackField.getText()
        );
    }
}