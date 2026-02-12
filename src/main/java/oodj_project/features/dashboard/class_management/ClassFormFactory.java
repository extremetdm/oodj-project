package oodj_project.features.dashboard.class_management;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import oodj_project.core.security.Session;
import oodj_project.core.ui.components.filter_editor.FilterOption;
import oodj_project.core.ui.components.filter_editor.InputStrategy;
import oodj_project.core.ui.components.form.Form;
import oodj_project.core.ui.components.management_view.FormFactory;
import oodj_project.core.ui.components.sort_editor.SortOption;
import oodj_project.features.dashboard.module_management.ModuleController;
import oodj_project.features.dashboard.team_management.TeamMemberController;

public class ClassFormFactory extends FormFactory<ClassGroup> {

    private static final List<SortOption<ClassGroup>> SORT_OPTIONS = List.of(
            SortOption.of("ID", ClassGroup::id),
            SortOption.of("Start Date", ClassGroup::startDate),
            SortOption.of("End Date", ClassGroup::endDate));

    private static final List<FilterOption<ClassGroup, ?, ?>> FILTER_OPTIONS = List.of(
            FilterOption.compare("ID", ClassGroup::id, InputStrategy.nonNegativeIntegerField()),
            FilterOption.text("Lecturer Name", classGroup -> classGroup.lecturer().name(), InputStrategy.textField()),
            FilterOption.compare("Start Date", ClassGroup::startDate, InputStrategy.dateField()),
            FilterOption.compare("End Date", ClassGroup::endDate, InputStrategy.dateField()));

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
        super(component, SORT_OPTIONS, FILTER_OPTIONS);
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