package oodj_project.features.dashboard.assessment_management;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import oodj_project.core.security.Permission;
import oodj_project.core.security.Session;
import oodj_project.core.ui.components.buttons.IconButton;
import oodj_project.core.ui.components.buttons.IconLabelButton;
import oodj_project.core.ui.components.management_view.DataList;
import oodj_project.core.ui.components.management_view.ManagementView;
import oodj_project.core.ui.styles.Icons;
import oodj_project.features.dashboard.class_management.ClassController;
import oodj_project.features.dashboard.module_management.ModuleGrid;

public class AssessmentView extends ManagementView<Assessment> {

    private static final double[]
        COLUMN_WEIGHTS_WITH_ACTION = { 1, 2, 7, 7, 4, 1, 2 },
        COLUMN_WEIGHTS_WITHOUT_ACTION = { 1, 2, 7, 7, 5, 2 };

    private final AssessmentController controller;

    private final DataList<Assessment> dataTable;
    private final AssessmentFormFactory formFactory;

    private final boolean hasActions, canUpdate, canDelete;
    
    public AssessmentView(
        Session session,
        AssessmentController assessmentController,
        ClassController classController
    ) {
        super(
            "Assessment Management",
            assessmentController::index,
            AssessmentView::buildSearchLogic
        );

        controller = assessmentController;

        canUpdate = session.can(Permission.UPDATE_ASSESSMENTS);
        canDelete = session.can(Permission.DELETE_ASSESSMENTS);
        var canCreate = session.can(Permission.CREATE_ASSESSMENTS);

        hasActions = canUpdate || canDelete;

        var columnWeight = hasActions?
            COLUMN_WEIGHTS_WITH_ACTION:
            COLUMN_WEIGHTS_WITHOUT_ACTION;

        dataTable = new DataList<>(
            columnWeight,
            createTableHeader(),
            this::createTableRow
        );

        formFactory = new AssessmentFormFactory(this, assessmentController, classController);
    
        if (canCreate) {
            var addButton = new IconLabelButton("Add", Icons.ADD);
            addButton.addActionListener(event -> {
                formFactory.getCreateForm(this::refreshData);
            });
            toolbarComponents.add(addButton);
        }    

        init();
    }
    
    private static Predicate<Assessment> buildSearchLogic(String searchQuery) {
        return assessment -> {
            return assessment.name().toLowerCase().contains(searchQuery);
        };
    }

    private Component[] createTableHeader() {
        var components = new ArrayList<>(List.<Component>of(
            DataList.createHeaderText("ID"),
            DataList.createHeaderText("Class"),
            DataList.createHeaderText("Module"),
            DataList.createHeaderText("Name"),
            DataList.createHeaderText("Type"),
            DataList.createHeaderText("Marks")
        ));

        if (hasActions) {
            var actionLabel = DataList.createHeaderText("Action");
            actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            components.add(actionLabel);
        }
        
        return components.toArray(Component[]::new);
    }

    private Component[] createTableRow(Assessment assessment) {
        var classGroup = assessment.classGroup();

        var components = new ArrayList<>(List.<Component>of(
            DataList.createText(assessment.id().toString()),
            DataList.createText(classGroup.id().toString()),
            new ModuleGrid(classGroup.module()),
            DataList.createText(assessment.name()),
            DataList.createText(assessment.type().name()),
            DataList.createText(String.valueOf(assessment.marks()))
        ));

        if (hasActions) {
            components.add(createActionMenu(assessment));
        }

        return components.toArray(Component[]::new);
    }

    private JPanel createActionMenu(Assessment assessment) {
        var actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setOpaque(false);

        ArrayList<Component> actionList = new ArrayList<>();
        
        if (canUpdate) {    
            actionList.add(createEditOption(assessment));
        }

        if (canDelete) {
            actionList.add(createDeleteOption(assessment));
        }

        actionPanel.add(Box.createHorizontalGlue());
        for (int x = 0; x < actionList.size(); x++) {
            if (x > 0) actionPanel.add(Box.createHorizontalStrut(5));
            actionPanel.add(actionList.get(x));
        }
        actionPanel.add(Box.createHorizontalGlue());

        return actionPanel;
    }

    private IconButton createEditOption(Assessment assessment) {
        var editOption = new IconButton(Icons.EDIT);
        editOption.setToolTipText("Edit assessment");
        editOption.addActionListener(event -> 
            formFactory.getEditForm(assessment, this::refreshData)
        );
        return editOption;
    }

    private IconButton createDeleteOption(Assessment assessment) {
        var deleteOption = new IconButton(Icons.DELETE);
        deleteOption.setToolTipText("Delete assessment");
        deleteOption.addActionListener(event -> {
            var action = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete assessment \"" + assessment.name() + "\"?",
                "Confirm delete assessment",
                JOptionPane.YES_NO_OPTION
            );

            if (action == JOptionPane.YES_OPTION) {
                try {
                    controller.delete(assessment);
                    refreshData();                
                } catch (IOException | NoSuchElementException e) {
                    String message = switch (e) {
                        case IOException _ -> "Failed to save changes.";
                        case NoSuchElementException _ -> "Failed to find assessment.";
                        default -> "Failed to delete assessment.";
                    };
                    JOptionPane.showMessageDialog(this, message, "Error deleting assessment", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return deleteOption;
    }

    @Override
    protected DataList<Assessment> getContent() {
        return dataTable;
    }
    @Override
    protected AssessmentFormFactory getFormFactory() {
        return formFactory;
    }
}
