package oodj_project.features.dashboard.team_management;

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
import oodj_project.features.dashboard.user_management.UserController;

public class TeamMemberFormFactory extends FormFactory<MemberAssignment> {

    private static final List<SortOption<MemberAssignment>> SORT_OPTIONS = List.of(
            SortOption.of("ID", relation -> relation.member().id()),
            SortOption.of("Academic Leader ID", relation -> relation.supervisor().id()));

    private static final List<FilterOption<MemberAssignment, ?, ?>> FILTER_OPTIONS = List.of(
            FilterOption.compare("ID", relation -> relation.member().id(), InputStrategy.nonNegativeIntegerField()),
            FilterOption.compare("Academic Leader ID", relation -> relation.supervisor().id(),
                    InputStrategy.nonNegativeIntegerField()),
            FilterOption.text("Academic Leader Name", relation -> relation.supervisor().name(),
                    InputStrategy.textField()));

    private final TeamMemberController relationController;
    private final UserController userController;

    public TeamMemberFormFactory(
            Component component,
            TeamMemberController relationController,
            UserController userController) {
        super(component, SORT_OPTIONS, FILTER_OPTIONS);
        this.relationController = relationController;
        this.userController = userController;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Lecturer Assignment";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Lecturer Assignment";
    }

    public Form getCreateForm(Runnable onCreate) {
        var content = new TeamMemberEditFormContent(
                relationController.getUnassigned(),
                userController.getSupervisors());

        var form = Form.builder(getParentWindow(), content, handleSubmit(null, content, onCreate))
                .windowTitle("Assign Academic Leader")
                .formTitle("Assign Academic Leader")
                .confirmText("Assign")
                .build();

        form.setVisible(true);

        return form;
    }

    public Form getEditForm(MemberAssignment relation, Runnable onUpdate) {
        var content = new TeamMemberEditFormContent(
                userController.getSupervisors(),
                relation);

        var form = Form.builder(getParentWindow(), content, handleSubmit(relation, content, onUpdate))
                .windowTitle("Edit Lecturer Assignment")
                .formTitle("Edit Lecturer Assignment")
                .confirmText("Update")
                .build();

        form.setVisible(true);

        return form;
    }

    private ActionListener handleSubmit(MemberAssignment relation, TeamMemberEditFormContent content,
            Runnable onUpdate) {
        return event -> {
            var window = SwingUtilities.getWindowAncestor((Component) event.getSource());
            var isCreate = relation == null || relation.isUnassigned();
            try {
                if (isCreate) {
                    relationController.create(content.getFormData());
                } else {
                    relationController.update(relation.toModel(), content.getFormData());
                }
                if (onUpdate != null)
                    onUpdate.run();
                window.dispose();

            } catch (IllegalStateException | IllegalArgumentException | IOException e) {
                String title = isCreate ? "Error assigning academic leader" : "Error editing lecturer assignment";
                JOptionPane.showMessageDialog(window, e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}
