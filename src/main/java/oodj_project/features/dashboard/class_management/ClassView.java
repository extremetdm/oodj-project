package oodj_project.features.dashboard.class_management;

import java.awt.Component;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.data.repository.LineFormatter;
import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.layout.FlexibleGridBuilder;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.module_management.ModuleController;
import oodj_project.features.dashboard.module_management.ModuleGrid;
import oodj_project.features.dashboard.team_management.TeamMemberController;
import oodj_project.features.dashboard.user_management.User;
import oodj_project.features.dashboard.user_management.UserGrid;

public class ClassView extends ManagementView<ClassGroup> {

    private static final double[] COLUMN_WEIGHT_WITH_ACTION = { 1, 6, 6, 4, 5, 2 },
            COLUMN_WEIGHT_WITHOUT_ACTION = { 1, 7, 7, 4, 5 };

    private final ClassController controller;

    private final ClassFormFactory formFactory;

    private final boolean hasActions, canUpdate, canDelete;

    private final DataList<ClassGroup> dataTable;

    public ClassView(
        Session session,
        ClassController classController,
        ModuleController moduleController,
        TeamMemberController teamMemberController
    ) {
        super(
            "Class Management",
            classController::index,
            ClassView::buildSearchLogic
        );

        controller = classController;

        canUpdate = session.can(Permission.UPDATE_CLASSES)
            || session.can(Permission.ASSIGN_TEACHER);

        canDelete = session.can(Permission.DELETE_CLASSES);

        hasActions = canUpdate || canDelete;

        formFactory = new ClassFormFactory(
            this,
            session,
            classController,
            moduleController,
            teamMemberController
        );

        if (session.can(Permission.CREATE_CLASSES)) {
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

    private static Predicate<ClassGroup> buildSearchLogic(String searchQuery) {
        return classGroup -> {
            var module = classGroup.module();
            var lecturer = classGroup.lecturer();

            return classGroup.id().toString().contains(searchQuery)
                    || module.id().toString().contains(searchQuery)
                    || module.name().toLowerCase().contains(searchQuery)
                    || (lecturer != null && (lecturer.id().toString().contains(searchQuery)
                            || lecturer.name().toLowerCase().contains(searchQuery)));
        };
    }

    @Override
    protected DataList<ClassGroup> getContent() {
        return dataTable;
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
                DataList.createHeaderText("ID"),
                DataList.createHeaderText("Module"),
                DataList.createHeaderText("Lecturer"),
                DataList.createHeaderText("Max capacity"),
                DataList.createHeaderText("Period")));

        if (hasActions) {
            var actionLabel = DataList.createHeaderText("Action");
            actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            components.add(actionLabel);
        }

        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(ClassGroup classGroup) {
        var components = new ArrayList<>(List.<Component>of(
                DataList.createText(classGroup.id().toString()),
                new ModuleGrid(classGroup.module()),
                createLecturerSection(classGroup.lecturer()),
                DataList.createText(String.valueOf(classGroup.maxCapacity())),
                createPeriodSection(classGroup.startDate(), classGroup.endDate())));

        if (hasActions) {
            components.add(createActionMenu(classGroup));
        }

        return components.toArray(Component[]::new);
    }

    private Component createLecturerSection(User lecturer) {
        if (lecturer == null)
            return DataList.createText("<html><i>(unassigned)</i></html>");

        return new UserGrid(lecturer);
    }

    private JPanel createPeriodSection(Date startDate, Date endDate) {
        var startDateLabel = DataList.createText("Start:");
        startDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        var endDateLabel = DataList.createText("End:");
        endDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        return new FlexibleGridBuilder(2)
                .setInsets(new Insets(0, 5, 0, 5))
                .add(
                        new double[] { 0, 1 },
                        startDateLabel,
                        DataList.createText(LineFormatter.formatDate(startDate, "yyyy-MM-dd")),
                        endDateLabel,
                        DataList.createText(LineFormatter.formatDate(endDate, "yyyy-MM-dd")))
                .build();
    }

    private JPanel createActionMenu(ClassGroup classGroup) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setOpaque(false);

        ArrayList<Component> actionList = new ArrayList<>();

        if (canUpdate)
            actionList.add(createEditButton(classGroup));
        if (canDelete)
            actionList.add(createDeleteButton(classGroup));

        actionPanel.add(Box.createHorizontalGlue());
        for (int x = 0; x < actionList.size(); x++) {
            if (x > 0)
                actionPanel.add(Box.createHorizontalStrut(5));
            actionPanel.add(actionList.get(x));
        }
        actionPanel.add(Box.createHorizontalGlue());

        return actionPanel;
    }

    private JButton createEditButton(ClassGroup classGroup) {
        var editButton = new IconButton(Icons.EDIT);
        editButton.setToolTipText("Edit Class");
        editButton.addActionListener(event -> formFactory.getEditForm(classGroup, this::refreshData));
        return editButton;
    }

    private JButton createDeleteButton(ClassGroup classGroup) {
        var deleteButton = new IconButton(Icons.DELETE);
        deleteButton.setToolTipText("Delete Class");
        deleteButton.addActionListener(event -> {
            var action = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete the class \"" + classGroup.id() + "\"?",
                    "Confirm delete classGroup",
                    JOptionPane.YES_NO_OPTION);

            if (action == JOptionPane.YES_OPTION) {
                try {
                    controller.delete(classGroup);
                    refreshData();
                } catch (IOException | NoSuchElementException e) {
                    String message = switch (e) {
                        case IOException _ -> "Failed to save changes.";
                        case NoSuchElementException _ -> "Failed to find class.";
                        default -> "Failed to delete class.";
                    };
                    JOptionPane.showMessageDialog(this, message, "Error deleting class", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return deleteButton;
    }

    @Override
    protected ClassFormFactory getFormFactory() {
        return formFactory;
    }
}
