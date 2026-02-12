package oodj_project.features.dashboard.class_management;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.security.Session;
import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.management_view.FormFactory;
import oodj_project.features.dashboard.module_management.ModuleController;
import oodj_project.features.dashboard.team_management.TeamMemberController;

public class ClassFormFactory extends FormFactory<ClassGroup> {

    private final Session session;
    private final ClassController classController;
    private final ModuleController moduleController;
    private final TeamMemberController teamMemberController;

    public ClassFormFactory(
            Component component,
            Session session,
            ClassController classController,
            ModuleController moduleController,
            TeamMemberController teamMemberController) {
        super(
            component,
            ClassDefinition.SORT_OPTIONS,
            ClassDefinition.FILTER_OPTIONS
        );
        this.session = session;
        this.classController = classController;
        this.moduleController = moduleController;
        this.teamMemberController = teamMemberController;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Class";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Class";
    }

    public Form getCreateForm(Runnable onCreate) {
        var content = new ClassEditFormContent(
                session,
                moduleController.index(),
                teamMemberController.getLecturersUnderMe());

        var form = Form.builder(getParentWindow(), content, handleCreate(content, onCreate))
                .windowTitle("Create Class")
                .formTitle("Create Class")
                .confirmText("Create")
                .build();

        form.setVisible(true);

        return form;
    }

    private ActionListener handleCreate(ClassEditFormContent content, Runnable onCreate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                classController.create(content.getFormData());
                if (onCreate != null)
                    onCreate.run();
                window.dispose();

            } catch (IllegalArgumentException | IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error creating class",
                        JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    public Form getEditForm(ClassGroup classGroup, Runnable onUpdate) {
        var content = new ClassEditFormContent(
                session,
                moduleController.index(),
                teamMemberController.getLecturersUnderMe(),
                classGroup);

        var form = Form.builder(getParentWindow(), content, handleEdit(classGroup, content, onUpdate))
                .windowTitle("Edit Class")
                .formTitle("Edit Class")
                .confirmText("Update")
                .build();

        form.setVisible(true);

        return form;
    }

    private ActionListener handleEdit(ClassGroup classGroup, ClassEditFormContent content, Runnable onUpdate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            try {
                classController.update(classGroup.id(), content.getFormData());
                if (onUpdate != null)
                    onUpdate.run();
                window.dispose();

            } catch (IllegalArgumentException | IOException e) {
                JOptionPane.showMessageDialog(window, e.getMessage(), "Error editing class", JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}