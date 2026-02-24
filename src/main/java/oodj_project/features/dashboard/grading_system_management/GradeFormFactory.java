package oodj_project.features.dashboard.grading_system_management;

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

public class GradeFormFactory extends FormFactory<Grade> {

    private static final List<SortOption<Grade>> SORT_OPTIONS = List.of(
        SortOption.text("Grade", Grade::name),
        SortOption.of("Min marks", Grade::min),
        SortOption.of("Max marks", Grade::max)
    );

    private static final List<FilterOption<Grade, ?, ?>> FILTER_OPTIONS = List.of(
        FilterOption.text("Grade", Grade::name, InputStrategy.textField()),
        FilterOption.sameAs(
            "Classification",
            Grade::classification,
            InputStrategy.selectField(
                Grade.Classification::name,
                List.of(Grade.Classification.values())
            ),
            Grade.Classification::name
        )
    );

    private final GradeController controller;

    public GradeFormFactory(Component component, GradeController controller) {
        super(component, SORT_OPTIONS, FILTER_OPTIONS);
        this.controller = controller;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Grade";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Grade";
    }

    public Form getCreateForm(Runnable onCreate) {
        var content = new GradeEditFormContent();

        var form = Form.builder(getParentWindow(), content, handleCreate(content, onCreate))
                .windowTitle("Create Grade")
                .formTitle("Create Grade")
                .confirmText("Create")
                .build();

        form.setVisible(true);

        return form;
    }

    private ActionListener handleCreate(GradeEditFormContent content, Runnable onCreate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                controller.create(content.getFormData());
                if (onCreate != null)
                    onCreate.run();
                window.dispose();

            } catch (IllegalStateException | IllegalArgumentException | IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error creating grade",
                        JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    public Form getEditForm(Grade grade, Runnable onUpdate) {
        var content = new GradeEditFormContent(grade);

        var form = Form.builder(getParentWindow(), content, handleEdit(grade, content, onUpdate))
                .windowTitle("Edit Grade")
                .formTitle("Edit Grade")
                .confirmText("Update")
                .build();

        form.setVisible(true);

        return form;
    }

    private ActionListener handleEdit(Grade grade, GradeEditFormContent content, Runnable onUpdate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                controller.update(grade.id(), content.getFormData());
                if (onUpdate != null)
                    onUpdate.run();
                window.dispose();

            } catch (IllegalStateException | IllegalArgumentException | IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error editing grade", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}