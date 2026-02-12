package oodj_project.features.dashboard.assessment_management;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.management_view.FormFactory;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.class_management.ClassController;

public class AssessmentFormFactory extends FormFactory<Assessment> {

    private static final List<SortOption<Assessment>> SORT_OPTIONS = List.of(
        SortOption.of("ID", Assessment::id),
        SortOption.text("Name", Assessment::name)
    );

    private static final List<FilterOption<Assessment, ?, ?>> FILTER_OPTIONS = List.of(
        FilterOption.compare("ID", Assessment::id, InputStrategy.positiveIntegerField()),
        FilterOption.text("Name", Assessment::name, InputStrategy.textField())
    );

    private final AssessmentController assessmentController;
    private final ClassController classController;

    public AssessmentFormFactory(
        Component component,
        AssessmentController assessmentController,
        ClassController classController
    ) {
        super(component, SORT_OPTIONS, FILTER_OPTIONS);
        this.assessmentController = assessmentController;
        this.classController = classController;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Assessment";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Assessment";
    }

    public Form getCreateForm(Runnable onCreate) {
        var myClasses = classController.getMyClasses();
        var content = new AssessmentEditFormContent(myClasses);

        var form = Form.builder(getParentWindow(), content, handleCreate(content, onCreate))
            .windowTitle("Create Assessment")
            .formTitle("Create Assessment")
            .confirmText("Create")
            .build();
        
        form.setVisible(true);
        
        return form;
    }

    private ActionListener handleCreate(AssessmentEditFormContent content, Runnable onCreate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                assessmentController.create(content.getFormData());
                if (onCreate != null)
                    onCreate.run();
                window.dispose();

            } catch (IllegalArgumentException|IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error creating assessment", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    public Form getEditForm(Assessment assessment, Runnable onUpdate) {
        var myClasses = classController.getMyClasses();
        var content = new AssessmentEditFormContent(myClasses, assessment);

        var form = Form.builder(getParentWindow(), content, handleEdit(assessment, content, onUpdate))
            .windowTitle("Edit Assessment")
            .formTitle("Edit Assessment")
            .confirmText("Update")
            .build();
            
        form.setVisible(true);

        return form;
    }

    private ActionListener handleEdit(Assessment assessment, AssessmentEditFormContent content, Runnable onUpdate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                assessmentController.update(assessment.id(), content.getFormData());
                if (onUpdate != null)
                    onUpdate.run();
                window.dispose();

            } catch (IllegalArgumentException|IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error editing assessment", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}