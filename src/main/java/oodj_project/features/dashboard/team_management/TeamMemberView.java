package oodj_project.features.dashboard.team_management;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.user_management.UserController;

public class TeamMemberView extends ManagementView<MemberAssignment> {

    private static final double[] COLUMN_WEIGHT_WITH_ACTION = { 4, 7, 4, 7, 2 },
            COLUMN_WEIGHT_WITHOUT_ACTION = { 4, 8, 4, 8 };

    private final TeamMemberController controller;

    private final TeamMemberFormFactory formFactory;

    private final boolean hasActions, canEdit;

    private final DataList<MemberAssignment> dataTable;

    public TeamMemberView(
            Session session,
            TeamMemberController relationController,
            UserController userController) {
        super(
                "Lecturer Management",
                relationController::index,
                TeamMemberView::buildSearchLogic);

        controller = relationController;

        canEdit = session.can(Permission.ASSIGN_SUPERVISOR);
        var canAdd = session.can(Permission.ASSIGN_SUPERVISOR);

        hasActions = canEdit;

        formFactory = new TeamMemberFormFactory(
                this,
                controller,
                userController);

        if (canAdd) {
            var addButton = new IconLabelButton("Add", Icons.ADD);
            addButton.addActionListener(event -> {
                formFactory.getCreateForm(this::refreshData);
            });
            toolbarComponents.add(addButton);
        }

        var columnWeight = hasActions ? COLUMN_WEIGHT_WITH_ACTION : COLUMN_WEIGHT_WITHOUT_ACTION;

        dataTable = new DataList<>(
                columnWeight,
                createTableHeader(),
                this::createTableRow);

        init();
    }

    private static Predicate<MemberAssignment> buildSearchLogic(String searchQuery) {
        return relation -> {
            var member = relation.member();
            var supervisor = relation.supervisor();
            return member.id().toString().contains(searchQuery)
                    || member.name().toLowerCase().contains(searchQuery)
                    || (supervisor != null && (supervisor.id().toString().contains(searchQuery)
                            || supervisor.name().toLowerCase().contains(searchQuery)));
        };
    }

    @Override
    protected DataList<MemberAssignment> getContent() {
        return dataTable;
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
                DataList.createHeaderText("ID"),
                DataList.createHeaderText("Lecturer Name"),
                DataList.createHeaderText("Academic Leader ID"),
                DataList.createHeaderText("Academic Leader Name")));

        if (hasActions) {
            var actionLabel = DataList.createHeaderText("Action");
            actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            components.add(actionLabel);
        }

        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(MemberAssignment relation) {
        var member = relation.member();
        var supervisor = relation.supervisor();

        String supervisorId, supervisorName;
        if (relation.isUnassigned()) {
            supervisorId = "<html><i>(unassigned)</i></html>";
            supervisorName = "<html><i>(unassigned)</i></html>";
        } else {
            supervisorId = supervisor.id().toString();
            supervisorName = supervisor.name();
        }

        var components = new ArrayList<>(List.<Component>of(
                DataList.createText(member.id().toString()),
                DataList.createText(member.name()),
                DataList.createText(supervisorId),
                DataList.createText(supervisorName)));

        if (hasActions) {
            components.add(createActionMenu(relation));
        }

        return components.toArray(Component[]::new);
    }

    private JPanel createActionMenu(MemberAssignment relation) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setOpaque(false);

        ArrayList<Component> actionList = new ArrayList<>();

        if (canEdit) {
            actionList.add(createEditButton(relation));
        }

        actionPanel.add(Box.createHorizontalGlue());
        for (int x = 0; x < actionList.size(); x++) {
            if (x > 0)
                actionPanel.add(Box.createHorizontalStrut(5));
            actionPanel.add(actionList.get(x));
        }
        actionPanel.add(Box.createHorizontalGlue());

        return actionPanel;
    }

    private JButton createEditButton(MemberAssignment relation) {
        var editButton = new IconButton(Icons.EDIT);
        editButton.setToolTipText("Edit Team Relation");
        editButton.addActionListener(event -> formFactory.getEditForm(relation, this::refreshData));
        return editButton;
    }

    @Override
    protected TeamMemberFormFactory getFormFactory() {
        return formFactory;
    }
}
