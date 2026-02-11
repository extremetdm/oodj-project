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
        SortOption.of("Lecturer ID", relation -> relation.member().id()),
        SortOption.of("Academic Leader ID", relation -> relation.supervisor().id())
    );

    private static final List<FilterOption<MemberAssignment, ?, ?>> FILTER_OPTIONS = List.of(
        FilterOption.compare("Lecturer ID", relation -> relation.member().id(), InputStrategy.nonNegativeIntegerField()),
        FilterOption.compare("Academic Leader ID", relation -> relation.supervisor().id(), InputStrategy.nonNegativeIntegerField())
    );

    private final TeamMemberController relationController;
    private final UserController userController;
    
    public TeamMemberFormFactory(
        Component component,
        TeamMemberController relationController,
        UserController userController
    ) {
        super(component, SORT_OPTIONS, FILTER_OPTIONS);
        this.relationController = relationController;
        this.userController = userController;
    }

    @Override
    protected String getSortWindowTitle() {
        return "Sort Team Relation";
    }

    @Override
    protected String getFilterWindowTitle() {
        return "Filter Team Relation";
    }

    public Form getCreateForm(Runnable onCreate) {
        var content = new TeamMemberEditFormContent(
            relationController.getUnassigned(),
            userController.getSupervisors()
        );

        var form = Form.builder(getParentWindow(), content, handleSubmit(null, content, onCreate))
            .windowTitle("Create Team Relation")
            .formTitle("Create Team Relation")
            .confirmText("Create")
            .build();
        
        form.setVisible(true);
        
        return form;
    }

    public Form getEditForm(MemberAssignment relation, Runnable onUpdate) {
        var content = new TeamMemberEditFormContent(
            userController.getSupervisors(),
            relation
        );

        var form = Form.builder(getParentWindow(), content, handleSubmit(relation, content, onUpdate))
            .windowTitle("Edit Team Relation")
            .formTitle("Edit Team Relation")
            .confirmText("Update")
            .build();
            
        form.setVisible(true);

        return form;
    }

    private ActionListener handleSubmit(MemberAssignment relation, TeamMemberEditFormContent content, Runnable onUpdate) {
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

            } catch (IllegalArgumentException|IOException e) {
                String title = isCreate? "Error creating team relation":
                    "Error editing team relation";
                JOptionPane.showMessageDialog(window, e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}
