package oodj_project.features.dashboard.assessment_grading;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.management_view.FormFactory;

public class AssessmentResultFormFactory extends FormFactory<GradeBook> {

    private final AssessmentResultController controller;
    
    public AssessmentResultFormFactory(
        Component component,
        AssessmentResultController controller
    ) {
        super(
            component,
            AssessmentResultDefinition.SORT_OPTIONS,
            AssessmentResultDefinition.FILTER_OPTIONS
        );
        this.controller = controller;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Assessment Result";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Assessment Result";
    }

    public Form getCreateForm(Runnable onCreate) {
        var content = new AssessmentResultEditFormContent(controller);

        var form = Form.builder(getParentWindow(), content, handleSubmit(null, content, onCreate))
            .windowTitle("Create Assessment Result")
            .formTitle("Create Assessment Result")
            .confirmText("Create")
            .build();
        
        form.setVisible(true);
        
        return form;
    }

    public Form getEditForm(GradeBook gradeBook, Runnable onUpdate) {
        var content = new AssessmentResultEditFormContent(controller, gradeBook);

        var form = Form.builder(getParentWindow(), content, handleSubmit(gradeBook, content, onUpdate))
            .windowTitle("Edit Assessment Result")
            .formTitle("Edit Assessment Result")
            .confirmText("Update")
            .build();
            
        form.setVisible(true);

        return form;
    }

    private ActionListener handleSubmit(GradeBook gradeBook, AssessmentResultEditFormContent content, Runnable onUpdate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            var isCreate = gradeBook == null || gradeBook.result() == null;
            try {
                if (isCreate) {
                    controller.create(content.getFormData());
                } else {
                    controller.update(gradeBook.result().id(), content.getFormData());
                }
                if (onUpdate != null)
                    onUpdate.run();
                window.dispose();

            } catch (IllegalStateException|IllegalArgumentException|IOException e) {
                String title = isCreate? "Error creating assessment result":
                    "Error editing assessment result";
                JOptionPane.showMessageDialog(window, e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}
