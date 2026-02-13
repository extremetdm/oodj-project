package oodj_project.features.dashboard.student_feedback;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.management_view.FormFactory;

public class StudentFeedbackFormFactory extends FormFactory<StudentFeedback> {

    private final StudentFeedbackController controller;

    public StudentFeedbackFormFactory(
        Component component,
        StudentFeedbackController controller
    ) {
        super(
            component,
            StudentFeedbackDefinition.SORT_OPTIONS,
            StudentFeedbackDefinition.FILTER_OPTIONS
        );
        this.controller = controller;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Feedback";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Feedback";
    }

    public Form getEditForm(StudentFeedback studentFeedback, Runnable onUpdate) {
        var content = new StudentFeedbackFormContent(studentFeedback);

        var form = Form.builder(
            getParentWindow(), content, handleEdit(studentFeedback, content, onUpdate)
        )
            .windowTitle("Edit Feedback")
            .formTitle("Edit Feedback")
            .confirmText("Update")
            .build();
            
        form.setVisible(true);

        return form;
    }

    private ActionListener handleEdit(
        StudentFeedback studentFeedback,
        StudentFeedbackFormContent content,
        Runnable onUpdate
    ) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                var feedback = studentFeedback.feedback();
                if (feedback == null) {
                    controller.create(content.getFormData());
                } else {
                    controller.update(feedback.id(), content.getFormData());
                }
                if (onUpdate != null)
                    onUpdate.run();
                window.dispose();

            } catch (IllegalStateException|IllegalArgumentException|IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error editing feedback", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}
